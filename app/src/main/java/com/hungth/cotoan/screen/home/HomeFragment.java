package com.hungth.cotoan.screen.home;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hungth.cotoan.R;
import com.hungth.cotoan.data.repository.ChessManRepository;
import com.hungth.cotoan.data.resource.local.ChessmanLocalDataSource;
import com.hungth.cotoan.databinding.FragmentHomeBinding;
import com.hungth.cotoan.screen.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class HomeFragment extends BaseFragment {
    public static String TAG = HomeFragment.class.getSimpleName();
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private CallbackManager callbackManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new HomeViewModel(ChessManRepository.getInstance(ChessmanLocalDataSource.getInstance(getActivity())));
        binding.setViewModel(viewModel);
        initData();
    }

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    public void initData() {
        final LoginButton loginButton = binding.loginButton;
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                System.out.println("---->>>JSON: " + response.toString());
                                try {
                                    String email = object.getString("email");
                                    String birthday = object.getString("birthday");
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
            public void onCancel() {
                System.out.println("---->>> cancle: ");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("---->>> error: " + error);
            }
        });
    }

//    public void loginResult() {
//        GraphRequest graphRequest = GraphRequest.newMeRequest(binding.loginButton.getAccessToken(),
//                new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        System.out.println("---->>>JSON: " + response.getJSONObject().toString());
//                        try {
//                            String email = object.getString("email");
//                            String birthday = object.getString("birthday");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,email");
//        graphRequest.setParameters(parameters);
//        graphRequest.executeAsync();
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
