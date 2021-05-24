package com.zybooks.rollerball;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

public class Ball {

    public final int RADIUS = 100;
    private Paint mPaint;
    private PointF mCenter;

    private int mSurfaceWidth;
    private int mSurfaceHeight;

    public Ball(int surfaceWidth, int surfaceHeight) {

        // Ball must stay within confines of surface
        mSurfaceWidth = surfaceWidth;
        mSurfaceHeight = surfaceHeight;

        // Set initial position
        mCenter = new PointF(RADIUS, RADIUS);

        // Ball color
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xffaaaaff);
    }

    public void setCenter(int x, int y) {

        // Move circle center
        mCenter.x = x;
        mCenter.y = y;
    }

    public void move(PointF velocity) {

        // Move ball's center by velocity
        mCenter.offset(-velocity.x, velocity.y);

        // Don't go too far down or up
        if (mCenter.y > mSurfaceHeight - RADIUS) {
            mCenter.y = mSurfaceHeight - RADIUS;
        }
        else if (mCenter.y < RADIUS) {
            mCenter.y = RADIUS;
        }

        // Don't go too far right or left
        if (mCenter.x > mSurfaceWidth - RADIUS) {
            mCenter.x = mSurfaceWidth - RADIUS;
        }
        else if (mCenter.x < RADIUS) {
            mCenter.x = RADIUS;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(mCenter.x, mCenter.y, RADIUS, mPaint);
    }

    public boolean intersects(Wall wall) {

        // Find point on wall that is closest to ball center
        Rect rect = wall.getRect();
        int nearestX = Math.max(rect.left, Math.min((int) mCenter.x, rect.right));
        int nearestY = Math.max(rect.top, Math.min((int) mCenter.y, rect.bottom));

        // Measure distance from nearest point to ball center
        int deltaX = (int) mCenter.x - nearestX;
        int deltaY = (int) mCenter.y - nearestY;

        // Return true if distance from ball center to nearest point is less than ball radius
        return (deltaX * deltaX + deltaY * deltaY) < (RADIUS * RADIUS);
    }

    public int getBottom() {

        // Return bottom of the ball
        return (int) mCenter.y + RADIUS;
    }
}