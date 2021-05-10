package com.zybooks.studyhelper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SubjectDialogFragment extends DialogFragment {

    public interface OnSubjectEnteredListener {
        void onSubjectEntered(String subject);
    }

    private OnSubjectEnteredListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final EditText subjectEditText = new EditText(requireActivity());
        subjectEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        subjectEditText.setMaxLines(1);

        return new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.subject)
                .setView(subjectEditText)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Notify listener
                        String subject = subjectEditText.getText().toString();
                        mListener.onSubjectEntered(subject.trim());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (OnSubjectEnteredListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}