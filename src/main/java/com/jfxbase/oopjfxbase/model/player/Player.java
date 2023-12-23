package com.jfxbase.oopjfxbase.model.player;

import com.jfxbase.oopjfxbase.utils.enums.Color;

public class Player {

    private final Color color;

    public Player(Color color) {
        this.color = color;
    }

    @Override
    public int hashCode() {
        return 7;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Player other = (Player) obj;
        return this.color == other.color;
    }

    @Override
    public String toString() {
        if (color == Color.BLACK) {
            return "white";
        }
        return "black";
    }

    public Color getColor() {
        return color;
    }
}
