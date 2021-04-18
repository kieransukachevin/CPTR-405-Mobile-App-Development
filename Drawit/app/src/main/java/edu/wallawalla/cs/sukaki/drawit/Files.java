package edu.wallawalla.cs.sukaki.drawit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Files extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "Image saved";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
    }

    public void saveImage(View view) {
//        Intent intent = new Intent();
//        intent.putExtra(EXTRA_MESSAGE, 0);
//        setResult(RESULT_OK, intent);
        finish();
    }
}