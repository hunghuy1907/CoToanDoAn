package com.hungth.cotoan.screen.play_with_friend;

import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.hungth.cotoan.R;
import com.hungth.cotoan.data.model.ChessMan;
import com.hungth.cotoan.data.repository.ChessManRepository;
import com.hungth.cotoan.data.resource.local.ChessmanLocalDataSource;
import com.hungth.cotoan.databinding.FragmentPlayWithFriendBinding;
import com.hungth.cotoan.screen.base.BaseFragment;
import com.hungth.cotoan.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class PlayWithFriendFragment extends BaseFragment{
    public static String TAG = PlayWithFriendFragment.class.getSimpleName();
    private FragmentPlayWithFriendBinding mBinding;
    private PlayWithFriendViewModel mViewModel;
    private int left, right, top, bottom;

    public static PlayWithFriendFragment getInstance() {
        return new PlayWithFriendFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_with_friend, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new PlayWithFriendViewModel(ChessManRepository.getInstance(ChessmanLocalDataSource.getInstance(getActivity())));
        mBinding.setViewModel(mViewModel);
        initChess();
    }

    public void initChess() {
        final ImageView imageView = mBinding.imageBoardChess;
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout() {
                left = imageView.getLeft();
                right = imageView.getRight();
                top = imageView.getTop();
                bottom = imageView.getBottom();

//                System.out.println("------>>>left: " + left + ", right" + right + ", top: " + top + ", bottom: " + bottom);
//                System.out.println("------>>>width: " + imageView.getWidth() + "height: " );

                mViewModel.getChessmanBlues(left, right, top, bottom, 41).observe(getActivity(), new Observer<List<ChessMan>>() {
                    @Override
                    public void onChanged(@Nullable List<ChessMan> chessMEN) {
                        drawChess(chessMEN);
                    }
                });

                mViewModel.getChessmanReds(left, right, top, bottom, Constant.NUMBER).observe(getActivity(), new Observer<List<ChessMan>>() {
                    @Override
                    public void onChanged(@Nullable List<ChessMan> chessMEN) {
                        drawChess(chessMEN);
                    }
                });
            }
        });
    }

    public void drawChess(List<ChessMan> mChessmanReds) {
        for (int i = 0; i < mChessmanReds.size(); i++) {
            Canvas canvas = new Canvas();
            ChessMan chessMan = mChessmanReds.get(i);
            Rect rect = new Rect(chessMan.getmLeft(), chessMan.getmTop(), chessMan.getmRight(), chessMan.getmBottom());
            canvas.drawBitmap(chessMan.getmBitmap(), null, rect, null);
        }
    }
}
