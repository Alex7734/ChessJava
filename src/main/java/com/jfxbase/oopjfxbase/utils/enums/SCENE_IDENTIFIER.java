package com.jfxbase.oopjfxbase.utils.enums;

public enum SCENE_IDENTIFIER {
    MENU("Menu.fxml"),
    HELLO("hello-view.fxml"),
    GOOD_BYE("good-bye-view.fxml");

    public final String label;

    SCENE_IDENTIFIER(String label) {
        this.label = label;
    }
}
