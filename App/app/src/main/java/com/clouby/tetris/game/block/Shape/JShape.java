package com.clouby.tetris.game.block.Shape;

import android.graphics.Color;

//"J" Shape
public class JShape extends Shape {
    public JShape() {
        //four rotations of "J" shape
        super(new int[]{0x6440, 0x0e20, 0x44c0, 0x8e00});
        setColor(Color.parseColor("#8fb8c9"));
    }
}
