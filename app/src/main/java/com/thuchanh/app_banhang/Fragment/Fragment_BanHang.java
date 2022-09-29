package com.thuchanh.app_banhang.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.thuchanh.app_banhang.BanHang_Activity;
import com.thuchanh.app_banhang.Adapter.Hang;
import com.thuchanh.app_banhang.MainActivity;
import com.thuchanh.app_banhang.OnSwipeTouchListener;
import com.thuchanh.app_banhang.R;
import com.thuchanh.app_banhang.retrofis.APIUtils;
import com.thuchanh.app_banhang.retrofis.DataClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_BanHang extends Fragment {

    EditText tensp,gia,sl,mota;
    public static ImageView anhsp;
    public static String linkid="";
    Button themsp,huy,xem;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banhang,container,false);


        tensp = (EditText) view.findViewById(R.id.tensp_up);
        gia = (EditText) view.findViewById(R.id.gia_up);
        sl = (EditText) view.findViewById(R.id.sl_up);
        mota = (EditText) view.findViewById(R.id.mota_up);
        anhsp = (ImageView) view.findViewById(R.id.anh_bh);
        themsp = (Button) view.findViewById(R.id.btn_update);
        huy = (Button) view.findViewById(R.id.btnhuy_up);
        xem = (Button)view.findViewById(R.id.btnxem_bh);


        xem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.mtk_user.equals(""))
                {
                    Toast.makeText(getActivity(), "Chua Dang Nhap Tai Khoan", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent it = new Intent(getActivity(), BanHang_Activity.class);
                    startActivity(it);
                }

            }
        });

        //lay anh tu thu muc
        anhsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).getImg();
            }
        });

        themsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linkid.equals(""))
                {
                    Toast.makeText(getActivity(), "Chua Chon Anh", Toast.LENGTH_SHORT).show();
                }
                else
                {
                   String tsp = tensp.getText().toString().trim();
                   String g = gia.getText().toString().trim();
                   String sol = sl.getText().toString().trim();
                   String mt = mota.getText().toString().trim();
                   if(tsp.equals("") || g.equals("") || sol.equals("") || mt.equals(""))
                   {
                       Toast.makeText(getActivity(), "Thong Tin Chua Day Du", Toast.LENGTH_SHORT).show();
                   }
                   else if(MainActivity.mtk_user.equals(""))
                   {
                       Toast.makeText(getActivity(), "Dang Nhap Tai Khoan De Ban Hang", Toast.LENGTH_SHORT).show();
                       ((MainActivity) getActivity()).nextdn();
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
                                       String masp = "SP"+System.currentTimeMillis();
                                       DataClient insert = APIUtils.getdata();
                                       Call<String> back =  insert.InsertHang(MainActivity.mtk_user,masp,tsp,sol,g,APIUtils.url+"image/"+mes,mt);
                                       back.enqueue(new Callback<String>() {
                                           @Override
                                           public void onResponse(Call<String> call, Response<String> response) {
                                               //lay gia tri gan vao data
                                               String suz = response.body();
                                               if(suz.equals("success"))
                                               {
                                                   Toast.makeText(getActivity(), "Them Thanh Cong", Toast.LENGTH_SHORT).show();
///do data vao mang co san
                                               }
                                               else
                                               {
                                                   Toast.makeText(getActivity(), "Them That Bai", Toast.LENGTH_SHORT).show();
                                               }
                                           }

                                           @Override
                                           public void onFailure(Call<String> call, Throwable t) {
                                               Toast.makeText(getActivity(), "loi "+t.getMessage(), Toast.LENGTH_SHORT).show();
                                           }
                                       });
                                   }

                               }
                           }

                           @Override
                           public void onFailure(Call<String> call, Throwable t) {
                               Toast.makeText(getActivity(), "Loi"+t.getMessage(), Toast.LENGTH_SHORT).show();


                           }
                       });

                   }
                }
            }
        });

        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            public void onSwipeTop() {
                //Toast.makeText(DangKy_Activity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                //Toast.makeText(DangKy_Activity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                //Toast.makeText(DangKy_Activity.this, "left", Toast.LENGTH_SHORT).show();
                if (MainActivity.mtk_user.equals(""))
                {
                    Toast.makeText(getActivity(), "Chua Dang Nhap Tai Khoan", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent it = new Intent(getActivity(), BanHang_Activity.class);
                    startActivity(it);
                }
            }
            public void onSwipeBottom() {
                //Toast.makeText(DangKy_Activity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }






}
