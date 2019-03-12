package com.hungth.cotoan.data.model;

import android.graphics.Bitmap;

public class ChessMan {
    private int mTypr;
    private float mX;
    private float mY;
    private Bitmap mBitmap;

    public ChessMan(int mTypr, float mX, float mY, Bitmap mBitmap) {
        this.mTypr = mTypr;
        this.mX = mX;
        this.mY = mY;
        this.mBitmap = mBitmap;
    }

    public int getmTypr() {
        return mTypr;
    }

    public void setmTypr(int mTypr) {
        this.mTypr = mTypr;
    }

    public float getmX() {
        return mX;
    }

    public void setmX(float mX) {
        this.mX = mX;
    }

    public float getmY() {
        return mY;
    }

    public void setmY(float mY) {
        this.mY = mY;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }
}
