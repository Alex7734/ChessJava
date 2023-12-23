package com.jfxbase.oopjfxbase.interfaces;

import com.jfxbase.oopjfxbase.model.game.Position;
import com.jfxbase.oopjfxbase.model.pieces.Piece;
import com.jfxbase.oopjfxbase.utils.enums.GameState;
import com.jfxbase.oopjfxbase.model.player.Player;

import java.util.List;

public interface GameModel {

    public void start();

    public Player getCurrentPlayer();

    public Player getOppositePlayer();

    public List<Position> getPossibleMoves(Position position);

    public Piece getPiece(Position pos);

    public boolean isCurrentPlayerPosition(Position pos);

    public void movePiecePosition(Position oldPos, Position newPos);

    public GameState getState();

    public boolean isValidMove(Position oldPos, Position newPos);

}
