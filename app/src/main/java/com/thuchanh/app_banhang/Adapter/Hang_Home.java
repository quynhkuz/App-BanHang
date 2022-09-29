package com.thuchanh.app_banhang.Adapter;

import java.io.Serializable;

public class Hang_Home implements Serializable {
   private  String masp,matk,tensp,soluong,gia,anhsp,mota,tk;

    public String getTk() {
        return tk;
    }



    public Hang_Home(String masp, String matk, String tensp, String soluong, String gia, String anhsp, String mota, String tk) {
        this.masp = masp;
        this.matk = matk;
        this.tensp = tensp;
        this.soluong = soluong;
        this.gia = gia;
        this.anhsp = anhsp;
        this.mota = mota;
        this.tk = tk;
    }

    public void setTk(String tk) {
        this.tk = tk;
    }

    public Hang_Home() {
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getMatk() {
        return matk;
    }

    public void setMatk(String matk) {
        this.matk = matk;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getAnhsp() {
        return anhsp;
    }

    public void setAnhsp(String anhsp) {
        this.anhsp = anhsp;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }
}
