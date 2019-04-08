package com.hungth.cotoan.screen.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hungth.cotoan.R;
import com.hungth.cotoan.data.model.CalculatorType;
import com.hungth.cotoan.data.repository.ChessManRepository;
import com.hungth.cotoan.data.resource.local.ChessmanLocalDataSource;
import com.hungth.cotoan.databinding.FragmentHomeBinding;
import com.hungth.cotoan.databinding.LayoutPlayOnlineBinding;
import com.hungth.cotoan.databinding.LayoutSettingMainBinding;
import com.hungth.cotoan.databinding.LayoutSettingManVsComBinding;
import com.hungth.cotoan.databinding.LayoutSettingManVsManBinding;
import com.hungth.cotoan.screen.base.BaseFragment;
import com.hungth.cotoan.screen.play_man_vs_man.PlayWithFriendFragment;
import com.hungth.cotoan.utils.Constant;
import com.hungth.cotoan.utils.common.FragmentTransactionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeFragment extends BaseFragment implements PlayChess, SettingPlayChess {
    public static String TAG = HomeFragment.class.getSimpleName();
    private FragmentHomeBinding binding;
    private LayoutSettingManVsManBinding bindingSetting;
    private LayoutSettingManVsComBinding manVsComBinding;
    private LayoutSettingMainBinding settingMainBinding;
    private LayoutPlayOnlineBinding onlineBinding;
    private HomeViewModel viewModel;
    private DialogSettingViewModel settingViewModel;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private Dialog dialogSettingManVsMan;
    private Dialog dialogSettingManVsCom;
    private Dialog dialogPlayOnlline;
    private Dialog dialogSettingHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplicationContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        viewModel = new HomeViewModel(ChessManRepository.getInstance(ChessmanLocalDataSource.getInstance(getActivity()))
                , this);
        binding.setViewModel(viewModel);
        initData();
    }

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    public void initData() {
        loginButton = binding.loginButton;
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginResult(loginResult);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("---->>> error: " + error);
            }
        });
        settingViewModel = new DialogSettingViewModel(this);

        initDialogSettingManVsMan();
        initDialogSettingManVsCom();
        initDialogPlayOnline();
        initDialogSettingMain();
    }

    public void loginResult(LoginResult loginResult) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        System.out.println("---->>>JSON: " + response.toString());
                        try {
                            String email = object.getString("email");
                            String birthday = object.getString("birthday");
                            String id = object.getString("id");
                            System.out.println("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        loginButton.setVisibility(View.INVISIBLE);
        System.out.println("---->>> success: ");
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void sendLinkToInvitePlayOnline() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "My message to send");
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.facebook.orca");

        try {
            startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "Please", Toast.LENGTH_SHORT).show();
        }
    }

    private void initDialogSettingManVsMan() {
        if (dialogSettingManVsMan == null) {
            dialogSettingManVsMan = new Dialog(getActivity());
        }
        bindingSetting = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                R.layout.layout_setting_man_vs_man, null, false);
        dialogSettingManVsMan.setContentView(bindingSetting.getRoot());
        bindingSetting.setViewModel(settingViewModel);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.7);

        dialogSettingManVsMan.getWindow().setLayout(width, height);
    }

    private void initDialogSettingManVsCom() {
        if (dialogSettingManVsCom == null) {
            dialogSettingManVsCom = new Dialog(getActivity());
        }
        manVsComBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                R.layout.layout_setting_man_vs_com, null, false);
        dialogSettingManVsCom.setContentView(manVsComBinding.getRoot());
        manVsComBinding.setViewModel(settingViewModel);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.75);

        dialogSettingManVsCom.getWindow().setLayout(width, height);
    }

    private void initDialogPlayOnline() {
        if (dialogPlayOnlline == null) {
            dialogPlayOnlline = new Dialog(getActivity());
        }
        onlineBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                R.layout.layout_play_online, null, false);
        dialogPlayOnlline.setContentView(onlineBinding.getRoot());
        onlineBinding.setViewModel(settingViewModel);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.5);

        dialogPlayOnlline.getWindow().setLayout(width, height);
    }

    private void initDialogSettingMain() {
        if (dialogSettingHome == null) {
            dialogSettingHome = new Dialog(getActivity());
        }
        settingMainBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                R.layout.layout_setting_main, null, false);
        dialogSettingHome.setContentView(settingMainBinding.getRoot());
        settingMainBinding.setViewModel(settingViewModel);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.5);

        dialogSettingHome.getWindow().setLayout(width, height);
    }


    // home

    @Override
    public void playOnline() {
        dialogPlayOnlline.show();
    }

    @Override
    public void playComputer() {
        dialogSettingManVsCom.show();
    }

    @Override
    public void playOneVsOne() {
        dialogSettingManVsMan.show();
    }

    @Override
    public void setting() {
        dialogSettingHome.show();
    }

    @Override
    public void guide() {

    }


    // Man vs Man

    @Override
    public void goFirstManVsMan() {
        if (bindingSetting.textGoFirst.getText().toString().equals("XANH")) {
            bindingSetting.textGoFirst.setText("ĐỎ");
        } else {
            bindingSetting.textGoFirst.setText("XANH");
        }
    }

    @Override
    public void nextPointManVsMan() {
        for (int i = 0; i < Constant.points.length; i++) {
            if (Constant.points[i].equals(bindingSetting.textPoint.getText())) {
                if (i != Constant.points.length - 1) {
                    bindingSetting.textPoint.setText(Constant.points[i + 1]);
                    return;
                } else {
                    bindingSetting.textPoint.setText(Constant.points[0]);
                    return;
                }
            }
        }
    }

    @Override
    public void previousPointManVsMan() {
        for (int i = 0; i < Constant.points.length; i++) {
            if (Constant.points[i].equals(bindingSetting.textPoint.getText())
                    && i == 0) {
                bindingSetting.textPoint.setText(Constant.points[Constant.points.length - 1]);
                return;
            } else if (Constant.points[i].equals(bindingSetting.textPoint.getText())) {
                bindingSetting.textPoint.setText(Constant.points[i - 1]);
                return;
            }
        }
    }

    @Override
    public void clickAddManVsMan(int type) {
        switch (type) {
            case CalculatorType.INVISIBLE:
                bindingSetting.buttonPlus.setBackgroundResource(R.drawable.pp_cong_ko_chon);
                break;
            case CalculatorType.VISIBLE:
                bindingSetting.buttonPlus.setBackgroundResource(R.drawable.pp_cong_chon);
        }
    }

    @Override
    public void clickSubManVsMan(int type) {
        switch (type) {
            case CalculatorType.INVISIBLE:
                bindingSetting.buttonSub.setBackgroundResource(R.drawable.pp_tru_ko_chon);
                break;
            case CalculatorType.VISIBLE:
                bindingSetting.buttonSub.setBackgroundResource(R.drawable.pp_tru_chon);
        }
    }

    @Override
    public void clickMultiManVsMan(int type) {
        switch (type) {
            case CalculatorType.INVISIBLE:
                bindingSetting.buttonMulti.setBackgroundResource(R.drawable.pp_nhan_ko_chon);
                break;
            case CalculatorType.VISIBLE:
                bindingSetting.buttonMulti.setBackgroundResource(R.drawable.pp_nhan_chon);
        }
    }

    @Override
    public void clickDivisionManVsMan(int type) {
        switch (type) {
            case CalculatorType.INVISIBLE:
                bindingSetting.buttonDivision.setBackgroundResource(R.drawable.pp_chia_ko_chon);
                break;
            case CalculatorType.VISIBLE:
                bindingSetting.buttonDivision.setBackgroundResource(R.drawable.pp_chia_chon);
        }

    }

    @Override
    public void nextTimeManVsMan() {
        for (int i = 0; i < Constant.times.length; i++) {
            if (Constant.times[i].equals(bindingSetting.textTime.getText())) {
                if (i != Constant.times.length - 1) {
                    bindingSetting.textTime.setText(Constant.times[i + 1]);
                    return;
                } else {
                    bindingSetting.textTime.setText(Constant.times[0]);
                    return;
                }
            }
        }
    }

    @Override
    public void previousTimeManVsMan() {
        for (int i = 0; i < Constant.times.length; i++) {
            if (Constant.times[i].equals(bindingSetting.textTime.getText())
                    && i == 0) {
                bindingSetting.textTime.setText(Constant.times[Constant.times.length - 1]);
                return;
            } else if (Constant.times[i].equals(bindingSetting.textTime.getText())) {
                bindingSetting.textTime.setText(Constant.times[i - 1]);
                return;
            }
        }
    }

    @Override
    public void agreeManVsMan() {
        dialogSettingManVsMan.dismiss();
        FragmentTransactionUtils.addFragment(getActivity().getSupportFragmentManager(),
                PlayWithFriendFragment.getInstance(),
                R.id.main_frame,
                PlayWithFriendFragment.TAG,
                true);
    }

    @Override
    public void cancleManVsMan() {
        dialogSettingManVsMan.dismiss();
        initDialogSettingManVsMan();
    }


    // Man vs Com

    @Override
    public void levelNext() {
        for (int i = 0; i < Constant.levels.length; i++) {
            if (Constant.levels[i].equals(manVsComBinding.textPlayLevel.getText())) {
                if (i != Constant.levels.length - 1) {
                    manVsComBinding.textPlayLevel.setText(Constant.levels[i + 1]);
                    return;
                } else {
                    manVsComBinding.textPlayLevel.setText(Constant.levels[0]);
                    return;
                }
            }
        }
    }

    @Override
    public void levelPrevious() {
        for (int i = 0; i < Constant.levels.length; i++) {
            if (Constant.levels[i].equals(manVsComBinding.textPlayLevel.getText())
                    && i == 0) {
                manVsComBinding.textPlayLevel.setText(Constant.levels[Constant.levels.length - 1]);
                return;
            } else if (Constant.levels[i].equals(manVsComBinding.textPlayLevel.getText())) {
                manVsComBinding.textPlayLevel.setText(Constant.levels[i - 1]);
                return;
            }
        }
    }

    @Override
    public void cancelManVsCom() {
        dialogSettingManVsCom.dismiss();
        initDialogSettingManVsCom();
    }

    @Override
    public void goFirstManVsCom() {
        if (manVsComBinding.textGoFirst.getText().toString().equals("NGƯỜI")) {
            manVsComBinding.textGoFirst.setText("MÁY");
        } else {
            manVsComBinding.textGoFirst.setText("NGƯỜI");
        }
    }

    @Override
    public void nextPointManVsCom() {
        for (int i = 0; i < Constant.points.length; i++) {
            if (Constant.points[i].equals(manVsComBinding.textPoint.getText())) {
                if (i != Constant.points.length - 1) {
                    manVsComBinding.textPoint.setText(Constant.points[i + 1]);
                    return;
                } else {
                    manVsComBinding.textPoint.setText(Constant.points[0]);
                    return;
                }
            }
        }
    }

    @Override
    public void previousPointManVsCom() {
        for (int i = 0; i < Constant.points.length; i++) {
            if (Constant.points[i].equals(manVsComBinding.textPoint.getText())
                    && i == 0) {
                manVsComBinding.textPoint.setText(Constant.points[Constant.points.length - 1]);
                return;
            } else if (Constant.points[i].equals(manVsComBinding.textPoint.getText())) {
                manVsComBinding.textPoint.setText(Constant.points[i - 1]);
                return;
            }
        }
    }

    @Override
    public void clickAddManVsCom(int type) {
        switch (type) {
            case CalculatorType.INVISIBLE:
                manVsComBinding.buttonPlus.setBackgroundResource(R.drawable.pp_cong_ko_chon);
                break;
            case CalculatorType.VISIBLE:
                manVsComBinding.buttonPlus.setBackgroundResource(R.drawable.pp_cong_chon);
        }
    }

    @Override
    public void clickSubManVsCom(int type) {
        switch (type) {
            case CalculatorType.INVISIBLE:
                manVsComBinding.buttonSub.setBackgroundResource(R.drawable.pp_tru_ko_chon);
                break;
            case CalculatorType.VISIBLE:
                manVsComBinding.buttonSub.setBackgroundResource(R.drawable.pp_tru_chon);
        }
    }

    @Override
    public void clickMultiManVsCom(int type) {
        switch (type) {
            case CalculatorType.INVISIBLE:
                manVsComBinding.buttonMulti.setBackgroundResource(R.drawable.pp_nhan_ko_chon);
                break;
            case CalculatorType.VISIBLE:
                manVsComBinding.buttonMulti.setBackgroundResource(R.drawable.pp_nhan_chon);
        }
    }

    @Override
    public void clickDivisionManVsCom(int type) {
        switch (type) {
            case CalculatorType.INVISIBLE:
                manVsComBinding.buttonDivision.setBackgroundResource(R.drawable.pp_chia_ko_chon);
                break;
            case CalculatorType.VISIBLE:
                manVsComBinding.buttonDivision.setBackgroundResource(R.drawable.pp_chia_chon);
        }
    }

    @Override
    public void nextTimeManVsCom() {
        for (int i = 0; i < Constant.times.length; i++) {
            if (Constant.times[i].equals(manVsComBinding.textTime.getText())) {
                if (i != Constant.times.length - 1) {
                    manVsComBinding.textTime.setText(Constant.times[i + 1]);
                    return;
                } else {
                    manVsComBinding.textTime.setText(Constant.times[0]);
                    return;
                }
            }
        }
    }

    @Override
    public void previousTimeManVsCom() {
        for (int i = 0; i < Constant.times.length; i++) {
            if (Constant.times[i].equals(manVsComBinding.textTime.getText())
                    && i == 0) {
                manVsComBinding.textTime.setText(Constant.times[Constant.times.length - 1]);
                return;
            } else if (Constant.times[i].equals(manVsComBinding.textTime.getText())) {
                manVsComBinding.textTime.setText(Constant.times[i - 1]);
                return;
            }
        }
    }

    @Override
    public void agreeManVsCom() {

    }

    @Override
    public void cancelPlayOnline() {
        dialogPlayOnlline.dismiss();
        initDialogPlayOnline();
    }

    @Override
    public void playMessenger() {
        sendLinkToInvitePlayOnline();
    }

    @Override
    public void playBlutooth() {

    }


    // setting home

    @Override
    public void soundSetting() {
        if (settingMainBinding.textSound.getText().toString().equals("BẬT")) {
            settingMainBinding.textSound.setText("TẮT");
        } else {
            settingMainBinding.textSound.setText("BẬT");
        }
    }

    @Override
    public void typeSetting() {
        if (settingMainBinding.textType.getText().toString().equals("SỐ")) {
            settingMainBinding.textType.setText("CHỮ");
        } else {
            settingMainBinding.textType.setText("SỐ");
        }
    }


    @Override
    public void doneSetting() {
        dialogSettingHome.dismiss();
        initDialogSettingMain();
    }


    public void showDialogConfirmExit() {

        new AlertDialog.Builder(getActivity())
                .setTitle("Information")
                .setMessage("Do you wish to exit from Menu Screen?")
                .setPositiveButton("No", null)
                .setNegativeButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).create().show();
    }
}
