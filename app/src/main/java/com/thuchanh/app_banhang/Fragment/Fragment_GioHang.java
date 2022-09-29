package com.thuchanh.app_banhang.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thuchanh.app_banhang.Adapter.Adapter_GioHang;
import com.thuchanh.app_banhang.Adapter.GioHang;
import com.thuchanh.app_banhang.BanHang_Activity;
import com.thuchanh.app_banhang.MainActivity;
import com.thuchanh.app_banhang.MuaHang_Activity;
import com.thuchanh.app_banhang.R;
import com.thuchanh.app_banhang.retrofis.APIUtils;
import com.thuchanh.app_banhang.retrofis.DataClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_GioHang extends Fragment {

    Adapter_GioHang adapter;
    RecyclerView recyclerView;
    ArrayList<GioHang> arr;
    ArrayList<GioHang> arr_new = new ArrayList<>();
    Button muahang;
    TextView tongtien;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_giohang,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        muahang = (Button)view.findViewById(R.id.btn_thnahtoan);
        tongtien = (TextView) view.findViewById(R.id.tex_tongtien);

        getdata();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration recyc = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(recyc);
        adapter =new Adapter_GioHang((MainActivity) getActivity(),arr,arr_new,tongtien);
        recyclerView.setAdapter(adapter);




        //sk mua hang
        muahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(Integer.parseInt(tongtien.getText().toString().trim()) >0)
               {
                   dialog();
               }
               else
               {
                   Toast.makeText(getActivity(), "Bạn Chưa Chọn Sản Phẩm", Toast.LENGTH_SHORT).show();
               }
            }
        });


        return view;
    }

    public void dialog()
    {
        AlertDialog.Builder bl = new AlertDialog.Builder(getActivity());
        bl.setTitle("Thông Báo");
        bl.setMessage("Số Tiền Bạn Phải Thanh Toán Là "+tongtien.getText());
        bl.setCancelable(false);
        bl.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                update();
            }
        });
        bl.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        AlertDialog al = bl.create();
        al.show();
    }


    public  void getdata()
    {
        arr = new ArrayList<>();
        String uri=MainActivity.api+"select_giohang.php";
        //do data da mang
        RequestQueue requestQueue = Volley.newRequestQueue( getActivity());
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

                        arr.add(new GioHang(mgh,msp,tsp,sl,gia,anh));
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

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
        //Log.d("BBB",""+arr.size());
    }


    public void update()
    {
        for (int i=0;i<arr_new.size();i++)
        {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat ngay = new SimpleDateFormat("hh:mm a dd-MM-yyyy");
            String time = ngay.format(calendar.getTime());

            GioHang gioHang = arr_new.get(i);
            DataClient dataClient = APIUtils.getdata();
            Call<String> call = dataClient.update_giohang(gioHang.getMagh(),time);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    String abc = response.body();
                    if (abc.equals("success"))
                    {
                        Toast.makeText(getActivity(),"Mua Hàng Thành Công",Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(getActivity(), MuaHang_Activity.class);
                        startActivity(it);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }

    }
}
