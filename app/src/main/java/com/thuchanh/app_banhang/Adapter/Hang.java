package com.thuchanh.app_banhang.Adapter;

import java.io.Serializable;

public class Hang implements Serializable {
    private  String masp,tensp,soluong,gia,anhsp,mota;

    public Hang() {
    }

    public Hang(String masp, String tensp, String soluong, String gia, String anhsp, String mota) {
        this.masp = masp;
        this.tensp = tensp;
        this.soluong = soluong;
        this.gia = gia;
        this.anhsp = anhsp;
        this.mota = mota;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
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
