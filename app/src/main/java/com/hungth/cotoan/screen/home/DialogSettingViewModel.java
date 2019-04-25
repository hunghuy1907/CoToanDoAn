package com.hungth.cotoan.screen.home;

import com.hungth.cotoan.data.model.CalculatorType;
import com.hungth.cotoan.screen.base.BaseViewModel;

public class DialogSettingViewModel extends BaseViewModel {
    private SettingPlayChess settingPlayOnline;
    private int addMode;
    private int subMode;
    private int multiMode;
    private int divMode;

    public DialogSettingViewModel(SettingPlayChess settingPlayOnline) {
        this.settingPlayOnline = settingPlayOnline;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onStop() {

    }

    // man vs man

    public void cancel() {
        settingPlayOnline.cancleManVsMan();
    }

    public void goFirst() {
        settingPlayOnline.goFirstManVsMan();
    }

    public void nextPoint() {
        settingPlayOnline.nextPointManVsMan();
    }

    public void previousPoint() {
        settingPlayOnline.previousPointManVsMan();
    }

    public void clickAdd() {
        if (addMode == CalculatorType.INVISIBLE) {
            addMode = CalculatorType.VISIBLE;
        } else {
            addMode = CalculatorType.INVISIBLE;
        }
        settingPlayOnline.clickAddManVsMan(addMode);
    }

    public void clickSub() {
        if (subMode == CalculatorType.INVISIBLE) {
            subMode = CalculatorType.VISIBLE;
        } else {
            subMode = CalculatorType.INVISIBLE;
        }
        settingPlayOnline.clickSubManVsMan(subMode);
    }

    public void clickMulti() {
        if (multiMode == CalculatorType.INVISIBLE) {
            multiMode = CalculatorType.VISIBLE;
        } else {
            multiMode = CalculatorType.INVISIBLE;
        }
        settingPlayOnline.clickMultiManVsMan(multiMode);
    }

    public void clickDivision() {
        if (divMode == CalculatorType.INVISIBLE) {
            divMode = CalculatorType.VISIBLE;
        } else {
            divMode = CalculatorType.INVISIBLE;
        }
        settingPlayOnline.clickDivisionManVsMan(divMode);
    }

    public void nextTime() {
        settingPlayOnline.nextTimeManVsMan();
    }

    public void previousTime() {
        settingPlayOnline.previousTimeManVsMan();
    }

    public void agree() {
        settingPlayOnline.agreeManVsMan();
    }


    // man vs com

    public void cancelManVsCom() {
        settingPlayOnline.cancelManVsCom();
    }

    public void goFirstManVsCom() {
        settingPlayOnline.goFirstManVsCom();
    }

    public void nextPointManVsCom() {
        settingPlayOnline.nextPointManVsCom();
    }

    public void previousPointManVsCom() {
        settingPlayOnline.previousPointManVsCom();
    }

    public void clickAddManVsCom() {
        if (addMode == CalculatorType.INVISIBLE) {
            addMode = CalculatorType.VISIBLE;
        } else {
            addMode = CalculatorType.INVISIBLE;
        }
        settingPlayOnline.clickAddManVsCom(addMode);
    }

    public void clickSubManVsCom() {
        if (subMode == CalculatorType.INVISIBLE) {
            subMode = CalculatorType.VISIBLE;
        } else {
            subMode = CalculatorType.INVISIBLE;
        }
        settingPlayOnline.clickSubManVsCom(subMode);
    }

    public void clickMultiManVsCom() {
        if (multiMode == CalculatorType.INVISIBLE) {
            multiMode = CalculatorType.VISIBLE;
        } else {
            multiMode = CalculatorType.INVISIBLE;
        }
        settingPlayOnline.clickMultiManVsCom(multiMode);
    }

    public void clickDivisionManVsCom() {
        if (divMode == CalculatorType.INVISIBLE) {
            divMode = CalculatorType.VISIBLE;
        } else {
            divMode = CalculatorType.INVISIBLE;
        }
        settingPlayOnline.clickDivisionManVsCom(divMode);
    }

    public void nextTimeManVsCom() {
        settingPlayOnline.nextTimeManVsCom();
    }

    public void previousTimeManVsCom() {
        settingPlayOnline.previousTimeManVsCom();
    }

    public void agreeManVsCom() {
        settingPlayOnline.agreeManVsCom();
    }

    public void nextLevel() {
        settingPlayOnline.levelNext();
    }

    public void previousLevel() {
        settingPlayOnline.levelPrevious();
    }

    // play online

    public void cancelPlayOnline() {
        settingPlayOnline.cancelPlayOnline();
    }

    public void playMessenger() {
        settingPlayOnline.playMessenger();
    }

    public void playBluetooth() {
        settingPlayOnline.playBlutooth();
    }

    // setting home

    public void soundSetting() {
        settingPlayOnline.soundSetting();
    }

    public void typeSetting() {
        settingPlayOnline.typeSetting();
    }

    public void doneSetting() {
        settingPlayOnline.doneSetting();
    }

    public void doneGuide() {
        settingPlayOnline.doneGuide();
    }
}
