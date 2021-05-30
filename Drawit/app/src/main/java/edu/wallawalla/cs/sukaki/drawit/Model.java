package edu.wallawalla.cs.sukaki.drawit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;

import java.util.ArrayList;

public class Model {
    private Path mDrawPath;
    private Paint mCanvasPaint;
    private Paint mDrawPaint;
    private Canvas mDrawCanvas;
    private Bitmap mCanvasBitmap;

    private int mPaintColor = 0xFF660000;
    private float mCurrentBrushSize, mLastBrushSize;

    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>();

    public ArrayList<Path> getPaths() { return paths; }

    public void clearUndonePaths() {
        undonePaths.clear();
    }

    public void addPath(Path path) {
        paths.add(path);
    }

    public Path removePath(int path) {
        return paths.remove(path);
    }

    public Path removeUndonePath(int path) {
        return undonePaths.remove(path);
    }

    public void addUndonePath(Path path) {
        undonePaths.add(path);
    }

    public int getPathsSize() { return paths.size(); }

    public int getUndonePathsSize() { return undonePaths.size(); }

    public void setCurrentBrushSize(float size) {
        mCurrentBrushSize = size;
    }

    public void setLastBrushSize(float size) { mLastBrushSize = size; }

    public float getLastBrushSize() { return mLastBrushSize; }

    public void setDrawPath(Path path) {
        mDrawPath = path;
    }

    public void setDrawPaint(Paint paint) {
        mDrawPaint = paint;
    }

    public void setColor() { mDrawPaint.setColor(mPaintColor); }

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

    public Bitmap getCanvasBitmap() {
        return mCanvasBitmap;
    }

    public Path getDrawPath() {
        return mDrawPath;
    }

    public Paint getDrawPaint() { return mDrawPaint; }

    public Canvas getDrawCanvas() { return mDrawCanvas; }

    public void setCanvasBitmap(Bitmap bitmap) { mCanvasBitmap = bitmap; }

    public void setDrawCanvas(Canvas canvas) { mDrawCanvas = canvas; }

    public void eraseAll() {
        mDrawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }

    public void setErase() {
        mPaintColor = 0xFFFFFFFF;
        updateBrush();
    }

    public void setBrushSize(int size) {
        mCurrentBrushSize = size;
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

    public void setNewColor(String item) {
        // TODO: set color.
    }

}
