package com.clouby.tetris.game.block;

import android.graphics.Color;

//"L" Shape
public class LShape extends Shape {
    public LShape() {
        //four rotations of "L" shape
        super(new int[]{0x6220, 0x1700, 0x2230, 0x0740});
        setColor(Color.parseColor("#356207"));
    }
}
