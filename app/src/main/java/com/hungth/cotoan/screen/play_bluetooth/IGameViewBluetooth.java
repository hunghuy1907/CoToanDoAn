package com.hungth.cotoan.screen.play_bluetooth;

import java.util.List;

public interface IGameViewBluetooth {
    void getLocation(int left, int right, int top, int bottom);

    void showMenu();

    void showConfirmWin(String win);

    void sendValueEnermyAte(List<Integer> values, int type, int sum);

    void sendTurn(boolean isBlueMove, String chessboardString);

}
