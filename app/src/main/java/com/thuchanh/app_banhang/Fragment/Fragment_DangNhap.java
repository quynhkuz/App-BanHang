package com.thuchanh.app_banhang.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thuchanh.app_banhang.DangKy_Activity;
import com.thuchanh.app_banhang.MainActivity;
import com.thuchanh.app_banhang.OnSwipeTouchListener;
import com.thuchanh.app_banhang.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_DangNhap extends Fragment {

    public EditText tk,mk;
    public TextView dktk;
    public Button dn,huy;

    ConstraintLayout cs;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dangnhap,container,false);
        tk= (EditText) view.findViewById(R.id.tensp_up);
        mk= (EditText) view.findViewById(R.id.sl_up);
        dktk = (TextView) view.findViewById(R.id.texdk_dn);
        dn = (Button) view.findViewById(R.id.btn_update);
        huy = (Button) view.findViewById(R.id.btnhuy_up);
        cs = (ConstraintLayout)view.findViewById(R.id.constraintlayout);

        //sk ban dang ky tk
        dktk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), DangKy_Activity.class);
                startActivity(it);
            }
        });

        dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri=MainActivity.api+"dangnhap.php";
                String taikhoan = tk.getText().toString().trim();
                String matkhau = mk.getText().toString().trim();

                if(taikhoan.equals("") || matkhau.equals(""))
                {
                    Toast.makeText(getActivity(), "Nhập Tài Khoản Hoạc Mật Khẩu", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if ((response.trim().equals("failure")))
                            {
                                Toast.makeText(getActivity(), "Tài Khoản Mật Khẩu Không Đúng", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                try {
                                    JSONArray arr =new JSONArray(response);

                                        JSONObject json = arr.getJSONObject(0);
                                        String mtk = json.getString("matk");
                                        String user = json.getString("tk");
                                        String pass = json.getString("mk");
                                        String gmail = json.getString("gmail");
                                        String sdt = json.getString("sdt");
                                        String anh = json.getString("anh");

                                    ((MainActivity) getActivity()).truyendata(mtk,user,pass,gmail,sdt,anh);
                                    ((MainActivity) getActivity()).nexthome();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getActivity(), "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String,String> params = new HashMap<>();
                            params.put("taikhoan",taikhoan);
                            params.put("matkhau",matkhau);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }

            }
        });

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).nexthome();
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
                Intent it = new Intent(getActivity(), DangKy_Activity.class);
                startActivity(it);
            }
            public void onSwipeBottom() {
                //Toast.makeText(DangKy_Activity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });


        return view;
    }


    //((MainActivity) getActivity()).nexthome();

}
