package edu.wallawalla.cs.sukaki.Drawit3;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SaveFragment extends Fragment {

    public interface OnNameTextChangedListener {
        void onNameTextChanged(CharSequence name);
    }

    private SaveFragment.OnNameTextChangedListener mNameListener;

    private MenuFragment.OnButtonSelectedListener mListener;

    private EditText mSaveText;
    private ImageButton mBackButton;
    private ImageButton mSaveButton;
    private CharSequence name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save, container, false);

        mSaveText = view.findViewById(R.id.saveText);
        mBackButton = view.findViewById(R.id.backButton);
        mSaveButton = view.findViewById(R.id.saveButton2);

        mBackButton.setOnClickListener(buttonClickListener);
        mSaveButton.setOnClickListener(buttonClickListener);

        mSaveText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s;
                mNameListener.onNameTextChanged(name);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSaveText.setOnClickListener(buttonClickListener);

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
        if (context instanceof OnNameTextChangedListener) {
            mNameListener = (OnNameTextChangedListener) context;
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
