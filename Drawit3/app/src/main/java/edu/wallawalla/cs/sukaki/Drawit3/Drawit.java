package edu.wallawalla.cs.sukaki.Drawit3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Path;

import java.util.ArrayList;

// Code heavily inspired from tutorial found here (https://medium.com/@valokafor/android-drawing-app-tutorial-pt-1-1927f11456ed).
public class Drawit extends View {

    private int TOUCH_TOLERANCE = 5;
    private Path mDrawPath;
    private Paint mCanvasPaint;
    private Paint mDrawPaint;
    private int mPaintColor = 0xFF0000CC;
    private int mLastPaintColor = 0xFF0000CC;
    private Canvas mDrawCanvas;
    private Bitmap mCanvasBitmap;
    private float mCurrentBrushSize;
    private float mLastBrushSize;

    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>();
    private ArrayList<Paint> paints = new ArrayList<Paint>();
    private ArrayList<Paint> undonePaints = new ArrayList<Paint>();

    float mX, mY;

    public Drawit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mCurrentBrushSize = 5;
        mLastBrushSize = mCurrentBrushSize;

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

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }
        canvas.drawPath(mDrawPath, mDrawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //create canvas of certain device size.
        super.onSizeChanged(w, h, oldw, oldh);

        //create Bitmap of certain w,h
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //apply bitmap to graphic to start drawing.
        mDrawCanvas = new Canvas(mCanvasBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        //respond to down, move and up events
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(touchX, touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(touchX, touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
            default:
                return false;
        }
        //redraw
        invalidate();
        return true;
    }

    private void touch_start(float x, float y) {
        undonePaths.clear();
        undonePaints.clear();
        mDrawPath.reset();
        mDrawPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mDrawPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mDrawPath.lineTo(mX, mY);
        mDrawCanvas.drawPath(mDrawPath, mDrawPaint);
        paths.add(mDrawPath);
        paints.add(mDrawPaint);
        mDrawPath = new Path();
        mDrawPaint = setPaint();
    }

    public void onClickUndo () {
        if (paths.size() > 0) {
            undonePaths.add(paths.remove(paths.size()-1));
            undonePaints.add(paints.remove(paints.size()-1));
            invalidate();
        }
    }

    public void onClickRedo () {
        if (undonePaths.size() > 0) {
            paths.add(undonePaths.remove(undonePaths.size()-1));
            paints.add(undonePaints.remove(undonePaints.size()-1));
            invalidate();
        }
    }

    public void eraseAll() {
        mDrawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        paths.clear();
        paints.clear();
        invalidate();
    }

    public void setBrushSize(float newSize) {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
        mCurrentBrushSize = pixelAmount;
        mDrawPaint = setPaint();
    }

    public void setLastBrushSize(float lastSize){
        mLastBrushSize = lastSize;
    }

    public void setColor(int color) {
        mPaintColor = color;
        mDrawPaint = setPaint();
    }

    public void setEraser() {
        if (mPaintColor != 0xFFFFFFFF) {
            mLastPaintColor = mPaintColor;
            mPaintColor = 0xFFFFFFFF;
        }
        mDrawPaint = setPaint();
    }

    public void setPencil() {
        if (mPaintColor == 0xFFFFFFFF) {
            mPaintColor = mLastPaintColor;
        }
        mDrawPaint = setPaint();
    }

    public float getLastBrushSize(){
        return mLastBrushSize;
    }

    public Paint setPaint() {
        Paint paint = new Paint();
        paint.setColor(mPaintColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mCurrentBrushSize);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        return paint;
    }

    public void setPaths(ArrayList newPaths) {
        paths = newPaths;
    }

    public void setUndonePaths(ArrayList newPaths) {
        undonePaths = newPaths;
    }

    public ArrayList getPaths() {
        return paths;
    }

    public ArrayList getUndonePaths() {
        return undonePaths;
    }

    public void setPaints(ArrayList newPaints) {
        paints = newPaints;
    }

    public void setUndonePaints(ArrayList newPaints) {
        undonePaints = newPaints;
    }

    public ArrayList getPaints() {
        return paints;
    }

    public ArrayList getUndonePaints() {
        return undonePaints;
    }
}
