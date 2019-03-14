package com.hungth.cotoan.data.model;

public class ChessBoard {
    private int left;
    private int right;
    private int top;
    private int bottom;
    private boolean isEmpty;

    public ChessBoard(int left, int right, int top, int bottom, boolean isEmpty) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.isEmpty = isEmpty;
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

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }
}
