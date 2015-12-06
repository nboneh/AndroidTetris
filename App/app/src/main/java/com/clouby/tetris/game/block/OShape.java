package com.clouby.tetris.game.block;

import android.graphics.Color;

//"O" shape
public class OShape extends Shape {
    public OShape() {
        super(new int[]{0x0660, 0x0660, 0x0660, 0x0660});
        setColor(Color.parseColor("#ffb359"));
    }
}
