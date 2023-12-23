package com.jfxbase.oopjfxbase.model.game;

import com.jfxbase.oopjfxbase.model.pieces.Piece;


public class Square {
    
    private Piece piece;

    public Square(Piece piece){
        this.piece = piece;
    }

    public Piece getPiece(){
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean isFree(){
        return piece == null;
    }  
}
