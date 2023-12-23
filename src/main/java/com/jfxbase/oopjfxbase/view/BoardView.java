package com.jfxbase.oopjfxbase.view;

import com.jfxbase.oopjfxbase.controllers.ResultController;
import com.jfxbase.oopjfxbase.interfaces.GameModel;
import com.jfxbase.oopjfxbase.model.game.Game;
import com.jfxbase.oopjfxbase.model.game.Position;
import com.jfxbase.oopjfxbase.model.pieces.Piece;
import com.jfxbase.oopjfxbase.utils.enums.Direction;
import com.jfxbase.oopjfxbase.utils.enums.GameState;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BoardView extends GridPane {

    private GameModel model;
    private Piece selectedPiece;
    private double startX;
    private double startY;
    private final double  SQUARE_SIZE = 64;
    private List<Position> highlightedPositions = new ArrayList<>();

    Stage stage;

    public BoardView(Stage stage) {
        this.stage = stage;
        initModel();
        displayBackground();
    }

    public void undoMove() {
        if (model instanceof Game game) {
            game.undoMove();
            displayBackground();
        }
    }

    public void displayBackground() {
        clearHighlights();

        for (Position position : highlightedPositions) {
            ImageView highlightImageView = new ImageView(getImage("highlight"));
            highlightImageView.toBack();
            this.add(highlightImageView, position.getColumn(), 7 - position.getRow());
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position position = new Position(j, i);
                ImageView backgroundImageView;

                if (i % 2 == 0 && j % 2 != 0 || i % 2 != 0 && j % 2 == 0) {
                    backgroundImageView = new ImageView(getImage("bgGreen2"));
                } else {
                    backgroundImageView = new ImageView(getImage("bgGreen1"));
                }

                Piece piece = model.getPiece(position);
                if (piece != null) {
                    StackPane stackPane = new StackPane();
                    ImageView pieceImageView = new ImageView(getImage(piece.getName()));
                    stackPane.getChildren().addAll(backgroundImageView, pieceImageView);
                    makeDraggable(pieceImageView, position);
                    stackPane.toFront();
                    this.add(stackPane, i, 7 - j);
                } else {
                    this.add(backgroundImageView, i, 7 - j);
                }
            }
        }
    }

    private Image getImage(String str) {
        switch (str) {
            case "highlight" -> str = "img/highlighted.png";
            case "bgGreen1" -> str = "img/boardGreen1.png";
            case "bgGreen2" -> str = "img/boardGreen2.png";
            case "BLACKRook" -> str = "img/blackRook.png";
            case "WHITERook" -> str = "img/whiteRook.png";
            case "BLACKBishop" -> str = "img/blackBishop.png";
            case "WHITEBishop" -> str = "img/whiteBishop.png";
            case "BLACKKing" -> str = "img/blackKing.png";
            case "WHITEKing" -> str = "img/whiteKing.png";
            case "BLACKQueen" -> str = "img/blackQueen.png";
            case "WHITEQueen" -> str = "img/whiteQueen.png";
            case "BLACKPawn" -> str = "img/blackPawn.png";
            case "WHITEPawn" -> str = "img/whitePawn.png";
            case "BLACKKnight" -> str = "img/blackKnight.png";
            case "WHITEKnight"-> str = "img/whiteKnight.png";
        }
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(str)),SQUARE_SIZE,SQUARE_SIZE,true,true);
    }

    private void initModel() {
        model = new Game();
        model.start();
    }


    private void highlightValidMoves(Piece selectedPiece, Position selectedPos) {
        clearHighlights();

        if (selectedPiece != null && model.getCurrentPlayer().getColor() == selectedPiece.getColor()) {
            highlightedPositions = model.getPossibleMoves(selectedPos);

            for (Position move : highlightedPositions) {
                ImageView highlightImageView = new ImageView(getImage("highlight"));
                highlightImageView.toBack();

                StackPane stackPane = new StackPane();
                stackPane.getChildren().add(highlightImageView);

                Piece capturedPiece = model.getPiece(move);
                if (capturedPiece != null) {
                    ImageView capturedPieceImageView = new ImageView(getImage(capturedPiece.getName()));
                    stackPane.getChildren().add(capturedPieceImageView);
                }

                this.add(stackPane, move.getColumn(), 7 - move.getRow());
            }
        }
    }

    private void clearHighlights() {
        for (Position position : highlightedPositions) {
            this.getChildren().removeIf(node -> Objects.equals(GridPane.getColumnIndex(node), position.getColumn()) &&
                    GridPane.getRowIndex(node) == 7 - position.getRow());
        }

        highlightedPositions.clear();
    }


    private void makeDraggable(Node node, Position oldPos) {
        if (model.getState() != GameState.CHECK_MATE && model.getState() != GameState.STALE_MATE) {
            node.setOnMouseEntered(e -> {
                node.setCursor(Cursor.CLOSED_HAND);
            });
            node.setOnMousePressed(e -> {
                startX = e.getSceneX() - node.getTranslateX();
                startY = e.getSceneY() - node.getTranslateY();
                if (node.getParent() != null) {
                    node.getParent().toFront();
                }

                selectedPiece = model.getPiece(oldPos);
                highlightValidMoves(selectedPiece, oldPos);
            });
            node.setOnMouseDragged(e -> {
                node.setTranslateX(e.getSceneX() - startX);
                node.setTranslateY(e.getSceneY() - startY);
                stage.getScene().setCursor(Cursor.CLOSED_HAND);

                if (node.getParent() instanceof StackPane) {
                    node.getParent().toFront();
                }
            });
            node.setOnMouseReleased(e -> {
                stage.getScene().setCursor(Cursor.DEFAULT);
                if (hasMoved(-node.getTranslateY(), node.getTranslateX())) {
                    Position newPos = oldPos;
                    double transalationY = -node.getTranslateY();
                    double transalationX = node.getTranslateX();
                    if (transalationY >= SQUARE_SIZE / 2.0645) { // UP
                        while (transalationY >= SQUARE_SIZE / 2.0645) {
                            newPos = newPos.next(Direction.N);
                            transalationY = transalationY - SQUARE_SIZE;
                        }
                    } else if (transalationY <= -SQUARE_SIZE / 2.37037) { // DOWN
                        while (transalationY <= -SQUARE_SIZE / 2.37037) {
                            newPos = newPos.next(Direction.S);
                            transalationY = transalationY + SQUARE_SIZE;
                        }
                    }
                    if (transalationX >= SQUARE_SIZE / 2.112) { // RIGHT
                        while (transalationX >= SQUARE_SIZE / 2.112) {
                            newPos = newPos.next(Direction.E);
                            transalationX = transalationX - SQUARE_SIZE;
                        }
                    } else if (transalationX <= -SQUARE_SIZE / 1.4545) { // LEFT
                        while (transalationX <= -SQUARE_SIZE / 1.4545) {
                            newPos = newPos.next(Direction.W);
                            transalationX = transalationX + SQUARE_SIZE;
                        }
                    }
                    try {
                        model.movePiecePosition(oldPos, newPos);
                        clearHighlights();
                    } catch (IllegalArgumentException ex) {
                        ImageView invalidMoveImageView = new ImageView(getImage("invalidMove"));
                        StackPane stackPane = new StackPane();
                        stackPane.getChildren().addAll(invalidMoveImageView, node);
                        this.add(stackPane, oldPos.getColumn(), 7 - oldPos.getRow());
                        displayBackground();

                        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), evt -> {
                            this.getChildren().remove(stackPane);
                            displayBackground();
                        }));
                        timeline.play();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                    selectedPiece = null;
                    clearHighlights();
                } else {
                    clearHighlights();
                }
                displayBackground();
                if (model.getState() == GameState.CHECK_MATE || model.getState() == GameState.STALE_MATE) {
                    URL FxmlLocation = getClass().getResource("Result.fxml");
                    FXMLLoader loader = new FXMLLoader(FxmlLocation);
                    try {
                        Stage secondaryStage = new Stage();
                        stage.setResizable(false);
                        Scene scene = new Scene(loader.load());
                        secondaryStage.setScene(scene);
                        secondaryStage.show();
                        ResultController controller = loader.getController();
                        controller.getPrimaryStage(stage, model.getCurrentPlayer().toString());
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            });
        }
    }

    private boolean hasMoved(double y,double x){
        return y <= -SQUARE_SIZE/2.37037 || y >= SQUARE_SIZE/2.0645 || x >= SQUARE_SIZE/2.112 || x <= -SQUARE_SIZE/2.112;
    }
}
