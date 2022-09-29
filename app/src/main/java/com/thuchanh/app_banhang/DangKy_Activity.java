package com.thuchanh.app_banhang;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.thuchanh.app_banhang.retrofis.APIUtils;
import com.thuchanh.app_banhang.retrofis.DataClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangKy_Activity extends AppCompatActivity {

    EditText tk,mk,gmail,sdt;
    ImageView avatar;
    Button dk,huy;
    String linkid="";
    Boolean chot=false;
    ConstraintLayout cs;
    Toolbar toolbar;
    ActivityResultLauncher<Intent> star = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent it = result.getData();
                        Uri uri = it.getData();
                        //lay duong dan file
                        linkid = getRealPathFromURI(uri);
                        chot=true;
                        try {
                            InputStream input = getContentResolver().openInputStream(uri);
                            Bitmap bm = BitmapFactory.decodeStream(input);
                            avatar.setImageBitmap(bm);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

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



    public void Anhxa()
    {
        cs = (ConstraintLayout)findViewById(R.id.constraintlayout);
        tk = (EditText) findViewById(R.id.tensp_up);
        mk = (EditText) findViewById(R.id.sl_up);
        gmail = (EditText) findViewById(R.id.gia_up);
        sdt = (EditText) findViewById(R.id.mota_up);
        avatar = (ImageView) findViewById(R.id.anh_bh);
        dk = (Button) findViewById(R.id.btn_update);
        huy = (Button) findViewById(R.id.btnhuy_up);
        toolbar = (Toolbar) findViewById(R.id.toolbar321);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        Anhxa();

        //tao nut ban thoat tren toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itent();
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //them quyen luu reu anh tu ben ngoai
                ActivityCompat.requestPermissions( DangKy_Activity.this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.MANAGE_EXTERNAL_STORAGE
                        }, 1
                );


                Intent it = new Intent(Intent.ACTION_PICK);
                it.setType("image/*");
                star.launch(it);
            }
        });


        dk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chot)
                {
                    String taikhoan = tk.getText().toString().trim();
                    String matkhau = mk.getText().toString().trim();
                    String email = gmail.getText().toString().trim();
                    String dt = sdt.getText().toString().trim();
                    if(taikhoan.equals("") || matkhau.equals("") || email.equals("") || dt.equals(""))
                    {
                        Toast.makeText(DangKy_Activity.this, "Thông Tin Chưa Đủ", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        //file anh
                        File file = new File(linkid);
                        //duong dan
                        String link = file.getAbsolutePath();
                        //cat file
                        String[] arr_tenfile = link.split("\\.");
                        //gan lai ten file chen them thoi gian System.currentTimeMillis()
                        link = arr_tenfile[0] + System.currentTimeMillis() + "." + arr_tenfile[1];

                        //xac dinh kieu du lieu cua file
                        RequestBody request = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        //gui file len server gom co key,duong dan,kieu du lieu cua file
                        MultipartBody.Part body = MultipartBody.Part.createFormData("anh", link, request);

                        DataClient dataclient = APIUtils.getdata();
                        Call<String> callback = dataclient.uploadphoto(body);
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                                if (response != null) {
                                    //lay gia tri gan vao mes
                                    String mes = response.body();
                                    if (mes.length() > 0) {
                                        String matk = "TK"+System.currentTimeMillis();
                                        DataClient insert = APIUtils.getdata();
                                        Call<String> back =  insert.InsertData(matk,taikhoan,matkhau,email,dt,APIUtils.url+"image/"+mes);

                                        back.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                //lay gia tri gan vao data
                                                String suz = response.body();
                                                if(suz.equals("success"))
                                                {
                                                    Toast.makeText(DangKy_Activity.this, "Dang Ky Thanh Cong", Toast.LENGTH_SHORT).show();
                                                    itent();
                                                }
                                                else
                                                {
                                                    Toast.makeText(DangKy_Activity.this, suz, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {
                                                Toast.makeText(DangKy_Activity.this, "loi "+t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(DangKy_Activity.this, "Loi"+t.getMessage(), Toast.LENGTH_SHORT).show();


                            }
                        });



                    }
                }
                else
                {
                    Toast.makeText(DangKy_Activity.this, "Chưa Có Anh", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cs.setOnTouchListener(new OnSwipeTouchListener(DangKy_Activity.this) {
            public void onSwipeTop() {
                //Toast.makeText(DangKy_Activity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                //Toast.makeText(DangKy_Activity.this, "right", Toast.LENGTH_SHORT).show();
                itent();
            }
            public void onSwipeLeft() {
                //Toast.makeText(DangKy_Activity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                //Toast.makeText(DangKy_Activity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });
    }
    public void itent()
    {
        Intent it = new Intent(DangKy_Activity.this,MainActivity.class);
        it.putExtra("key",1);
        startActivity(it);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            itent();
        }
        return super.onOptionsItemSelected(item);
    }

}