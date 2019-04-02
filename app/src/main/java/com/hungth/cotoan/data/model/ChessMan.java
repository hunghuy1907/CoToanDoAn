package com.hungth.cotoan.data.model;

import android.graphics.Bitmap;

public class ChessMan {
    private int mLeft;
    private int mRight;
    private int mTop;
    private int mBottom;
    private Bitmap mBitmap;
    private int mType;
    private int value;

    public ChessMan(int mLeft, int mRight, int mTop, int mBottom, Bitmap mBitmap, int mType,
            int value) {
        this.mLeft = mLeft;
        this.mRight = mRight;
        this.mTop = mTop;
        this.mBottom = mBottom;
        this.mBitmap = mBitmap;
        this.mType = mType;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getmLeft() {
        return mLeft;
    }

    public void setmLeft(int mLeft) {
        this.mLeft = mLeft;
    }

    public int getmRight() {
        return mRight;
    }

    public void setmRight(int mRight) {
        this.mRight = mRight;
    }

    public int getmTop() {
        return mTop;
    }

    public void setmTop(int mTop) {
        this.mTop = mTop;
    }

    public int getmBottom() {
        return mBottom;
    }

    public void setmBottom(int mBottom) {
        this.mBottom = mBottom;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ChessMan chessMan = (ChessMan)obj;
        return mLeft == chessMan.mLeft
                && mRight == chessMan.mRight
                && mTop == chessMan.mTop
                && mBottom == chessMan.mBottom;
    }
}
