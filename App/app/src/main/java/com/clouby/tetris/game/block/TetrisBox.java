package com.clouby.tetris.game.block;

import android.graphics.Paint;

public class TetrisBox implements Cloneable {
    Paint paint= new Paint();
    private Dimension size = new Dimension();

    public TetrisBox(int color) {
        //paint properties
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getColor() {
        return paint.getColor();
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setStyle(Paint.Style style){ paint.setStyle(style); }

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
