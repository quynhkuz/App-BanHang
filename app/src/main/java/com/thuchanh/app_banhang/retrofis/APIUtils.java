package com.thuchanh.app_banhang.retrofis;

public class APIUtils {
    public static final String url = "https://abh1997.000webhostapp.com/";

    public static DataClient getdata()
    {
        return RetrofitClien.getclient(url).create(DataClient.class);
    }
}
