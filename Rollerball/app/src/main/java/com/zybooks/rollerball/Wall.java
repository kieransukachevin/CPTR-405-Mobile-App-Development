package com.zybooks.rollerball;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Wall {

    public int WALL_SPEED = 10;
    private int mMoveDistance;
    private Rect mRect;
    private int mSurfaceWidth;
    private Paint mPaint;

    public Wall(int x, int y, boolean initialDirectionRight, int surfaceWidth, int surfaceHeight) {

        mSurfaceWidth = surfaceWidth;

        // Determine wall dimensions based on surface width and height
        int width = surfaceWidth / 6;
        int height = surfaceHeight / 20;

        // Make sure wall fits completely on the surface
        x = Math.min(x, surfaceWidth - width);
        y = Math.min(y, surfaceHeight - height);

        // Create wall's rectangle based on location and dimensions
        mRect = new Rect(x, y, x + width, y + height);

        // Determine how many pixels walls move each iteration
        mMoveDistance = initialDirectionRight ? WALL_SPEED : -WALL_SPEED;

        // Wall color
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xffffaaff);
    }

    public Rect getRect() {
        return mRect;
    }

    public void relocate(int x) {

        // Move wall to a new x location
        x = Math.min(x, mSurfaceWidth - mRect.width());
        mRect.offsetTo(x, mRect.top);
    }

    public void move() {

        // Move wall right or left
        mRect.offset(mMoveDistance, 0);

        // Bounce wall off surface edges
        if (mRect.right > mSurfaceWidth) {
            mRect.offsetTo(mSurfaceWidth - mRect.width(), mRect.top);
            mMoveDistance *= -1;
        }
        else if (mRect.left < 0) {
            mRect.offsetTo(0, mRect.top);
            mMoveDistance *= -1;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);
    }
}