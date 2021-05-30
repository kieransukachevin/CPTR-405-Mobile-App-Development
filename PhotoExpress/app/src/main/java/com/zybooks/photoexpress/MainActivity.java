package com.zybooks.photoexpress;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_TAKE_PHOTO = 1;

    private File mPhotoFile;

    private ImageView mPhotoImageView;
    private SeekBar mSeekBar;
    private Button mSaveButton;

    // For adding brightness
    private int mMultColor = 0xffffffff;
    private int mAddColor = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotoImageView = findViewById(R.id.photo);

        mSaveButton = findViewById(R.id.saveButton);
        mSaveButton.setEnabled(false);

        mSeekBar = findViewById(R.id.brightnessSeekBar);
        mSeekBar.setVisibility(View.INVISIBLE);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changeBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void takePhotoClick(View view) {
        // Create implicit intent
        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoCaptureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            try {
                mPhotoFile = createImageFile();
            }
            catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }

            // If the File was successfully created, start camera app
            if (mPhotoFile != null) {

                // Create content URI to grant camera app write permission to photoFile
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.zybooks.photoexpress.fileprovider",
                        mPhotoFile);

                // Add content URI to intent
                photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                // Start camera app
                startActivityForResult(photoCaptureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            displayPhoto();

            mSeekBar.setProgress(100);
            mSeekBar.setVisibility(View.VISIBLE);
            mSaveButton.setEnabled(true);
        }
    }

    private File createImageFile() throws IOException {
        // Create a unique image filename
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFilename = "photo_" + timeStamp + ".jpg";

        // Get file path where the app can save a private image
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(storageDir, imageFilename);
    }

    private void displayPhoto() {
        // Get ImageView dimensions
        int targetW = mPhotoImageView.getWidth();
        int targetH = mPhotoImageView.getHeight();

        // Get bitmap dimensions
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a smaller bitmap that fills the ImageView
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath(), bmOptions);

        // Display smaller bitmap
        mPhotoImageView.setImageBitmap(bitmap);
    }

    private void changeBrightness(int brightness) {

        // 100 is the middle value
        if (brightness > 100) {
            // Add color
            float addMult = brightness / 100.0f - 1;
            mAddColor = Color.argb(255, (int) (255 * addMult), (int) (255 * addMult), (int) (255 * addMult));
            mMultColor = 0xffffffff;
        }
        else {
            // Scale color down
            float brightMult = brightness / 100.0f;
            mMultColor = Color.argb(255, (int) (255 * brightMult), (int) (255 * brightMult), (int) (255 * brightMult));
            mAddColor = 0;
        }

        LightingColorFilter colorFilter = new LightingColorFilter(mMultColor, mAddColor);
        mPhotoImageView.setColorFilter(colorFilter);
    }

    public void savePhotoClick(View view) {
        // Don't allow Save button to be pressed while image is saving
        mSaveButton.setEnabled(false);

        // Save image in background thread
        ImageSaver imageSaver = new ImageSaver(this);
        imageSaver.saveAlteredPhotoAsync(mPhotoFile, mMultColor, mAddColor, new ImageSaver.SaveImageCallback() {
            @Override
            public void onComplete(boolean result) {
                // Show appropriate message
                int message = result ? R.string.photo_saved : R.string.photo_not_saved;
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                // Allow Save button to be clicked again
                mSaveButton.setEnabled(true);
            }
        });
    }
}