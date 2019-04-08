package com.hungth.cotoan.screen.home;

import com.hungth.cotoan.data.repository.ChessManRepository;
import com.hungth.cotoan.screen.base.BaseViewModel;

public class HomeViewModel extends BaseViewModel {
    private ChessManRepository mChessManRepository;
    private PlayChess playChess;

    public HomeViewModel(ChessManRepository mChessManRepository, PlayChess playChess) {
        this.mChessManRepository = mChessManRepository;
        this.playChess = playChess;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onStop() {

    }

    public void playOnline() {
        playChess.playOnline();
    }

    public void playManVsMan() {
        playChess.playOneVsOne();
    }

    public void playManVsCom() {
        playChess.playComputer();
    }

    public void setting() {
        playChess.setting();
    }

    public void guide() {
        playChess.guide();
    }
}
