package com.jfxbase.oopjfxbase.model.pieces;

import com.jfxbase.oopjfxbase.model.game.Board;
import com.jfxbase.oopjfxbase.model.game.Position;
import com.jfxbase.oopjfxbase.utils.enums.Color;
import com.jfxbase.oopjfxbase.utils.enums.Direction;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen (Color color){
        super(color);
    }

    public String getName(){
        return color + "Queen";
    }

    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> moves = new ArrayList<>();
        line(board, position, Direction.E, moves);
        line(board, position, Direction.N, moves);
        line(board, position, Direction.S, moves);
        line(board, position, Direction.W, moves);
        diagonal(board, position, Direction.NW, moves);
        diagonal(board, position, Direction.NE, moves);
        diagonal(board, position, Direction.SW, moves);
        diagonal(board, position, Direction.SE, moves);
        
        return moves;
    }

    @Override
    public Piece copy() {
        return new Queen(this.color);
    }
}
