package com.thuchanh.app_banhang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MuaHang_Activity extends AppCompatActivity {

    Toolbar toolbar;
    Button home,ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mua_hang);
        toolbar = (Toolbar) findViewById(R.id.toolbar321);
        home = (Button)findViewById(R.id.btn_home);
        ls = (Button)findViewById(R.id.btn_histori);
        setSupportActionBar(toolbar);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it =new Intent(MuaHang_Activity.this,MainActivity.class);
                startActivity(it);
            }
        });

        ls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MuaHang_Activity.this,MainActivity.class);
                it.putExtra("key",3);
                startActivity(it);
            }
        });

    }
}