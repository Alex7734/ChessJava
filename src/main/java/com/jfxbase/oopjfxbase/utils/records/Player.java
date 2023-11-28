package com.jfxbase.oopjfxbase.utils.records;

import com.jfxbase.oopjfxbase.utils.enums.Color;

public record Player(Color color) {

    @Override
    public int hashCode() {
        return 7;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        return this.color == other.color;
    }

    @Override
    public String toString() {
        if (color == Color.BLACK) {
            return "white";
        }
        return "black";
    }
}
