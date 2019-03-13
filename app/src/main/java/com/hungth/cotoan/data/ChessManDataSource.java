package com.hungth.cotoan.data;

import com.hungth.cotoan.data.model.ChessMan;

import java.util.List;

public interface ChessManDataSource {

    public interface ChessManLocalDataSource {
        List<ChessMan> getChessmanReds(int left, int right, int top, int bottom, int type);

        List<ChessMan> getChessmanBlues(int left, int right, int top, int bottom, int type);
    }


    public interface ChessManRemoteDataSource {

    }
}
