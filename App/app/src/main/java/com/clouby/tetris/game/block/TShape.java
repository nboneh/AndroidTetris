package com.clouby.tetris.game.block;

import android.graphics.Color;

//"T" shape
public class TShape extends Shape {
    public TShape() {
        super(new int[]{0x04e0, 0x0464, 0x00e4, 0x04c4});
        setColor(Color.parseColor("#f6990a"));
    }
}
