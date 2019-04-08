package com.hungth.cotoan.screen.play_man_vs_man;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.hungth.cotoan.R;
import com.hungth.cotoan.data.model.ChessBoard;
import com.hungth.cotoan.data.model.ChessMan;
import com.hungth.cotoan.data.repository.ChessManRepository;
import com.hungth.cotoan.data.resource.local.ChessmanLocalDataSource;
import com.hungth.cotoan.databinding.FragmentPlayWithFriendBinding;
import com.hungth.cotoan.screen.base.BaseFragment;
import com.hungth.cotoan.utils.Constant;

import java.util.List;

public class PlayWithFriendFragment extends BaseFragment implements IGameView {
    public static String TAG = PlayWithFriendFragment.class.getSimpleName();
    private FragmentPlayWithFriendBinding mBinding;
    private PlayWithFriendViewModel mViewModel;
    private static int left, right, top, bottom;
    private DrawView drawView;
    private PopupMenu popupMenu;

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
        mViewModel = new PlayWithFriendViewModel(this, ChessManRepository.getInstance(ChessmanLocalDataSource.getInstance(getActivity())));
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
        List<ChessMan> redChessmans = mViewModel.getChessManReds(left, right, top, bottom, Constant.RED_NUMBER);
        List<ChessMan> blueChessmans = mViewModel.getChessManBlues(left, right, top, bottom, Constant.BLUE_NUMBER);
        List<ChessBoard> chessBoards = mViewModel.getChessBoards(left, right, top, bottom, true);

        drawView.newBoard = mViewModel.getChessManInChessBoard(chessBoards, redChessmans, blueChessmans);
        drawView.setChessBoardList(mViewModel.getChessManInChessBoard(chessBoards, redChessmans, blueChessmans));
    }

    @Override
    public void getLocation(int left, int right, int top, int bottom) {
        observeChessmans(left, right, top, bottom);
    }

    @Override
    public void showMenu() {
        showPopup();
    }

    public void showPopup() {
        Context colorContext= new ContextThemeWrapper(getActivity(), R.style.PopupMenu);
        popupMenu = new PopupMenu(colorContext, mBinding.buttonMenu);
        popupMenu.getMenuInflater().inflate(R.menu.menu_play_chess, popupMenu.getMenu());
        setClickMenu();
        popupMenu.show();
    }

    public void setClickMenu() {
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_return:
                        Toast.makeText(getActivity(), "return", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.item_new_game:
                        initChess(PlayWithFriendFragment.this);
                        drawView.newGame();
                        return true;
                    case R.id.item_guide:
                        Toast.makeText(getActivity(), "guide", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
    }
}
