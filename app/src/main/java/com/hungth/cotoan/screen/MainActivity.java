package com.hungth.cotoan.screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hungth.cotoan.R;
import com.hungth.cotoan.screen.play_with_friend.PlayWithFriendFragment;
import com.hungth.cotoan.screen.play_with_friend.PlayWithFriendViewModel;
import com.hungth.cotoan.utils.common.FragmentTransactionUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransactionUtils.addFragment(getSupportFragmentManager(),
                PlayWithFriendFragment.getInstance(),
                R.id.main_frame,
                PlayWithFriendFragment.TAG,
                false);
    }

}
