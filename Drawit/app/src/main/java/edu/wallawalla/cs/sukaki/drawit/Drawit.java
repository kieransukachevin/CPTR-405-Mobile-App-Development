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

    private Model mModel;

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
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mModel.getCanvasBitmap(), 0 , 0, mModel.getCanvasPaint());
        canvas.drawPath(mModel.getDrawPath(), mModel.getDrawPaint());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mModel.setCanvasBitmap(Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888));
        mModel.setDrawCanvas(new Canvas(mModel.getCanvasBitmap()));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float X = event.getX();
        float Y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mModel.moveDrawPath(X, Y);
                break;
            case MotionEvent.ACTION_MOVE:
                mModel.drawLine(X, Y);
                break;
            case MotionEvent.ACTION_UP:
                mModel.drawLine(X, Y);
                mModel.drawPath();
                mModel.resetPath();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setColor(String item) {
        mModel.setColor();
    }

    public void setProgress(int progress) {
        mModel.setBrushSize(progress);
    }
}
