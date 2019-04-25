package com.hungth.cotoan.screen.home;

public interface SettingPlayChess {

    //Man vs Man
    void goFirstManVsMan();

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


    // Man VS com

    void levelNext();

    void levelPrevious();

    void cancelManVsCom();

    void goFirstManVsCom();

    void nextPointManVsCom();

    void previousPointManVsCom();

    void clickAddManVsCom(int type);

    void clickSubManVsCom(int type);

    void clickMultiManVsCom(int type);

    void clickDivisionManVsCom(int type);

    void nextTimeManVsCom();

    void previousTimeManVsCom();

    void agreeManVsCom();


    // Play online

    void cancelPlayOnline();

    void playMessenger();

    void playBlutooth();


    // setting home

    void soundSetting();

    void typeSetting();

    void doneSetting();

    // setting guide

    void doneGuide();

}
