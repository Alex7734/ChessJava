package com.jfxbase.oopjfxbase.model.pieces;

import com.jfxbase.oopjfxbase.model.*;
import com.jfxbase.oopjfxbase.utils.enums.Color;
import com.jfxbase.oopjfxbase.utils.enums.Direction;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Color color) {
        super(color);
    }

    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> moves = new ArrayList<>();
        knightMove(board, position, Direction.NE, moves);
        knightMove(board, position, Direction.NW, moves);
        knightMove(board, position, Direction.SE, moves);
        knightMove(board, position, Direction.SW, moves);
        return moves;
    }

    private void knightMove(Board board, Position position, Direction dir, List<Position> deplacement) {
        Position pos1 = position;
        Position pos2 = position;
        switch (dir) {
            case NE -> {
                pos1 = pos1.next(Direction.N).next(Direction.N).next(Direction.E);
                pos2 = pos2.next(Direction.E).next(Direction.E).next(Direction.N);
            }
            case NW -> {
                pos1 = pos1.next(Direction.N).next(Direction.N).next(Direction.W);
                pos2 = pos2.next(Direction.W).next(Direction.W).next(Direction.N);
            }
            case SE -> {
                pos1 = pos1.next(Direction.S).next(Direction.S).next(Direction.E);
                pos2 = pos2.next(Direction.E).next(Direction.E).next(Direction.S);
            }
            case SW -> {
                pos1 = pos1.next(Direction.S).next(Direction.S).next(Direction.W);
                pos2 = pos2.next(Direction.W).next(Direction.W).next(Direction.S);
            }
        }
        try {
            if (board.isFree(pos1)) {
                deplacement.add(pos1);
            }
            if (this.color != board.getPiece(pos1).color){
                deplacement.add(pos1);
            }
        } catch (Exception ignored) {}
        try {
            if (board.isFree(pos2)) {
                deplacement.add(pos2);
            }
            if (this.color != board.getPiece(pos2).color){
                deplacement.add(pos2);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public Piece copy() {
        return new Knight(this.color);
    }

    public String getName(){
        return color + "Knight";
    }
}
