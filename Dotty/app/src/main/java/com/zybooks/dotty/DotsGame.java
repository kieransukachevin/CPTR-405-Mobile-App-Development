package com.zybooks.dotty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DotsGame {

    public static final int NUM_COLORS = 5;
    public static final int GRID_SIZE = 6;
    public static final int INIT_MOVES = 10;

    public enum DotStatus { Added, Rejected, Removed };

    private static DotsGame mDotsGame;

    private int mMovesLeft;
    private int mScore;
    private Dot[][] mDots;
    private ArrayList<Dot> mSelectedDots;

    private DotsGame() {

        mScore = 0;

        // Create dots for the 2d array
        mDots = new Dot[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                mDots[row][col] = new Dot(row, col);
            }
        }

        mSelectedDots = new ArrayList();
    }

    public static DotsGame getInstance() {
        if (mDotsGame == null) {
            mDotsGame = new DotsGame();
        }
        return mDotsGame;
    }

    public int getMovesLeft() {
        return mMovesLeft;
    }

    public int getScore() {
        return mScore;
    }

    public Dot getDot(int row, int col) {
        if (row >= GRID_SIZE || row < 0 || col >= GRID_SIZE || col < 0) {
            return null;
        } else {
            return mDots[row][col];
        }
    }

    // Return list of selected dots
    public ArrayList<Dot> getSelectedDots() {
        return mSelectedDots;
    }

    // Return the last selected dot
    public Dot getLastSelectedDot() {
        if (mSelectedDots.size() > 0) {
            return mSelectedDots.get(mSelectedDots.size() - 1);
        } else {
            return null;
        }
    }

    // Return the lowest selected dot in each column
    public ArrayList<Dot> getLowestSelectedDots() {

        ArrayList<Dot> dots = new ArrayList<>();
        for (int col = 0; col < GRID_SIZE; col++) {
            for (int row = GRID_SIZE - 1; row >= 0; row--) {
                if (mDots[row][col].selected) {
                    dots.add(mDots[row][col]);
                    break;
                }
            }
        }

        return dots;
    }

    // Clear the list of selected dots
    public void clearSelectedDots() {

        // Reset board so none selected
        for (Dot dot : mSelectedDots) {
            dot.selected = false;
        }

        mSelectedDots.clear();
    }

    // Attempt to add the dot to the list of selected dots
    public DotStatus processDot(Dot dot) {
        DotStatus status = DotStatus.Rejected;

        // Check if first dot selected
        if (mSelectedDots.size() == 0) {
            mSelectedDots.add(dot);
            dot.selected = true;
            status = DotStatus.Added;
        }
        else if (!dot.selected) {
            // Make sure new is same color and adjacent to last selected dot
            Dot lastDot = getLastSelectedDot();
            if (lastDot.color == dot.color && lastDot.isAdjacent(dot)) {
                mSelectedDots.add(dot);
                dot.selected = true;
                status = DotStatus.Added;
            }
        }
        else if (mSelectedDots.size() > 1) {
            // Dot is already selected, so remove last dot if backtracking
            Dot secondLast = mSelectedDots.get(mSelectedDots.size() - 2);
            if (secondLast.equals(dot)) {
                Dot removedDot = mSelectedDots.remove(mSelectedDots.size() - 1);
                removedDot.selected = false;
                status = DotStatus.Removed;
            }
        }

        return status;
    }

    // Sort by rows ascending
    private void sortSelectedDots() {
        Collections.sort(mSelectedDots, new Comparator<Dot>() {
            public int compare(Dot dot1, Dot dot2) {
                return dot1.row - dot2.row;
            }
        });
    }

    // Call after completing a dot path to relocate the dots and update the score and moves
    public void finishMove() {
        if (mSelectedDots.size() > 1) {
            // Sort by row so dots are processed top-down
            sortSelectedDots();

            // Move all dots above each selected dot down by changing color
            for (Dot dot : mSelectedDots) {
                for (int row = dot.row; row > 0; row--) {
                    Dot dotCurrent = mDots[row][dot.col];
                    Dot dotAbove = mDots[row - 1][dot.col];
                    dotCurrent.color = dotAbove.color;
                }

                // Add new dot at top
                Dot topDot = mDots[0][dot.col];
                topDot.setRandomColor();
            }

            mScore += mSelectedDots.size();
            mMovesLeft--;

            clearSelectedDots();
        }
    }

    // Start a new game
    public void newGame() {
        mScore = 0;
        mMovesLeft = INIT_MOVES;

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                mDots[row][col].setRandomColor();
            }
        }
    }

    // Determine if the game is over
    public boolean isGameOver() {
        return mMovesLeft == 0;
    }
}