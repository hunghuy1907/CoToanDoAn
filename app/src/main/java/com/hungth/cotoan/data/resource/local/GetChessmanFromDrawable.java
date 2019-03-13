package com.hungth.cotoan.data.resource.local;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hungth.cotoan.R;
import com.hungth.cotoan.data.model.ChessMan;
import com.hungth.cotoan.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class GetChessmanFromDrawable {

    private Context mContext;

    public GetChessmanFromDrawable(Context mContext) {
        this.mContext = mContext;
    }

    public static final int[] CHESSMAN_RED_DOT = {
            R.drawable.cham_do_0, R.drawable.cham_do_1, R.drawable.cham_do_2, R.drawable.cham_do_3,
            R.drawable.cham_do_4, R.drawable.cham_do_5, R.drawable.cham_do_6, R.drawable.cham_do_7,
            R.drawable.cham_do_8, R.drawable.cham_do_9};

    public static final int[] CHESSMAN_BLUE_DOT = {
            R.drawable.cham_xanh_0, R.drawable.cham_xanh_1, R.drawable.cham_xanh_2,
            R.drawable.cham_xanh_3, R.drawable.cham_xanh_4, R.drawable.cham_xanh_5,
            R.drawable.cham_xanh_6, R.drawable.cham_xanh_7, R.drawable.cham_xanh_8,
            R.drawable.cham_xanh_9};

    public static final int[] CHESSMAN_RED_NUMBER = {
            R.drawable.so_do_0, R.drawable.so_do_1, R.drawable.so_do_2, R.drawable.so_do_3,
            R.drawable.so_do_4, R.drawable.so_do_5, R.drawable.so_do_6, R.drawable.so_do_7,
            R.drawable.so_do_8, R.drawable.so_do_9};

    public static final int[] CHESSMAN_BLUE_NUMBER = {
            R.drawable.so_xanh_0, R.drawable.so_xanh_1, R.drawable.so_xanh_2,
            R.drawable.so_xanh_3, R.drawable.so_xanh_4, R.drawable.so_xanh_5,
            R.drawable.so_xanh_6, R.drawable.so_xanh_7, R.drawable.so_xanh_8,
            R.drawable.so_xanh_9};

    public List<ChessMan> getChessmanRed(int left, int right, int top, int bottom, int type) {
        List<ChessMan> chessManReds = new ArrayList<>();
        for (int i = 0; i < CHESSMAN_BLUE_DOT.length; i++) {
            int leftChessman = (right - left) / 9 * (i + 1);
            int rightChessman = leftChessman + (right - left) / 9;
            int topChessman = top;
            int bottomChessman = top + (bottom - top) / 9;
            Bitmap bitmap;
            if (type == Constant.DOT) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), CHESSMAN_RED_DOT[i]);
            } else {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), CHESSMAN_RED_NUMBER[i]);
            }

            chessManReds.add(new ChessMan(leftChessman, rightChessman, topChessman, bottomChessman, bitmap, type));
        }

        return chessManReds;
    }

    public List<ChessMan> getChessmanBlue(int left, int right, int top, int bottom, int type) {
        List<ChessMan> chessManBlues = new ArrayList<>();
        for (int i = 0; i < CHESSMAN_BLUE_DOT.length; i++) {
            int leftChessman = (right - left) / 9 * (i + 1);
            int rightChessman = leftChessman + (right - left) / 9;
            int bottomChessman = bottom;
            int topChessman = bottom + (bottom - top) / 9;

            Bitmap bitmap;
            if (type == Constant.DOT) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), CHESSMAN_BLUE_DOT[i]);
            } else {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), CHESSMAN_BLUE_NUMBER[i]);
            }

            chessManBlues.add(new ChessMan(leftChessman, rightChessman, topChessman, bottomChessman, bitmap, type));
        }

        return chessManBlues;
    }
}
