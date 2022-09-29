package com.thuchanh.app_banhang.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.thuchanh.app_banhang.GioHang_Activity;
import com.thuchanh.app_banhang.MainActivity;
import com.thuchanh.app_banhang.R;

import java.io.Serializable;
import java.util.ArrayList;

public class Adapter_Hang_Home extends RecyclerView.Adapter<Adapter_Hang_Home.Hang_Holder> {

    Activity contax;
    ArrayList<Hang_Home> arr;

    public void setArr(ArrayList<Hang_Home> arr) {
        this.arr = arr;
        notifyDataSetChanged();
    }

    public Adapter_Hang_Home(Activity contax, ArrayList<Hang_Home> arr) {
        this.contax = contax;
        this.arr = arr;
    }


    @NonNull
    @Override
    public Hang_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_home,parent,false);
        return new Hang_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Hang_Holder holder, int position) {
        Hang_Home hang = arr.get(position);
        if(hang == null)
        {
            return;
        }
        holder.ten.setText("Tên: "+hang.getTensp());
        holder.gia.setText("Gía: "+hang.getGia());
        //  Picasso.get().load(hang.getAnhsp()).into(holder.anh);

        Glide.with(contax).load(hang.getAnhsp()).into(holder.anh);

        //tao hieu ung anim
        Animation scale = AnimationUtils.loadAnimation(contax,R.anim.animation_listhome);
        holder.cardView.startAnimation(scale);

        //chuyen activity
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.mtk_user.equals(""))
                {
                    Toast.makeText(contax, "Đăng Nhập Để Mua Sản Phẩm", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //chuyen data sang activity_giohang
                    Intent it = new Intent(contax, GioHang_Activity.class);
                    it.putExtra("KEY_NAME",hang);
                    contax.startActivity(it);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        if(arr != null)
        {
            return arr.size();
        }
        else
        {
            return 0;
        }
    }

    class Hang_Holder extends RecyclerView.ViewHolder
    {
        ImageView anh;
        TextView ten,gia;
        CardView cardView;

        public Hang_Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.anim);
            anh = itemView.findViewById(R.id.anh);
            ten = itemView.findViewById(R.id.ten);
            gia = itemView.findViewById(R.id.gia);
        }
    }
}
