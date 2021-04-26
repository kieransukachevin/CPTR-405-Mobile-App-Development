package com.zybooks.thebanddatabase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    private Band mBand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the first band
        mBand = BandDatabase.getInstance(getContext()).getBand(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        TextView nameTextView = view.findViewById(R.id.bandName);
        nameTextView.setText(mBand.getName());

        TextView descriptionTextView = view.findViewById(R.id.bandDescription);
        descriptionTextView.setText(mBand.getDescription());

        return view;
    }
}