package edu.wallawalla.cs.sukaki.drawit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class Drawit extends View {

    private Path mDrawPath;
    private Paint mCanvasPaint;
    private Paint mDrawPaint;

    private int mPaintColor = 0xFF660000;
    private String mPaintName;
    private Canvas mDrawCanvas;
    private Bitmap mCanvasBitmap;
    private float mCurrentBrushSize, mLastBrushSize;

    public Drawit(Context context) {
        super(context);
        init(context);
    }

    public Drawit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Drawit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mCurrentBrushSize = 5;

        mDrawPath = new Path();
        mDrawPaint = new Paint();

        mDrawPaint.setColor(mPaintColor);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(mCurrentBrushSize);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);

        mCanvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void setBrushSize(int size) {
        mCurrentBrushSize = size;
    }

    public void setColor(String color) {
        mPaintName = color;
    }
}
