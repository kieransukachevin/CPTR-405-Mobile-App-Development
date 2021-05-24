package com.zybooks.rollerball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Random;

public class RollerGame {

    public final int NUM_WALLS = 3;

    private Ball mBall;
    private ArrayList<Wall> mWalls;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private Paint mPaint;
    private boolean mGameOver;
    private Random mRandom;


    public RollerGame(int surfaceWidth, int surfaceHeight) {
        mSurfaceWidth = surfaceWidth;
        mSurfaceHeight = surfaceHeight;

        mRandom = new Random();

        // For drawing text
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(90);
        mPaint.setColor(Color.RED);

        mBall = new Ball(mSurfaceWidth, mSurfaceHeight);

        int wallY = mSurfaceHeight / (NUM_WALLS + 1);
        mWalls = new ArrayList<>();

        // Add walls at random locations, and alternate initial direction
        for (int c = 1; c <= NUM_WALLS; c++) {
            boolean initialRight = c % 2 == 0;
            mWalls.add(new Wall(mRandom.nextInt(mSurfaceWidth), wallY * c,
                    initialRight, mSurfaceWidth, mSurfaceHeight));
        }

        newGame();
    }

    public void newGame() {
        mGameOver = false;

        // Reset ball at the top of the screen
        mBall.setCenter(mSurfaceWidth / 2, mBall.RADIUS + 10);

        // Reset walls at random spots
        for (Wall wall: mWalls) {
            wall.relocate(mRandom.nextInt(mSurfaceWidth));
        }
    }

    public void update(PointF velocity) {

        if (mGameOver) return;

        // Move ball and walls
        mBall.move(velocity);
        for (Wall wall : mWalls) {
            wall.move();
        }

        // Check for collision
        for (Wall wall : mWalls) {
            if (mBall.intersects(wall)) {
                mGameOver = true;
            }
        }

        // Check for win
        if (mBall.getBottom() >= mSurfaceHeight) {
            mGameOver = true;
        }
    }

    public void draw(Canvas canvas) {

        // Wipe canvas clean
        canvas.drawColor(Color.WHITE);

        // Draw ball and walls
        mBall.draw(canvas);
        for (Wall wall : mWalls) {
            wall.draw(canvas);
        }

        // User win?
        if (mBall.getBottom() >= mSurfaceHeight) {
            String text = "You won!";
            Rect textBounds = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), textBounds);
            canvas.drawText(text, mSurfaceWidth / 2f - textBounds.exactCenterX(),
                    mSurfaceHeight / 2f - textBounds.exactCenterY(), mPaint);
        }
    }
}