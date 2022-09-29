package com.thuchanh.app_banhang.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.thuchanh.app_banhang.Adapter.Adapter_LSMH;
import com.thuchanh.app_banhang.Adapter.GioHang;
import com.thuchanh.app_banhang.Adapter.LichSuMuaHang;
import com.thuchanh.app_banhang.MainActivity;
import com.thuchanh.app_banhang.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment_LichSuMua extends Fragment {
    ArrayList<LichSuMuaHang> arr;
    RecyclerView recyc;
    Adapter_LSMH adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lichsumua,container,false);
        recyc = (RecyclerView) view.findViewById(R.id.recyc);
        getdata();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyc.setLayoutManager(linearLayoutManager);

        adapter = new Adapter_LSMH(getActivity(),arr);
        recyc.setAdapter(adapter);
        RecyclerView.ItemDecoration item = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        recyc.addItemDecoration(item);
        return view;
    }

    public  void getdata()
    {
        arr = new ArrayList<>();
        String uri=MainActivity.api+"select_lsmh.php";
        //do data da mang
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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

    }
}
