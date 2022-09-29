package com.thuchanh.app_banhang.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;
import com.thuchanh.app_banhang.MainActivity;
import com.thuchanh.app_banhang.R;
import com.thuchanh.app_banhang.UpdateSP_Activity;
import com.thuchanh.app_banhang.retrofis.APIUtils;
import com.thuchanh.app_banhang.retrofis.DataClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_Hang extends RecyclerView.Adapter<Adapter_Hang.Hang_holder> {

    Activity contax;
    ArrayList<Hang> arr;
    ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public Adapter_Hang(Activity contax, ArrayList<Hang> arr) {
        this.contax = contax;
        this.arr = arr;
    }

    public void setArr(ArrayList<Hang> arr) {
        this.arr = arr;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Hang_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_listbanhang,parent,false);

        return new Hang_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Hang_holder holder, int position) {
        Hang hang = arr.get(position);
        if(hang == null)
        {
            return;
        }
        //sk keo sang pgai xuat hien cac nut
        viewBinderHelper.bind(holder.swipeRevealLayout, hang.getMasp());
        viewBinderHelper.setOpenOnlyOne(true);

        Picasso.get().load(hang.getAnhsp()).into(holder.anh);
        holder.tensp.setText("Tên Sản Phẩm: "+hang.getTensp());
        holder.masp.setText("MSP: "+hang.getMasp());
        holder.sl.setText("Số Lượng: "+hang.getSoluong());
        holder.gia.setText("Gía: "+hang.getGia());

        Animation translate = AnimationUtils.loadAnimation(contax,R.anim.translate_anim);
        holder.swipeRevealLayout.startAnimation(translate);


        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(contax, UpdateSP_Activity.class);
                it.putExtra("KEY_NAME", hang);
                contax.startActivity(it);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String image =hang.getAnhsp().substring(hang.getAnhsp().lastIndexOf("/"));
                AlertDialog.Builder bl = new AlertDialog.Builder(contax);
                bl.setTitle("Thong Bao");
                bl.setMessage("Ban co muon xoa "+ hang.getTensp());
                bl.setCancelable(false);
                bl.setNegativeButton("Khong", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                bl.setPositiveButton("Co", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        arr.clear();
                        DataClient callback = APIUtils.getdata();
                        Call<String> delete = callback.delete(hang.getMasp(),image);
                        delete.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String key =response.body();
                                if (key.equals("success"))
                                {
                                    Toast.makeText(contax, "Delete Thanh Cong", Toast.LENGTH_SHORT).show();

                                    arr.clear();
                                    DataClient callback = APIUtils.getdata();
                                    Call<JsonArray> login = callback.getdata(MainActivity.mtk_user);
                                    login.enqueue(new Callback<JsonArray>() {
                                        @Override
                                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                                            //chuyen ve sstring
                                            String a = response.body().toString();

                                            if (!a.equals(""))
                                            {
                                                try {
                                                    //khoi tao json moi co thu vien org.json
                                                    JSONArray mang =new JSONArray(a);

                                                    for (int i=0; i< mang.length(); i++)
                                                    {
                                                        JSONObject json = mang.getJSONObject(i);
                                                        String ma = json.getString("masp");
                                                        String tensp = json.getString("tensp");
                                                        String soluong = json.getString("soluong");
                                                        String gia = json.getString("gia");
                                                        String mota = json.getString("mota");
                                                        String anh = json.getString("anh");

                                                        arr.add(new Hang(ma,tensp,soluong,gia,anh,mota));

                                                    }
                                                    notifyDataSetChanged();

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<JsonArray> call, Throwable t) {

                                        }
                                    });

                                }
                                else
                                {
                                    Toast.makeText(contax, "Delete That Bai", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }
                });
                AlertDialog al = bl.create();
                al.show();
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

    public class Hang_holder extends RecyclerView.ViewHolder
    {
        private SwipeRevealLayout swipeRevealLayout;
       private Button delete,update;
        private TextView tensp,masp,sl,gia;
        private ImageView anh;


        public Hang_holder(@NonNull View itemView) {
            super(itemView);
            swipeRevealLayout = itemView.findViewById(R.id.SwipeRevealLayout);
            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update);
            tensp = itemView.findViewById(R.id.tensp);
            masp = itemView.findViewById(R.id.masp);
            sl = itemView.findViewById(R.id.soluong);
            gia = itemView.findViewById(R.id.gia);
            anh = itemView.findViewById(R.id.anh);
        }
    }


}
