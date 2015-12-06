package com.clouby.tetris.game.block;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class TetrisBox {
    Paint paint = new Paint();
    private Dimension size = new Dimension();
    private boolean empty;
    private final static int EMPTY_COLOR = Color.parseColor("#007fff");

    public static TetrisBox createEmptyTetrisBox() {
        TetrisBox tetrisBox = new TetrisBox();
        tetrisBox.setInActive();
        return tetrisBox;
    }

    private TetrisBox() {
        paint.setStyle(Paint.Style.FILL);
    }

    public void setActive(int color) {
        paint.setColor(color);
        empty = false;
    }

    public void setInActive() {
        paint.setColor(EMPTY_COLOR);
        empty = true;
    }

    public boolean isActive() {
        return !empty;
    }

    public Paint getPaint() {
        return paint;
    }

    public int getColor() {
        return paint.getColor();
    }


}
