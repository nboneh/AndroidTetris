package com.clouby.tetris.game.block;

public class Dimension {
    public float width;
    public float height;

    public Dimension() {
    }

    public Dimension(float w, float h) {
        width = w;
        height = h;
    }

    public Dimension(Dimension p) {
        this.width = p.width;
        this.height = p.height;
    }

    public final void set(float w, float h) {
        width = w;
        height = h;
    }

    public final void set(Dimension d) {
        this.width = d.width;
        this.height = d.height;
    }

    public final boolean equals(float w, float h) {
        return this.width == w && this.height == h;
    }

    public final boolean equals(Object o) {
        return o instanceof Dimension && (o == this || equals(((Dimension) o).width, ((Dimension) o).height));
    }
}
