package com.hungth.cotoan.screen.play_bluetooth;

import android.arch.lifecycle.MutableLiveData;

import com.hungth.cotoan.data.model.ChessBoard;
import com.hungth.cotoan.data.model.ChessMan;
import com.hungth.cotoan.data.repository.ChessManRepository;
import com.hungth.cotoan.screen.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlayBluetoothViewModel extends BaseViewModel {
    private IGameViewBluetooth iGameViewBluetooth;

    private ChessManRepository mChessManRepository;
    private MutableLiveData<List<ChessMan>> mChessManRedMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<ChessMan>> mChessManBlueMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<ChessBoard>> mChessBoardMutableLiveData = new MutableLiveData<>();

    public PlayBluetoothViewModel(IGameViewBluetooth iGameViewBluetooth, ChessManRepository mChessManRepository) {
        this.iGameViewBluetooth = iGameViewBluetooth;
        this.mChessManRepository = mChessManRepository;
    }

    @Override
    protected void onStart() {

    }

//    public MutableLiveData<List<ChessMan>> getChessmanReds(int left, int right, int top, int bottom, int type) {
//        mChessManRedMutableLiveData.setValue(mChessManRepository.getChessmanReds(left, right, top, bottom, type));
//        return mChessManRedMutableLiveData;
//    }
//
//    public MutableLiveData<List<ChessMan>> getChessmanBlues(int left, int right, int top, int bottom, int type) {
//        mChessManBlueMutableLiveData.setValue(mChessManRepository.getChessmanBlues(left, right, top, bottom, type));
//        return mChessManBlueMutableLiveData;
//    }
//
//    public MutableLiveData<List<ChessBoard>> getChessboard(int left, int right, int top, int bottom, boolean isEmpty) {
//        mChessBoardMutableLiveData.setValue(mChessManRepository.getBoardChess(left, right, top, bottom, isEmpty));
//        return mChessBoardMutableLiveData;
//    }

    @Override
    protected void onStop() {

    }

    public void showMenu() {
        iGameViewBluetooth.showMenu();
    }

    public List<ChessMan> getChessManReds(int left, int right, int top, int bottom, int type) {
        return mChessManRepository.getChessmanReds(left, right, top, bottom, type);
    }

    public List<ChessMan> getChessManBlues(int left, int right, int top, int bottom, int type) {
        return mChessManRepository.getChessmanBlues(left, right, top, bottom, type);
    }

    public List<ChessBoard> getChessBoards(int left, int right, int top, int bottom, boolean isEmpty) {
        return mChessManRepository.getBoardChess(left, right, top, bottom, isEmpty);
    }

    public List<ChessBoard> getChessManInChessBoard(List<ChessBoard> chessBoards,
                                                    List<ChessMan> chessManRedList,
                                                    List<ChessMan> chessManBlueList) {
        List<ChessMan> chessManList = new ArrayList<>();
        chessManList.addAll(chessManRedList);
        chessManList.addAll(chessManBlueList);
        for (int i = 0; i < chessBoards.size(); i++) {
            for (int j = 0; j < chessManList.size(); j++) {
                if (checkNotEmpty(chessManList.get(j), chessBoards.get(i))) {
                    chessBoards.get(i).setChessMan(chessManList.get(j));
                }
            }
        }
        return chessBoards;
    }

    private boolean checkNotEmpty(ChessMan chessMan, ChessBoard chessBoard) {
        return (chessMan.getmLeft() > chessBoard.getLeft()
                && chessMan.getmRight() < chessBoard.getRight()
                && chessMan.getmTop() > chessBoard.getTop()
                && chessMan.getmBottom() < chessBoard.getBottom());
    }
}
