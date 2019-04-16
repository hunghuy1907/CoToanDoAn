package com.hungth.cotoan.screen.ai;

public class AIConfig {
    public static final String QUAN_XANH = "X";
    public static final String QUAN_DO = "D";
    public static final String QUAN_TRONG = "0";
    public static final String PT_CONG = "cong";
    public static final String PT_TRU = "tru";
    public static final String PT_NHAN = "nhan";
    public static final String PT_CHIA = "chia";

    public static final int LEVEL_DE = 2;
    public static final int LEVEL_TRUNG_BINH = 4;
    public static final int LEVEL_KHO = 6;


    public static final int DIEM_CUOC_MAC_DINH = -1;


    public static final int HS_NHAN_QUAN_CO = 5;
    public static final int HS_NHAN_AN_QUAN = 3;

    public static class Location {
        public int xLocation ;
        public int yLocation ;

        public Location(int xLocation, int yLocation) {
            this.xLocation = xLocation;
            this.yLocation = yLocation;
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
    }

    /*
     * khởi tạo AIControler(List<String> danh sách phép tính, int level <độ sâu tìm kiếm>)
     * aiControler.search(String listCell, quan co di chuyen, so diem con lai)
     *
     ***************** cách sử dụng với aicontroler là biến toàn cục***************************
     * AIControler aiControler = new AIControler(danhsachPheptinh, AIConfig.LEVEL_DE);
     * int[] lstI = aiControler.searchMove(cellToString(), AIConfig.QUAN_DO, AIConfig.DIEM_CUOC_MAC_DINH, this);
     *
     *
     *
     *
     * giá trị điểm cược truyền vào (khong cuoc =-1)
     * Điểm nut bằng tổng điểm quâ đang được di chuyển- tổng điểm quân còn lại (*10)
     *      Nếu 2 quân cạnh nhau và có thẻ bắt được quân đối phương + diểm quân đối phương*7
     *      với quân 0+1000;
     * Nếu không ăn được quân 0 và điểm cược kém từ 1 điểm +500;
     *
     * bonus 7 o xung quanh quân  0. nếu có quân bên mình đứng được công thêm 1 điểm cho mỗi quân
     */
}
