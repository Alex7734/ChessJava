package com.jfxbase.oopjfxbase.model.game;

import com.jfxbase.oopjfxbase.model.player.Player;
import com.jfxbase.oopjfxbase.model.pieces.Piece;
import com.jfxbase.oopjfxbase.utils.enums.Color;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final Square[][] squares;

    public Board(Board board) {
        squares = new Square[8][8];
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares.length; j++) {
                squares[i][j] = new Square(null);
            }
        }
    }

    public Square[][] getBoard() {
        return squares;
    }

    public boolean contains(Position pos) {
        Integer row = pos.getRow();
        Integer column = pos.getColumn();

        return !(row < 0 || row > squares.length - 1 || column < 0 || column > squares.length - 1);
    }

    public void setPiece(Piece piece, Position position) {
        if (!contains(position)) {
            throw new IllegalArgumentException("The given position is not on the board.");
        }
        Integer row = position.getRow();
        Integer column = position.getColumn();

        squares[column][row].setPiece(piece);
    }

    public Piece getPiece(Position pos) {
        if (!contains(pos)) {
            throw new IllegalArgumentException("The given position is not on the board.");
        }
        Integer row = pos.getRow();
        Integer column = pos.getColumn();

        return squares[column][row].getPiece();
    }

    public Integer getInitialPawnRow(Color color) {
        return color == Color.BLACK ? 6 : 1;
    }

    public void dropPiece(Position pos) {
        if (!contains(pos)) {
            throw new IllegalArgumentException("The given position is not on the board.");
        }
        setPiece(null, pos);
    }

    public boolean isFree(Position pos) {
        if (!contains(pos)) {
            throw new IllegalArgumentException("The given position is not on the board.");
        }
        Integer row = pos.getRow();
        Integer column = pos.getColumn();

        return squares[column][row].isFree();
    }

    public boolean containsOppositeColor(Position pos, Color col) {
        if (!contains(pos)) {
            throw new IllegalArgumentException("The given position is not on the board.");
        }
        if (isFree(pos)) {
            return false;
        }
        return this.getPiece(pos).getColor() != col;
    }

    public List<Position> getPositionOccupiedBy(Player player) {
        List<Position> nbPieces = new ArrayList<>();

        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                Position pos = new Position(j, i);
                if (!isFree(pos)) {
                    if (player.getColor() == getPiece(pos).getColor()) {
                        nbPieces.add(new Position(j, i));
                    }
                }
            }
        }
        return nbPieces;
    }

    public Position getPiecePosition(Piece piece) {
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                Position pos = new Position(j, i);
                if (getPiece(pos) == piece) {
                    return pos;
                }
            }
        }
        return null;
    }
}
