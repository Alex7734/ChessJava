package com.jfxbase.oopjfxbase.controllers;

import com.jfxbase.oopjfxbase.view.BoardGrid;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuController {
    @FXML
    private VBox root;

    public void play() {
        Stage stage = (Stage) root.getScene().getWindow();
        BoardGrid board = new BoardGrid(stage);
        Scene scene = new Scene(board);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.Z) {
                board.undoMove();
                board.displayBackground();
            }
        });

        stage.setScene(scene);
    }



    public void logout() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
        System.exit(0);
    }


}