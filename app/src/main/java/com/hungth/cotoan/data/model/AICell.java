package com.hungth.cotoan.data.model;

public class AICell {

    /*
     * Đầu vào (lstCell, Quân cần di chuyển (là máy),  số điểm cược còn lại, do sau tim kiem)
     */
    public int xLocation ;
    public int yLocation ;
    public String typeQuanCo ; //quan do "D", quan xanh "X", khong co quan "0"
    public int giaTri ;

    public AICell() {

    }

    public AICell(AICell aICell) {
        this.xLocation = aICell.xLocation;
        this.yLocation = aICell.yLocation;
        this.typeQuanCo = aICell.typeQuanCo;
        this.giaTri = aICell.giaTri;
    }

    public AICell(int xLocation, int yLocation, String typeQuanCo, int giaTri) {
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.typeQuanCo = typeQuanCo;
        this.giaTri = giaTri;
    }

    public int getxLocation() {
        return xLocation;
    }

    public void setxLocation(int xLocation) {
        this.xLocation = xLocation;
    }

    public int getyLocation() {
        return yLocation;
    }

    public void setyLocation(int yLocation) {
        this.yLocation = yLocation;
    }

    public String getTypeQuanCo() {
        return typeQuanCo;
    }

    public void setTypeQuanCo(String typeQuanCo) {
        this.typeQuanCo = typeQuanCo;
    }

    public int getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(int giaTri) {
        this.giaTri = giaTri;
    }
}
