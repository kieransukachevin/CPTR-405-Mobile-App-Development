package edu.wallawalla.cs.sukaki.Drawit3;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class WarningDialogFragment extends DialogFragment {

    public interface OnDialogItemSelectedListener {
        void onDialogItemSelected(Boolean which);
    }

    private OnDialogItemSelectedListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle
                                         savedInstanceState) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.warning);
        builder.setMessage(R.string.warning_message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Yes
                mListener.onDialogItemSelected(true);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked No
                mListener.onDialogItemSelected(false);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDialogItemSelectedListener) {
            mListener = (OnDialogItemSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBandSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}