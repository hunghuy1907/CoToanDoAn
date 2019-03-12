package com.hungth.cotoan.data.model;

public class ChessBoard {
    private float mX;
    private float m;
    private boolean isEmpty;

    public ChessBoard(float mX, float m, boolean isEmpty) {
        this.mX = mX;
        this.m = m;
        this.isEmpty = isEmpty;
    }

    public float getmX() {
        return mX;
    }

    public void setmX(float mX) {
        this.mX = mX;
    }

    public float getM() {
        return m;
    }

    public void setM(float m) {
        this.m = m;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }
}
