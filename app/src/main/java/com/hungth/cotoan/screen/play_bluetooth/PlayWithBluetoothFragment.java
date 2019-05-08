package com.hungth.cotoan.screen.play_bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hungth.cotoan.R;
import com.hungth.cotoan.data.model.ChessBoard;
import com.hungth.cotoan.data.model.ChessMan;
import com.hungth.cotoan.data.repository.ChessManRepository;
import com.hungth.cotoan.data.resource.local.ChessmanLocalDataSource;
import com.hungth.cotoan.databinding.FragmentPlayBluetoothBinding;
import com.hungth.cotoan.screen.base.BaseFragment;
import com.hungth.cotoan.screen.home.HomeFragment;
import com.hungth.cotoan.screen.home.OnSettingChess;
import com.hungth.cotoan.screen.play_bluetooth.bluetoothchat.BluetoothService;
import com.hungth.cotoan.screen.play_bluetooth.bluetoothchat.Constants;
import com.hungth.cotoan.screen.play_bluetooth.bluetoothchat.DeviceListActivity;
import com.hungth.cotoan.utils.Constant;
import com.hungth.cotoan.utils.common.ChessLogic;
import com.hungth.cotoan.utils.common.FragmentTransactionUtils;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PlayWithBluetoothFragment extends BaseFragment implements IGameViewBluetooth,
        OnSettingChess.OnManVsMan, OnClickBluetooth {
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

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private String mConnectedDeviceName;

    private StringBuffer mOutStringBuffer;

    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothService mChatService;

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
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
        initChess(this);
        getInfor();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            mChatService = new BluetoothService(getActivity(), mHandler);
            mOutStringBuffer = new StringBuffer("");
        }
    }

    private void sendMessage(String message) {
        if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        if (message.length() > 0) {
            byte[] send = message.getBytes();
            mChatService.write(send);

            mOutStringBuffer.setLength(0);
        }
    }

    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the item_message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    drawViewBluetooth.setMove(false);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    drawViewBluetooth.setChessBoardList(ChessLogic.convertStringToChessboard(getActivity(), readMessage, getChessBoardNew()));
                    drawViewBluetooth.setMove(true);
                    drawViewBluetooth.invalidate();
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    mChatService = new BluetoothService(getActivity(), mHandler);
                    mOutStringBuffer = new StringBuffer("");
                } else {
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mChatService.connect(device, secure);
    }

    public void initChess(final IGameViewBluetooth iGameView) {
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

    public void setGoFirst() {

    }

    public void getInfor() {
        SharedPreferences prefs = getActivity().getSharedPreferences(Constant.INFORMATION, MODE_PRIVATE);
        String name = prefs.getString(Constant.NAME, "name");
        String view = prefs.getString(Constant.VIEW, "view");
        mBinding.imageAvatar1.setProfileId(view);
        mBinding.textName1.setText(name);
        mBinding.progressBar1.setMax(Integer.valueOf(time) * 1000);
        mBinding.progressBar2.setMax(Integer.valueOf(time) * 1000);

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
        List<ChessBoard> chessBoards = mViewModel.getChessBoards(left, right, top, bottom, true);
        return mViewModel.getChessManInChessBoard(chessBoards, redChessmans, blueChessmans);
    }

    @Override
    public void getLocation(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.top = top;
        if (drawViewBluetooth.getChessBoardList() == null) {
            drawViewBluetooth.setChessBoardList(getChessBoardNew());
        }
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
    public void sendTurn(boolean isBlueMove, String chessboardString) {
        if (isBlueMove) {
            mBinding.progressBar2.setVisibility(View.INVISIBLE);
            setValueProgressBar1();
        } else {
            mBinding.progressBar1.setVisibility(View.INVISIBLE);
            setValueProgressBar2();
        }

        sendMessage(chessboardString);
    }

    private void setValueProgressBar1() {
        if (countDownTimerBlue != null) {
            countDownTimerBlue.cancel();
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
            }
        };
        countDownTimerBlue.start();
    }

    private void setValueProgressBar2() {
        if (countDownTimeRed != null) {
            countDownTimeRed.cancel();
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

    public void resetBackgroundRedEat() {
        mBinding.imageEat0Man2.setBackgroundResource(R.drawable.ic_an_quan_false_1);
        mBinding.imageEat1Man2.setBackgroundResource(R.drawable.ic_an_quan_false_2);
        mBinding.imageEat2Man2.setBackgroundResource(R.drawable.ic_an_quan_false_3);
        mBinding.imageEat3Man2.setBackgroundResource(R.drawable.ic_an_quan_false_4);
        mBinding.imageEat4Man2.setBackgroundResource(R.drawable.ic_an_quan_false_5);
        mBinding.imageEat5Man2.setBackgroundResource(R.drawable.ic_an_quan_false_6);
        mBinding.imageEat6Man2.setBackgroundResource(R.drawable.ic_an_quan_false_7);
        mBinding.imageEat7Man2.setBackgroundResource(R.drawable.ic_an_quan_false_8);
        mBinding.imageEat8Man2.setBackgroundResource(R.drawable.ic_an_quan_false_9);
    }

    public void resetBackgroundBlueEat() {
        mBinding.imageEat0Man1.setBackgroundResource(R.drawable.ic_an_quan_false_1);
        mBinding.imageEat1Man1.setBackgroundResource(R.drawable.ic_an_quan_false_2);
        mBinding.imageEat2Man1.setBackgroundResource(R.drawable.ic_an_quan_false_3);
        mBinding.imageEat3Man1.setBackgroundResource(R.drawable.ic_an_quan_false_4);
        mBinding.imageEat4Man1.setBackgroundResource(R.drawable.ic_an_quan_false_5);
        mBinding.imageEat5Man1.setBackgroundResource(R.drawable.ic_an_quan_false_6);
        mBinding.imageEat6Man1.setBackgroundResource(R.drawable.ic_an_quan_false_7);
        mBinding.imageEat7Man1.setBackgroundResource(R.drawable.ic_an_quan_false_8);
        mBinding.imageEat8Man1.setBackgroundResource(R.drawable.ic_an_quan_false_9);
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
                        drawViewBluetooth.back();
                        return true;
                    case R.id.item_new_game:
                        newGame();
                        return true;
                    case R.id.item_guide:
                        HomeFragment.dialogGuide.show();
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
                        clickButtonOkNewgame();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    public void clickButtonOkNewgame() {
        drawViewBluetooth.setChessBoardList(getChessBoardNew());
        drawViewBluetooth.setNewGame();
        drawViewBluetooth.invalidate();
        setGoFirst();
        resetBackgroundBlueEat();
        resetBackgroundRedEat();
        if (drawViewBluetooth.isBlueMove()) {
            Toast.makeText(getActivity(), "Xanh đi trước", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Đỏ đi trước", Toast.LENGTH_SHORT).show();
        }
        mBinding.progressBar1.setVisibility(View.INVISIBLE);
        mBinding.progressBar2.setVisibility(View.INVISIBLE);
        mBinding.textPoint.setText(("Điểm: 00/" + point));
        mBinding.textPoint2.setText(("Điểm: 00/" + point));
        countDown3s();
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
    public void clickCancle() {

    }

    @Override
    public void clickDone() {

    }
}
