package com.clouby.tetris.game.block;

public class Shape implements Cloneable {
    /*
     * each shape occupies 4 rows and 4 columns
     */
    public final static int BOXES_ROWS = 4;
    public final static int BOXES_COLS = 4;

    private int[] s;

    //color of each shape
    private int color;
    private int x ;
    private int y ;
    private int rotation;



    public void setDefault(){
        x = 0;
        y =0;
        rotation =0;
    }


    public int getStyle(){
        return s[rotation];
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    //defer the initiation of shape to subclass
    public Shape(int[] s) {
        this.s = s;
        setDefault();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getNextRotation() {
        int nextRotatio = rotation+ 1;
        if(nextRotatio >= 4)
            nextRotatio = 0;
        return nextRotatio;
    }

    public int getStyleForRotation(int rotation){
        return s[rotation];
    }


    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    @Override
    public Shape clone() throws CloneNotSupportedException {

        Shape clone = new Shape(s);
        clone.setColor(getColor());

        return clone;
    }

}
