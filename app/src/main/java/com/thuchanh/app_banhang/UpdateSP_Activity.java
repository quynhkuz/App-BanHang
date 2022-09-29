package com.thuchanh.app_banhang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.thuchanh.app_banhang.Fragment.Fragment_BanHang;
import com.thuchanh.app_banhang.retrofis.APIUtils;
import com.thuchanh.app_banhang.retrofis.DataClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateSP_Activity extends AppCompatActivity {

    Toolbar toolbar;
    EditText ten,sl,gia,mota;
    Button update,huy;
    Hang hang = new Hang();
    String tensp,soluong,g,mtct;

    public void Anhxa()
    {
        toolbar = (Toolbar)findViewById(R.id.toolbar321);
        ten = (EditText) findViewById(R.id.tensp_up);
        sl = (EditText) findViewById(R.id.sl_up);
        gia = (EditText) findViewById(R.id.gia_up);
        mota = (EditText) findViewById(R.id.mota_up);
        update = (Button) findViewById(R.id.btn_update);
        huy = (Button) findViewById(R.id.btnhuy_up);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sp);
        Anhxa();
        hang = (Hang) getIntent().getSerializableExtra("KEY_NAME");
        //han lai gia tri choc ac edittex
        ten.setText(hang.getTensp());
        sl.setText(hang.getSoluong());
        gia.setText(hang.getGia());
        mota.setText(hang.getMota());

        //tao nut ban thoat tren tanh toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tensp = ten.getText().toString().trim();
                soluong = sl.getText().toString().trim();
                g = gia.getText().toString().trim();
                mtct = mota.getText().toString().trim();
                if(tensp.equals("") || soluong.equals("") || g.equals("") || mtct.equals(""))
                {
                    Toast.makeText(UpdateSP_Activity.this, "Thong Tin KHong Day Du", Toast.LENGTH_SHORT).show();
                }
                else
                {

                   getdata();
                    intent();
                }
            }
        });
    }

    public void intent()
    {
        Intent it = new Intent(UpdateSP_Activity.this,BanHang_Activity.class);
        startActivity(it);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
           intent();
        }
        return super.onOptionsItemSelected(item);
    }


    public  void getdata()
    {
        String uri=MainActivity.api+"updateSP.php";
        //do data da mang
        RequestQueue requestQueue = Volley.newRequestQueue( UpdateSP_Activity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("success"))
                {
                    Toast.makeText(UpdateSP_Activity.this, "Update Thành Công", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(UpdateSP_Activity.this, "Update Thất Bại", Toast.LENGTH_SHORT).show();
                }


            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateSP_Activity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("masp",hang.getMasp());
                params.put("tensp",ten.getText().toString().toString());
                params.put("sl",sl.getText().toString().trim());
                params.put("gia",gia.getText().toString().trim());
                params.put("mota",mota.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
        //Toast.makeText(getActivity(), ""+arr.size(), Toast.LENGTH_SHORT).show();

    }
}