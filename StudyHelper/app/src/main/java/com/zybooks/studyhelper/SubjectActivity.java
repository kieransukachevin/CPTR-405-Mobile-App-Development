package com.zybooks.studyhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.graphics.Color;

import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class SubjectActivity extends AppCompatActivity
        implements SubjectDialogFragment.OnSubjectEnteredListener {

    private StudyDatabase mStudyDb;
    private SubjectAdapter mSubjectAdapter;
    private RecyclerView mRecyclerView;
    private int[] mSubjectColors;
    private Subject mSelectedSubject;
    private int mSelectedSubjectPosition = RecyclerView.NO_POSITION;
    private ActionMode mActionMode = null;

    private boolean mDarkTheme;
    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Change the theme if preference is true
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mDarkTheme = mSharedPrefs.getBoolean("theme", false);
        if (mDarkTheme) {
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        // Singleton
        mStudyDb = StudyDatabase.getInstance(getApplicationContext());

        mSubjectColors = getResources().getIntArray(R.array.subjectColors);

        // Create 2 grid layout columns
        mRecyclerView = findViewById(R.id.subjectRecyclerView);
        RecyclerView.LayoutManager gridLayoutManager =
                new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // Show the available subjects
        mSubjectAdapter = new SubjectAdapter(loadSubjects());
        mRecyclerView.setAdapter(mSubjectAdapter);
    }

    @Override
    public void onSubjectEntered(String subjectText) {
        if (subjectText.length() > 0) {
            Subject subject = new Subject(subjectText);
            long subjectId = mStudyDb.subjectDao().insertSubject(subject);
            subject.setId(subjectId);
            mSubjectAdapter.addSubject(subject);
            Toast.makeText(this, "Added " + subjectText, Toast.LENGTH_SHORT).show();
        }
    }

    public void addSubjectClick(View view) {
        FragmentManager manager = getSupportFragmentManager();
        SubjectDialogFragment dialog = new SubjectDialogFragment();
        dialog.show(manager, "subjectDialog");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If theme changed, recreate the activity so theme is applied
        boolean darkTheme = mSharedPrefs.getBoolean("theme", false);
        if (darkTheme != mDarkTheme) {
            recreate();
        }

        // Load subjects here in case settings changed
        mSubjectAdapter = new SubjectAdapter(loadSubjects());
        mRecyclerView.setAdapter(mSubjectAdapter);
    }

    private List<Subject> loadSubjects() {
        String order = mSharedPrefs.getString("subject_order", "alpha");
        if (order.equals("alpha")) {
            return mStudyDb.subjectDao().getSubjects();
        }
        else if (order.equals("new_first")) {
            return mStudyDb.subjectDao().getSubjectsNewerFirst();
        }
        else {
            return mStudyDb.subjectDao().getSubjectsOlderFirst();
        }
    }

    private class SubjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private Subject mSubject;
        private TextView mTextView;

        public SubjectHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_items, parent, false));
            itemView.setOnClickListener(this);
            mTextView = itemView.findViewById(R.id.subjectTextView);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Subject subject, int position) {
            mSubject = subject;
            mTextView.setText(subject.getText());

            if (mSelectedSubjectPosition == position) {
                // Make selected subject stand out
                mTextView.setBackgroundColor(Color.RED);
            }
            else {
                // Make the background color dependent on the length of the subject string
                int colorIndex = subject.getText().length() % mSubjectColors.length;
                mTextView.setBackgroundColor(mSubjectColors[colorIndex]);
            }
        }

        @Override
        public void onClick(View view) {
            // Start QuestionActivity with the selected subject
            Intent intent = new Intent(SubjectActivity.this, QuestionActivity.class);
            intent.putExtra(QuestionActivity.EXTRA_SUBJECT_ID, mSubject.getId());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            if (mActionMode != null) {
                return false;
            }

            mSelectedSubject = mSubject;
            mSelectedSubjectPosition = getAdapterPosition();

            // Re-bind the selected item
            mSubjectAdapter.notifyItemChanged(mSelectedSubjectPosition);

            // Show the CAB
            mActionMode = SubjectActivity.this.startActionMode(mActionModeCallback);

            return true;
        }
    }

    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Provide context menu for CAB
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // Process action item selection
            if (item.getItemId() == R.id.delete) {
                // Delete from the database and remove from the RecyclerView
                mStudyDb.subjectDao().deleteSubject(mSelectedSubject);
                mSubjectAdapter.removeSubject(mSelectedSubject);

                // Close the CAB
                mode.finish();
                return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;

            // CAB closing, need to deselect item if not deleted
            mSubjectAdapter.notifyItemChanged(mSelectedSubjectPosition);
            mSelectedSubjectPosition = RecyclerView.NO_POSITION;
        }
    };

    private class SubjectAdapter extends RecyclerView.Adapter<SubjectHolder> {

        private List<Subject> mSubjectList;

        public SubjectAdapter(List<Subject> subjects) {
            mSubjectList = subjects;
        }

        @NonNull
        @Override
        public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new SubjectHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SubjectHolder holder, int position){
            holder.bind(mSubjectList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mSubjectList.size();
        }

        public void addSubject(Subject subject) {
            // Add the new subject at the beginning of the list
            mSubjectList.add(0, subject);

            // Notify the adapter that item was added to the beginning of the list
            notifyItemInserted(0);

            // Scroll to the top
            mRecyclerView.scrollToPosition(0);
        }

        public void removeSubject(Subject subject) {
            // Find subject in the list
            int index = mSubjectList.indexOf(subject);
            if (index >= 0) {
                // Remove the subject
                mSubjectList.remove(index);

                // Notify adapter of subject removal
                notifyItemRemoved(index);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.subject_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.import_questions) {
            Intent intent = new Intent(this, ImportActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}