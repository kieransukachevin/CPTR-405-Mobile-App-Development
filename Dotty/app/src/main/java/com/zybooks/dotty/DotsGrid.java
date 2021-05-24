package com.zybooks.dotty;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;

import java.util.ArrayList;

public class DotsGrid extends View {

    private AnimatorSet mAnimatorSet;

    public enum DotSelectionStatus { First, Additional, Last };

    public interface DotsGridListener {
        void onDotSelected(Dot dot, DotSelectionStatus status);
        void onAnimationFinished();
    }

    private final int DOT_RADIUS = 40;

    private DotsGame mGame;
    private Path mDotPath;
    private DotsGridListener mGridListener;
    private int[] mDotColors;
    private int mCellWidth;
    private int mCellHeight;
    private Paint mDotPaint;
    private Paint mPathPaint;

    public DotsGrid(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Used to access the game state
        mGame = DotsGame.getInstance();

        // Get color resources
        mDotColors = getResources().getIntArray(R.array.dotColors);

        // For drawing dots
        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // For drawing the path between connected dots
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setStrokeWidth(10);
        mPathPaint.setStyle(Paint.Style.STROKE);

        // The path between connected dots
        mDotPath = new Path();

        mAnimatorSet = new AnimatorSet();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        int boardWidth = (width - getPaddingLeft() - getPaddingRight());
        int boardHeight = (height - getPaddingTop() - getPaddingBottom());
        mCellWidth = boardWidth / DotsGame.GRID_SIZE;
        mCellHeight = boardHeight / DotsGame.GRID_SIZE;
        resetDots();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw dots
        for (int row = 0; row < DotsGame.GRID_SIZE; row++) {
            for (int col = 0; col < DotsGame.GRID_SIZE; col++) {
                Dot dot = mGame.getDot(row, col);
                mDotPaint.setColor(mDotColors[dot.color]);
                canvas.drawCircle(dot.centerX, dot.centerY, dot.radius, mDotPaint);
            }
        }

        if (!mAnimatorSet.isRunning()) {
            // Draw connector
            ArrayList<Dot> selectedDots = mGame.getSelectedDots();
            if (!selectedDots.isEmpty()) {
                mDotPath.reset();
                Dot dot = selectedDots.get(0);
                mDotPath.moveTo(dot.centerX, dot.centerY);

                for (int i = 1; i < selectedDots.size(); i++) {
                    dot = selectedDots.get(i);
                    mDotPath.lineTo(dot.centerX, dot.centerY);
                }

                mPathPaint.setColor(mDotColors[dot.color]);
                canvas.drawPath(mDotPath, mPathPaint);
            }
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Only execute when a listener exists
        if (mGridListener == null || mAnimatorSet.isRunning()) return true;

        // Determine which dot is pressed
        int x = (int) event.getX();
        int y = (int) event.getY();
        int col = x / mCellWidth;
        int row = y / mCellHeight;
        Dot selectedDot = mGame.getDot(row, col);

        // Return previously selected dot if touch moves outside the grid
        if (selectedDot == null) {
            selectedDot = mGame.getLastSelectedDot();
        }

        // Notify activity that a dot is selected
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mGridListener.onDotSelected(selectedDot, DotSelectionStatus.First);
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            mGridListener.onDotSelected(selectedDot, DotSelectionStatus.Additional);
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            mGridListener.onDotSelected(selectedDot, DotSelectionStatus.Last);
        }

        // Allow accessibility services to perform this action
        performClick();

        return true;
    }

    public void setGridListener(DotsGridListener gridListener) {
        mGridListener = gridListener;
    }

    private void resetDots() {
        for (int row = 0; row < DotsGame.GRID_SIZE; row++) {
            for (int col = 0; col < DotsGame.GRID_SIZE; col++) {
                Dot dot = mGame.getDot(row, col);
                dot.radius = DOT_RADIUS;
                dot.centerX = col * mCellWidth + (mCellWidth / 2f);
                dot.centerY = row * mCellHeight + (mCellHeight / 2f);
            }
        }
    }

    public void animateDots() {

        // For storing many animations
        ArrayList<Animator> animations = new ArrayList<>();

        // Get an animation to make selected dots disappear
        animations.add(getDisappearingAnimator());

        ArrayList<Dot> lowestDots = mGame.getLowestSelectedDots();
        for (Dot dot : lowestDots) {
            int rowsToMove = 1;
            for (int row = dot.row - 1; row >= 0; row--) {
                Dot dotToMove = mGame.getDot(row, dot.col);
                if (dotToMove.selected) {
                    rowsToMove++;
                }
                else {
                    float targetY = dotToMove.centerY + (rowsToMove * mCellHeight);
                    animations.add(getFallingAnimator(dotToMove, targetY));
                }
            }
        }

        // Play animations (just one right now) together, then reset radius to full size
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animations);
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                resetDots();
                mGridListener.onAnimationFinished();
            }
        });
        mAnimatorSet.start();
    }

    private ValueAnimator getDisappearingAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        animator.setDuration(100);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (Dot dot : mGame.getSelectedDots()) {
                    dot.radius = DOT_RADIUS * (float) animation.getAnimatedValue();
                }
                invalidate();
            }
        });
        return animator;
    }

    private ValueAnimator getFallingAnimator(final Dot dot, float destinationY) {
        ValueAnimator animator = ValueAnimator.ofFloat(dot.centerY, destinationY);
        animator.setDuration(300);
        animator.setInterpolator(new BounceInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dot.centerY = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        return animator;
    }
}