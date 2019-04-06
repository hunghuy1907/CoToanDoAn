package com.hungth.cotoan.screen.home;

import com.hungth.cotoan.data.repository.ChessManRepository;
import com.hungth.cotoan.screen.base.BaseViewModel;

public class HomeViewModel extends BaseViewModel {
    private ChessManRepository mChessManRepository;

    public HomeViewModel(ChessManRepository mChessManRepository) {
        this.mChessManRepository = mChessManRepository;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onStop() {

    }
}
