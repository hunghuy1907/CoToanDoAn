package com.hungth.cotoan.data.resource.local;

import android.content.Context;

import com.hungth.cotoan.data.ChessManDataSource;
import com.hungth.cotoan.data.model.ChessBoard;
import com.hungth.cotoan.data.model.ChessMan;

import java.util.List;

public class ChessmanLocalDataSource implements ChessManDataSource.ChessManLocalDataSource {

    private Context mContext;

    public ChessmanLocalDataSource(Context mContext) {
        this.mContext = mContext;
    }

    public static ChessmanLocalDataSource sInstance;

    public static ChessmanLocalDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ChessmanLocalDataSource(context);
        }
        return sInstance;
    }

    @Override
    public List<ChessMan> getChessmanReds(int left, int right, int top, int bottom, int type) {
        return new GetChessmanFromDrawable(mContext).getChessmanRed(left, right, top, bottom, type);
    }

    @Override
    public List<ChessMan> getChessmanBlues(int left, int right, int top, int bottom, int type) {
        return new GetChessmanFromDrawable(mContext).getChessmanBlue(left, right, top, bottom, type);
    }

    @Override
    public List<ChessBoard> getBoardChess(int left, int right, int top, int bottom, boolean isEmpty) {
        return new GetChessmanFromDrawable(mContext).getChessBoards(left, right, top, bottom);
    }
}
