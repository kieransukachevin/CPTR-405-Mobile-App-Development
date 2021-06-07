package edu.wallawalla.cs.sukaki.Drawit3;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class BrushSizeFragment extends Fragment {

    public interface OnSeekBarChangedListener {
        void onSeekBarChanged(int progress);
    }

    private ImageButton mBackButton;
    private SeekBar mSizeChanger;
    private int mProgress = 0;

    private MenuFragment.OnButtonSelectedListener mListener;
    private BrushSizeFragment.OnSeekBarChangedListener mSeekBarListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brush_size, container, false);

        mBackButton = view.findViewById(R.id.backButton);
        mSizeChanger = view.findViewById(R.id.seekbar_brush_size);
        mBackButton.setOnClickListener(buttonClickListener);

        Bundle args = getArguments();
        int progress = args.getInt("progress");

        mSizeChanger.setProgress(progress);
        mSizeChanger.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSeekBarListener.onSeekBarChanged(mProgress);
            }
        });

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
        if (context instanceof OnSeekBarChangedListener) {
            mSeekBarListener = (OnSeekBarChangedListener) context;
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
