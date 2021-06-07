package edu.wallawalla.cs.sukaki.Drawit3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DrawActivity extends AppCompatActivity implements MenuFragment.OnButtonSelectedListener, MenuFragment.OnColorSelectedListener,
        SettingsFragment.OnButtonSelectedListener,
    BrushSizeFragment.OnSeekBarChangedListener {

    private String mButtonId;
    private Drawit mCanvas;
    private int mProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        mCanvas = findViewById(R.id.canvas);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MenuFragment fragment = new MenuFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
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
                break;
            case "loadButton2":
                break;
            case "sizeButton":
                // Replace the old fragment with the BrushSizeFragment.
                BrushSizeFragment brushFragment = new BrushSizeFragment();

                // Set the progress of the Seek Bar.
                Bundle args = new Bundle();
                args.putInt("progress", mProgress);
                brushFragment.setArguments(args);

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
