package com.clouby.tetris.game.block;

public class TetrisBox implements Cloneable {
    /*
     * Color->true, use foreground color, ->false, use background color
     */
    private boolean Color;
    private Dimension size = new Dimension();

    public TetrisBox(boolean Color) {
        this.Color = Color;
    }

    public boolean isColor() {
        return Color;
    }

    public void setColor(boolean color) {
        Color = color;
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
