package com.thuchanh.app_banhang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.squareup.picasso.Picasso;
import com.thuchanh.app_banhang.Adapter.Hang;
import com.thuchanh.app_banhang.Adapter.Hang_Home;
import com.thuchanh.app_banhang.retrofis.APIUtils;
import com.thuchanh.app_banhang.retrofis.DataClient;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GioHang_Activity extends AppCompatActivity {

    Toolbar toolbar;
    Hang_Home hang=new Hang_Home();
    ConstraintLayout cs;
    TextView tensp,gia,soluong,masp,mota,themsl,tenshop;
    ImageView avtar,add,remove;
    CardView cardView;
    Button themgh;
    Boolean bl=true;
    int soluongsp;
    int sl=1;
    TextView textCartItemCount;


    public void Anhxa()
    {
        cs = (ConstraintLayout)findViewById(R.id.constraintlayout) ;
        cardView = (CardView)findViewById(R.id.cardView);
        tenshop = (TextView)findViewById(R.id.texten_gh);
        tensp = (TextView) findViewById(R.id.texsp_gh);
        gia = (TextView) findViewById(R.id.texgia_gh);
        soluong = (TextView) findViewById(R.id.texsl_gh);
        masp = (TextView) findViewById(R.id.texmsp_gh);
        mota = (TextView) findViewById(R.id.texmt_gh);
        themsl = (TextView) findViewById(R.id.texslthem_gh);
        avtar = (ImageView) findViewById(R.id.anh_gh);
        add = (ImageView) findViewById(R.id.add_gh);
        remove = (ImageView) findViewById(R.id.remove_gh);
        themgh = (Button) findViewById(R.id.them_gh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        Anhxa();
        toolbar = (Toolbar) findViewById(R.id.toolbar321);

        //tao nut back tren toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //lay data

        hang = (Hang_Home) getIntent().getSerializableExtra("KEY_NAME");
        //do data vao view
        tensp.setText("Tên SP: "+hang.getTensp());
        gia.setText("Giá: "+hang.getGia());
        soluong.setText("Số Lượng: "+hang.getSoluong());
        masp.setText("MSP: "+hang.getMasp());
        tenshop.setText("Người Bán: "+hang.getTk());
        mota.setText(hang.getMota());
        Picasso.get().load(hang.getAnhsp()).into(avtar);
        themsl.setText(String.valueOf(sl));


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sl < Integer.parseInt(hang.getSoluong()))
                {
                    sl = sl+1;
                    themsl.setText(String.valueOf(sl));
                }
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sl > 1)
                {
                    sl = sl-1;
                    themsl.setText(String.valueOf(sl));
                }
            }
        });

        themgh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bl)
                {
                    bl=false;
                    if (!MainActivity.mtk_user.equals(hang.getMatk().trim()))
                    {

                        if(Integer.parseInt(hang.getSoluong()) > 0 )
                        {
                            //tao sk anim
                            Animation animation = AnimationUtils.loadAnimation(GioHang_Activity.this,R.anim.anim_giohang);
                            cardView.startAnimation(animation);


                            //them vao gio hang
                            String magh = "GH"+System.currentTimeMillis();
                            DataClient dataClient = APIUtils.getdata();
                            Call<String> insert_giohang = dataClient.InsertGioHang(magh,MainActivity.mtk_user,hang.getMasp(), hang.getTensp(),themsl.getText().toString().trim(), hang.getGia(),hang.getAnhsp());
                            insert_giohang.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String suz = response.body();
                                    if(suz.equals("success"))
                                    {
                                        //update lai ds san pham
                                        getdata();
                                        // doi 2s chay code
                                        Handler hl =new Handler();
                                        hl.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                back();
                                            }
                                        },1000);
                                    }


                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(GioHang_Activity.this, "Sản Này Đã Bán Hết Vui Lòng Chon Sản Phẩm Khác", Toast.LENGTH_SHORT).show();
                            back();
                        }

                    }
                    else
                    {
                        Toast.makeText(GioHang_Activity.this, "Bạn Không Thể Mua Sản Phẩm Của Mình", Toast.LENGTH_SHORT).show();
                        back();
                    }
                }

            }
        });

       cs.setOnTouchListener(new OnSwipeTouchListener(GioHang_Activity.this) {
            public void onSwipeTop() {
                //Toast.makeText(DangKy_Activity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                //Toast.makeText(DangKy_Activity.this, "right", Toast.LENGTH_SHORT).show();
               back();
            }
            public void onSwipeLeft() {
                //Toast.makeText(DangKy_Activity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                //Toast.makeText(DangKy_Activity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });
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
    //tao thong bao icon giohang
    private void setupBadge() {
        if (textCartItemCount != null) {
            if (MainActivity.mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(MainActivity.mCartItemCount, 99)));
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
                Intent it = new Intent(GioHang_Activity.this,MainActivity.class);
                it.putExtra("key",2);
                startActivity(it);
                break;
            case R.id.thongbao:
                Toast.makeText(GioHang_Activity.this, "Thông Báo", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                back();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void back()
    {
        Intent intent =new Intent(GioHang_Activity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    //update lai ds san pham
    public  void getdata()
    {
        soluongsp = Integer.parseInt(hang.getSoluong())-Integer.parseInt(themsl.getText().toString().trim());
        String uri=MainActivity.api+"updateSP.php";
        //do data da mang
        RequestQueue requestQueue = Volley.newRequestQueue( GioHang_Activity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("success"))
                {
                    Toast.makeText(GioHang_Activity.this, "Đã Thêm Vào Gio Hàng", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(GioHang_Activity.this, "Khong Them Được", Toast.LENGTH_SHORT).show();
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GioHang_Activity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("masp",hang.getMasp());
                params.put("tensp",hang.getTensp());
                params.put("sl",String.valueOf(soluongsp));
                params.put("gia",hang.getGia());
                params.put("mota",hang.getMota());
                return params;
            }
        };
        requestQueue.add(stringRequest);
        //Toast.makeText(getActivity(), ""+arr.size(), Toast.LENGTH_SHORT).show();

    }
}