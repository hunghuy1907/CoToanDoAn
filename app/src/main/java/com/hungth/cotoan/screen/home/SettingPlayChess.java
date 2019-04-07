package com.hungth.cotoan.screen.home;

public interface SettingPlayChess {
    void nextGoFirstManVsMan();

    void previousGoFirstManVsMan();

    void nextPointManVsMan();

    void previousPointManVsMan();

    void clickAddManVsMan(int type);

    void clickSubManVsMan(int type);

    void clickMultiManVsMan(int type);

    void clickDivisionManVsMan(int type);

    void nextTimeManVsMan();

    void previousTimeManVsMan();

    void agreeManVsMan();

    void cancleManVsMan();

    void levelNext();

    void levelPrevious();

    void cancelManVsCom();

    void nextGoFirstManVsCom();

    void previousGoFirstManVsCom();

    void nextPointManVsCom();

    void previousPointManVsCom();

    void clickAddManVsCom(int type);

    void clickSubManVsCom(int type);

    void clickMultiManVsCom(int type);

    void clickDivisionManVsCom(int type);

    void nextTimeManVsCom();

    void previousTimeManVsCom();

    void agreeManVsCom();
}
