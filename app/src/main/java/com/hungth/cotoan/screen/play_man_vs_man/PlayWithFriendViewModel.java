package com.hungth.cotoan.screen.play_man_vs_man;

import android.arch.lifecycle.MutableLiveData;

import com.hungth.cotoan.data.model.ChessBoard;
import com.hungth.cotoan.data.model.ChessMan;
import com.hungth.cotoan.data.repository.ChessManRepository;
import com.hungth.cotoan.screen.base.BaseViewModel;

import java.util.List;

public class PlayWithFriendViewModel extends BaseViewModel {
    private IGameView iGameView;

    private ChessManRepository mChessManRepository;
    private MutableLiveData<List<ChessMan>> mChessManRedMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<ChessMan>> mChessManBlueMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<ChessBoard>> mChessBoardMutableLiveData = new MutableLiveData<>();

    public PlayWithFriendViewModel(IGameView iGameView, ChessManRepository mChessManRepository) {
        this.iGameView = iGameView;
        this.mChessManRepository = mChessManRepository;
    }

    @Override
    protected void onStart() {

    }

    public MutableLiveData<List<ChessMan>> getChessmanReds(int left, int right, int top, int bottom, int type) {
        mChessManRedMutableLiveData.setValue(mChessManRepository.getChessmanReds(left, right, top, bottom, type));
        return mChessManRedMutableLiveData;
    }

    public MutableLiveData<List<ChessMan>> getChessmanBlues(int left, int right, int top, int bottom, int type) {
        mChessManBlueMutableLiveData.setValue(mChessManRepository.getChessmanBlues(left, right, top, bottom, type));
        return mChessManBlueMutableLiveData;
    }

    public MutableLiveData<List<ChessBoard>> getChessboard(int left, int right, int top, int bottom, boolean isEmpty) {
        mChessBoardMutableLiveData.setValue(mChessManRepository.getBoardChess(left, right, top, bottom, isEmpty));
        return mChessBoardMutableLiveData;
    }

    @Override
    protected void onStop() {

    }

    public void showMenu() {
        iGameView.showMenu();
    }
}
