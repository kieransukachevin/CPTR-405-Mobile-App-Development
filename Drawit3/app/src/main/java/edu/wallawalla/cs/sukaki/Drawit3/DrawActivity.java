package edu.wallawalla.cs.sukaki.Drawit3;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DrawActivity extends AppCompatActivity implements MenuFragment.OnButtonSelectedListener,
        MenuFragment.OnColorSelectedListener, SettingsFragment.OnButtonSelectedListener,
    BrushSizeFragment.OnSeekBarChangedListener, WarningDialogFragment.OnDialogItemSelectedListener,
    SaveFragment.OnNameTextChangedListener {

    private String mButtonId;
    private String mName;
    private Drawit mCanvas;
    private int mProgress = 0;
    private final String KEY_PATHS = "paths";
    private final String KEY_UNDONE_PATHS = "undonePaths";
    private final String KEY_PAINTS = "paints";
    private final String KEY_UNDONE_PAINTS = "undonePaints";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        mCanvas = findViewById(R.id.canvas);

        if (savedInstanceState != null) {
            mCanvas.setPaths(savedInstanceState.getParcelableArrayList(KEY_PATHS));
            mCanvas.setUndonePaths(savedInstanceState.getParcelableArrayList(KEY_UNDONE_PATHS));
            mCanvas.setPaints(savedInstanceState.getParcelableArrayList(KEY_PAINTS));
            mCanvas.setUndonePaints(savedInstanceState.getParcelableArrayList(KEY_UNDONE_PAINTS));
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MenuFragment fragment = new MenuFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_PATHS, mCanvas.getPaths());
        outState.putParcelableArrayList(KEY_UNDONE_PATHS, mCanvas.getUndonePaths());
        outState.putParcelableArrayList(KEY_PAINTS, mCanvas.getPaints());
        outState.putParcelableArrayList(KEY_UNDONE_PAINTS, mCanvas.getUndonePaints());
    }

    @Override
    public void onButtonSelected(String buttonId) {
        mButtonId = buttonId;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (mButtonId) {
            case "settingsButton":
                // Replace the old fragment with the SettingsFragment.
                SettingsFragment settingsFragment = new SettingsFragment();
                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.replace(R.id.fragment_container, settingsFragment, null);

                fragmentTransaction.commit();
                break;
            case "penButton":
                // Replace the old fragment with the BrushTypeFragment.
                BrushTypeFragment brushTypeFragment = new BrushTypeFragment();
                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.replace(R.id.fragment_container, brushTypeFragment, null);

                fragmentTransaction.commit();
                break;
            case "pencilButton":
                mCanvas.setPencil();
                break;
            case "eraserButton":
                mCanvas.setEraser();
                break;
            case "undoButton":
                mCanvas.onClickUndo();
                break;
            case "redoButton":
                mCanvas.onClickRedo();
                break;
            case "saveButton":
                SaveFragment saveFragment = new SaveFragment();
                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.replace(R.id.fragment_container, saveFragment, null);

                fragmentTransaction.commit();
                break;
            case "saveButton2":
                saveImage();
                resetMenu();
                break;
            case "eraseAllButton":
                WarningDialogFragment dialog = new WarningDialogFragment();
                dialog.show(fragmentManager, "warningDialog");
                break;
            case "sizeButton":
                // Replace the old fragment with the BrushSizeFragment.
                BrushSizeFragment brushFragment = new BrushSizeFragment();

                // Set the progress of the Seek Bar.
                Bundle args2 = new Bundle();
                args2.putInt("progress", mProgress);
                brushFragment.setArguments(args2);

                fragmentTransaction.setReorderingAllowed(true);
                fragmentTransaction.replace(R.id.fragment_container, brushFragment, null);

                fragmentTransaction.commit();
                break;
            case "backButton":
                resetMenu();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNameTextChanged(CharSequence name) {
        mName = name.toString();
    }

    @Override
    public void onDialogItemSelected(Boolean which) {
        if (which) {
            mCanvas.eraseAll();
        }
    }

    private void saveImage() {
        if (isExternalStorageWritable()) {
            mCanvas.setDrawingCacheEnabled(true);
            mCanvas.invalidate();
            File file = new File(getExternalFilesDir(null), mName);
            Log.d("TAG", "File path = " + file.getAbsolutePath());
            FileOutputStream outputStream = null;

            try {
                outputStream = new FileOutputStream(file);
            } catch (Exception e) {
                Log.e("LOG_CAT", e.getCause() + e.getMessage());
            }

            if (mCanvas.getDrawingCache() == null) {
                Log.e("LOG_CAT","Unable to get drawing cache ");
            }

            mCanvas.getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 85, outputStream);

            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                Log.e("LOG_CAT", e.getCause() + e.getMessage());
            }

        }
    }

    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onColorSelected(int color) {
        mCanvas.setColor(color);
    }

    @Override
    public void onSeekBarChanged(int progress) {
        mProgress = progress;
        mCanvas.setBrushSize(mProgress);
        mCanvas.setLastBrushSize(mProgress);
    }

    public void resetMenu() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the old fragment with the MenuFragment.
        MenuFragment fragment = new MenuFragment();
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.fragment_container, fragment, null);

        fragmentTransaction.commit();
    }
}
