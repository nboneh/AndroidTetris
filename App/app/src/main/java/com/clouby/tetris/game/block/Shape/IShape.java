package com.clouby.tetris.game.block.Shape;

import android.graphics.Color;

// "I" shape
public class IShape extends Shape {
    public IShape() {
        //four rotations of "I" shape
        super(new int[]{0x0f00, 0x4444, 0x0f00, 0x4444});
//        setColor(Color.CYAN);
        setColor(Color.parseColor("#21e1bf"));
    }
}
