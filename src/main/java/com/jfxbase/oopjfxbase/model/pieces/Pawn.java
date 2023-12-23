package com.jfxbase.oopjfxbase.model.pieces;

import com.jfxbase.oopjfxbase.model.game.Board;
import com.jfxbase.oopjfxbase.model.game.Position;
import com.jfxbase.oopjfxbase.utils.enums.Color;
import com.jfxbase.oopjfxbase.utils.enums.Direction;

import java.util.ArrayList;
import java.util.List;


public class Pawn extends Piece {
    private boolean enPassant;

    public Pawn(Color color) {
        super(color);
    }

    @Override
    public List<Position> getPossibleMoves(Position position, Board board) {
        List<Position> moves = new ArrayList<>();
        boolean isBlack = this.color == Color.BLACK;
        if (isBlack) {
            whenAdvancing(board, position, Direction.S, moves);
            canAttack(board, position, Direction.SW, moves);
            canAttack(board, position, Direction.SE, moves);
        } else {
            whenAdvancing(board, position, Direction.N, moves);
            canAttack(board, position, Direction.NW, moves);
            canAttack(board, position, Direction.NE, moves);
        }
        return moves;
    }

    private void whenCapturing(Board board, Position position, Direction direction, List<Position> capture) {
        Position pos = position.next(direction);
        if (board.contains(pos)) {
            if (board.getPiece(pos) != null) {
                if (board.getPiece(position).color != board.getPiece(pos).getColor()){
               capture.add(pos); 
            }
            } else {
                capture.add(pos);
            }
            
        }
    }


    private void whenAdvancing(Board board, Position position, Direction mouvement, List<Position> deplacement) {
        Position pos = position.next(mouvement);
        if (isEmptyPosition(board, pos)) {
            deplacement.add(pos);
            if (board.getInitialPawnRow(color).equals(position.getRow())) {
                pos = pos.next(mouvement);
                if (isEmptyPosition(board,pos)) {
                    deplacement.add(pos);
                }
            }
        }
        canEnPassant(board,position,deplacement,mouvement);
    }

    private void canEnPassant(Board board, Position position,List<Position> deplacement,Direction mouvement){
        String color = getColor() == Color.BLACK ? "WHITE": "BLACK";
        try {
            Pawn pawnPassedWest = (Pawn) board.getPiece(position.next(Direction.W));
            if (pawnPassedWest.getName().equals(color + "Pawn") && pawnPassedWest.enPassant
                    && board.isFree(position.next(Direction.W).next(mouvement))) {
                deplacement.add(position.next(Direction.W).next(mouvement));
            }
        } catch (Exception ignored) {}
        try {
            Pawn pawnPassedEast = (Pawn) board.getPiece(position.next(Direction.E));
            if (pawnPassedEast.getName().equals(color + "Pawn") && pawnPassedEast.enPassant
                    && board.isFree(position.next(Direction.E).next(mouvement))) {
                deplacement.add(position.next(Direction.E).next(mouvement));
            }
        } catch (Exception ignored) {}
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    public String getName(){
        return color + "Pawn";
    }

    @Override
    public List<Position> getCapturePositions(Position position, Board board) {
        if (board.getPiece(position) != this) {
            throw new IllegalArgumentException("cette position ne contient pas la piece");
        }
        List<Position> capture = new ArrayList<>();
        boolean isBlack = this.color == Color.BLACK;
        if (isBlack) {
            whenCapturing(board, position, Direction.SE, capture);
            whenCapturing(board, position, Direction.SW, capture);
        } else {
            whenCapturing(board, position, Direction.NE, capture);
            whenCapturing(board, position, Direction.NW, capture);
        }
        return capture;
    }

    @Override
    public Piece copy() {
        return new Pawn(this.color);
    }
}
