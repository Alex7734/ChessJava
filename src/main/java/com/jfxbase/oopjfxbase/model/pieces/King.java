package com.jfxbase.oopjfxbase.model.pieces;

import com.jfxbase.oopjfxbase.model.game.Board;
import com.jfxbase.oopjfxbase.model.game.Position;
import com.jfxbase.oopjfxbase.utils.enums.Color;
import com.jfxbase.oopjfxbase.utils.enums.Direction;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(Color color) {
        super(color);
    }

    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> moves = new ArrayList<>();
        kingMove(board, position, Direction.E, moves);
        kingMove(board, position, Direction.N, moves);
        kingMove(board, position, Direction.NE, moves);
        kingMove(board, position, Direction.NW, moves);
        kingMove(board, position, Direction.S, moves);
        kingMove(board, position, Direction.SE, moves);
        kingMove(board, position, Direction.SW, moves);
        kingMove(board, position, Direction.W, moves);
        return moves;
    }

    public String getName(){
        return color + "King";
    }

    private void kingMove(Board board, Position position, Direction dir, List<Position> deplacement) {
        Position pos = position.next(dir);
        try {
            if (board.isFree(pos)) {
                deplacement.add(pos);
            } else if (this.color != board.getPiece(pos).color){
                deplacement.add(pos);
            }
            if(getInitialKingRow(color).equals(position.getRow()) &&
                board.getPiece(pos) == null && board.getPiece(pos.next(dir)) == null){
                if(dir == Direction.E){
                    deplacement.add(pos.next(dir));
                } else if (dir == Direction.W && board.getPiece(pos.next(dir).next(dir)) == null) {
                    deplacement.add(pos.next(dir));
                }
            }
        } catch (Exception ignored) {}
    }

    @Override
    public Piece copy() {
        return new King(this.color);
    }

    public Integer getInitialKingRow(Color color) {
        return color == Color.BLACK ? 7 : 0;
    }
}
