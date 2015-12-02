package com.clouby.tetris.game.block.Shape;

public class Shape {
    /*
     * each shape occupies 4 rows and 4 columns
     */
    public final static int BOXES_ROWS = 4;
    public final static int BOXES_COLS = 4;
    //each shape has 4 statuses
    public final static int SHAPE_STATUS_NUMBER = 4;

    public int[] getSTYLES() {
        return STYLES;
    }

    public void setSTYLES(int[] STYLES) {
        this.STYLES = STYLES;
    }

    private int[] STYLES = new int[SHAPE_STATUS_NUMBER];

    //color of each shape
    public static int color;

    //pausing or moving
    private boolean pausing = false, moving = true;

    //defer the initiation of shape to subclass
    public Shape(int[] s) {
        for (int i = 0; i < SHAPE_STATUS_NUMBER; ++i) {
            STYLES[i] = s[i];
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isPausing() {
        return pausing;
    }

    public void setPausing(boolean pausing) {
        this.pausing = pausing;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
