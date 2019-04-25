package com.hungth.cotoan.screen.ai;

import com.hungth.cotoan.data.model.AICell;

import java.util.List;

public class AINote {
    public int scoreTrangThai ; // tong diem tinh tren vi tri cac quan co
    public boolean daDuyet ;  // mac dinh =0
    public String quanDiChuyen ;//cho note con luot di sau
    public AICell[][] trangThaiBanCo;
    public int diemCuocConLai ;
    public List<AICell> lstQuanXanh ;
    public List<AICell> lstQuanDo ;
    public List<AINote> noteConS ;
    public AINote noteFather ;
    public int countLevel ; // các cap de dem level nut con bang nut cha +1

    public AINote() {

    }

    public AINote(int scoreTrangThai, boolean daDuyet, String quanDiChuyen,
                  AICell[][] trangThaiBanCo, int diemCuocConLai,
                  List<AICell> lstQuanXanh, List<AICell> lstQuanDo,
                  List<AINote> noteConS, AINote noteFather,
                  int countLevel) {
        this.scoreTrangThai = scoreTrangThai;
        this.daDuyet = daDuyet;
        this.quanDiChuyen = quanDiChuyen;
        this.trangThaiBanCo = trangThaiBanCo;
        this.diemCuocConLai = diemCuocConLai;
        this.lstQuanXanh = lstQuanXanh;
        this.lstQuanDo = lstQuanDo;
        this.noteConS = noteConS;
        this.noteFather = noteFather;
        this.countLevel = countLevel;
    }

    public AINote(AINote aINote) {
        this. scoreTrangThai   = aINote.scoreTrangThai;
        this.daDuyet = aINote.daDuyet;
        this.quanDiChuyen = aINote.quanDiChuyen;
        this.trangThaiBanCo = aINote.trangThaiBanCo;
        this.diemCuocConLai = aINote.diemCuocConLai;
        this.lstQuanXanh = aINote.lstQuanXanh;
        this.lstQuanDo = aINote.lstQuanDo;
        this.noteConS = aINote.noteConS;
        this.noteFather = aINote.noteFather;
        this.countLevel = aINote.countLevel;

    }

    public int getScoreTrangThai() {
        return scoreTrangThai;
    }

    public void setScoreTrangThai(int scoreTrangThai) {
        this.scoreTrangThai = scoreTrangThai;
    }

    public boolean isDaDuyet() {
        return daDuyet;
    }

    public void setDaDuyet(boolean daDuyet) {
        this.daDuyet = daDuyet;
    }

    public String getQuanDiChuyen() {
        return quanDiChuyen;
    }

    public void setQuanDiChuyen(String quanDiChuyen) {
        this.quanDiChuyen = quanDiChuyen;
    }

    public AICell[][] getTrangThaiBanCo() {
        return trangThaiBanCo;
    }

    public void setTrangThaiBanCo(AICell[][] trangThaiBanCo) {
        this.trangThaiBanCo = trangThaiBanCo;
    }

    public int getDiemCuocConLai() {
        return diemCuocConLai;
    }

    public void setDiemCuocConLai(int diemCuocConLai) {
        this.diemCuocConLai = diemCuocConLai;
    }

    public List<AICell> getLstQuanXanh() {
        return lstQuanXanh;
    }

    public void setLstQuanXanh(List<AICell> lstQuanXanh) {
        this.lstQuanXanh = lstQuanXanh;
    }

    public List<AICell> getLstQuanDo() {
        return lstQuanDo;
    }

    public void setLstQuanDo(List<AICell> lstQuanDo) {
        this.lstQuanDo = lstQuanDo;
    }

    public List<AINote> getNoteConS() {
        return noteConS;
    }

    public void setNoteConS(List<AINote> noteConS) {
        this.noteConS = noteConS;
    }

    public AINote getNoteFather() {
        return noteFather;
    }

    public void setNoteFather(AINote noteFather) {
        this.noteFather = noteFather;
    }

    public int getCountLevel() {
        return countLevel;
    }

    public void setCountLevel(int countLevel) {
        this.countLevel = countLevel;
    }
}
