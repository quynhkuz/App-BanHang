package com.thuchanh.app_banhang.Adapter;

public class GioHang {
    private String magh,masp,tensp,sl,gia,anh;

    public GioHang(String magh, String masp, String tensp, String sl, String gia, String anh) {
        this.magh = magh;
        this.masp = masp;
        this.tensp = tensp;
        this.sl = sl;
        this.gia = gia;
        this.anh = anh;
    }

    public String getMagh() {
        return magh;
    }

    public void setMagh(String magh) {
        this.magh = magh;
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

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }
}
