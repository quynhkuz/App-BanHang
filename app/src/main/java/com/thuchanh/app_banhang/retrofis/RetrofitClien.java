package com.thuchanh.app_banhang.retrofis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClien {
    private static Retrofit retrofit = null;
    public  static Retrofit getclient(String url)
    {
        OkHttpClient builder = new OkHttpClient.Builder()
                .readTimeout(2000,TimeUnit.MILLISECONDS)
                .writeTimeout(2000,TimeUnit.MILLISECONDS)
                .connectTimeout(3000,TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();

        Gson gson = new GsonBuilder().setLenient().create();

        //khoi tao
        retrofit = new Retrofit.Builder()
                //link duong dan api
                .baseUrl(url)
                .client(builder)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }
}
