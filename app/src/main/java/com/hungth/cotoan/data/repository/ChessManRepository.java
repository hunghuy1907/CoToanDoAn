package com.hungth.cotoan.data.repository;

import com.hungth.cotoan.data.model.ChessMan;
import com.hungth.cotoan.data.resource.local.ChessmanLocalDataSource;

import java.util.List;

public class ChessManRepository {
    public static ChessManRepository sInstance;
    public ChessmanLocalDataSource mChessmanLocalDataSource;

    public ChessManRepository(ChessmanLocalDataSource mChessmanLocalDataSource) {
        this.mChessmanLocalDataSource = mChessmanLocalDataSource;
    }

    public static ChessManRepository getInstance(ChessmanLocalDataSource chessmanLocalDataSource) {
        if (sInstance == null) {
            synchronized (ChessManRepository.class) {
                if (sInstance == null) {
                    sInstance = new ChessManRepository(chessmanLocalDataSource);
                }
            }
        }
        return sInstance;
    }

    public List<ChessMan> getChessmanReds(int left, int right, int top, int bottom, int type) {
        return mChessmanLocalDataSource.getChessmanReds(left, right, top, bottom, type);
    }


    public List<ChessMan> getChessmanBlues(int left, int right, int top, int bottom, int type) {
        return mChessmanLocalDataSource.getChessmanBlues(left, right, top, bottom, type);
    }
}
