package com.hungth.cotoan.screen;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;

import com.hungth.cotoan.R;
import com.hungth.cotoan.screen.home.HomeFragment;
import com.hungth.cotoan.utils.common.FragmentTransactionUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransactionUtils.addFragment(getSupportFragmentManager(),
                HomeFragment.getInstance(),
                R.id.main_frame,
                HomeFragment.TAG,
                false);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.hungth.cotoan",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
//                System.out.println("--->>> key: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

}
