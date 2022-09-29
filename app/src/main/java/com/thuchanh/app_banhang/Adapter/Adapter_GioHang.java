package com.thuchanh.app_banhang.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.squareup.picasso.Picasso;
import com.thuchanh.app_banhang.MainActivity;
import com.thuchanh.app_banhang.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adapter_GioHang extends RecyclerView.Adapter<Adapter_GioHang.GioHang_Holder> {

    ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    MainActivity mainActivity;
    ArrayList<GioHang> arr;
    ArrayList<GioHang> arrnew;
    TextView tongtien;
    int s = 0;

    public Adapter_GioHang(MainActivity mainActivity, ArrayList<GioHang> arr, ArrayList<GioHang> arrnew, TextView tongtien) {
        this.mainActivity = mainActivity;
        this.arr = arr;
        this.arrnew = arrnew;
        this.tongtien = tongtien;
    }

    @NonNull
    @Override
    public GioHang_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_giohang,parent,false);
        return new GioHang_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHang_Holder holder, int position) {
        GioHang gh = arr.get(position);
        if (gh == null)
        {
            return;
        }

        viewBinderHelper.bind(holder.swipeRevealLayout, gh.getMasp());
        viewBinderHelper.setOpenOnlyOne(true);

        holder.ck.setChecked(false);
        holder.ten.setText("Tên SP: "+gh.getTensp());
        holder.sl.setText("SL: "+gh.getSl());
        holder.gia.setText("Gía: "+gh.getGia());
        holder.msp.setText("MSP: "+gh.getMasp());
        Picasso.get().load(gh.getAnh()).error(R.drawable.vn).into(holder.img);



        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrnew.clear();
                s=0;
                tongtien.setText("0");


                arr.clear();
                String uri=MainActivity.api+"delete_giohang.php";
                //do data da mang
                RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);
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
                            notifyDataSetChanged();

                            int s = arr.size();
                            mainActivity.load_thongbao(s);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mainActivity, error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("matk",MainActivity.mtk_user);
                        params.put("magh",gh.getMagh());
                        params.put("msp",gh.getMasp());
                        params.put("sl",gh.getSl());
                        return params;
                    }
                };
                requestQueue.add(stringRequest);

                Toast.makeText(mainActivity, "Đã Xóa", Toast.LENGTH_SHORT).show();
            }
        });

        //sk checkbok
        holder.ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.ck.isChecked())
                {
                    s=s+( Integer.parseInt(gh.getGia()) * Integer.parseInt(gh.getSl()) );
                    tongtien.setText(String.valueOf(s));
                    arrnew.add(gh);
                }
                else
                {
                    s=s-( Integer.parseInt(gh.getGia()) * Integer.parseInt(gh.getSl()) );
                    tongtien.setText(String.valueOf(s));
                    arrnew.remove(gh);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (arr != null)
        {
            return arr.size();
        }
        else
        {
            return 0;
        }

    }

    class GioHang_Holder extends RecyclerView.ViewHolder {
        CheckBox ck;
        ImageView img;
        TextView ten,sl,gia,msp;
        Button delete;
        SwipeRevealLayout swipeRevealLayout;
        public GioHang_Holder(@NonNull View itemView) {
            super(itemView);

            swipeRevealLayout = itemView.findViewById(R.id.SwipeRevealLayout);
            delete=itemView.findViewById(R.id.delete);
            ck = itemView.findViewById(R.id.check);
            img = itemView.findViewById(R.id.anh);
            ten = itemView.findViewById(R.id.tensp);
            sl = itemView.findViewById(R.id.soluong);
            gia = itemView.findViewById(R.id.gia);
            msp = itemView.findViewById(R.id.masp);

        }
    }
    public  void getdata()
    {


    }
}
