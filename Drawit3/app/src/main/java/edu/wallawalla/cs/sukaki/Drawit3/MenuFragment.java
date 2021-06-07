package edu.wallawalla.cs.sukaki.Drawit3;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import top.defaults.colorpicker.ColorPickerPopup;

public class MenuFragment extends Fragment {

    public interface OnButtonSelectedListener {
        void onButtonSelected(String buttonId);
    }

    public interface OnColorSelectedListener {
        void onColorSelected(int color);
    }

    private OnButtonSelectedListener mListener;
    private OnColorSelectedListener mColorListener;

    private ImageButton mSizeButton;
    private ImageButton mColorButton;
    private ImageButton mPenButton;
    private ImageButton mSettingsButton;

    private int mDefaultColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        mSizeButton = view.findViewById(R.id.sizeButton);
        mColorButton = view.findViewById(R.id.colorButton);
        mPenButton = view.findViewById(R.id.penButton);
        mSettingsButton = view.findViewById(R.id.settingsButton);

        mSizeButton.setOnClickListener(buttonClickListener);
        mColorButton.setOnClickListener(buttonClickListener);
        mPenButton.setOnClickListener(buttonClickListener);
        mSettingsButton.setOnClickListener(buttonClickListener);

        registerForContextMenu(mSizeButton);
        registerForContextMenu(mColorButton);
        registerForContextMenu(mPenButton);
        registerForContextMenu(mSettingsButton);

        // handling the Pick Color Button to open color
        // picker dialog
        mColorButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new ColorPickerPopup.Builder(getContext()).initialColor(Color.RED) // set initial color
                            // of the color
                            // picker dialog
                            .enableBrightness(true) // enable color brightness
                            // slider or not
                            .enableAlpha(true) // enable color alpha
                            // changer on slider or
                            // not
                            .okTitle("Choose") // this is top right
                            // Choose button
                            .cancelTitle("Cancel") // this is top left
                            // Cancel button which
                            // closes the
                            .showIndicator(true) // this is the small box
                            // which shows the chosen
                            // color by user at the
                            // bottom of the cancel
                            // button
                            .showValue(true) // this is the value which
                            // shows the selected
                            // color hex code
                            // the above all values can be made
                            // false to disable them on the
                            // color picker dialog.
                            .build()
                            .show(v, new ColorPickerPopup.ColorPickerObserver() {
                                @Override
                                public void onColorPicked(int color) {
                                    // set the color
                                    // which is returned
                                    // by the color
                                    // picker
                                    mDefaultColor = color;
                                    mColorListener.onColorSelected(mDefaultColor);
                                }
                            });
                    }
                });

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v == mColorButton) {
            getActivity().getMenuInflater().inflate(R.menu.color_menu, menu);
        }
        else if (v == mPenButton) {
            getActivity().getMenuInflater().inflate(R.menu.brush_type_menu, menu);
        }
        else if (v == mSettingsButton) {
            getActivity().getMenuInflater().inflate(R.menu.settings_menu, menu);
        }
        else if (v == mSizeButton) {
            getActivity().getMenuInflater().inflate(R.menu.brush_size_menu, menu);
        }
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
        if (context instanceof OnColorSelectedListener) {
            mColorListener = (OnColorSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBandSelectedListener");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settingsButtonMenu) {
            mListener.onButtonSelected("settingsButton");
            return true;
        }
        else if (item.getItemId() == R.id.brushTypeMenu) {
            mListener.onButtonSelected("penButton");
            return true;
        }
        else if (item.getItemId() == R.id.brushSizeMenu) {
            mListener.onButtonSelected("sizeButton");
            return true;
        }

        return super.onContextItemSelected(item);
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
