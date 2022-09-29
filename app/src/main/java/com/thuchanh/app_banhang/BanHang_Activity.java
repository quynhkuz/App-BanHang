package com.thuchanh.app_banhang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.thuchanh.app_banhang.Adapter.Adapter_Hang;
import com.thuchanh.app_banhang.Adapter.Hang;
import com.thuchanh.app_banhang.Adapter.Hang_Home;
import com.thuchanh.app_banhang.Fragment.Fragment_BanHang;
import com.thuchanh.app_banhang.retrofis.APIUtils;
import com.thuchanh.app_banhang.retrofis.DataClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BanHang_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    Adapter_Hang adapter;
    ConstraintLayout constraintLayout;
    Button back,home;
    ArrayList<Hang> arr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_hang);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintlayout);
        back = (Button)findViewById(R.id.btn_back);
        home = (Button)findViewById(R.id.btn_home);
        toolbar = (Toolbar)findViewById(R.id.toolbar321);

        //tao nut back tren toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        arr= new ArrayList<>();
       getdata();

        recyclerView = (RecyclerView)findViewById(R.id.recysview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BanHang_Activity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

//        if(arr.size()<1)
//        {
//            Toast.makeText(BanHang_Activity.this, "Không có Sản Phẩm", Toast.LENGTH_SHORT).show();
//        }

        adapter=new Adapter_Hang(BanHang_Activity.this,arr);
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration recyc = new DividerItemDecoration(BanHang_Activity.this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(recyc);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BanHang_Activity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BanHang_Activity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


        constraintLayout.setOnTouchListener(new OnSwipeTouchListener(BanHang_Activity.this) {
            public void onSwipeTop() {
                //Toast.makeText(BanHang_Activity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Intent intent=new Intent(BanHang_Activity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
            public void onSwipeLeft() {
                //Toast.makeText(BanHang_Activity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                //Toast.makeText(BanHang_Activity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

    }


    //sk cua back cua toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent=new Intent(BanHang_Activity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


/*
    public void retrofis()
    {

        DataClient callback = APIUtils.getdata();
        Call<JsonArray> login = callback.getdata(MainActivity.mtk_user);
        login.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                //chuyen ve sstring
                String a = response.body().toString();

                if (!a.equals(""))
                {
                    try {
                        //khoi tao json moi co thu vien org.json
                        JSONArray mang =new JSONArray(a);

                        for (int i=0; i< mang.length(); i++)
                        {
                            JSONObject json = mang.getJSONObject(i);
                            String ma = json.getString("masp");
                            String tensp = json.getString("tensp");
                            String soluong = json.getString("soluong");
                            String gia = json.getString("gia");
                            String mota = json.getString("mota");
                            String anh = json.getString("anh");

                            arr.add(new Hang(ma,tensp,soluong,gia,anh,mota));

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

    }
*/
    public  void getdata()
    {
        String uri=MainActivity.api+"select_hang.php";
        //do data da mang
        RequestQueue requestQueue = Volley.newRequestQueue( BanHang_Activity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray mang =new JSONArray(response);

                    for (int i=0; i< mang.length(); i++)
                    {
                        JSONObject json = mang.getJSONObject(i);
                        String ma = json.getString("masp");
                        String tensp = json.getString("tensp");
                        String soluong = json.getString("soluong");
                        String gia = json.getString("gia");
                        String mota = json.getString("mota");
                        String anh = json.getString("anh");

                        arr.add(new Hang(ma,tensp,soluong,gia,anh,mota));

                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BanHang_Activity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("matk",MainActivity.mtk_user);
                return params;
            }
        };
        requestQueue.add(stringRequest);
        //Toast.makeText(getActivity(), ""+arr.size(), Toast.LENGTH_SHORT).show();

    }
}