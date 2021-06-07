package edu.wallawalla.cs.sukaki.Drawit3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageButton mNewButton;
    private ImageButton mLoadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewButton = findViewById(R.id.newButton);
        mLoadButton = findViewById(R.id.loadButton);
    }

    public void onNewButtonClicked(View view) {
        Intent intent = new Intent(this, DrawActivity.class);
        startActivity(intent);
    }

    public void onLoadButtonClicked(View view) {
        Intent intent = new Intent(this, DrawActivity.class);
        startActivity(intent);
    }
}