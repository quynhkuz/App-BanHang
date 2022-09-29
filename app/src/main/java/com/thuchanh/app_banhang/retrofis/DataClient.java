package com.thuchanh.app_banhang.retrofis;

import com.google.gson.JsonArray;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DataClient {

    //gui du lieu dang file len server
    @Multipart
    @POST("uploadimg.php")
    //call<> server tra ve kieu dl gi
    //@Part MultipartBody.Part photo gui len server gom nhung gi
    Call<String> uploadphoto(@Part MultipartBody.Part photo);

    //gui du lieu dang chuoi len server
    @FormUrlEncoded
    @POST("insert.php")
    Call<String> InsertData(@Field("matk") String matk,
                            @Field("taikhoan") String taikhoan,
                            @Field("matkhau") String matkhau,
                            @Field("gmail") String gmail,
                            @Field("sdt") String sdt,
                            @Field("anh") String anh);


    //gui du lieu dang chuoi len server
    @FormUrlEncoded
    @POST("themsp.php")
    Call<String> InsertHang(@Field("matk") String matk,
                            @Field("masp") String masp,
                            @Field("ten") String ten,
                            @Field("soluong") String soluong,
                            @Field("gia") String gia,
                            @Field("anhsp") String anhsp,
                            @Field("mota") String mota);


    @FormUrlEncoded
    @POST("themsp_giohang.php")
    Call<String> InsertGioHang(@Field("magh") String magh,
                            @Field("matk") String matk,
                            @Field("masp") String masp,
                            @Field("tensp") String tensp,
                            @Field("soluong") String soluong,
                            @Field("gia") String gia,
                            @Field("anh") String anh);


    @FormUrlEncoded
    @POST("select_hang.php")
    Call<JsonArray> getdata(@Field("matk") String matk);



    @FormUrlEncoded
    @POST("selelct_home.php")
    Call<JsonArray> select_home(@Field("matk") String abc);




    @FormUrlEncoded
    @POST("delete.php")
    Call<String> delete(@Field("masp") String masp,
                        @Field("anh") String anh);


    @FormUrlEncoded
    @POST("update_giohang.php")
    Call<String> update_giohang(@Field("magh") String magh,
                                @Field("time") String time);

}
