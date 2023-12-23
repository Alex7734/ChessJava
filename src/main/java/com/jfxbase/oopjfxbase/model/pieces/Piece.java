package com.jfxbase.oopjfxbase.model.pieces;

import com.jfxbase.oopjfxbase.model.game.Board;
import com.jfxbase.oopjfxbase.model.game.Position;
import com.jfxbase.oopjfxbase.utils.enums.Color;
import com.jfxbase.oopjfxbase.utils.enums.Direction;

import java.util.List;

public abstract class Piece {

    Color color;

    public Piece(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public abstract List<Position> getPossibleMoves(Position position, Board board);

    public abstract Piece copy();

    public List<Position> getCapturePositions(Position position, Board board) {
        return  getPossibleMoves(position, board);
    }


    protected void canAttack(Board board, Position position, Direction attaque, List<Position> deplacement) {
        Position pos = position.next(attaque);

        if (board.contains(pos)) {
            if (board.containsOppositeColor(pos, color)) {
                deplacement.add(pos);
            }
        }
    }

    protected boolean isEmptyPosition(Board board, Position pos) {
        if (board.contains(pos)) {
            return board.isFree(pos);
        }
        return false;
    }

    protected boolean containsColor(Board board, Position pos, Color col){
        if (!board.contains(pos)) {
            throw new IllegalArgumentException("The given position is not on the board.");
        }
        if (board.isFree(pos)) {
            return false;
        }
        return this.color == col;
    }
    
    protected void line(Board board, Position position, Direction dir, List<Position> moves) {
        Position pos = position;
        try{
        switch (dir) {
            case E -> {
                pos = pos.next(Direction.E);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            moves.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        moves.add(pos);
                        pos = pos.next(Direction.E);
                        
                    }
                }
            case N -> {
                pos = pos.next(Direction.N);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            moves.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        moves.add(pos);
                        pos = pos.next(Direction.N);
                        
                    }
                    
                }
            case S -> {
                pos = pos.next(Direction.S);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            moves.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        moves.add(pos);
                        pos = pos.next(Direction.S);
                    }
                    
                }
            case W -> {
                pos = pos.next(Direction.W);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            moves.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        moves.add(pos);
                        pos = pos.next(Direction.W);
                    }
                } 
            } 
        } catch(Exception e){}
    }

    protected void diagonal(Board board, Position position, Direction dir, List<Position> moves) {
        Position pos = position;
        try {
            switch (dir) {
                case NE -> {
                    pos = pos.next(Direction.NE);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            moves.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        moves.add(pos);
                        pos = pos.next(Direction.NE);
                    }
                }
                case NW -> {
                    pos = pos.next(Direction.NW);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            moves.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        moves.add(pos);
                        pos = pos.next(Direction.NW);
                    }
                }
                case SE -> {
                    pos = pos.next(Direction.SE);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            moves.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        moves.add(pos);
                        pos = pos.next(Direction.SE);
                    }
                }
                case SW -> {
                    pos = pos.next(Direction.SW);
                    while (true) {
                        if (board.containsOppositeColor(pos, color)) {
                            moves.add(pos);
                            break;
                        }
                        if(containsColor(board, pos, color)){
                            break;
                        }
                        moves.add(pos);
                        pos = pos.next(Direction.SW);
                    }
                }
            }
        } catch (Exception e) {}
    }

    public abstract String getName();

    @Override
    public int hashCode() {
        return 3;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Piece other = (Piece) obj;
        return this.color == other.color;
    }

}
