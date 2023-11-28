package com.jfxbase.oopjfxbase.utils.enums;

public enum Direction {
    NW(1,-1),N(1,0),NE(1,1),W(0,-1),E(0,1),SW(-1,-1),S(-1,0),SE(-1,1);
    
    private final Integer deltaRow;
    private final Integer deltaColumn;

    private Direction(Integer deltaR, Integer deltaC){
        deltaRow = deltaR;
        deltaColumn = deltaC;
    }

    public Integer getDeltaRow() {
        return deltaRow;
    }

    public Integer getDeltaColumn() {
        return deltaColumn;
    }
}
