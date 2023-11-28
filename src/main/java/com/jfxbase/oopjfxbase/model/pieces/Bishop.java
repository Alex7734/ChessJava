package com.jfxbase.oopjfxbase.model.pieces;

import  com.jfxbase.oopjfxbase.model.*;
import com.jfxbase.oopjfxbase.utils.enums.Color;
import com.jfxbase.oopjfxbase.utils.enums.Direction;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece{

    public Bishop(Color color){
        super(color);
    }

    public String getName(){
        return color + "Bishop";
    }

    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> moves = new ArrayList<>();
        
        diagonal(board, position, Direction.NW, moves);
        diagonal(board, position, Direction.NE, moves);
        diagonal(board, position, Direction.SW, moves);
        diagonal(board, position, Direction.SE, moves);
        
        return moves;
    }

    @Override
    public Piece copy() {
        return new Bishop(this.color);
    }

}
