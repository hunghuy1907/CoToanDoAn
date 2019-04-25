package com.hungth.cotoan.data.resource.local;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.hungth.cotoan.R;
import com.hungth.cotoan.data.model.ChessBoard;
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
            R.drawable.cham_do_0, R.drawable.cham_do_9, R.drawable.cham_do_8, R.drawable.cham_do_7,
            R.drawable.cham_do_6, R.drawable.cham_do_5, R.drawable.cham_do_4, R.drawable.cham_do_3,
            R.drawable.cham_do_2, R.drawable.cham_do_1
    };

    public static final int[] CHESSMAN_BLUE_DOT = {
            R.drawable.cham_xanh_0, R.drawable.cham_xanh_1, R.drawable.cham_xanh_2,
            R.drawable.cham_xanh_3, R.drawable.cham_xanh_4, R.drawable.cham_xanh_5,
            R.drawable.cham_xanh_6, R.drawable.cham_xanh_7, R.drawable.cham_xanh_8,
            R.drawable.cham_xanh_9
    };

    public static final int[] CHESSMAN_RED_NUMBER = {
            R.drawable.so_do_0, R.drawable.so_do_9, R.drawable.so_do_8, R.drawable.so_do_7,
            R.drawable.so_do_6, R.drawable.so_do_5, R.drawable.so_do_4, R.drawable.so_do_3,
            R.drawable.so_do_2, R.drawable.so_do_1
    };

    public static final int[] CHESSMAN_BLUE_NUMBER = {
            R.drawable.so_xanh_0, R.drawable.so_xanh_1, R.drawable.so_xanh_2, R.drawable.so_xanh_3,
            R.drawable.so_xanh_4, R.drawable.so_xanh_5, R.drawable.so_xanh_6, R.drawable.so_xanh_7,
            R.drawable.so_xanh_8, R.drawable.so_xanh_9
    };

    public List<ChessMan> getChessmanRed(int left, int right, int top, int bottom, int type) {
        List<ChessMan> chessManReds = new ArrayList<>();
        for (int i = 0; i < CHESSMAN_BLUE_DOT.length; i++) {
            int leftChessman, rightChessman, topChessman, bottomChessman;
            Bitmap bitmap = null;
            int value;
            if (i == 0) {
                leftChessman = left + (right - left) / 9 * 4 + 8;
                rightChessman = right - (right - left) / 9 * 4 - 8;
                topChessman = top + (bottom - top) / 11 + 12;
                bottomChessman = top + (bottom - top) / 11 * 2 - 6;
                value = 0;
            } else {
                leftChessman = left + (right - left) / 9 * (i - 1) + 8;
                rightChessman = right - (right - left) / 9 * (9 - i) - 8;
                topChessman = top + 8;
                bottomChessman = top + (bottom - top) / 11 - 8;
                value = 10 - i;
            }

            if (type == Constant.RED_DOT) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), CHESSMAN_RED_DOT[i]);
            } if (type == Constant.RED_NUMBER){
                bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                        CHESSMAN_RED_NUMBER[i]);
            }
            chessManReds.add(
                    new ChessMan(leftChessman, rightChessman, topChessman, bottomChessman, bitmap,
                            type, value));
        }

        return chessManReds;
    }

    public List<ChessMan> getChessmanBlue(int left, int right, int top, int bottom, int type) {
        List<ChessMan> chessManBlues = new ArrayList<>();
        for (int i = 0; i < CHESSMAN_BLUE_DOT.length; i++) {
            int leftChessman, rightChessman, topChessman, bottomChessman;
            Bitmap bitmap = null;
            if (i == 0) {
                leftChessman = left + (right - left) / 9 * 4 + 8;
                rightChessman = right - (right - left) / 9 * 4 - 8;
                topChessman = top + (bottom - top) / 11 * 9 + 10;
                bottomChessman = bottom - (bottom - top) / 11 - 10;
            } else {
                leftChessman = left + (right - left) / 9 * (i - 1) + 8;
                rightChessman = right - (right - left) / 9 * (9 - i) - 8;
                bottomChessman = bottom - 8;
                topChessman = bottom - (bottom - top) / 11 + 8;
            }
            if (type == Constant.BLUE_DOT) {
                bitmap =
                        BitmapFactory.decodeResource(mContext.getResources(), CHESSMAN_BLUE_DOT[i]);
            } if (type == Constant.BLUE_NUMBER){
                bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                        CHESSMAN_BLUE_NUMBER[i]);
            }

            chessManBlues.add(
                    new ChessMan(leftChessman, rightChessman, topChessman, bottomChessman, bitmap,
                            type, i));
        }

        return chessManBlues;
    }

    public List<ChessBoard> getChessBoards(int left, int right, int top, int bottom) {
        List<ChessBoard> chessBoards = new ArrayList<>();
        int leftChessBoard, rightChessBoard, topChessboard, bottomChessboard;
        int cellVertical = (bottom - top) / 11;
        int cellHorizontal = (right - left) / 9;
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 9; j++) {
                leftChessBoard = left + cellHorizontal * j;
                rightChessBoard = right - cellHorizontal * (9 - j - 1);
                topChessboard = top + cellVertical * i;
                bottomChessboard = bottom - cellVertical * (11 - i - 1);
                chessBoards.add(new ChessBoard(leftChessBoard, rightChessBoard, topChessboard,
                        bottomChessboard, null, null));
            }
        }
        return chessBoards;
    }
}
