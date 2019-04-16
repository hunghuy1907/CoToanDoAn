package com.hungth.cotoan.utils.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hungth.cotoan.R;
import com.hungth.cotoan.data.model.ChessBoard;
import com.hungth.cotoan.data.model.ChessMan;
import com.hungth.cotoan.utils.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChessLogic {
    public static String convertChessboardToString(List<ChessBoard> chessBoardList) {
        String chessboard = "";
        for (int i = 0; i < chessBoardList.size(); i++) {
            ChessMan chessMan = chessBoardList.get(i).getChessMan();
            String type;
            String value;
            if (chessMan == null) {
                type = "0";
                value = "0";
            } else {
                if (chessMan.getmType() == Constant.BLUE_NUMBER || chessMan.getmType() == Constant.BLUE_DOT) {
                    type = "X";
                } else {
                    type = "D";
                }
                value = chessMan.getValue() + "";
            }
            chessboard = chessboard + type + value + "_";
        }
        return chessboard.substring(0, chessboard.length() - 1);
    }

    public static List<ChessBoard> convertStringToChessboard(Context context, String stringBoard, List<ChessBoard> chessBoards) {
        List<String> strings = Arrays.asList(stringBoard.split("_"));
        for (int i = 0; i < strings.size(); i++) {
            ChessBoard chessBoard = chessBoards.get(i);
            String type = strings.get(i).substring(0, 1);
            int value = Integer.valueOf(strings.get(i).substring(1, 2));
            int typeNumber;
            Bitmap bitmap;
            if (type.equals("0")) {
                chessBoard.setChessMan(null);
            } else {
                if (type.equals("X")) {
                    bitmap = setBitmapBlue(context, value);
                    typeNumber = Constant.BLUE_NUMBER;
                } else {
                    bitmap = setBitmapRed(context, value);
                    typeNumber = Constant.RED_NUMBER;
                }
                ChessMan chessMan = new ChessMan(chessBoard.getLeft() + 8, chessBoard.getRight() - 8,
                        chessBoard.getTop() + 12, chessBoard.getBottom() - 12,
                        bitmap, typeNumber, value);
                chessBoard.setChessMan(chessMan);
            }
        }
        return chessBoards;
    }

    public static List<ChessBoard> revertListChessboard(List<ChessBoard> chessBoards) {
        List<ChessBoard> boardRevert = new ArrayList<>();
        for (int i = 10; i >= 0; i--) {
            for (int j = 0; j < 9; j++) {
                boardRevert.add(chessBoards.get(i * 9 + j));
            }
        }
        return boardRevert;
    }

    public static Bitmap setBitmapBlue(Context context, int value) {
        switch (value) {
            case 0:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_xanh_0);
            case 1:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_xanh_1);
            case 2:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_xanh_2);
            case 3:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_xanh_3);
            case 4:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_xanh_4);
            case 5:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_xanh_5);
            case 6:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_xanh_6);
            case 7:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_xanh_7);
            case 8:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_xanh_8);
            case 9:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_xanh_9);
            default:
                return null;
        }
    }

    public static Bitmap setBitmapRed(Context context, int value) {
        switch (value) {
            case 0:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_do_0);
            case 1:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_do_1);
            case 2:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_do_2);
            case 3:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_do_3);
            case 4:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_do_4);
            case 5:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_do_5);
            case 6:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_do_6);
            case 7:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_do_7);
            case 8:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_do_8);
            case 9:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.so_do_9);
            default:
                return null;
        }
    }
}
