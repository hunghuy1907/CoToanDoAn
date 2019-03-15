package com.hungth.cotoan.screen.play_with_friend;

import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.hungth.cotoan.R;
import com.hungth.cotoan.data.model.ChessBoard;
import com.hungth.cotoan.data.model.ChessMan;
import com.hungth.cotoan.data.repository.ChessManRepository;
import com.hungth.cotoan.data.resource.local.ChessmanLocalDataSource;
import com.hungth.cotoan.databinding.FragmentPlayWithFriendBinding;
import com.hungth.cotoan.screen.base.BaseFragment;
import com.hungth.cotoan.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class PlayWithFriendFragment extends BaseFragment implements IGameView{
    public static String TAG = PlayWithFriendFragment.class.getSimpleName();
    private FragmentPlayWithFriendBinding mBinding;
    private PlayWithFriendViewModel mViewModel;
    private static int left, right, top, bottom;
    private DrawView drawView;

    public static PlayWithFriendFragment getInstance() {
        return new PlayWithFriendFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_with_friend, container, false);
        drawView = new DrawView(getActivity());
        mBinding.contrainLayoutBoard.addView(drawView);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new PlayWithFriendViewModel(ChessManRepository.getInstance(ChessmanLocalDataSource.getInstance(getActivity())));
        mBinding.setViewModel(mViewModel);
        initChess(this);
    }


    public void initChess(final IGameView iGameView) {
        final ImageView imageView = mBinding.imageBoardChess;
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                left = imageView.getLeft();
                right = imageView.getRight();
                top = imageView.getTop();
                bottom = imageView.getBottom();
                iGameView.getLocation(left, right, top, bottom);
            }
        });
    }

    private void observeChessmans(int left, int right, int top, int bottom) {
        mViewModel.getChessmanBlues(left, right, top, bottom, 41).observe(getActivity(), new Observer<List<ChessMan>>() {
            @Override
            public void onChanged(@Nullable List<ChessMan> chessMEN) {
                drawView.setChessManBlueList(chessMEN);
            }
        });

        mViewModel.getChessmanReds(left, right, top, bottom, Constant.NUMBER).observe(getActivity(), new Observer<List<ChessMan>>() {
            @Override
            public void onChanged(@Nullable List<ChessMan> chessMEN) {
                drawView.setchessManRedList(chessMEN);
            }
        });
    }

    private void observerBoardchess(int left, int right, int top, int bottom) {
        mViewModel.getChessboard(left, right, top, bottom, true).observe(getActivity(), new Observer<List<ChessBoard>>() {
            @Override
            public void onChanged(@Nullable List<ChessBoard> chessBoards) {
                drawView.setChessBoardList(chessBoards);
            }
        });
    }

    @Override
    public void getLocation(int left, int right, int top, int bottom) {
        observeChessmans(left, right, top, bottom);
        observerBoardchess(left, right, top, bottom);
    }
}
