package com.thuchanh.app_banhang;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;
import com.thuchanh.app_banhang.Adapter.GioHang;
import com.thuchanh.app_banhang.Adapter.Hang;
import com.thuchanh.app_banhang.Adapter.Hang_Home;
import com.thuchanh.app_banhang.Fragment.Fragment_BanHang;
import com.thuchanh.app_banhang.Fragment.Fragment_DangNhap;
import com.thuchanh.app_banhang.Fragment.Fragment_GioHang;
import com.thuchanh.app_banhang.Fragment.Fragment_Home;
import com.thuchanh.app_banhang.Fragment.Fragment_LichSuMua;
import com.thuchanh.app_banhang.Fragment.Fragment_XacNhanHang;
import com.thuchanh.app_banhang.retrofis.APIUtils;
import com.thuchanh.app_banhang.retrofis.DataClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView nav;
    ImageView avatar;
    TextView tentk,tengmail;


    public ArrayList<GioHang> arr_giohang =null;
    //notificiton
    TextView textCartItemCount;
    public static int mCartItemCount=0;

    MenuItem menuItem;
    int comment = 0;

    public static String mtk_user="";
    String tk_user="";
    String mk_user="";
    String gmail_user="";
    String sdt_user="";
    String anh_user="";

    //nghi nho mk tk;
    SharedPreferences sharedPreferences;


    public static final int frame_home = 0;
    public static final int frame_giohang = 1;
    public static final int frame_banhang = 2;
    public static final int frame_xacnhanhang = 3;
    public static final int frame_dangnhap = 4;
    public static final int frame_lichsumua=5;
    public int dangxuat = 10;

    private int currentffragment = frame_home;

    public static String api="https://abh1997.000webhostapp.com/";



    Fragment_DangNhap fragment_dangNhap;

    ActivityResultLauncher<Intent> star = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent it = result.getData();
                        Uri uri = it.getData();
                        //lay duong dan file
                        Fragment_BanHang.linkid = getRealPathFromURI(uri);
                        try {
                            InputStream input = getContentResolver().openInputStream(uri);
                            Bitmap bm = BitmapFactory.decodeStream(input);
                            Fragment_BanHang.anhsp.setImageBitmap(bm);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            //lay data tu bang gio hang
            getdata();




        // khoi tao bien va bat sk click navigtion
        nav = (NavigationView) findViewById(R.id.navigtion_view);
        nav.setNavigationItemSelectedListener(this);
        View headerview = nav.getHeaderView(0);
        avatar = (ImageView) headerview.findViewById(R.id.avatar);
        tentk = (TextView) headerview.findViewById(R.id.taikhoan);
        tengmail = (TextView) headerview.findViewById(R.id.gmail);

        luutaikhoan();


        //thay doi thong tin header
        tentk.setText(tk_user);
        tengmail.setText(gmail_user);
        if (anh_user.equals(""))
        {
           avatar.setImageResource(R.drawable.porsion);
        }
        else
        {
            Picasso.get().load(anh_user).into(avatar);
        }


//lay gia tri khi chuyen ve tu dang ky tk
        Intent it = getIntent();
        if(it != null)
        {
            comment = it.getIntExtra("key",0);
        }
        // tao Fragment_Home() khi bat dau chay app
        if(comment == 1)
        {
            frangment(new Fragment_DangNhap());
            currentffragment = frame_dangnhap;
        }
        else if(comment == 2)
        {
            frangment(new Fragment_GioHang());
            currentffragment = frame_giohang;
        }
        else if (comment == 3)
        {
            frangment(new Fragment_LichSuMua());
            currentffragment = frame_lichsumua;
        }
        else
        {
            frangment(new Fragment_Home());
        }

//khai bao menu
        Menu menuNav = nav.getMenu();
        menuItem = menuNav.findItem(R.id.dangnhap);

        if (dangxuat == 10)
        {
            menuItem.setTitle("Đăng Nhập");
        }
        else
        {
            menuItem.setTitle("Đăng Xuất");
        }

//add toolbar va bat su kien mo drawarlayout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawar_layout);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.nav_open,R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.trangchu:
                if (currentffragment != frame_home)
                {
                    frangment(new Fragment_Home());
                    currentffragment = frame_home;
                }
                break;
            case R.id.giohang:
                if (currentffragment != frame_giohang)
                {
                    frangment(new Fragment_GioHang());
                    currentffragment = frame_giohang;
                }
                break;
            case R.id.banhang:
                if (currentffragment != frame_banhang)
                {
                    frangment(new Fragment_BanHang());
                    currentffragment = frame_banhang;
                    //menuItem.setTitle("hahaha");
                }
                break;
            case R.id.xacnhandon:
                if (currentffragment != frame_xacnhanhang)
                {
                    frangment(new Fragment_XacNhanHang());
                    currentffragment = frame_xacnhanhang;
                }
                break;
            case R.id.hangmua:
                if(currentffragment != frame_lichsumua)
                {
                    frangment(new Fragment_LichSuMua());
                    currentffragment = frame_lichsumua;
                }
                break;
            case R.id.dangnhap:
                if (currentffragment != frame_dangnhap && dangxuat == 10)
                {
                    frangment(new Fragment_DangNhap());
                    currentffragment = frame_dangnhap;
                }
                else
                {
                    //xoa thong tin nguoi dung
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.remove("mtk");
                    edit.remove("tk");
                    edit.remove("mk");
                    edit.remove("gmail");
                    edit.remove("sdt");
                    edit.remove("anh");
                    edit.remove("dangxuat");
                    edit.commit();

                    //thay doi thong tin header
                    tentk.setText("");
                    tengmail.setText("");
                     avatar.setImageResource(R.drawable.porsion);

                     luutaikhoan();

                     item.setTitle("Đăng Nhập");
                     dangxuat=10;

                    frangment(new Fragment_DangNhap());
                    currentffragment = frame_dangnhap;

                    arr_giohang.clear();
                    mCartItemCount=arr_giohang.size();
                    setupBadge();
                }

                break;
        }
 //dong drawerlyout khi click sk
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

 //chuyen qua lai giua cac fragmnet
    public void frangment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction =getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_content,fragment);
        fragmentTransaction.commit();
    }

    //chuyen ve fragmenthome
    public void truyendata(String mtk, String tk, String mk, String gmail, String sdt, String anh) {
       getdata();

        //gan gia tri
        mtk_user=mtk;
        tk_user=tk;
        mk_user = mk;
        gmail_user = gmail;
        sdt_user = sdt;
        anh_user = anh;
        Log.d("AAA",mtk_user+"--"+mk_user);


       menuItem.setTitle("Đăng Xuất");
       dangxuat=11;

        tentk.setText(tk_user);
        tengmail.setText(gmail_user);
        Picasso.get().load(anh_user).into(avatar);


       //luu nho thong tin tai khoan nguoi dung
       SharedPreferences.Editor edit = sharedPreferences.edit();
       edit.putString("mtk",mtk_user);
        edit.putString("tk",tk_user);
        edit.putString("mk",mk_user);
        edit.putString("gmail",gmail_user);
        edit.putString("sdt",sdt_user);
        edit.putString("anh",anh_user);
        edit.putInt("dangxuat",11);
        edit.commit();

    }

    public void nexthome()
    {
        FragmentTransaction fragmentTransaction =getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_content,new Fragment_Home());
        fragmentTransaction.commit();
        currentffragment = frame_home;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }

    }


    public void getImg()
    {
        //them quyen luu reu anh tu ben ngoai
        ActivityCompat.requestPermissions( MainActivity.this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                }, 1
        );


        Intent it = new Intent(Intent.ACTION_PICK);
        it.setType("image/*");
        star.launch(it);

    }

    //lay duong dan anh tu tgu muc
    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    public void nextdn()
    {
        FragmentTransaction fragmentTransaction =getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_content,new Fragment_DangNhap());
        fragmentTransaction.commit();
        currentffragment = frame_dangnhap;
    }

    //gan lai thong tin tai khaon
    public void luutaikhoan()
    {
        //nghi nhi tai khoan mat khau
        sharedPreferences = getSharedPreferences("dangnhap",MODE_PRIVATE);
        mtk_user = sharedPreferences.getString("mtk","");
        tk_user = sharedPreferences.getString("tk","");
        mk_user = sharedPreferences.getString("mk","");
        gmail_user = sharedPreferences.getString("gmail","");
        sdt_user = sharedPreferences.getString("sdt","");
        anh_user = sharedPreferences.getString("anh","");
        dangxuat = sharedPreferences.getInt("dangxuat", 10);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_thongbao, menu);

        final MenuItem menuItem = menu.findItem(R.id.giohang);

        View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    //hien thi soluong thong báo
    private void setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    //sk click menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.giohang:
                //chuyen toi gio hang
                if (currentffragment != frame_giohang)
                {
                    frangment(new Fragment_GioHang());
                    currentffragment = frame_giohang;
                }
                break;
            case R.id.thongbao:
                Toast.makeText(MainActivity.this, "Thông Báo", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public  void getdata()
    {
        arr_giohang = new ArrayList<>();
        String uri=api+"select_giohang.php";
        //do data da mang
        RequestQueue requestQueue = Volley.newRequestQueue( MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray mang =new JSONArray(response);

                    for (int i=0; i< mang.length(); i++)
                    {
                        JSONObject json = mang.getJSONObject(i);
                        String mgh = json.getString("mgh");
                        String msp = json.getString("msp");
                        String tsp = json.getString("tsp");
                        String sl = json.getString("sl");
                        String gia = json.getString("gia");
                        String anh = json.getString("anh");

                        arr_giohang.add(new GioHang(mgh,msp,tsp,sl,gia,anh));

                    }
                    mCartItemCount = arr_giohang.size();
                    setupBadge();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("matk",mtk_user);
                return params;
            }
        };
        requestQueue.add(stringRequest);

        //Toast.makeText(getActivity(), ""+arr.size(), Toast.LENGTH_SHORT).show();
        //Log.d("BBB",""+arr.size());
    }

    public void load_thongbao(int s)
    {
        mCartItemCount=s;
        setupBadge();
    }

}