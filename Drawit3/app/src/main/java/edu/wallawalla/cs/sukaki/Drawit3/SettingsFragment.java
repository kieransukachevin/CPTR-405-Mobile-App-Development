package edu.wallawalla.cs.sukaki.Drawit3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    public interface OnButtonSelectedListener {
        void onButtonSelected(String buttonId);
    }

    private MenuFragment.OnButtonSelectedListener mListener;

    private ImageButton mRedoButton;
    private ImageButton mUndoButton;
    private ImageButton mSaveButton;
    private ImageButton mLoadButton;
    private ImageButton mBackButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mRedoButton = view.findViewById(R.id.redoButton);
        mUndoButton = view.findViewById(R.id.undoButton);
        mSaveButton = view.findViewById(R.id.saveButton);
        mLoadButton = view.findViewById(R.id.eraseAllButton);
        mBackButton = view.findViewById(R.id.backButton);

        mRedoButton.setOnClickListener(buttonClickListener);
        mUndoButton.setOnClickListener(buttonClickListener);
        mSaveButton.setOnClickListener(buttonClickListener);
        mLoadButton.setOnClickListener(buttonClickListener);
        mBackButton.setOnClickListener(buttonClickListener);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MenuFragment.OnButtonSelectedListener) {
            mListener = (MenuFragment.OnButtonSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBandSelectedListener");
        }
    }

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Notify activity of band selection
            String buttonId = (String) view.getTag();
            mListener.onButtonSelected(buttonId);
        }
    };
}
