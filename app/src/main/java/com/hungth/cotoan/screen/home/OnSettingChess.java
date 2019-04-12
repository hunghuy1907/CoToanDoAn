package com.hungth.cotoan.screen.home;

public interface OnSettingChess {
    interface OnManVsMan {
        void getSettingManVsMan(String goFirst, String point, String time);

        void getcalculatorManVsMan(boolean isAdd, boolean isSub, boolean isMulti, boolean isDiv);
    }

    interface OnManVsCom {
        void getSettingManVsCom(String goFirst, String point, String time);

        void getcalculatorManVsCom(boolean isAdd, boolean isSub, boolean isMulti, boolean isDiv);
    }
}
