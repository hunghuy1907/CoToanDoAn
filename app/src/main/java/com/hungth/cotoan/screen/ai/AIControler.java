package com.hungth.cotoan.screen.ai;

import android.util.Log;

import com.hungth.cotoan.data.model.AICell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIControler {
    private boolean enableCong = false;
    private boolean enableTru = false;
    private boolean enableNhan = false;
    private boolean enableChia = false;
    private int doSau = 0;
    Random random = new Random();
    //    MainUi.AISuccess interfaceSuccess;
    String mauQuanCuaMay = "D";

    public AIControler(List<String> dsPhepTinh, int level) {
        for (int i = 0; i < dsPhepTinh.size(); i++) {
            if (dsPhepTinh.get(i).equals(AIConfig.PT_CONG)) {
                enableCong = true;
            }
            if (dsPhepTinh.get(i).equals(AIConfig.PT_TRU)) {
                enableTru = true;
            }
            if (dsPhepTinh.get(i).equals(AIConfig.PT_NHAN)) {
                enableNhan = true;
            }
            if (dsPhepTinh.get(i).equals(AIConfig.PT_CHIA)) {
                enableChia = true;
            }
        }
        doSau = level;
    }


    // ham duoc goi de tim kiem nuoc di
    public int[] searchMove(String sTrangThai, String quanCoDiChuyen, int diemCuocConLai) { //quancodichuyen ;Quan co di chuyen o luot sau
        //tao note goc
        List<AICell> lstQuanxanh = new ArrayList<>();
//        this.interfaceSuccess = interfaceSuccess;
        List<AICell> lstQuanDo = new ArrayList<>();
        AICell[][] trangThaiBanCo = coverStringToLstCell(sTrangThai, lstQuanxanh, lstQuanDo);
        List<AINote> lstNoteCon = new ArrayList<>();
        AINote noteGoc = new AINote(0, false, quanCoDiChuyen,
                trangThaiBanCo, diemCuocConLai, lstQuanxanh, lstQuanDo, lstNoteCon, null, 0);
//        Debug.Log("Hoan thanhsssssssssssss   " + trangThaiBanCo[2, 0].typeQuanCo + "" + trangThaiBanCo[2, 0].giaTri);

        taoCayTroChoi(noteGoc);
         print(noteGoc);
        Log.d("######", "searchMove: ");

//        Debug.Log("Hoan thanh    "+ trangThaiBanCo[2,0].typeQuanCo+""+ trangThaiBanCo[2, 0].giaTri);
        int[] result = duyetCay(noteGoc);
//        Debug.Log("Hoan thanh 22222222");
          print(noteGoc);
//        interfaceSuccess.update(result);
        return result;


    }

    private void print(AINote aINote) {
//        Debug.Log("IN " + aINote.countLevel + " score " + aINote.scoreTrangThai);
        Log.d("######", "IN " + aINote.countLevel + " score " + aINote.scoreTrangThai);
        List<AINote> lstNoteCon = aINote.noteConS;
        if (lstNoteCon != null) {

            for (AINote note : lstNoteCon) {
                print(note);
            }
        }
    }

    private AICell[][] coverStringToLstCell(String value, List<AICell> lstQuanxanh, List<AICell> lstQuanDo) {
        AICell[][] lstCell = new AICell[9][11];
        String[] tempNew = value.split("_");
        for (int i = 0; i < tempNew.length; i++) {
            int xLocation = i % 9;
            int yLocation = i / 9;
            String sValue = tempNew[i];
            char c1 = sValue.charAt(0);
            char c2 = sValue.charAt(1);

            AICell aICell = new AICell(xLocation, yLocation, String.valueOf(c1), Integer.valueOf(c2 + ""));
            lstCell[xLocation][yLocation] = aICell;
            //Debug.Log("X: " + xLocation + " Y:" + yLocation + " name" + lstCell[xLocation,yLocation].typeQuanCo+""+ lstCell[xLocation, yLocation].giaTri);
            if (String.valueOf(c1).equals(AIConfig.QUAN_DO)) {
                lstQuanDo.add(aICell);
            } else {
                lstQuanxanh.add(aICell);
            }
        }
        return lstCell;
    }

    private int tinhDiemTrangThaiBanCo(String quanDangDiChuyen, List<AICell> lstQuanDo,
                                       List<AICell> lstQuanXanh, AICell[][] trangThai, int diemCuocConLai) {
        int score = 0;
        //if (quanDangDiChuyen.equals(AIConfig.QUAN_DO)) {
        //tinh diem theo so quan co tren ban co
        for (AICell cellQuanDo : lstQuanDo) {
            score += cellQuanDo.giaTri * AIConfig.HS_NHAN_QUAN_CO;
        }

        for (AICell cellQuanXanh : lstQuanXanh) {
            score -= cellQuanXanh.giaTri * AIConfig.HS_NHAN_QUAN_CO;
        }

        //Kiem tra co the an quan doi phuong hay khong. Neu co the bang gia tri quan duoc an *7
        int diemAnQuanDo = tinhDiemAnQuan(lstQuanDo, trangThai, diemCuocConLai);
        int diemAnQuanXanh = tinhDiemAnQuan(lstQuanXanh, trangThai, diemCuocConLai);

        score += diemAnQuanDo;
        score -= diemAnQuanXanh;

        // tinh diem bonus bao ve quan 0
        String type01 = trangThai[4][1].typeQuanCo;
        String type02 = trangThai[4][9].typeQuanCo;

        if (type01.equals(quanDangDiChuyen)) {
            int bonus1 = tinhDiemBonus(type01, 0, trangThai);
            int bonus2 = tinhDiemBonus(type02, 1, trangThai);
            score += bonus1;
            score -= bonus2;
        } else {
            int bonus1 = tinhDiemBonus(type02, 1, trangThai);
            int bonus2 = tinhDiemBonus(type01, 0, trangThai);
            score += bonus1;
            score -= bonus2;
        }
        //} else {
        //    //tinh diem theo so quan co tren ban co
        //    foreach (AICell cellQuanDo in lstQuanDo) {
        //        score -= cellQuanDo.giaTri * AIConfig.HS_NHAN_QUAN_CO;
        //    }

        //    foreach (AICell cellQuanXanh in lstQuanXanh) {
        //        score += cellQuanXanh.giaTri * AIConfig.HS_NHAN_QUAN_CO;
        //    }

        //    //Kiem tra co the an quan doi phuong hay khong. Neu co the bang gia tri quan duoc an *7
        //    int diemAnQuanDo = tinhDiemAnQuan(lstQuanDo, trangThai, diemCuocConLai);
        //    int diemAnQuanXanh = tinhDiemAnQuan(lstQuanXanh, trangThai, diemCuocConLai);

        //    score -= diemAnQuanDo;
        //    score += diemAnQuanXanh;

        //    // tinh diem bonus bao ve quan 0
        //    String type01 = trangThai[4, 1].typeQuanCo;
        //    String type02 = trangThai[4, 9].typeQuanCo;

        //    if (type01.equals(quanDangDiChuyen)) {
        //        int bonus1 = tinhDiemBonus(type01, 0, trangThai);
        //        int bonus2 = tinhDiemBonus(type02, 1, trangThai);
        //        score += bonus1;
        //        score -= bonus2;
        //    } else {
        //        int bonus1 = tinhDiemBonus(type02, 1, trangThai);
        //        int bonus2 = tinhDiemBonus(type01, 0, trangThai);
        //        score += bonus1;
        //        score -= bonus2;
        //    }

        //}
        int a = random.nextInt(10);
        score += a;
        return score;
    }

    private int tinhDiemBonus(String typeQuan0, int indexQuan0, AICell[][] trangThai) { //index=0 --> vi tri 4,1, index =1--> vi tri 4,9
        int score = 0;
        if (indexQuan0 == 0) {
            //quan0 duoi
            if (trangThai[3][0].typeQuanCo.equals(typeQuan0)) {
                score++;
            }
            if (trangThai[3][1].typeQuanCo.equals(typeQuan0)) {
                score++;
            }
            if (trangThai[3][2].typeQuanCo.equals(typeQuan0)) {
                score++;
            }
            if (trangThai[4][2].typeQuanCo.equals(typeQuan0)) {
                score++;
            }
            if (trangThai[5][2].typeQuanCo.equals(typeQuan0)) {
                score++;
            }
            if (trangThai[5][1].typeQuanCo.equals(typeQuan0)) {
                score++;
            }
            if (trangThai[5][0].typeQuanCo.equals(typeQuan0)) {
                score++;
            }

        } else {
            //quan0 tren
            if (trangThai[3][10].typeQuanCo.equals(typeQuan0)) {
                score++;
            }
            if (trangThai[3][9].typeQuanCo.equals(typeQuan0)) {
                score++;
            }
            if (trangThai[3][8].typeQuanCo.equals(typeQuan0)) {
                score++;
            }
            if (trangThai[4][8].typeQuanCo.equals(typeQuan0)) {
                score++;
            }
            if (trangThai[5][8].typeQuanCo.equals(typeQuan0)) {
                score++;
            }
            if (trangThai[5][9].typeQuanCo.equals(typeQuan0)) {
                score++;
            }
            if (trangThai[5][10].typeQuanCo.equals(typeQuan0)) {
                score++;
            }
        }
        return score;
    }

    private int tinhDiemAnQuan(List<AICell> lstQuanTinh, AICell[][] trangThai, int diemCuocConLai) {
        int scoreAnQuan = 0;
        for (AICell cellQuan : lstQuanTinh) {
            int x = cellQuan.xLocation;
            int y = cellQuan.yLocation;
            String typeQuanCo = cellQuan.typeQuanCo;
            if (x == 4 && y == 1 || x == 4 && y == 9) {

            } else {
                //region kiem tra ben trai x--, y==
                if (x > 0) {
                    if (trangThai[x - 1][y].typeQuanCo.equals(typeQuanCo)) {
                        List<Integer> dsPt = new ArrayList<>();
                        int giaTri1 = cellQuan.giaTri;
                        int giaTri2 = trangThai[x - 1][y].giaTri;

                        if (enableCong) {
                            int tempC = giaTri1 + giaTri2;
                            dsPt.add(tempC % 10);
                        }
                        if (enableTru) {
                            if (giaTri1 > giaTri2) {
                                int tempT = giaTri1 - giaTri2;
                                dsPt.add(tempT % 10);
                            }
                        }
                        if (enableNhan) {
                            int tempN = giaTri1 * giaTri2;
                            dsPt.add(tempN % 10);
                        }
                        if (enableChia) {
                            if (giaTri1 > giaTri2) {
                                int tempCh = giaTri1 / giaTri2;
                                dsPt.add(tempCh % 10);
                            }
                        }
                        int count = kiemTraAnQuanTrai(trangThai, x, y, dsPt, typeQuanCo);
                        if (count != -1) {
                            count += 1;
                            if (diemCuocConLai != -1 && diemCuocConLai - trangThai[x - count][y].giaTri <= 1) {
                                scoreAnQuan += 500;
                            } else {
                                scoreAnQuan += trangThai[x - count][y].giaTri * AIConfig.HS_NHAN_AN_QUAN;
                            }
                        }
                    }
                }

                //endregion

                //region kiem tra ben tren - trai x--, y++
                if (x > 0 && y < 10) {
                    if (trangThai[x - 1][y + 1].typeQuanCo.equals(typeQuanCo)) {
                        List<Integer> dsPt = new ArrayList<>();
                        int giaTri1 = cellQuan.giaTri;
                        int giaTri2 = trangThai[x - 1][y + 1].giaTri;

                        if (enableCong) {
                            int tempC = giaTri1 + giaTri2;
                            dsPt.add(tempC % 10);
                        }
                        if (enableTru) {
                            if (giaTri1 > giaTri2) {
                                int tempT = giaTri1 - giaTri2;
                                dsPt.add(tempT % 10);
                            }
                        }
                        if (enableNhan) {
                            int tempN = giaTri1 * giaTri2;
                            dsPt.add(tempN % 10);
                        }
                        if (enableChia) {
                            if (giaTri1 > giaTri2) {
                                int tempCh = giaTri1 / giaTri2;
                                dsPt.add(tempCh % 10);
                            }
                        }
                        int count = kiemTraAnQuanTraiTren(trangThai, x, y, dsPt, typeQuanCo);
                        if (count != -1) {
                            count += 1;
                            if (diemCuocConLai != -1 && diemCuocConLai - trangThai[x - count][y + count].giaTri <= 1) {
                                scoreAnQuan += 500;
                            } else {
                                scoreAnQuan += trangThai[x - count][y + count].giaTri * AIConfig.HS_NHAN_AN_QUAN;
                            }
                        }
                    }
                }
                //endregion

                //region kiem tra ben tren x==, y++
                if (y < 10) {
                    if (trangThai[x][y + 1].typeQuanCo.equals(typeQuanCo)) {
                        List<Integer> dsPt = new ArrayList<>();
                        int giaTri1 = cellQuan.giaTri;
                        int giaTri2 = trangThai[x][y + 1].giaTri;

                        if (enableCong) {
                            int tempC = giaTri1 + giaTri2;
                            dsPt.add(tempC % 10);
                        }
                        if (enableTru) {
                            if (giaTri1 > giaTri2) {
                                int tempT = giaTri1 - giaTri2;
                                dsPt.add(tempT % 10);
                            }
                        }
                        if (enableNhan) {
                            int tempN = giaTri1 * giaTri2;
                            dsPt.add(tempN % 10);
                        }
                        if (enableChia) {
                            if (giaTri1 > giaTri2) {
                                int tempCh = giaTri1 / giaTri2;
                                dsPt.add(tempCh % 10);
                            }
                        }
                        int count = kiemTraAnQuanTren(trangThai, x, y, dsPt, typeQuanCo);
                        if (count != -1) {
                            count += 1;
                            if (diemCuocConLai != -1 && diemCuocConLai - trangThai[x][y + count].giaTri <= 1) {
                                scoreAnQuan += 500;
                            } else {
                                scoreAnQuan += trangThai[x][y + count].giaTri * AIConfig.HS_NHAN_AN_QUAN;
                            }
                        }
                    }
                }
                //endregion

                //region kiem tra ben tren- phai y++, x++
                if (x < 8 && y < 10) {
                    if (trangThai[x + 1][y + 1].typeQuanCo.equals(typeQuanCo)) {
                        List<Integer> dsPt = new ArrayList<>();
                        int giaTri1 = cellQuan.giaTri;
                        int giaTri2 = trangThai[x + 1][y + 1].giaTri;

                        if (enableCong) {
                            int tempC = giaTri1 + giaTri2;
                            dsPt.add(tempC % 10);
                        }
                        if (enableTru) {
                            if (giaTri1 > giaTri2) {
                                int tempT = giaTri1 - giaTri2;
                                dsPt.add(tempT % 10);
                            }
                        }
                        if (enableNhan) {
                            int tempN = giaTri1 * giaTri2;
                            dsPt.add(tempN % 10);
                        }
                        if (enableChia) {
                            if (giaTri1 > giaTri2) {
                                int tempCh = giaTri1 / giaTri2;
                                dsPt.add(tempCh % 10);
                            }
                        }
                        int count = kiemTraAnQuanTrenPhai(trangThai, x, y, dsPt, typeQuanCo);
                        if (count != -1) {
                            count += 1;
                            if (diemCuocConLai != -1 && diemCuocConLai - trangThai[x + count][y + count].giaTri <= 1) {
                                scoreAnQuan += 500;
                            } else {
                                scoreAnQuan += trangThai[x + count][y + count].giaTri * AIConfig.HS_NHAN_AN_QUAN;
                            }
                        }
                    }
                }
                //endregion

                //region kiem tra ben phai x++, y==
                if (x < 8) {
                    if (trangThai[x + 1][y].typeQuanCo.equals(typeQuanCo)) {
                        List<Integer> dsPt = new ArrayList<>();
                        int giaTri1 = cellQuan.giaTri;
                        int giaTri2 = trangThai[x + 1][y].giaTri;

                        if (enableCong) {
                            int tempC = giaTri1 + giaTri2;
                            dsPt.add(tempC % 10);
                        }
                        if (enableTru) {
                            if (giaTri1 > giaTri2) {
                                int tempT = giaTri1 - giaTri2;
                                dsPt.add(tempT % 10);
                            }
                        }
                        if (enableNhan) {
                            int tempN = giaTri1 * giaTri2;
                            dsPt.add(tempN % 10);
                        }
                        if (enableChia) {
                            if (giaTri1 > giaTri2) {
                                int tempCh = giaTri1 / giaTri2;
                                dsPt.add(tempCh % 10);
                            }
                        }
                        int count = kiemTraAnQuanPhai(trangThai, x, y, dsPt, typeQuanCo);
                        if (count != -1) {
                            count += 1;
                            if (diemCuocConLai != -1 && diemCuocConLai - trangThai[x + count][y].giaTri <= 1) {
                                scoreAnQuan += 500;
                            } else {
                                scoreAnQuan += trangThai[x + count][y].giaTri * AIConfig.HS_NHAN_AN_QUAN;
                            }
                        }
                    }
                }
                //endregion

                //region kiem tra ben phai ben duoi x++, y--
                if (x < 8 && y > 0) {
                    if (trangThai[x + 1][y - 1].typeQuanCo.equals(typeQuanCo)) {
                        List<Integer> dsPt = new ArrayList<>();
                        int giaTri1 = cellQuan.giaTri;
                        int giaTri2 = trangThai[x + 1][y - 1].giaTri;

                        if (enableCong) {
                            int tempC = giaTri1 + giaTri2;
                            dsPt.add(tempC % 10);
                        }
                        if (enableTru) {
                            if (giaTri1 > giaTri2) {
                                int tempT = giaTri1 - giaTri2;
                                dsPt.add(tempT % 10);
                            }
                        }
                        if (enableNhan) {
                            int tempN = giaTri1 * giaTri2;
                            dsPt.add(tempN % 10);
                        }
                        if (enableChia) {
                            if (giaTri1 > giaTri2) {
                                int tempCh = giaTri1 / giaTri2;
                                dsPt.add(tempCh % 10);
                            }
                        }
                        int count = kiemTraAnQuanPhaiDuoi(trangThai, x, y, dsPt, typeQuanCo);
                        if (count != -1) {
                            count += 1;
                            if (diemCuocConLai != -1 && diemCuocConLai - trangThai[x + count][y - count].giaTri <= 1) {
                                scoreAnQuan += 500;
                            } else {
                                scoreAnQuan += trangThai[x + count][y - count].giaTri * AIConfig.HS_NHAN_AN_QUAN;
                            }
                        }
                    }
                }
                //endregion

                //region kiem tra ben ben duoi x==, y--;
                if (y > 0) {
                    if (trangThai[x][y - 1].typeQuanCo.equals(typeQuanCo)) {
                        List<Integer> dsPt = new ArrayList<>();
                        int giaTri1 = cellQuan.giaTri;
                        int giaTri2 = trangThai[x][y - 1].giaTri;

                        if (enableCong) {
                            int tempC = giaTri1 + giaTri2;
                            dsPt.add(tempC % 10);
                        }
                        if (enableTru) {
                            if (giaTri1 > giaTri2) {
                                int tempT = giaTri1 - giaTri2;
                                dsPt.add(tempT % 10);
                            }
                        }
                        if (enableNhan) {
                            int tempN = giaTri1 * giaTri2;
                            dsPt.add(tempN % 10);
                        }
                        if (enableChia) {
                            if (giaTri1 > giaTri2) {
                                int tempCh = giaTri1 / giaTri2;
                                dsPt.add(tempCh % 10);
                            }
                        }
                        int count = kiemTraAnQuanDuoi(trangThai, x, y, dsPt, typeQuanCo);
                        if (count != -1) {
                            count += 1;
                            if (diemCuocConLai != -1 && diemCuocConLai - trangThai[x][y - count].giaTri <= 1) {
                                scoreAnQuan += 500;
                            } else {
                                scoreAnQuan += trangThai[x][y - count].giaTri * AIConfig.HS_NHAN_AN_QUAN;
                            }
                        }
                    }
                }
                //endregion

                //region kiem tra ben duoi- trai x--, y--
                if (x > 0 && y > 0) {
                    if (trangThai[x - 1][y - 1].typeQuanCo.equals(typeQuanCo)) {
                        List<Integer> dsPt = new ArrayList<>();
                        int giaTri1 = cellQuan.giaTri;
                        int giaTri2 = trangThai[x - 1][y - 1].giaTri;

                        if (enableCong) {
                            int tempC = giaTri1 + giaTri2;
                            dsPt.add(tempC % 10);
                        }
                        if (enableTru) {
                            if (giaTri1 > giaTri2) {
                                int tempT = giaTri1 - giaTri2;
                                dsPt.add(tempT % 10);
                            }
                        }
                        if (enableNhan) {
                            int tempN = giaTri1 * giaTri2;
                            dsPt.add(tempN % 10);
                        }
                        if (enableChia) {
                            if (giaTri1 > giaTri2) {
                                int tempCh = giaTri1 / giaTri2;
                                dsPt.add(tempCh % 10);
                            }
                        }
                        int count = kiemTraAnQuanDuoiTrai(trangThai, x, y, dsPt, typeQuanCo);
                        if (count != -1) {
                            count += 1;
                            if (diemCuocConLai != -1 && diemCuocConLai - trangThai[x - count][y - count].giaTri <= 1) {
                                scoreAnQuan += 500;
                            } else {
                                scoreAnQuan += trangThai[x - count][y - count].giaTri * AIConfig.HS_NHAN_AN_QUAN;
                            }
                        }
                    }
                }
                //endregion
            }
        }
        return scoreAnQuan;
    }

    //region kiem tra an quan tra ve thu tu cua o co the an
    //return -1: khong an duoc
    private int kiemTraAnQuanTrai(AICell[][] trangThai, int xlocation, int yLocation,
                                  List<Integer> giaTri, String mauQuanDiChuyen) {
        //y==, x--;
        if (xlocation > 1) {
            xlocation -= 2;
            int count = 0;
            int max = findMax(giaTri);
            while (xlocation >= 0) {
                count++;
                if (!trangThai[xlocation][yLocation].typeQuanCo.equals(AIConfig.QUAN_TRONG)) {
                    if (!trangThai[xlocation][yLocation].typeQuanCo.equals(mauQuanDiChuyen)) {
                        if (findIntInList(giaTri, count)) {
                            return count;
                        }
                        return -1;
                    }
                    return -1;
                }
                if (count >= max) {
                    return -1;
                }
                xlocation--;
            }
            return -1;
        } else {
            return -1;
        }
    }

    private int kiemTraAnQuanTraiTren(AICell[][] trangThai, int xlocation,
                                      int yLocation, List<Integer> giaTri, String mauQuanDiChuyen) {
        //y++, x--;
        if (xlocation > 1 && yLocation < 9) {
            xlocation -= 2;
            yLocation += 2;
            int count = 0;
            int max = findMax(giaTri);
            while (xlocation >= 0) {
                count++;
                if (!trangThai[xlocation][yLocation].typeQuanCo.equals(AIConfig.QUAN_TRONG)) {
                    if (!trangThai[xlocation][yLocation].typeQuanCo.equals(mauQuanDiChuyen)) {
                        if (findIntInList(giaTri, count)) {
                            return count;
                        }
                        return -1;
                    }
                    return -1;
                }
                xlocation--;
                yLocation++;
                if ((count >= max) || yLocation >= 10) {
                    return -1;
                }

            }
            return -1;
        } else {
            return -1;
        }
    }

    private int kiemTraAnQuanTren(AICell[][] trangThai, int xlocation, int yLocation,
                                  List<Integer> giaTri, String mauQuanDiChuyen) {
        //y++, x==;
        if (yLocation < 9) {
            yLocation += 2;
            int count = 0;
            int max = findMax(giaTri);
            while (yLocation <= 10) {
                count++;
                if (!trangThai[xlocation][yLocation].typeQuanCo.equals(AIConfig.QUAN_TRONG)) {
                    if (!trangThai[xlocation][yLocation].typeQuanCo.equals(mauQuanDiChuyen)) {
                        if (findIntInList(giaTri, count)) {
                            return count;
                        }
                        return -1;
                    }
                    return -1;
                }
                yLocation++;
                if ((count >= max)) {
                    return -1;
                }

            }
            return -1;
        } else {
            return -1;
        }
    }

    private int kiemTraAnQuanTrenPhai(AICell[][] trangThai, int xlocation, int yLocation,
                                      List<Integer> giaTri, String mauQuanDiChuyen) {
        //y++, x++;

        if (yLocation < 9 && xlocation < 7) {
            yLocation += 2;
            xlocation += 2;
            int count = 0;
            int max = findMax(giaTri);
            while (yLocation <= 10) {
                count++;
                if (!trangThai[xlocation][yLocation].typeQuanCo.equals(AIConfig.QUAN_TRONG)) {
                    if (!trangThai[xlocation][yLocation].typeQuanCo.equals(mauQuanDiChuyen)) {
                        if (findIntInList(giaTri, count)) {
                            return count;
                        }
                        return -1;
                    }
                    return -1;
                }
                yLocation++;
                xlocation++;
                if ((count >= max) || xlocation >= 8) {
                    return -1;
                }

            }
            return -1;
        } else {
            return -1;
        }
    }

    private int kiemTraAnQuanPhai(AICell[][] trangThai, int xlocation, int yLocation,
                                  List<Integer> giaTri, String mauQuanDiChuyen) {
        //y==, x++;
        if (xlocation < 7) {
            xlocation += 2;
            int count = 0;
            int max = findMax(giaTri);
            while (xlocation <= 8) {
                count++;
                if (!trangThai[xlocation][yLocation].typeQuanCo.equals(AIConfig.QUAN_TRONG)) {
                    if (!trangThai[xlocation][yLocation].typeQuanCo.equals(mauQuanDiChuyen)) {
                        if (findIntInList(giaTri, count)) {
                            return count;
                        }
                        return -1;
                    }
                    return -1;
                }
                xlocation++;
                if ((count >= max)) {
                    return -1;
                }

            }
            return -1;
        } else {
            return -1;
        }
    }

    private int kiemTraAnQuanPhaiDuoi(AICell[][] trangThai, int xlocation, int yLocation,
                                      List<Integer> giaTri, String mauQuanDiChuyen) {
        //y--, x++;
        if (xlocation < 7 && yLocation > 1) {
            xlocation += 2;
            yLocation -= 2;
            int count = 0;
            int max = findMax(giaTri);
            while (xlocation <= 8) {
                count++;
                if (!trangThai[xlocation][yLocation].typeQuanCo.equals(AIConfig.QUAN_TRONG)) {
                    if (!trangThai[xlocation][yLocation].typeQuanCo.equals(mauQuanDiChuyen)) {
                        if (findIntInList(giaTri, count)) {
                            return count;
                        }
                        return -1;
                    }
                    return -1;
                }
                xlocation++;
                yLocation--;
                if ((count >= max) || yLocation <= 0) {
                    return -1;
                }

            }
            return -1;
        } else {
            return -1;
        }
    }

    private int kiemTraAnQuanDuoi(AICell[][] trangThai, int xlocation, int yLocation, List<Integer> giaTri, String mauQuanDiChuyen) {
        //y--, x==;
        if (yLocation > 1) {
            yLocation -= 2;
            int count = 0;
            int max = findMax(giaTri);
            while (yLocation >= 0) {
                count++;
                if (!trangThai[xlocation][yLocation].typeQuanCo.equals(AIConfig.QUAN_TRONG)) {
                    if (!trangThai[xlocation][yLocation].typeQuanCo.equals(mauQuanDiChuyen)) {
                        if (findIntInList(giaTri, count)) {
                            return count;
                        }
                        return -1;
                    }
                    return -1;
                }
                yLocation--;
                if ((count >= max)) {
                    return -1;
                }
            }
            return -1;
        } else {
            return -1;
        }
    }

    private int kiemTraAnQuanDuoiTrai(AICell[][] trangThai, int xlocation, int yLocation,
                                      List<Integer> giaTri, String mauQuanDiChuyen) {
        //y--, x--;
        if (yLocation > 1 && xlocation > 1) {
            yLocation -= 2;
            xlocation -= 2;
            int count = 0;
            int max = findMax(giaTri);
            while (yLocation >= 0) {
                count++;
                if (!trangThai[xlocation][yLocation].typeQuanCo.equals(AIConfig.QUAN_TRONG)) {
                    if (!trangThai[xlocation][yLocation].typeQuanCo.equals(mauQuanDiChuyen)) {
                        if (findIntInList(giaTri, count)) {
                            return count;
                        }
                        return -1;
                    }
                    return -1;
                }
                yLocation--;
                xlocation--;
                if ((count >= max) || xlocation <= 0) {
                    return -1;
                }
            }
            return -1;
        } else {
            return -1;
        }
    }

    private boolean findIntInList(List<Integer> lstValue, int value) {
        for (int temp : lstValue) {
            if (temp == value) {
                return true;
            }
        }
        return false;
    }

    private int findMax(List<Integer> lstValue) {
        int max = lstValue.get(0);
        for (int temp : lstValue) {
            if (temp > max) {
                max = temp;
            }
        }
        return max;
    }
    //endregion

    //region tao note Con
    private void taoCayTroChoi(AINote noteFather) {
        int countLevel = noteFather.countLevel;
        String quanCoDiChuyen = (noteFather.quanDiChuyen);
        List<AICell> lstQuanCoCanDi = new ArrayList<>();
        if (quanCoDiChuyen.equals(AIConfig.QUAN_DO)) {
            lstQuanCoCanDi = noteFather.lstQuanDo;
        } else {
            lstQuanCoCanDi = noteFather.lstQuanXanh;
        }
        //Debug.Log("COUNT " + lstQuanCoCanDi.Count + "____" + quanCoDiChuyen);
        for (AICell quanCo : lstQuanCoCanDi) {
            //Debug.Log("vong for " + noteFather.countLevel + "_____" + quanCo.typeQuanCo + "" + quanCo.giaTri);
            //Kiem tra cac nuoc co the buoc chua tinh nuoc an
            List<AIConfig.Location> lstNuocDi = timNuocDi(quanCo.xLocation, quanCo.yLocation, quanCo.giaTri, noteFather.trangThaiBanCo);
            //Debug.Log("so nuoc di 1: " + lstNuocDi.Count);

            lstNuocDi.addAll(timCacNuocAnQuan(quanCo.xLocation, quanCo.yLocation, quanCo.giaTri, noteFather.trangThaiBanCo, quanCoDiChuyen));
            //Debug.Log("so nuoc di 2: " + lstNuocDi.Count);

            for (AIConfig.Location location : lstNuocDi) {
                AINote note = taoNoteCon(noteFather, quanCo.xLocation, quanCo.yLocation,
                        location.xLocation, location.yLocation, quanCoDiChuyen);
                noteFather.noteConS.add(note);
                if (note.countLevel < doSau) {
                    taoCayTroChoi(note);
                }
            }
        }
    }


    //tim cac nuoc di co the co cua 1 quan co
    private List<AIConfig.Location> timNuocDi(int xlocation, int ylocation, int giaTri, AICell[][] trangThai) {
        List<AIConfig.Location> lstLocation = new ArrayList<>();
        //kiem tra trai x-- y==
        for (int i = 1; i <= giaTri; i++) {
            if (xlocation - i >= 0) {
                if (trangThai[xlocation - i][ylocation].typeQuanCo.equals(AIConfig.QUAN_TRONG)) {
                    lstLocation.add(new AIConfig.Location(xlocation - i, ylocation));
                } else {
                    break;
                }
            }
        }

        //kiem tra trai -tren x-- y++
        for (int i = 1; i <= giaTri; i++) {
            if (xlocation - i >= 0 && ylocation + i <= 10) {
                if (trangThai[xlocation - i][ylocation + i].typeQuanCo.equals(AIConfig.QUAN_TRONG)) {
                    lstLocation.add(new AIConfig.Location(xlocation - i, ylocation + i));
                } else {
                    break;
                }
            }
        }

        //kiem tra tren x== y++
        for (int i = 1; i <= giaTri; i++) {
            if (ylocation + i <= 10) {
                if (trangThai[xlocation][ylocation + i].typeQuanCo.equals(AIConfig.QUAN_TRONG)){
                    lstLocation.add(new AIConfig.Location(xlocation, ylocation + i));
                } else{
                    break;
                }
            }
        }

        //kiem tra tren phai x++ y++
        for (int i = 1; i <= giaTri; i++) {
            if (xlocation + i <= 8 && ylocation + i <= 10) {
                if (trangThai[xlocation + i][ylocation + i].typeQuanCo.equals(AIConfig.QUAN_TRONG)){
                    lstLocation.add(new AIConfig.Location(xlocation + i, ylocation + i));
                } else{
                    break;
                }
            }
        }

        //kiem tra phai x++ y==
        for (int i = 1; i <= giaTri; i++) {
            if (xlocation + i <= 8) {
                if (trangThai[xlocation + i][ylocation].typeQuanCo.equals(AIConfig.QUAN_TRONG)){
                    lstLocation.add(new AIConfig.Location(xlocation + i, ylocation));
                } else{
                    break;
                }
            }
        }

        //kiem tra phai duoi x++ y--
        for (int i = 1; i <= giaTri; i++) {
            if (xlocation + i <= 8 && ylocation - i >= 0) {
                if (trangThai[xlocation + i][ylocation - i].typeQuanCo.equals(AIConfig.QUAN_TRONG)){
                    lstLocation.add(new AIConfig.Location(xlocation + i, ylocation - i));
                } else{
                    break;
                }
            }
        }

        //kiem tra duoi x== y--
        for (int i = 1; i <= giaTri; i++) {
            if (ylocation - i >= 0) {
                if (trangThai[xlocation][ylocation - i].typeQuanCo.equals(AIConfig.QUAN_TRONG)){
                    lstLocation.add(new AIConfig.Location(xlocation, ylocation - i));
                } else{
                    break;
                }

            }
        }

        //kiem tra duoi trai x-- y--
        for (int i = 1; i <= giaTri; i++) {
            if (xlocation - i >= 0 && ylocation - i >= 0) {
                if (trangThai[xlocation - i][ylocation - i].typeQuanCo.equals(AIConfig.QUAN_TRONG)){
                    lstLocation.add(new AIConfig.Location(xlocation - i, ylocation - i));
                } else{
                    break;
                }
            }
        }
        return lstLocation;
    }

    private List<AIConfig.Location> timCacNuocAnQuan(int xlocation, int ylocation, int giaTri,
                                                     AICell[][] trangThai, String quanDuocDiChuyen) {
        List<AIConfig.Location> value = new ArrayList<>();
        //region kiem tra trai x-- y==
        if (xlocation > 0) {
            if (trangThai[xlocation - 1][ylocation].typeQuanCo.equals(quanDuocDiChuyen)) {
                List<Integer> dsPt = new ArrayList<>();
                int giaTri1 = giaTri;
                int giaTri2 = trangThai[xlocation - 1][ylocation].giaTri;

                if (enableCong) {
                    int tempC = giaTri1 + giaTri2;
                    dsPt.add(tempC % 10);
                }
                if (enableTru) {
                    if (giaTri1 > giaTri2) {
                        int tempT = giaTri1 - giaTri2;
                        dsPt.add(tempT % 10);
                    }
                }
                if (enableNhan) {
                    int tempN = giaTri1 * giaTri2;
                    dsPt.add(tempN % 10);
                }
                if (enableChia) {
                    if (giaTri1 > giaTri2) {
                        int tempCh = giaTri1 / giaTri2;
                        dsPt.add(tempCh % 10);
                    }
                }
                int count = kiemTraAnQuanDuoiTrai(trangThai, xlocation, ylocation, dsPt, quanDuocDiChuyen);
                if (count != -1) {
                    count += 1;
                    AIConfig.Location location = new AIConfig.Location(xlocation - count, ylocation);
                    value.add(location);
                }
            }
        }
        //endregion

        //region kiem tra trai tren x-- y++
        if (xlocation > 0 && ylocation < 10) {
            if (trangThai[xlocation - 1][ylocation + 1].typeQuanCo.equals(quanDuocDiChuyen)) {
                List<Integer> dsPt = new ArrayList<>();
                int giaTri1 = giaTri;
                int giaTri2 = trangThai[xlocation - 1][ylocation + 1].giaTri;

                if (enableCong) {
                    int tempC = giaTri1 + giaTri2;
                    dsPt.add(tempC % 10);
                }
                if (enableTru) {
                    if (giaTri1 > giaTri2) {
                        int tempT = giaTri1 - giaTri2;
                        dsPt.add(tempT % 10);
                    }
                }
                if (enableNhan) {
                    int tempN = giaTri1 * giaTri2;
                    dsPt.add(tempN % 10);
                }
                if (enableChia) {
                    if (giaTri1 > giaTri2) {
                        int tempCh = giaTri1 / giaTri2;
                        dsPt.add(tempCh % 10);
                    }
                }
                int count = kiemTraAnQuanTraiTren(trangThai, xlocation, ylocation, dsPt, quanDuocDiChuyen);
                if (count != -1) {
                    count += 1;
                    AIConfig.Location location = new AIConfig.Location(xlocation - count, ylocation + count);
                    value.add(location);
                }
            }
        }
        //endregion

        //region kiem tra tren x== y++
        if (ylocation < 10) {
            if (trangThai[xlocation][ylocation + 1].typeQuanCo.equals(quanDuocDiChuyen)) {
                List<Integer> dsPt = new ArrayList<>();
                int giaTri1 = giaTri;
                int giaTri2 = trangThai[xlocation][ylocation + 1].giaTri;

                if (enableCong) {
                    int tempC = giaTri1 + giaTri2;
                    dsPt.add(tempC % 10);
                }
                if (enableTru) {
                    if (giaTri1 > giaTri2) {
                        int tempT = giaTri1 - giaTri2;
                        dsPt.add(tempT % 10);
                    }
                }
                if (enableNhan) {
                    int tempN = giaTri1 * giaTri2;
                    dsPt.add(tempN % 10);
                }
                if (enableChia) {
                    if (giaTri1 > giaTri2) {
                        int tempCh = giaTri1 / giaTri2;
                        dsPt.add(tempCh % 10);
                    }
                }
                int count = kiemTraAnQuanTren(trangThai, xlocation, ylocation, dsPt, quanDuocDiChuyen);
                if (count != -1) {
                    count += 1;
                    AIConfig.Location location = new AIConfig.Location(xlocation, ylocation + count);
                    value.add(location);
                }
            }
        }
        //endregion

        //region kiem tra tren phai x++ y++
        if (xlocation < 8 && ylocation < 10) {
            if (trangThai[xlocation + 1][ylocation + 1].typeQuanCo.equals(quanDuocDiChuyen)) {
                List<Integer> dsPt = new ArrayList<>();
                int giaTri1 = giaTri;
                int giaTri2 = trangThai[xlocation + 1][ylocation + 1].giaTri;

                if (enableCong) {
                    int tempC = giaTri1 + giaTri2;
                    dsPt.add(tempC % 10);
                }
                if (enableTru) {
                    if (giaTri1 > giaTri2) {
                        int tempT = giaTri1 - giaTri2;
                        dsPt.add(tempT % 10);
                    }
                }
                if (enableNhan) {
                    int tempN = giaTri1 * giaTri2;
                    dsPt.add(tempN % 10);
                }
                if (enableChia) {
                    if (giaTri1 > giaTri2) {
                        int tempCh = giaTri1 / giaTri2;
                        dsPt.add(tempCh % 10);
                    }
                }
                int count = kiemTraAnQuanTrenPhai(trangThai, xlocation, ylocation, dsPt, quanDuocDiChuyen);
                if (count != -1) {
                    count += 1;
                    AIConfig.Location location = new AIConfig.Location(xlocation + count, ylocation + count);
                    value.add(location);
                }
            }
        }
        //endregion

        //region kiem tra phai x++ y==
        if (xlocation < 8) {
            if (trangThai[xlocation + 1][ylocation].typeQuanCo.equals(quanDuocDiChuyen)) {
                List<Integer> dsPt = new ArrayList<>();
                int giaTri1 = giaTri;
                int giaTri2 = trangThai[xlocation + 1][ylocation].giaTri;

                if (enableCong) {
                    int tempC = giaTri1 + giaTri2;
                    dsPt.add(tempC % 10);
                }
                if (enableTru) {
                    if (giaTri1 > giaTri2) {
                        int tempT = giaTri1 - giaTri2;
                        dsPt.add(tempT % 10);
                    }
                }
                if (enableNhan) {
                    int tempN = giaTri1 * giaTri2;
                    dsPt.add(tempN % 10);
                }
                if (enableChia) {
                    if (giaTri1 > giaTri2) {
                        int tempCh = giaTri1 / giaTri2;
                        dsPt.add(tempCh % 10);
                    }
                }
                int count = kiemTraAnQuanPhai(trangThai, xlocation, ylocation, dsPt, quanDuocDiChuyen);
                if (count != -1) {
                    count += 1;
                    AIConfig.Location location = new AIConfig.Location(xlocation + count, ylocation);
                    value.add(location);
                }
            }
        }
        //endregion

        //region kiem tra phai duoi x++ y--
        if (xlocation < 8 && ylocation > 0) {
            if (trangThai[xlocation + 1][ylocation - 1].typeQuanCo.equals(quanDuocDiChuyen)) {
                List<Integer> dsPt = new ArrayList<>();
                int giaTri1 = giaTri;
                int giaTri2 = trangThai[xlocation + 1][ylocation - 1].giaTri;

                if (enableCong) {
                    int tempC = giaTri1 + giaTri2;
                    dsPt.add(tempC % 10);
                }
                if (enableTru) {
                    if (giaTri1 > giaTri2) {
                        int tempT = giaTri1 - giaTri2;
                        dsPt.add(tempT % 10);
                    }
                }
                if (enableNhan) {
                    int tempN = giaTri1 * giaTri2;
                    dsPt.add(tempN % 10);
                }
                if (enableChia) {
                    if (giaTri1 > giaTri2) {
                        int tempCh = giaTri1 / giaTri2;
                        dsPt.add(tempCh % 10);
                    }
                }
                int count = kiemTraAnQuanPhaiDuoi(trangThai, xlocation, ylocation, dsPt, quanDuocDiChuyen);
                if (count != -1) {
                    count += 1;
                    AIConfig.Location location = new AIConfig.Location(xlocation + count, ylocation - count);
                    value.add(location);
                }
            }
        }
        //endregion

        //region kiem tra duoi x== y--
        if (ylocation > 0) {
            if (trangThai[xlocation][ylocation - 1].typeQuanCo.equals(quanDuocDiChuyen)) {
                List<Integer> dsPt = new ArrayList<>();
                int giaTri1 = giaTri;
                int giaTri2 = trangThai[xlocation][ylocation - 1].giaTri;

                if (enableCong) {
                    int tempC = giaTri1 + giaTri2;
                    dsPt.add(tempC % 10);
                }
                if (enableTru) {
                    if (giaTri1 > giaTri2) {
                        int tempT = giaTri1 - giaTri2;
                        dsPt.add(tempT % 10);
                    }
                }
                if (enableNhan) {
                    int tempN = giaTri1 * giaTri2;
                    dsPt.add(tempN % 10);
                }
                if (enableChia) {
                    if (giaTri1 > giaTri2) {
                        int tempCh = giaTri1 / giaTri2;
                        dsPt.add(tempCh % 10);
                    }
                }
                int count = kiemTraAnQuanDuoi(trangThai, xlocation, ylocation, dsPt, quanDuocDiChuyen);
                if (count != -1) {
                    count += 1;
                    AIConfig.Location location = new AIConfig.Location(xlocation, ylocation - count);
                    value.add(location);
                }
            }
        }
        //endregion

        // region kiem tra duoi trai x-- y--
        if (xlocation > 0 && ylocation > 0) {
            if (trangThai[xlocation - 1][ylocation - 1].typeQuanCo.equals(quanDuocDiChuyen)) {
                List<Integer> dsPt = new ArrayList<>();
                int giaTri1 = giaTri;
                int giaTri2 = trangThai[xlocation - 1][ylocation - 1].giaTri;

                if (enableCong) {
                    int tempC = giaTri1 + giaTri2;
                    dsPt.add(tempC % 10);
                }
                if (enableTru) {
                    if (giaTri1 > giaTri2) {
                        int tempT = giaTri1 - giaTri2;
                        dsPt.add(tempT % 10);
                    }
                }
                if (enableNhan) {
                    int tempN = giaTri1 * giaTri2;
                    dsPt.add(tempN % 10);
                }
                if (enableChia) {
                    if (giaTri1 > giaTri2) {
                        int tempCh = giaTri1 / giaTri2;
                        dsPt.add(tempCh % 10);
                    }
                }
                int count = kiemTraAnQuanDuoiTrai(trangThai, xlocation, ylocation, dsPt, quanDuocDiChuyen);
                if (count != -1) {
                    count += 1;
                    AIConfig.Location location = new AIConfig.Location(xlocation - count, ylocation - count);
                    value.add(location);
                }
            }
        }
        //endregion

        return value;
    }

    //diChuyenQuanTaoNutMoi
    private AINote taoNoteCon(AINote noteFather, int x1, int y1, int x2, int y2, String quanDangDiChuyen) {
        int diemCuocConLai = -1;
        List<AICell> lstQuanxanh = new ArrayList<>();
        List<AICell> lstQuanDo = new ArrayList<>();
        AICell[][] trangThai = cloneTrangThaiBanCo(noteFather.trangThaiBanCo, lstQuanxanh, lstQuanDo);

        //region di chuyen quan co cap nhat trang thai ban co va sanh sach quan co
        int giaTriQC = trangThai[x1][y1].giaTri;
        String typeQc = trangThai[x1][y1].typeQuanCo;
        if (typeQc.equals(AIConfig.QUAN_DO)) {
            //di chuyen quan do
            int index = findQcInList(trangThai[x1][y1], lstQuanDo);
            if (index != -1) {
                lstQuanDo.remove(index);

                //Kiem tra an quan doi phuong
                if (trangThai[x2][y2].typeQuanCo.equals(daoNguocTypeQuanCo(typeQc))) {
                    diemCuocConLai = noteFather.diemCuocConLai - trangThai[x2][y2].giaTri;
                    int index2 = findQcInList(trangThai[x1][y1], lstQuanxanh);
                    if (index2 != -1) {
                        lstQuanxanh.remove(index);
                    }
                }

                trangThai[x1][y1].giaTri = 0;
                trangThai[x1][y1].typeQuanCo = AIConfig.QUAN_TRONG;

                trangThai[x2][y2].giaTri = giaTriQC;
                trangThai[x2][y2].giaTri = giaTriQC;
                trangThai[x2][y2].typeQuanCo = typeQc;

                lstQuanDo.add(trangThai[x2][y2]);
            }
        } else {
            //di chuyen quan xanh --- khong co truong hop o co trong
            int index = findQcInList(trangThai[x1][y1], lstQuanxanh);
            if (index != -1) {
                lstQuanxanh.remove(index);

                //Kiem tra an quan doi phuong
                if (trangThai[x2][y2].typeQuanCo.equals(daoNguocTypeQuanCo(typeQc))) {
                    diemCuocConLai = noteFather.diemCuocConLai - trangThai[x2][y2].giaTri;
                    int index2 = findQcInList(trangThai[x1][y1], lstQuanDo);
                    if (index2 != -1) {
                        lstQuanDo.remove(index2);
                    }

                    trangThai[x1][y1].giaTri = 0;
                    trangThai[x1][y1].typeQuanCo = AIConfig.QUAN_TRONG;

                    trangThai[x2][y2].giaTri = giaTriQC;
                    trangThai[x2][y2].typeQuanCo = typeQc;

                    lstQuanxanh.add(trangThai[x2][y2]);
                }
            }
        }
        //endregion

        if (diemCuocConLai <= -1) {
            if (noteFather.diemCuocConLai != -1) {
                diemCuocConLai = 0;
            } else {
                diemCuocConLai = -1;
            }
        }
        int countLevel = noteFather.countLevel + 1;
        List<AINote> lstNoteCon = null;
        int score = 0;
        if (countLevel != doSau) {
            lstNoteCon = new ArrayList<>();
        } else {
            score = tinhDiemTrangThaiBanCo(quanDangDiChuyen, lstQuanDo, lstQuanxanh, trangThai, diemCuocConLai);
        }
        AINote aINote = new AINote(score, false, daoNguocTypeQuanCo(quanDangDiChuyen),
                trangThai, diemCuocConLai, lstQuanxanh, lstQuanDo, lstNoteCon, noteFather, countLevel);
        return aINote;
    }


    private int findQcInList(AICell quanCo, List<AICell> listQuanCo) {
        for (int i = 0; i < listQuanCo.size(); i++) {
            if (listQuanCo.get(i).typeQuanCo.equals(quanCo.typeQuanCo)
                    && listQuanCo.get(i).giaTri == quanCo.giaTri) {
                return i;
            }
        }
        return -1;
    }

    private AICell[][]   cloneTrangThaiBanCo(AICell[][]trangThai, List<AICell> lstQuanxanh, List<AICell> lstQuanDo) {
        AICell[][] clone = new AICell[9][ 11];
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 9; x++) {
                clone[x][y] =new AICell(trangThai[x][ y]);
            }
        }
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 11; y++) {
                if (clone[x][y].typeQuanCo.equals(AIConfig.QUAN_DO)){
                    lstQuanDo.add(new AICell(clone[x][ y]));
                }
                if (clone[x][y].typeQuanCo.equals(AIConfig.QUAN_XANH)){
                    lstQuanxanh.add(new AICell(clone[x][ y]));
                }
            }
        }
        return clone;
    }

    private String daoNguocTypeQuanCo(String typeQuanCo) {
        if (typeQuanCo.equals(AIConfig.QUAN_DO)) {
            return AIConfig.QUAN_XANH;
        }
        if (typeQuanCo.equals(AIConfig.QUAN_XANH)) {
            return AIConfig.QUAN_DO;
        }
        return AIConfig.QUAN_TRONG;
    }
    //endregion


    // region duyet cay
    private int[] duyetCay(AINote noteGoc) {
        AINote noteConTrai = getConTrai(noteGoc);
        AINote bestNote = satart(noteConTrai);
        List<AINote> lstCon = noteGoc.noteConS;
        AINote max = lstCon.get(0);
        for (AINote note : lstCon) {
            if (note.scoreTrangThai > max.scoreTrangThai) {
                max = note;
            }
        }
        String sold1 = cellToString(noteGoc.trangThaiBanCo);
        String snew = cellToString(max.trangThaiBanCo);
        int[] result = findIndexChanged(snew, sold1);
        return result;

    }

    private int[] findIndexChanged(String valueNew, String valueOld) {
        int[] result = new int[4];
        String[] tempOld = valueOld.split("_");
        String[] tempNew = valueNew.split("_");
        if (tempOld.length == tempNew.length) {
            int oldI = -1, newI = -1;
            int[] tempIndex = new int[2];
            tempIndex[0]=-1;
            tempIndex[1]=-1;
            int count = 0;
            for (int i = 0; i < tempNew.length; i++) {
                if (!tempNew[i].equals(tempOld[i])) {
                    if (count >= 2) {
                        result[0] = -1;
                        result[1] = -1;
                        result[2] = -1;
                        result[3] = -1;
                        return result;
                    }
                    tempIndex[count] = i;
                    count++;

                }
            }
            if (tempNew[tempIndex[0]].equals("00") && !tempOld[tempIndex[0]].equals("00")) {
                oldI = tempIndex[0];
                newI = tempIndex[1];
            } else {
                oldI = tempIndex[1];
                newI = tempIndex[0];
            }
            result[0] = oldI % 9;
            result[1] = oldI / 9;
            result[2] = newI % 9;
            result[3] = newI / 9;
        }
        return result;
    }


    private AINote satart(AINote noteCon) {
        AINote noteFather = noteCon.noteFather;
        AINote bestNote = timGiaTriNote(noteFather);
        if (noteFather.noteFather != null) {
            satart(noteFather);
        }
        return bestNote;
    }


    private AINote getConTrai(AINote notGoc) {
        List<AINote> lstTemp = notGoc.noteConS;
        while (lstTemp.get(0).noteConS != null) {
            lstTemp = lstTemp.get(0).noteConS;
        }
        return lstTemp.get(0);
    }


    //private AINote findBestNote(AINote noteCon) {
    //    AINote noteFather = getFather(noteCon);
    //    List<AINote> lstTemp = noteFather.noteConS;
    //    //kiem tra neu nut cha co countLevel +1 != do sau --> nut co vai tro lam ong/ dung for duyet dong thoi cac con
    //    if (noteFather.countLevel + 1 != doSau) {
    //        foreach(AINote noteTemp in lstTemp) {
    //            if (!noteTemp.daDuyet) {

    //            }
    //        }
    //        while
    //    } else {
    //        int value = findMaxMin(lstTemp, 1, false, 0);
    //        noteFather.scoreTrangThai = value;
    //        noteFather.daDuyet = true;
    //        findBestNote(noteFather);
    //    }
    //    if (noteFather.countLevel % 2 == 0) {
    //        //chia het cho 2 tim min
    //    } else {
    //        // chia su cho 2 tim max
    //    }
    //}


    private AINote timGiaTriNote(AINote noteFather) {
        if (noteFather.countLevel + 1 != doSau) {
            List<AINote> lstNoteCon = noteFather.noteConS;
            //Debug.Log("So con note khac " + lstNoteCon.Count+" level "+ noteFather.countLevel);

            for (AINote noteTemp : lstNoteCon) {
                if (!noteTemp.daDuyet) {
                    timGiaTriNote(noteTemp);
                }
            }
            if (noteFather.countLevel % 2 == 0) {
                int min = lstNoteCon.get(0).scoreTrangThai;
                int index = 0;
                for (int i = 0; i < lstNoteCon.size(); i++) {
                    if (lstNoteCon.get(i).scoreTrangThai < min) {
                        min = lstNoteCon.get(i).scoreTrangThai;
                        index = i;
                    }
                }
                noteFather.scoreTrangThai = min;
                noteFather.daDuyet = true;
                return lstNoteCon.get(index);
            } else {
                int max = lstNoteCon.get(0).scoreTrangThai;
                int index = 0;
                for (int i = 0; i < lstNoteCon.size(); i++) {
                    if (lstNoteCon.get(i).scoreTrangThai > max) {
                        max = lstNoteCon.get(i).scoreTrangThai;
                        index = i;
                    }
                }
                noteFather.scoreTrangThai = max;
                noteFather.daDuyet = true;
                return lstNoteCon.get(index);
            }
        } else {
            List<AINote> lstNoteCon = noteFather.noteConS;

            //Debug.Log("So con nkơt cuoi " + lstNoteCon.Count);
            if (lstNoteCon.size() > 0) {
                int max = lstNoteCon.get(0).scoreTrangThai;
                int index = 0;
                for (int i = 0; i < lstNoteCon.size(); i++) {
                    if (lstNoteCon.get(i).scoreTrangThai > max) {
                        max = lstNoteCon.get(i).scoreTrangThai;
                        index = i;
                    }
                }
                noteFather.scoreTrangThai = max;
                noteFather.daDuyet = true;
                return lstNoteCon.get(index);
            }
            return null;
        }
    }


    ////alpha bata cut
    //private void findMaxMin(AINote noteFather, int status, bool enableAlBe, int albe) { //status ==2: min , status==1 max
    //    if (status == 1) {
    //        //tim max
    //        if (enableAlBe) {
    //            //tim max voi gia tri nho hon albe neu lon hon gia tri nut cha bang albe ex: alb=3, find= 5
    //            if(noteFather.countLevel+1== doSau) {
    //                if()
    //                List<AINote> lstNoteCon = noteFather.noteConS;
    //                int max = lstNoteCon[0].scoreTrangThai;
    //                foreach (AINote note in lstNoteCon) {

    //                    if (note.scoreTrangThai > max) {
    //                        max = note.scoreTrangThai;
    //                    }
    //                    //kiem tra alpha
    //                    if (max > albe) {
    //                        noteFather.daDuyet = true;
    //                        noteFather.scoreTrangThai= max;
    //                        break;
    //                    }
    //                }
    //            } else {
    //                //khong phai note gan cuoi
    //                List<AINote> lstNoteCon = noteFather.noteConS;
    //                if()

    //            }

    //            return max;
    //        } else {
    //            //tim max khong dieu kien
    //            List<AINote> lstNoteCon = noteFather.noteConS;
    //            int max = lstNoteCon[0].scoreTrangThai;
    //            foreach (AINote note in lstNoteCon) {
    //                if (note.scoreTrangThai > max) {
    //                    max = note.scoreTrangThai;
    //                }
    //            }
    //            noteFather.noteFather.scoreTrangThai = max;
    //        }
    //    } else {
    //        //tim min
    //        if (enableAlBe) {
    //            //tim min voi gia tri lon hon albe neu nho hon gia tri nut cha bang albe ex: alb= 5, find= 3
    //            int min = lstNoteCon[0].scoreTrangThai;
    //            foreach (AINote note in lstNoteCon) {
    //                if (note.scoreTrangThai < min) {
    //                    min = note.scoreTrangThai;
    //                }
    //                //kiem tra alpha
    //                if (min < albe) {
    //                    return min;
    //                }
    //            }
    //            return min;
    //        } else {
    //            //tim min khong dieu kien
    //            int min = lstNoteCon[0].scoreTrangThai;
    //            foreach (AINote note in lstNoteCon) {
    //                if (note.scoreTrangThai < min) {
    //                    min = note.scoreTrangThai;
    //                }
    //            }
    //            return min;
    //        }
    //    }
    //}
    ////
    ///


    private String cellToString(AICell[][]trangThai) {
        String value = "";
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 9; x++) {
                AICell temp = trangThai[x][ y];
                switch (temp.typeQuanCo) {
                    case AIConfig.QUAN_XANH:
                        int va = temp.giaTri;
                        value += AIConfig.QUAN_XANH + va;
                        break;
                    case AIConfig.QUAN_DO:
                        int va1 = temp.giaTri;
                        value += AIConfig.QUAN_DO + va1;
                        break;
                    case AIConfig.QUAN_TRONG:
                        value += "00";
                        break;
                    default:
                        break;
                }
                value += "_";
            }
        }
        String s = value.substring(0, value.length() - 1);
        return s;
    }
    //endregion


}
