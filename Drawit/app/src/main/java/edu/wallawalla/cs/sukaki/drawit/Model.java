package edu.wallawalla.cs.sukaki.drawit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Model {
    private Path mDrawPath;
    private Paint mCanvasPaint;
    private Paint mDrawPaint;
    private Canvas mDrawCanvas;
    private Bitmap mCanvasBitmap;

    private int mPaintColor = 0xFF660000;
    private String mPaintName;
    private float mCurrentBrushSize, mLastBrushSize;

    public Path getPath() {
        return mDrawPath;
    }

    public void setCurrentBrushSize(int size) {
        mCurrentBrushSize = size;
    }

    public void setDrawPath(Path path) {
        mDrawPath = path;
    }

    public void setDrawPaint(Paint paint) {
        mDrawPaint = paint;
    }

    public void setColor() {
        mDrawPaint.setColor(mPaintColor);
    }

    public void setAntiAlias() {
        mDrawPaint.setAntiAlias(true);
    }

    public void setStrokeWidth() {
        mDrawPaint.setStrokeWidth(mCurrentBrushSize);
    }

    public void setStyle(Paint.Style style) {
        mDrawPaint.setStyle(style);
    }

    public void setStrokeJoin(Paint.Join join) {
        mDrawPaint.setStrokeJoin(join);
    }

    public void setStrokeCap(Paint.Cap cap) {
        mDrawPaint.setStrokeCap(cap);
    }

    public void setCanvasPaint(Paint paint) {
        mCanvasPaint = paint;
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

//    public Path getPath() {
//        return mDrawPath;
//    }

    public void updateBrush() {
        mDrawPaint.setColor(mPaintColor);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(mCurrentBrushSize);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public Bitmap getCanvasBitmap() {
        return mCanvasBitmap;
    }

    public Paint getCanvasPaint() {
        return mCanvasPaint;
    }

    public Path getDrawPath() {
        return mDrawPath;
    }

    public Paint getDrawPaint() {
        return mDrawPaint;
    }

    public void setCanvasBitmap(Bitmap bitmap) {
        mCanvasBitmap = bitmap;
    }

    public void setDrawCanvas(Canvas canvas) {
        mDrawCanvas = canvas;
    }

    public void moveDrawPath(float X, float Y) {
        mDrawPath.moveTo(X, Y);
    }

    public void drawLine(float X, float Y) {
        mDrawPath.lineTo(X, Y);
    }

    public void drawPath() {
        mDrawCanvas.drawPath(mDrawPath, mDrawPaint);
    }

    public void resetPath() {
        mDrawPath.reset();
    }

    public void setNewColor(String item) {
        // TODO: set color.
    }
}
