package com.clouby.tetris.game.block.Shape;

public class Shape {
    /*
     * each shape occupies 4 rows and 4 columns
     */
    public final static int BOXES_ROWs = 4;
    public final static int BOXES_COLS = 4;
    //each shape has 4 statuses
    private final static int BLOCK_STATUS_NUMBER = 4;

    public final static int[] STYLES = new int[BLOCK_STATUS_NUMBER];

    //position of shape
    private int x, y;

    //pausing or moving
    private boolean pausing = false, moving = true;

    //defer the initiation of shape to subclass
    public Shape(int[] s) {
        for (int i = 0; i < BLOCK_STATUS_NUMBER; ++i) {
            STYLES[i] = s[i];
        }
    }

}
