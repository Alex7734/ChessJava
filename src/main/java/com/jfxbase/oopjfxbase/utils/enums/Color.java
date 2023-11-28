package com.jfxbase.oopjfxbase.utils.enums;

public enum Color {
    WHITE, BLACK;
    public Color opposite(){
        return this == BLACK ? WHITE : BLACK;
    }
}
