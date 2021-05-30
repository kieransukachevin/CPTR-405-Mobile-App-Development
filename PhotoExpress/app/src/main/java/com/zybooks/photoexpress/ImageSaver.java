package com.zybooks.photoexpress;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ImageSaver {

    interface SaveImageCallback {
        void onComplete(boolean result);
    }

    private final Executor mExecutor;
    private final Handler mHandler;
    private final Context mActivityContext;

    public ImageSaver(Context context) {
        mExecutor = Executors.newSingleThreadExecutor();
        mHandler = new Handler(Looper.getMainLooper());
        mActivityContext = context;
    }

    public void saveAlteredPhotoAsync(final File photoFile, final int filterMultColor,
                                      final int filterAddColor, final SaveImageCallback callback) {
        // Call saveAlteredPhoto() on a background thread
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    saveAlteredPhoto(photoFile, filterMultColor, filterAddColor);
                    notifyResult(callback, true);
                } catch (IOException e) {
                    e.printStackTrace();
                    notifyResult(callback, false);
                }
            }
        });
    }

    private void notifyResult(final SaveImageCallback callback, final boolean result) {
        // Call onComplete() on the main thread
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(result);
            }
        });
    }

    private void saveAlteredPhoto(File photoFile, int filterMultColor, int filterAddColor)
            throws IOException {

        // Read original image
        Bitmap origBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), null);

        // Create a new origBitmap with the same dimensions as the original
        Bitmap alteredBitmap = Bitmap.createBitmap(origBitmap.getWidth(), origBitmap.getHeight(),
                origBitmap.getConfig());

        // Draw original origBitmap on canvas and apply the color filter
        Canvas canvas = new Canvas(alteredBitmap);
        Paint paint = new Paint();
        LightingColorFilter colorFilter = new LightingColorFilter(filterMultColor, filterAddColor);
        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(origBitmap, 0, 0, paint);

        // Create an entry for the MediaStore
        ContentValues imageValues = new ContentValues();
        imageValues.put(MediaStore.MediaColumns.DISPLAY_NAME, photoFile.getName());
        imageValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        imageValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        // Insert a new row into the MediaStore
        ContentResolver resolver = mActivityContext.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageValues);
        OutputStream outStream = null;

        try {
            if (uri == null) {
                throw new IOException("Failed to insert MediaStore row");
            }

            // Save the image using the URI
            outStream = resolver.openOutputStream(uri);
            alteredBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        }
        finally {
            if (outStream != null) {
                outStream.close();
            }
        }
    }
}