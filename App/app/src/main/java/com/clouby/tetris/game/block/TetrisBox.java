package com.clouby.tetris.game.block;

import android.graphics.Paint;

public class TetrisBox implements Cloneable {
    Paint paint= new Paint();
    private Dimension size = new Dimension();

    public TetrisBox(int color) {
        //border's properties
        paint.setColor(color);
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.STROKE);
    }

    public Paint getPaint() {
        return paint;
    }

    public int getColor() {
        return paint.getColor();
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

}
