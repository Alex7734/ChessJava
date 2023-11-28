package com.jfxbase.oopjfxbase.model;

import com.jfxbase.oopjfxbase.interfaces.GameModel;
import com.jfxbase.oopjfxbase.model.pieces.*;
import com.jfxbase.oopjfxbase.utils.enums.Color;
import com.jfxbase.oopjfxbase.utils.enums.Direction;
import com.jfxbase.oopjfxbase.utils.enums.GameState;
import com.jfxbase.oopjfxbase.utils.records.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Game implements GameModel {

    private final Board board;
    private final Player white;
    private final Player black;
    private Player currentPlayer;
    private final King whiteKing;
    private final King blackKing;
    private GameState state;
    private  Position pawnEnPassant;
    private int fiftyMoveCounter;
    private Stack<GameSnapshot> moveHistory;


    public Game() {
        white = new Player(Color.WHITE);
        black = new Player(Color.BLACK);
        board = new Board();
        whiteKing = new King(Color.WHITE);
        blackKing = new King(Color.BLACK);
        moveHistory = new Stack<>();
    }

    @Override
    public void start() {
        for (int i = 0; i < board.getSquares().length; i++) {
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
        return currentPlayer.color() == Color.BLACK ? white : black;
    }

    @Override
    public List<Position> getPossibleMoves(Position position) {
        return board.getPiece(position).getPossibleMoves(position, board);
    }

    @Override
    public Piece getPiece(Position pos) {
        if (!board.contains(pos)) {
            throw new IllegalArgumentException("The given position is not on the board.");
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
            throw new IllegalArgumentException("The given position is not on the board.");
        }
        if (getPiece(pos) == null) {
            throw new IllegalArgumentException("There is no piece at this position.");
        }
        return getPiece(pos).getColor() == currentPlayer.color();
    }

    @Override
    public void movePiecePosition(Position oldPos, Position newPos) {
        if (!board.contains(oldPos)) {
            throw new IllegalArgumentException("The first position given does not fit in the table.");
        } else if (!board.contains(newPos)) {
            throw new IllegalArgumentException("The second position given does not fit into the table.");
        } else if (board.isFree(oldPos)) {
            throw new IllegalArgumentException("There is no piece at this position.");
        } else if (!this.isCurrentPlayerPosition(oldPos)) {
            throw new IllegalArgumentException("This piece does not belong to you.");
        } else if (!board.getPiece(oldPos).getPossibleMoves(oldPos, board).contains(newPos)) {
            throw new IllegalArgumentException("You cannot make this move.");
        } else if (!isValidMove(oldPos, newPos)) {
            throw new IllegalArgumentException("This move is invalid.");
        }

        GameSnapshot snapshot = new GameSnapshot(board.copy(), currentPlayer, state, pawnEnPassant, fiftyMoveCounter);
        moveHistory.push(snapshot);
        Piece piece = getPiece(oldPos);
        board.setPiece(piece, newPos);
        board.dropPiece(oldPos);

        enPassant(piece,oldPos,newPos);
        promotion(newPos);
        castle(piece,oldPos,newPos);

        if (check()){
            System.out.println("Check");
            this.state = GameState.CHECK;
        } else if (this.stalemate()) {
            this.state = GameState.STALE_MATE;
        } else {
            System.out.println("Play");
            this.state = GameState.PLAY;
        }

        if (board.getPiece(newPos) == null || board.getPiece(newPos) instanceof Pawn) {
            fiftyMoveCounter++;
        } else {
            fiftyMoveCounter = 0;
        }

        if (fiftyMoveCounter >= 50) {
            state = GameState.STALE_MATE;
        }

        currentPlayer = getOppositePlayer();
    }


    public void undoMove() {
        if (!moveHistory.isEmpty()) {
            GameSnapshot snapshot = moveHistory.pop();
            board.restore(snapshot.getBoard());
            currentPlayer = snapshot.getCurrentPlayer();
            state = snapshot.getState();
            pawnEnPassant = snapshot.getPawnEnPassant();
            fiftyMoveCounter = snapshot.getFiftyMoveCounter();
        }
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
        Piece piece = getPiece(newPos);
        if ("♟".equals(piece.toString())) {
            if (newPos.getRow() == 0) {
                board.setPiece(new Queen(Color.BLACK), newPos);
            }
        } else if ("♙".equals(piece.toString())) {
                if (newPos.getRow() == 7) {
                board.setPiece(new Queen(Color.WHITE), newPos);
            }
        }
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

    protected Board getBoard() {
        return board;
    }

    protected void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public boolean isValidMove(Position oldPos, Position newPos) {
        if (board.isFree(oldPos)) {
            throw new IllegalArgumentException("The given position does not contain a piece.");
        } else if (!board.getPiece(oldPos).getPossibleMoves(oldPos, board).contains(newPos)) {
            throw new IllegalArgumentException("It's impossible to move here.");
        }

        Piece piece = board.getPiece(oldPos);
        Color myColor = piece.getColor();
        Color opponentColor = piece.getColor().opposite();

        GameSnapshot snapshot = new GameSnapshot(board.copy(), currentPlayer, state, pawnEnPassant, fiftyMoveCounter);
        board.setPiece(piece, newPos);
        board.dropPiece(oldPos);

        Position kingPosition = board.getPiecePosition(getKing(new Player(myColor)));

        boolean isValid = !isSquareUnderAttack(kingPosition, opponentColor);

        board.restore(snapshot.getBoard());

        if (isValid) {
            List<Position> opponentMoves = getCapturePositions(getOppositePlayer());
            if (opponentMoves.contains(kingPosition)) {
                state = GameState.CHECK;
                if (checkMate()) {
                    state = GameState.CHECK_MATE;
                }
            }
        }

        return isValid;
    }

    private boolean isSquareUnderAttack(Position square, Color attackerColor) {
        List<Position> attackerPositions = board.getPositionOccupiedBy(new Player(attackerColor));

        for (Position attackerPos : attackerPositions) {
            List<Position> attackerMoves = board.getPiece(attackerPos).getCapturePositions(attackerPos, board);
            if (attackerMoves.contains(square)) {
                return true;
            }
        }

        return false;
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
        return player.color() == Color.WHITE ? whiteKing : blackKing;
    }


    private boolean check() {
        King king = getKing(currentPlayer);
        return isSquareUnderAttack(board.getPiecePosition(king), currentPlayer.color().opposite());
    }

    private boolean checkMate() {
        List<Position> pieces = board.getPositionOccupiedBy(currentPlayer);
        for (Position piecePos : pieces) {
            Piece piece = board.getPiece(piecePos);
            List<Position> moves = piece.getPossibleMoves(piecePos, board);

            for (Position newPos : moves) {
                GameSnapshot snapshot = new GameSnapshot(board.copy(), currentPlayer, state, pawnEnPassant, fiftyMoveCounter);
                board.setPiece(piece, newPos);
                board.dropPiece(piecePos);

                if (!check()) {
                    moveHistory.push(snapshot);
                    return false;
                }

                board.restore(snapshot.getBoard());
            }
        }
        return true;
    }

    private boolean stalemate() {
        List<Position> pieces = board.getPositionOccupiedBy(getOppositePlayer());
        boolean stalemate = false;
        for (Position piece : pieces) {
            List<Position> moves = getPossibleMoves(piece);

            for (Position newPos : moves) {
                if (isValidMove(piece, newPos)) {
                    return stalemate;
                }
            }
        }
        stalemate = true;
        return stalemate;
    }

    private static class GameSnapshot {
        private final Board board;
        private final Player currentPlayer;
        private final GameState state;
        private final Position pawnEnPassant;
        private final int fiftyMoveCounter;

        public GameSnapshot(Board board, Player currentPlayer, GameState state, Position pawnEnPassant, int fiftyMoveCounter) {
            this.board = board.copy();
            this.currentPlayer = currentPlayer;
            this.state = state;
            this.pawnEnPassant = pawnEnPassant;
            this.fiftyMoveCounter = fiftyMoveCounter;
        }

        public Board getBoard() {
            return board;
        }

        public Player getCurrentPlayer() {
            return currentPlayer;
        }

        public GameState getState() {
            return state;
        }

        public Position getPawnEnPassant() {
            return pawnEnPassant;
        }

        public int getFiftyMoveCounter() {
            return fiftyMoveCounter;
        }
    }
}
    

