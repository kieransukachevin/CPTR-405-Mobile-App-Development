package com.zybooks.rollerball;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.SurfaceHolder;

public class RollerThread extends Thread  {

    private SurfaceHolder mSurfaceHolder;
    private RollerGame mRollerGame;
    private boolean mThreadRunning;
    private PointF mVelocity;

    public RollerThread(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        mThreadRunning = true;

        mVelocity = new PointF();

        // Create a ball with boundaries determined by SurfaceView
        Canvas canvas = mSurfaceHolder.lockCanvas();
        mRollerGame = new RollerGame(canvas.getWidth(), canvas.getHeight());
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void run() {
        try {
            while (mThreadRunning) {
                Canvas canvas = mSurfaceHolder.lockCanvas();
                mRollerGame.update(mVelocity);
                mRollerGame.draw(canvas);
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        catch (NullPointerException ex) {
            // In case canvas is destroyed while thread is running
            ex.printStackTrace();
        }
    }

    public void changeAcceleration(float xForce, float yForce) {
        mVelocity.x = xForce;
        mVelocity.y = yForce;
    }

    public void stopThread() {
        mThreadRunning = false;
    }

    public void shake() {
        mRollerGame.newGame();
    }
}