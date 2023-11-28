package com.jfxbase.oopjfxbase.model.pieces;

import com.jfxbase.oopjfxbase.model.*;
import com.jfxbase.oopjfxbase.utils.enums.Color;
import com.jfxbase.oopjfxbase.utils.enums.Direction;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece{

    public Rook(Color color){
        super(color);
    }

    public String getName(){
        return color + "Rook";
    }

    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> moves = new ArrayList<>();
        line(board, position, Direction.E, moves);
        line(board, position, Direction.N, moves);
        line(board, position, Direction.S, moves);
        line(board, position, Direction.W, moves);
        
        return moves;
    }

    @Override
    public Piece copy() {
        return new Rook(this.color);
    }
}
