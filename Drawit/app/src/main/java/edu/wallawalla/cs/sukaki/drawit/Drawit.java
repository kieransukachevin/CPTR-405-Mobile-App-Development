package edu.wallawalla.cs.sukaki.drawit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

// Code heavily inspired from tutorial found here (https://medium.com/@valokafor/android-drawing-app-tutorial-pt-1-1927f11456ed).
public class Drawit extends View {

    private Model mModel;

    private float mX, mY;
    private final int TOUCH_TOLERANCE = 1;

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

    public Model getModel() {
        return mModel;
    }

    public void setModel(Model model) {
        mModel = model;
    }

    private void init(Context context) {
        mModel = new Model();
        mModel.setCurrentBrushSize(5);

        mModel.setDrawPath(new Path());
        mModel.setDrawPaint(new Paint());

        mModel.setColor();
        mModel.setAntiAlias();
        mModel.setStrokeWidth();
        mModel.setStyle(Paint.Style.STROKE);
        mModel.setStrokeJoin(Paint.Join.ROUND);
        mModel.setStrokeCap(Paint.Cap.ROUND);

        mModel.setCanvasPaint(new Paint(Paint.DITHER_FLAG));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mModel.setCanvasBitmap(Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888));
        mModel.setDrawCanvas(new Canvas(mModel.getCanvasBitmap()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Path p : mModel.getPaths()) {
            canvas.drawPath(p, mModel.getDrawPaint());
        }
        canvas.drawPath(mModel.getDrawPath(), mModel.getDrawPaint());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()){
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
        return true;
    }

    private void touch_start(float x, float y) {
        mModel.clearUndonePaths();
        mModel.getDrawPath().reset();
        mModel.getDrawPath().moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mModel.getDrawPath().quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mModel.getDrawPath().lineTo(mX, mY);
        mModel.getDrawCanvas().drawPath(mModel.getDrawPath(), mModel.getDrawPaint());
        mModel.addPath(mModel.getDrawPath());
        mModel.setDrawPath(new Path());
    }

    public void eraseAll() {
        mModel.eraseAll();
        mModel = new Model();
        mModel.setCurrentBrushSize(5);

        mModel.setDrawPath(new Path());
        mModel.setDrawPaint(new Paint());

        mModel.setColor();
        mModel.setAntiAlias();
        mModel.setStrokeWidth();
        mModel.setStyle(Paint.Style.STROKE);
        mModel.setStrokeJoin(Paint.Join.ROUND);
        mModel.setStrokeCap(Paint.Cap.ROUND);

        mModel.setCanvasPaint(new Paint(Paint.DITHER_FLAG));
        invalidate();
    }

    public void onClickUndo () {
        if (mModel.getPathsSize()>0) {
            mModel.addUndonePath(mModel.removePath(mModel.getPathsSize()-1));
            invalidate();
        }

    }

    public void onClickRedo (){
        if (mModel.getUndonePathsSize()>0) {
            mModel.addPath(mModel.removeUndonePath(mModel.getUndonePathsSize()-1));
            invalidate();
        }

    }

    public void setColor(String item) {
        mModel.setCanvasPaint(new Paint(Paint.DITHER_FLAG));
        if (item == "Eraser") {
            mModel.setErase();
        } else {
            mModel.setColor();
        }
    }

    public void setProgress(int progress) {
        mModel.setBrushSize(progress);
    }

    public void setBrushSize(float newSize) {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        mModel.setCurrentBrushSize(pixelAmount);
        mModel.setStrokeWidth();
    }

    public void setLastBrushSize(float lastSize){
        mModel.setLastBrushSize(lastSize);
    }
}
