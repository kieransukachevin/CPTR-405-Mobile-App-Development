package edu.wallawalla.cs.sukaki.drawit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SaveFragment extends Fragment {

    public interface OnButtonSelectedListener {
        void onButtonSelected(String buttonId);
    }

    private OnButtonSelectedListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.save_fragment, container, false);

        Button openButton = view.findViewById(R.id.openButton);
        openButton.setOnClickListener(v -> {
            String buttonId = (String) view.getTag();
            mListener.onButtonSelected(buttonId);
        });

        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            String buttonId = (String) view.getTag();
            mListener.onButtonSelected(buttonId);
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
                    + " must implement OnBandSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    public void saveClick(View view) {
//        String buttonId = (String) view.getTag();
//        mListener.onButtonSelected(Integer.parseInt(buttonId));
//    }
//
//    public void openClick(View view) {
//        String buttonId = (String) view.getTag();
//        mListener.onButtonSelected(Integer.parseInt(buttonId));
//    }
}