package com.hungth.cotoan.screen.play_bluetooth;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.hungth.cotoan.R;
import com.hungth.cotoan.data.model.ChessBoard;
import com.hungth.cotoan.data.model.ChessMan;
import com.hungth.cotoan.data.repository.ChessManRepository;
import com.hungth.cotoan.data.resource.local.ChessmanLocalDataSource;
import com.hungth.cotoan.databinding.FragmentPlayBluetoothBinding;
import com.hungth.cotoan.databinding.LayoutChooseDeveiceBluetoothBinding;
import com.hungth.cotoan.screen.base.BaseFragment;
import com.hungth.cotoan.screen.home.HomeFragment;
import com.hungth.cotoan.screen.home.OnSettingChess;
import com.hungth.cotoan.utils.Constant;
import com.hungth.cotoan.utils.common.FragmentTransactionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class PlayWithBluetoothFragment extends BaseFragment implements IGameViewBluetooth,
        OnSettingChess.OnManVsMan, OnClickBluetooth, BluetoothAdapter.OnClickItemBluetooth {
    public static String TAG = PlayWithBluetoothFragment.class.getSimpleName();
    private static PlayWithBluetoothFragment sInstance;
    private FragmentPlayBluetoothBinding mBinding;
    private PlayBluetoothViewModel mViewModel;
    private int left, right, top, bottom;
    private DrawViewBluetooth drawViewBluetooth;
    private PopupMenu popupMenu;
    private String point, time, goFirst;
    private boolean isAdd, isSub, ismulti, isDiv;
    private CountDownTimer countDownTimerBlue;
    private CountDownTimer countDownTimeRed;
    private LayoutChooseDeveiceBluetoothBinding bluetoothBinding;
    private Dialog dialogBlustooth;
    private BluetoothAdapter bluetoothAdapter;
    private List<String> listBluetooth = new ArrayList<>();
    private BroadcastReceiver receiverBluetooth = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                listBluetooth.add(bluetoothDevice.getName());
                bluetoothAdapter.notifyDataSetChanged();
            }
        }
    };

    public static PlayWithBluetoothFragment getInstance() {
        if (sInstance == null) {
            sInstance = new PlayWithBluetoothFragment();
        }
        return sInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_bluetooth, container, false);
        drawViewBluetooth = new DrawViewBluetooth(getActivity(), this);
        mBinding.contrainLayoutBoard.addView(drawViewBluetooth);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new PlayBluetoothViewModel(this, ChessManRepository.getInstance(ChessmanLocalDataSource.getInstance(getActivity())));
        mBinding.setViewModel(mViewModel);
        initChess(this);
        getInfor();
        initDialogBluetooth();
        initDataForListBluetooth();
        dialogBlustooth.show();
    }

    public void initDataForListBluetooth() {
        bluetoothAdapter = new BluetoothAdapter(listBluetooth, this);
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(receiverBluetooth, intentFilter);

        bluetoothBinding.recycleBluetooth.setAdapter(bluetoothAdapter);
    }

    private void initDialogBluetooth() {
        if (dialogBlustooth == null) {
            dialogBlustooth = new Dialog(getActivity());
        }
        bluetoothBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                R.layout.layout_choose_deveice_bluetooth, null, false);
        dialogBlustooth.setContentView(bluetoothBinding.getRoot());
        bluetoothBinding.setViewModel(mViewModel);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.5);

        dialogBlustooth.getWindow().setLayout(width, height);
    }

    public void initChess(final IGameViewBluetooth iGameViewBluetooth) {
        final ImageView imageView = mBinding.imageBoardChess;
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                left = imageView.getLeft();
                right = imageView.getRight();
                top = imageView.getTop();
                bottom = imageView.getBottom();
                iGameViewBluetooth.getLocation(left, right, top, bottom);
            }
        });
    }

    public void setGoFirst() {
        if (goFirst.equals("XANH")) {
            drawViewBluetooth.setBlueMove(true);
        } else {
            drawViewBluetooth.setBlueMove(false);
        }
    }

    public void getInfor() {
        SharedPreferences prefs = getActivity().getSharedPreferences(Constant.INFORMATION, MODE_PRIVATE);
        String name = prefs.getString(Constant.NAME, "name");
        String view = prefs.getString(Constant.VIEW, "view");
        mBinding.imageAvatar1.setProfileId(view);
        mBinding.textName1.setText(name);
        mBinding.progressBar1.setMax(Integer.valueOf(time)*1000);
        mBinding.progressBar2.setMax(Integer.valueOf(time)*1000);

        setGoFirst();
        setBackgroundIconCal();
        drawViewBluetooth.setAdd(isAdd);
        drawViewBluetooth.setSub(isSub);
        drawViewBluetooth.setMulti(ismulti);
        drawViewBluetooth.setDiv(isDiv);
        drawViewBluetooth.setPoint(Integer.valueOf(point));
        drawViewBluetooth.setTime(Integer.valueOf(time));

        if (drawViewBluetooth.isBlueMove()) {
            Toast.makeText(getActivity(), "Xanh đi trước", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Đỏ đi trước", Toast.LENGTH_SHORT).show();
        }
        countDown3s();
    }

    public void countDown3s() {
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (drawViewBluetooth.isBlueMove()) {
                    setValueProgressBar1();
                } else {
                    setValueProgressBar2();
                }
            }
        }.start();
    }

    public void setBackgroundIconCal() {
        if (isAdd) {
            mBinding.imageAdd.setBackgroundResource(R.drawable.pp_cong_chon);
        } else {
            mBinding.imageAdd.setBackgroundResource(R.drawable.pp_cong_ko_chon);
        }

        if (isSub) {
            mBinding.imageSub.setBackgroundResource(R.drawable.pp_tru_chon);
        } else {
            mBinding.imageSub.setBackgroundResource(R.drawable.pp_tru_ko_chon);
        }

        if (ismulti) {
            mBinding.imageMulti.setBackgroundResource(R.drawable.pp_nhan_chon);
        } else {
            mBinding.imageMulti.setBackgroundResource(R.drawable.pp_nhan_ko_chon);
        }

        if (isDiv) {
            mBinding.imageDivision.setBackgroundResource(R.drawable.pp_chia_chon);
        } else {
            mBinding.imageDivision.setBackgroundResource(R.drawable.pp_chia_ko_chon);
        }
    }

    public List<ChessBoard> getChessBoardNew() {
        List<ChessMan> redChessmans = mViewModel.getChessManReds(left, right, top, bottom, Constant.RED_NUMBER);
        List<ChessMan> blueChessmans = mViewModel.getChessManBlues(left, right, top, bottom, Constant.BLUE_NUMBER);
        List<ChessBoard> chessBoards =  mViewModel.getChessBoards(left, right, top, bottom, true);
        return mViewModel.getChessManInChessBoard(chessBoards, redChessmans, blueChessmans);
    }

    @Override
    public void getLocation(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        drawViewBluetooth.setChessBoardList(getChessBoardNew());
    }

    @Override
    public void showMenu() {
        showPopup();
    }

    @Override
    public void showConfirmWin(String win) {
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Thông báo")
                .setMessage("Bên " + win + " thắng, bạn có muốn chơi tiếp không")
                .setCancelable(false)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newGame();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentTransactionUtils.addFragment(getActivity().getSupportFragmentManager(),
                                HomeFragment.getInstance(), R.id.main_frame, HomeFragment.TAG, false);
                    }
                })
                .show();
    }

    @Override
    public void sendValueEnermyAte(List<Integer> values, int type, int sum) {
        setbackgroundForListAte(values, type);
        settotalPointAndProgressbar(sum, type);
    }

    @Override
    public void sendTurn(boolean isBlueMove) {
        if (isBlueMove) {
            mBinding.progressBar2.setVisibility(View.INVISIBLE);
            setValueProgressBar1();
        } else {
            mBinding.progressBar1.setVisibility(View.INVISIBLE);
            setValueProgressBar2();
        }
    }

    private void setValueProgressBar1() {
        if (countDownTimeRed != null) {
            countDownTimeRed.cancel();
        }
        mBinding.progressBar1.setProgress(60000);
        mBinding.progressBar1.setVisibility(View.VISIBLE);
        countDownTimerBlue = new CountDownTimer(Integer.valueOf(time) * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int current = mBinding.progressBar1.getProgress();
                mBinding.progressBar1.setProgress(current - 1000);
            }

            @Override
            public void onFinish() {
                Toast.makeText(getActivity(), "Mất lượt, đến lượt đỏ", Toast.LENGTH_SHORT).show();
                mBinding.progressBar1.setVisibility(View.INVISIBLE);
                setValueProgressBar2();
                drawViewBluetooth.setBlueMove(false);
            }
        };
        countDownTimerBlue.start();
    }

    private void setValueProgressBar2() {
        if (countDownTimerBlue != null) {
            countDownTimerBlue.cancel();
        }
        mBinding.progressBar2.setProgress(60000);
        mBinding.progressBar2.setVisibility(View.VISIBLE);
        countDownTimeRed = new CountDownTimer(Integer.valueOf(time) * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int current = mBinding.progressBar2.getProgress();
                mBinding.progressBar2.setProgress(current - 1000);
            }

            @Override
            public void onFinish() {
                Toast.makeText(getActivity(), "Mất lượt, đến lượt xanh", Toast.LENGTH_SHORT).show();
                mBinding.progressBar2.setVisibility(View.INVISIBLE);
                setValueProgressBar1();
                drawViewBluetooth.setBlueMove(true);
            }
        };
        countDownTimeRed.start();
    }

    public void settotalPointAndProgressbar(int sum, int type) {
        if (type == Constant.BLUE_DOT || type == Constant.BLUE_NUMBER) {
            if (sum < Integer.valueOf(point)) {
                mBinding.textPoint2.setText(("Điểm: 0" + sum + "/" + point));
            } else {
                mBinding.textPoint2.setText(("Điểm:" + sum + "/" + point));
            }
            mBinding.progressBar2.setProgress(sum);
        } else {
            if (sum < Integer.valueOf(point)) {
                mBinding.textPoint.setText(("Điểm: 0" + sum + "/" + point));
            } else {
                mBinding.textPoint.setText(("Điểm:" + sum + "/" + point));
            }
            mBinding.progressBar1.setProgress(sum);
        }
    }

    public void setbackgroundForListAte(List<Integer> values, int type) {
        if (type == Constant.BLUE_DOT || type == Constant.BLUE_NUMBER) {
            setBackgroundBlueEat(values);
        } else {
            setBackgroundRedEat(values);
        }
    }

    private void setBackgroundRedEat(List<Integer> values) {
        for (int i = 0; i < values.size(); i++) {
            switch (values.get(i)) {
                case 1:
                    mBinding.imageEat0Man1.setBackgroundResource(R.drawable.ic_an_quan_true_1);
                    break;
                case 2:
                    mBinding.imageEat1Man1.setBackgroundResource(R.drawable.ic_an_quan_true_2);
                    break;
                case 3:
                    mBinding.imageEat2Man1.setBackgroundResource(R.drawable.ic_an_quan_true_3);
                    break;
                case 4:
                    mBinding.imageEat3Man1.setBackgroundResource(R.drawable.ic_an_quan_true_4);
                    break;
                case 5:
                    mBinding.imageEat4Man1.setBackgroundResource(R.drawable.ic_an_quan_true_5);
                    break;
                case 6:
                    mBinding.imageEat5Man1.setBackgroundResource(R.drawable.ic_an_quan_true_6);
                    break;
                case 7:
                    mBinding.imageEat6Man1.setBackgroundResource(R.drawable.ic_an_quan_true_7);
                    break;
                case 8:
                    mBinding.imageEat7Man1.setBackgroundResource(R.drawable.ic_an_quan_true_8);
                    break;
                case 9:
                    mBinding.imageEat8Man1.setBackgroundResource(R.drawable.ic_an_quan_true_9);
                    break;
                default:
                    break;
            }
        }
    }

    private void setBackgroundBlueEat(List<Integer> values) {
        for (int i = 0; i < values.size(); i++) {
            switch (values.get(i)) {
                case 1:
                    mBinding.imageEat0Man2.setBackgroundResource(R.drawable.ic_an_quan_true_1);
                    break;
                case 2:
                    mBinding.imageEat1Man2.setBackgroundResource(R.drawable.ic_an_quan_true_2);
                    break;
                case 3:
                    mBinding.imageEat2Man2.setBackgroundResource(R.drawable.ic_an_quan_true_3);
                    break;
                case 4:
                    mBinding.imageEat3Man2.setBackgroundResource(R.drawable.ic_an_quan_true_4);
                    break;
                case 5:
                    mBinding.imageEat4Man2.setBackgroundResource(R.drawable.ic_an_quan_true_5);
                    break;
                case 6:
                    mBinding.imageEat5Man2.setBackgroundResource(R.drawable.ic_an_quan_true_6);
                    break;
                case 7:
                    mBinding.imageEat6Man2.setBackgroundResource(R.drawable.ic_an_quan_true_7);
                    break;
                case 8:
                    mBinding.imageEat7Man2.setBackgroundResource(R.drawable.ic_an_quan_true_8);
                    break;
                case 9:
                    mBinding.imageEat8Man2.setBackgroundResource(R.drawable.ic_an_quan_true_9);
                    break;
                default:
                    break;
            }
        }
    }

    public void showPopup() {
        Context colorContext = new ContextThemeWrapper(getActivity(), R.style.PopupMenu);
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
                        drawViewBluetooth.back();
                        return true;
                    case R.id.item_new_game:
                        newGame();
                        return true;
                    case R.id.item_guide:
                        Toast.makeText(getActivity(), "guide", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
    }

    public void newGame() {
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Thông báo")
                .setMessage("Bạn có muốn chơi lại không")
                .setCancelable(false)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawViewBluetooth.setChessBoardList(getChessBoardNew());
                        drawViewBluetooth.invalidate();
                        setGoFirst();
                    }
                })
                .setNegativeButton("Không",null)
                .show();
    }

    @Override
    public void getSettingManVsMan(String goFirst, String point, String time) {
        this.goFirst = goFirst;
        this.time = time;
        this.point = point;
    }

    @Override
    public void getcalculatorManVsMan(boolean isAdd, boolean isSub, boolean isMulti, boolean isDiv) {
        this.isAdd = isAdd;
        this.isDiv = isDiv;
        this.ismulti = isMulti;
        this.isSub = isSub;
    }

    @Override
    public void clickItemBluetooth(int position) {

    }

    @Override
    public void clickCancle() {

    }

    @Override
    public void clickDone() {
        dialogBlustooth.dismiss();
    }
}
