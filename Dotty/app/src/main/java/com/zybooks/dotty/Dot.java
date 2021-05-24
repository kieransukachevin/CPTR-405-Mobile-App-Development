package com.zybooks.dotty;

import java.util.Random;

public class Dot {
    public int color;
    public int row;
    public int col;
    public float centerX;
    public float centerY;
    public float radius;
    public boolean selected;

    private Random randomGen;

    public Dot(int row, int col) {
        randomGen = new Random();
        setRandomColor();
        selected = false;
        radius = 1;
        this.row = row;
        this.col = col;
    }

    public void setRandomColor() {
        color = randomGen.nextInt(DotsGame.NUM_COLORS);
    }

    public boolean isAdjacent(Dot dot) {
        int colDiff = Math.abs(col - dot.col);
        int rowDiff = Math.abs(row - dot.row);
        return colDiff + rowDiff == 1;
    }
}