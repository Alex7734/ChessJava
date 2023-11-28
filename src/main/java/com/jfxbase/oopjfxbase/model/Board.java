package com.jfxbase.oopjfxbase.model;

import com.jfxbase.oopjfxbase.model.pieces.Piece;
import com.jfxbase.oopjfxbase.utils.enums.Color;
import com.jfxbase.oopjfxbase.utils.records.Player;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final Square[][] squares;

    public Board() {
        squares = new Square[8][8];
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares.length; j++) {
                squares[i][j] = new Square(null);
            }
        }
    }

    public Square[][] getSquares() {
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
                    if (player.color() == getPiece(pos).getColor()) {
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

    public Board copy() {
        Board copiedBoard = new Board();

        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                Position pos = new Position(j, i);
                Piece originalPiece = getPiece(pos);
                Piece copiedPiece = (originalPiece != null) ? originalPiece.copy() : null;
                copiedBoard.setPiece(copiedPiece, pos);
            }
        }

        return copiedBoard;
    }

    public void restore(Board otherBoard) {
        Square[][] otherSquares = otherBoard.getSquares();

        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                Position pos = new Position(j, i);
                Piece otherPiece = otherBoard.getPiece(pos);
                Piece thisPiece = (otherPiece != null) ? otherPiece.copy() : null;

                squares[i][j].setPiece(thisPiece);
            }

        }
    }
}
