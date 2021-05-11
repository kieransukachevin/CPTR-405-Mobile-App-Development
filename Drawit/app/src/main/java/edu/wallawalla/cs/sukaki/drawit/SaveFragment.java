package edu.wallawalla.cs.sukaki.drawit;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SaveFragment extends Fragment {

    public interface OnButtonSelectedListener {
        void onButtonSelected(String name, String state);
    }

    private OnButtonSelectedListener mListener;
    private EditText mNameEditText;
    private String mName;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.save_fragment, container, false);

        Button openButton = view.findViewById(R.id.openButton);
        openButton.setOnClickListener(v -> {
            String buttonId = (String) view.getTag();
            mListener.onButtonSelected(mName, "OPEN");
        });

        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            String buttonId = (String) view.getTag();
            mListener.onButtonSelected(mName, "SAVE");
        });

        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            String buttonId = (String) view.getTag();
            mListener.onButtonSelected(mName, "CANCEL");
        });

        mNameEditText = view.findViewById(R.id.nameEditText);

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mName = s.toString();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonSelectedListener) {
            mListener = (OnButtonSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnButtonSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}