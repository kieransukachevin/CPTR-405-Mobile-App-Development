package com.zybooks.drawit2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.nio.file.Path;

public class Drawit extends View {

    private Path drawPath;
    private Paint canvasPaint;
    private Paint drawPaint;
    private int paintColor = 0xFF660000;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private float currentBrushSize
    private float lastBrushSize;

    public Drawit(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
