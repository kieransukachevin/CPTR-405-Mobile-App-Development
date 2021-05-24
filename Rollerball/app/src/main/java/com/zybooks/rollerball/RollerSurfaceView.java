package com.zybooks.rollerball;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RollerSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private RollerThread mRollerThread;

    public RollerSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mRollerThread = new RollerThread(holder);
        mRollerThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mRollerThread.stopThread();
    }

    public void changeAcceleration(float x, float y) {
        if (mRollerThread != null) {
            mRollerThread.changeAcceleration(x, y);
        }
    }

    public void shake() {
        if (mRollerThread != null) {
            mRollerThread.shake();
        }
    }
}