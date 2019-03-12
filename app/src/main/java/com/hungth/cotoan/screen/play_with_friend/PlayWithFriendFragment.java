package com.hungth.cotoan.screen.play_with_friend;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hungth.cotoan.R;
import com.hungth.cotoan.databinding.FragmentPlayWithFriendBinding;
import com.hungth.cotoan.screen.base.BaseFragment;

public class PlayWithFriendFragment extends BaseFragment {
    public static String TAG = PlayWithFriendFragment.class.getSimpleName();
    public FragmentPlayWithFriendBinding mBinding;

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
    }
}
