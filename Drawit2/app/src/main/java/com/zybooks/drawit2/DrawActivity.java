package com.zybooks.drawit2;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class DrawActivity extends AppCompatActivity {

    private ImageButton mSizeButton;
    private ImageButton mPenButton;
    private ImageButton mColorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        mSizeButton = findViewById(R.id.sizeButton);
        mPenButton = findViewById(R.id.penButton);
        mColorButton = findViewById(R.id.colorButton);
    }
}
