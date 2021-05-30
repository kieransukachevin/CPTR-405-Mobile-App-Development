package edu.wallawalla.cs.sukaki.drawit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private Spinner mOptionsTab;
    private Spinner mColorTab;
    private SeekBar mSizeBar;
    private Drawit mCanvas;
    private Button mUndo;
    private Button mRedo;
    private String mName;
    private String mState;
    private final int KEY_NAME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOptionsTab = findViewById(R.id.optionsTab);
        mColorTab = findViewById(R.id.colorTab);
        mSizeBar = findViewById(R.id.seekBar);
        mCanvas = findViewById(R.id.canvas);
        mUndo = findViewById(R.id.undoButton);
        mRedo = findViewById(R.id.redoButton);

        ArrayAdapter<CharSequence> adapterOptions = ArrayAdapter.createFromResource(this,
                R.array.options_array, android.R.layout.simple_spinner_item);
        adapterOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOptionsTab.setAdapter(adapterOptions);
        mOptionsTab.setSelection(0, false);
        mOptionsTab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                if (item != "Eraser") {
                    Log.d("ITEM", "Truuuueee");
                    mCanvas.setColor(item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<CharSequence> adapterColor = ArrayAdapter.createFromResource(this,
                R.array.color_array, android.R.layout.simple_spinner_item);
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mColorTab.setAdapter(adapterColor);
        mColorTab.setSelection(0, false);
        mColorTab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                mCanvas.setColor(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mSizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mCanvas.setProgress(progress);
                mCanvas.setBrushSize(progress);
                mCanvas.setLastBrushSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString(KEY_NAME, mName);
//        outState.putString(KEY_STATE, mState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        mName = savedInstanceState.getString(KEY_NAME);
//        mState = savedInstanceState.getString(KEY_STATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case KEY_NAME:
                if (resultCode == Activity.RESULT_OK) {
                    mName = data.getStringExtra("Name");
                    mState = data.getStringExtra("State");
                }
        }
        switch(mState) {
            case "SAVE":
                saveFile();
                break;
            case "OPEN":
                openFile();
                break;
            case "CANCEL":
                break;
                // Do nothing.
        }
    }

    public void onFilesClick(View view) {
        Intent intent = new Intent(this, SaveActivity.class);
        startActivityForResult(intent, KEY_NAME);
    }

    public void saveFile() {
        SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mCanvas.getModel(), Model.class);
        prefsEditor.putString(mName, json);
        prefsEditor.apply();
    }

    public void openFile() {
        SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(mName, "");
        if (json != "") {
//            mCanvas.eraseAll();
            mCanvas.setModel(gson.fromJson(json, Model.class));
        }
    }

    public void onRedoButtonClicked(View view) {
        mCanvas.onClickRedo();
    }

    public void onUndoButtonClicked(View view) {
        mCanvas.onClickUndo();
    }
}