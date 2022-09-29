package com.thuchanh.app_banhang.Fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.thuchanh.app_banhang.Adapter.Adapter_Hang_Home;
import com.thuchanh.app_banhang.Adapter.Hang_Home;
import com.thuchanh.app_banhang.MainActivity;
import com.thuchanh.app_banhang.R;
import com.thuchanh.app_banhang.retrofis.APIUtils;
import com.thuchanh.app_banhang.retrofis.DataClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {

    SearchView search;
    RecyclerView recys;
    Adapter_Hang_Home adapter;
    ArrayList<Hang_Home> arr;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trangchu,container,false);
        arr = new ArrayList<>();

        search = (SearchView) view.findViewById(R.id.searchView);
        recys = (RecyclerView) view.findViewById(R.id.recysview);

        //xoa tex searchview
        search.clearFocus();
        //tim kiem search
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                setdata(s);
                return true;
            }
        });


        //lay data do vao recyclerview
        getdata();
        adapter = new Adapter_Hang_Home(getActivity(), arr);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recys.setLayoutManager(gridLayoutManager);

        recys.setAdapter(adapter);


        return view;
    }



    public void setdata(String tex)
    {
        ArrayList<Hang_Home> mang = new ArrayList<>();
        for (Hang_Home home : arr)
        {
            if(home.getTensp().toLowerCase().contains(tex.toLowerCase()))
            {
                mang.add(home);
            }

        }

        if(mang.isEmpty())
        {
            Toast.makeText(getActivity(), "Không Tìm Thấy "+tex, Toast.LENGTH_SHORT).show();
        }
        else
        {
            adapter.setArr(mang);
        }

    }


    public  void getdata()
    {
        String uri=MainActivity.api+"selelct_home.php";
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
                            String masp = json.getString("msp");
                            String matk = json.getString("mtk");
                            String tensp = json.getString("tsp");
                            String soluong = json.getString("sl");
                            String gia = json.getString("gia");
                            String mota = json.getString("mota");
                            String anh = json.getString("anh");
                            String tk = json.getString("tk");

                            arr.add(new Hang_Home(masp,matk,tensp,soluong,gia,anh,mota,tk));

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
        });
        requestQueue.add(stringRequest);
        //Toast.makeText(getActivity(), ""+arr.size(), Toast.LENGTH_SHORT).show();
        //Log.d("BBB",""+arr.size());
    }


}
