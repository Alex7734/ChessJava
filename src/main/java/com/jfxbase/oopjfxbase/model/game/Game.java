package com.jfxbase.oopjfxbase.model.game;

import com.jfxbase.oopjfxbase.interfaces.GameModel;
import com.jfxbase.oopjfxbase.model.player.Player;
import com.jfxbase.oopjfxbase.model.pieces.*;
import com.jfxbase.oopjfxbase.utils.Logger;
import com.jfxbase.oopjfxbase.utils.enums.Color;
import com.jfxbase.oopjfxbase.utils.enums.Direction;
import com.jfxbase.oopjfxbase.utils.enums.GameState;

import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class Game implements GameModel {

    private Board board;
    private final Player white;
    private final Player black;
    private Player currentPlayer;
    private final King whiteKing;
    private final King blackKing;
    private GameState state;
    private Position pawnEnPassant;
    private final Stack<Board> boardHistory = new Stack<>();


    public Game() {
        white = new Player(Color.WHITE);
        black = new Player(Color.BLACK);
        board = new Board(null);
        whiteKing = new King(Color.WHITE);
        blackKing = new King(Color.BLACK);
    }

    @Override
    public void start() {
        for (int i = 0; i < board.getBoard().length; i++) {
            board.setPiece(new Pawn(Color.WHITE), new Position(1, i));
            board.setPiece(new Pawn(Color.BLACK), new Position(6, i));
            switch (i) {
                case 0, 7 -> {
                    board.setPiece(new Rook(Color.WHITE), new Position(0, i));
                    board.setPiece(new Rook(Color.BLACK), new Position(7, i));
                }
                case 1, 6 -> {
                    board.setPiece(new Knight(Color.WHITE), new Position(0, i));
                    board.setPiece(new Knight(Color.BLACK), new Position(7, i));
                }
                case 2, 5 -> {
                    board.setPiece(new Bishop(Color.WHITE), new Position(0, i));
                    board.setPiece(new Bishop(Color.BLACK), new Position(7, i));
                }
                case 3 -> {
                    board.setPiece(new Queen(Color.WHITE), new Position(0, i));
                    board.setPiece(new Queen(Color.BLACK), new Position(7, i));
                }
                case 4 -> {
                    board.setPiece(whiteKing, new Position(0, i));
                    board.setPiece(blackKing, new Position(7, i));
                }
            }
        }
        currentPlayer = white;
        state = GameState.PLAY;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public Player getOppositePlayer() {
        return currentPlayer.getColor() == Color.BLACK ? white : black;
    }

    @Override
    public List<Position> getPossibleMoves(Position position) {
        return board.getPiece(position).getPossibleMoves(position, board);
    }

    @Override
    public Piece getPiece(Position pos) {
        if (!board.contains(pos)) {
            Logger.error("The given position is not on the board.");
        }
        return board.getPiece(pos);
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public boolean isCurrentPlayerPosition(Position pos) {
        if (!board.contains(pos)) {
            throw new IllegalArgumentException("La position donnée n'est pas dans le tableau.");
        }
        if (getPiece(pos) == null) {
            throw new IllegalArgumentException("Il n'y a pas de piece à cette position.");
        }
        return getPiece(pos).getColor() == currentPlayer.getColor();
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void movePiecePosition(Position oldPos, Position newPos) {
        if (!board.contains(oldPos)) {
            Logger.error("The first position given is not in the board.");
            throw new IllegalArgumentException("ERROR: The first position given is not in the board.");
        } else if (!board.contains(newPos)) {
            Logger.error("The second position given is not in the board.");
            throw new IllegalArgumentException("ERROR: The second position given is not in the board.");
        } else if (board.isFree(oldPos)) {
            Logger.error("There is no piece at this position");
            throw new IllegalArgumentException("ERROR: There is no piece at this position");
        } else if (!this.isCurrentPlayerPosition(oldPos)) {
            Logger.error("You can't move this piece!");
            throw new IllegalArgumentException("ERROR: You can't move this piece!");
        } else if (!board.getPiece(oldPos).getPossibleMoves(oldPos, board).contains(newPos)) {
            Logger.error("This move is not possible!");
            throw new IllegalArgumentException("ERROR: This move is not possible!");
        } else if (!isValidMove(oldPos, newPos)) {
            Logger.error("This move is not valid!");
            throw new IllegalArgumentException("ERROR: This move is not valid!");
        }

        Piece piece = getPiece(oldPos);
        board.setPiece(piece, newPos);
        board.dropPiece(oldPos);

        enPassant(piece,oldPos,newPos);
        promotion(newPos);
        castle(piece,oldPos,newPos);

        state = GameState.PLAY;

        if (check()) {
            state = GameState.CHECK;
            if (checkMate()) {
                state = GameState.CHECK_MATE;
            }
        } else if (stalemate()) {
            state = GameState.STALE_MATE;
        }
        boardHistory.push(new Board(board));
        currentPlayer = getOppositePlayer();
    }

    public List<Position> getPossibleMovesForPlayer(Player player) {
        List<Position> possibleMoves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position pos = new Position(i, j);
                Piece piece = getPiece(pos);

                if (piece != null && piece.getColor() == player.getColor()) {
                    List<Position> pieceMoves = piece.getPossibleMoves(pos, this.getBoard());

                    pieceMoves.removeIf(move -> !isValidMove(pos, move));

                    possibleMoves.addAll(pieceMoves);
                }
            }
        }

        return possibleMoves;
    }



    private void castle(Piece piece, Position oldPos, Position newPos){
        try {
            if(piece.getName().toLowerCase().contains("king")) {
                if (oldPos.next(Direction.E).next(Direction.E).equals(newPos)) {
                    Rook rook = (Rook) getPiece(newPos.next(Direction.E));
                    board.setPiece(rook, oldPos.next(Direction.E));
                    board.dropPiece(newPos.next(Direction.E));
                } else if (oldPos.next(Direction.W).next(Direction.W).equals(newPos)) {
                    Rook rook = (Rook) getPiece(newPos.next(Direction.W).next(Direction.W));
                    board.setPiece(rook, oldPos.next(Direction.W));
                    board.dropPiece(newPos.next(Direction.W).next(Direction.W));
                }
            }
        } catch (Exception ignored){}
    }

    private void promotion(Position newPos) {
        try {
            Pawn pawn = (Pawn) board.getPiece(newPos);
            if (pawn.getColor() == Color.WHITE && newPos.getRow() == 7
                    || pawn.getColor() == Color.BLACK && newPos.getRow() == 0) {
                board.setPiece(new Queen(pawn.getColor()), newPos);
            }
        } catch (Exception ignored) {}
    }

    private void enPassant(Piece piece,Position oldPos, Position newPos){
        try{
            Pawn p = (Pawn) board.getPiece(pawnEnPassant);
            p.setEnPassant(false);
        } catch (Exception ignored){}
        try{
            Pawn pawn = (Pawn) piece;
            if (pawn.getColor() == Color.WHITE && oldPos.next(Direction.N).next(Direction.N).equals(newPos)
                    || pawn.getColor() == Color.BLACK && oldPos.next(Direction.S).next(Direction.S).equals(newPos)){
                pawn.setEnPassant(true);
                pawnEnPassant = newPos;
            }
            if (pawn.getColor() == Color.WHITE && oldPos.next(Direction.N).next(Direction.E).equals(newPos)
                    || oldPos.next(Direction.N).next(Direction.W).equals(newPos)){
                board.dropPiece(newPos.next(Direction.S));
            } else if (pawn.getColor() == Color.BLACK && oldPos.next(Direction.S).next(Direction.E).equals(newPos)
                    || oldPos.next(Direction.S).next(Direction.W).equals(newPos)) {
                board.dropPiece(newPos.next(Direction.N));
            }
        } catch (Exception ignored) {}
    }

    @Override
    public boolean isValidMove(Position oldPos, Position newPos) {
        if (board.isFree(oldPos)) {
            Logger.error("There is no piece at this position");
            throw new IllegalArgumentException("ERROR: There is no piece at this position");
        } else if (!board.getPiece(oldPos).getPossibleMoves(oldPos, board).contains(newPos)) {
            Logger.error("This move is not possible!");
            throw new IllegalArgumentException("ERROR: This move is not possible!");
        }
        Piece piece = board.getPiece(oldPos);
        Color myColor = piece.getColor();
        Piece piece2 = board.getPiece(newPos);
        Color color = piece.getColor().opposite();

        board.setPiece(piece, newPos);
        board.dropPiece(oldPos);

        Position whitePosition = board.getPiecePosition(getKing(new Player(myColor)));

        boolean isValid = !getCapturePositions(new Player(color)).contains(whitePosition);

        board.setPiece(piece, oldPos);
        board.dropPiece(newPos);
        board.setPiece(piece2, newPos);

        return isValid;
    }

    private List<Position> getCapturePositions(Player player) {
        List<Position> capturesPossible = new ArrayList<>();
        List<Position> pieces = board.getPositionOccupiedBy(player);
        for (Position position : pieces) {
            List<Position> capturesPiece = board.getPiece(position).getCapturePositions(position, board);
            capturesPossible.addAll(capturesPiece);
        }
        return capturesPossible;
    }

    private King getKing(Player player) {
        return player.getColor() == Color.WHITE ? whiteKing : blackKing;
    }

    private boolean check() {
        return getCapturePositions(currentPlayer).contains(board.getPiecePosition(getKing(getOppositePlayer())));
    }

    private boolean checkMate() {
        List<Position> pieces = board.getPositionOccupiedBy(getOppositePlayer());
        for (Position piece : pieces) {
            List<Position> possibleMoves = getPossibleMoves(piece);
            for (Position possibleMove : possibleMoves) {
                if (isValidMove(piece, possibleMove)) {
                    Piece piece2 = getPiece(possibleMove);
                    board.setPiece(getPiece(piece), possibleMove);
                    board.dropPiece(piece);
                    if (!check()) {
                        board.setPiece(getPiece(possibleMove), piece);
                        board.dropPiece(possibleMove);
                        board.setPiece(piece2, possibleMove);
                        return false;
                    }
                    board.setPiece(getPiece(possibleMove), piece);
                    board.dropPiece(possibleMove);
                    board.setPiece(piece2, possibleMove);
                }
            }
        }
        return true;
    }

    private boolean stalemate() {
        List<Position> pieces = board.getPositionOccupiedBy(getOppositePlayer());
        for (Position piece : pieces) {
            List<Position> possibleMoves = getPossibleMoves(piece);
            for (Position newPos : possibleMoves) {
                if (isValidMove(piece, newPos)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void undoMove() {
        if (!boardHistory.isEmpty()) {
            board = new Board(boardHistory.pop());
            currentPlayer = getOppositePlayer();
            state = GameState.PLAY;
        }
    }
}