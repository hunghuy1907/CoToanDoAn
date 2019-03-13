package com.hungth.cotoan.data.model;

import android.graphics.Bitmap;

public class ChessMan {
    private int mLeft;
    private int mRight;
    private int mTop;
    private int mBottom;
    private Bitmap mBitmap;
    private int mType;

    public ChessMan(int mLeft, int mRight, int mTop, int mBottom, Bitmap mBitmap, int mType) {
        this.mLeft = mLeft;
        this.mRight = mRight;
        this.mTop = mTop;
        this.mBottom = mBottom;
        this.mBitmap = mBitmap;
        this.mType = mType;
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
}
