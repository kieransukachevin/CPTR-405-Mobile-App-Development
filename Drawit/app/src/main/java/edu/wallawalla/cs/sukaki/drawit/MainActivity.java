package edu.wallawalla.cs.sukaki.drawit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Spinner mOptionsTab;
    private Spinner mColorTab;
    private SeekBar mSizeBar;
    private EditText mNameEditText;
    private TextView mNameText;
    private Drawit mCanvas;
    private String mMessage;
    private String mName;
    private final String KEY_NAME = "Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOptionsTab = findViewById(R.id.optionsTab);
        mColorTab = findViewById(R.id.colorTab);
        mSizeBar = findViewById(R.id.seekBar);
        mNameEditText = findViewById(R.id.nameEditText);
        mNameText = findViewById(R.id.nameText);
        mCanvas = findViewById(R.id.canvas);

        ArrayAdapter<CharSequence> adapterOptions = ArrayAdapter.createFromResource(this,
                R.array.options_array, android.R.layout.simple_spinner_item);
        adapterOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOptionsTab.setAdapter(adapterOptions);
        mOptionsTab.setSelection(0, false);
        mOptionsTab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);

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
                mCanvas.setBrushSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mNameText.setText(s.toString());
                mName = s.toString();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_NAME, mName);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mName = savedInstanceState.getString(KEY_NAME);
        mNameText.setText(mName);
    }

    public void onFilesClick(View view) {
        Intent intent = new Intent(this, SaveActivity.class);
        startActivity(intent);
    }
}