package com.thuchanh.app_banhang.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class Adapter_LSMH extends RecyclerView.Adapter<Adapter_LSMH.LSMH> {

    ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    Activity contax;
    ArrayList<LichSuMuaHang> arr;

    public Adapter_LSMH(Activity contax, ArrayList<LichSuMuaHang> arr) {
        this.contax = contax;
        this.arr = arr;
    }

    @NonNull
    @Override
    public LSMH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_lsmh,parent,false);
        return new LSMH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LSMH holder, int position) {
        LichSuMuaHang ls = arr.get(position);
        if (ls == null)
        {
            return;
        }
        viewBinderHelper.bind(holder.swipeRevealLayout, ls.getMasp());
        viewBinderHelper.setOpenOnlyOne(true);


        holder.ten.setText("Tên SP: "+ls.getTensp());
        holder.sl.setText("SL: "+ls.getSl());
        holder.gia.setText("Gía: "+ls.getGia());
        holder.msp.setText("Time: "+ls.getTime());
        Picasso.get().load(ls.getAnh()).error(R.drawable.vn).into(holder.img);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arr.clear();
                String uri= MainActivity.api+"delete_lsmh.php";
                //do data da mang
                RequestQueue requestQueue = Volley.newRequestQueue(contax);
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
                                String time = json.getString("time");

                                arr.add(new LichSuMuaHang(mgh,msp,tsp,sl,gia,anh,time));

                            }
                            Toast.makeText(contax, "Đã Xóa", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(contax, error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("magh",ls.getMagh());
                        params.put("matk",MainActivity.mtk_user);
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(arr != null)
        {
            return  arr.size();
        }
        else
        {
            return 0;
        }

    }

    class LSMH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView ten,sl,gia,msp;
        Button delete;
        SwipeRevealLayout swipeRevealLayout;
        public LSMH(@NonNull View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.SwipeRevealLayout);
            delete=itemView.findViewById(R.id.delete);
            img = itemView.findViewById(R.id.anh);
            ten = itemView.findViewById(R.id.tensp);
            sl = itemView.findViewById(R.id.soluong);
            gia = itemView.findViewById(R.id.gia);
            msp = itemView.findViewById(R.id.masp);
        }
    }
}
