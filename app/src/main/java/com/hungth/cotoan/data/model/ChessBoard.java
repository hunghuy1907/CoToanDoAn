package com.hungth.cotoan.data.model;

import android.graphics.Bitmap;

public class ChessBoard {
    private int left;
    private int right;
    private int top;
    private int bottom;
    private ChessMan chessMan;
    private Bitmap bitmap;

    public ChessBoard(int left, int right, int top, int bottom, ChessMan chessMan, Bitmap bitmap) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.chessMan = chessMan;
        this.bitmap = bitmap;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public ChessMan getChessMan() {
        return chessMan;
    }

    public void setChessMan(ChessMan chessMan) {
        this.chessMan = chessMan;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
