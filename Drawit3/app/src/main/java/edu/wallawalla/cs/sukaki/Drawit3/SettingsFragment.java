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

    private ImageButton mSizeButton;
    private ImageButton mColorButton;
    private ImageButton mPenButton;
    private ImageButton mSettingsButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mSizeButton = view.findViewById(R.id.sizeButton);
        mColorButton = view.findViewById(R.id.colorButton);
        mPenButton = view.findViewById(R.id.penButton);
        mSettingsButton = view.findViewById(R.id.settingsButton);

        mSizeButton.setOnClickListener(buttonClickListener);
        mColorButton.setOnClickListener(buttonClickListener);
        mPenButton.setOnClickListener(buttonClickListener);
        mSettingsButton.setOnClickListener(buttonClickListener);

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
