package edu.wallawalla.cs.sukaki.drawit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

// Code heavily inspired from tutorial found here (https://medium.com/@valokafor/android-drawing-app-tutorial-pt-1-1927f11456ed).
public class Drawit extends View {

    private Path mDrawPath;
    private Paint mCanvasPaint;
    private Paint mDrawPaint;
    private Canvas mDrawCanvas;
    private Bitmap mCanvasBitmap;

    private int mPaintColor = 0xFF660000;
    private String mPaintName;
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
        updateBrush();
    }

    public void setColor(String color) {
        mPaintName = color;
//        mPaintColor =
        updateBrush();
    }

    public void updateBrush() {
        mDrawPaint.setColor(mPaintColor);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(mCurrentBrushSize);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mCanvasBitmap, 0 , 0, mCanvasPaint);
        canvas.drawPath(mDrawPath, mDrawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mDrawCanvas = new Canvas(mCanvasBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float X = event.getX();
        float Y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDrawPath.moveTo(X, Y);
                break;
            case MotionEvent.ACTION_MOVE:
                mDrawPath.lineTo(X, Y);
                break;
            case MotionEvent.ACTION_UP:
                mDrawPath.lineTo(X, Y);
                mDrawCanvas.drawPath(mDrawPath, mDrawPaint);
                mDrawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }
}
