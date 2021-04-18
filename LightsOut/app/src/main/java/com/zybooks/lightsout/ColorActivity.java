package com.zybooks.lightsout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;

public class ColorActivity extends AppCompatActivity {

    public static final String EXTRA_COLOR = "com.zybooks.lightsout.color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        // Get the color ID from MainActivity
        Intent intent = getIntent();
        int colorId = intent.getIntExtra(EXTRA_COLOR, R.color.yellow);

        // Select the radio button matching the color ID
        int radioId = R.id.radio_yellow;
        if (colorId == R.color.red) {
            radioId = R.id.radio_red;
        }
        else if (colorId == R.color.orange) {
            radioId = R.id.radio_orange;
        }
        else if (colorId == R.color.green) {
            radioId = R.id.radio_green;
        }

        RadioButton radio = findViewById(radioId);
        radio.setChecked(true);
    }

    public void onColorSelected(View view) {
        int colorId = R.color.yellow;
        if (view.getId() == R.id.radio_red) {
            colorId = R.color.red;
        }
        else if (view.getId() == R.id.radio_orange) {
            colorId = R.color.orange;
        }
        else if (view.getId() == R.id.radio_green) {
            colorId = R.color.green;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_COLOR, colorId);
        setResult(RESULT_OK, intent);
        finish();
    }
}