package com.example.k6elearninginteraktif;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PembelajarankuAdaptor extends RecyclerView.Adapter<PembelajarankuAdaptor.ViewItemPembelajaranku> {
    Context context;
    ArrayList<PembelajarankuModel> list_pembelajaran;
    public PembelajarankuAdaptor(Context context, ArrayList<PembelajarankuModel> list_data){
        this.context = context;
        this.list_pembelajaran = list_data;
    }

    @NonNull
    @Override
    public ViewItemPembelajaranku onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View item_view = layoutInflater.inflate(R.layout.item_pembelajaranku,parent,false);
        return new ViewItemPembelajaranku(item_view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewItemPembelajaranku holder, int position) {

        PembelajarankuModel pembelajaran = list_pembelajaran.get(position);

        holder.tv_judul.setText(pembelajaran.getJudul());
        holder.tv_kategori.setText(pembelajaran.getKategori());
        holder.tv_deskripsi.setText(pembelajaran.getDeskripsi());
        holder.id_pembelajaran = pembelajaran.getId_pembelajaran();

    }

    @Override
    public int getItemCount() {
        return list_pembelajaran.size();
    }
    class ViewItemPembelajaranku extends  RecyclerView.ViewHolder{
        TextView tv_judul, tv_kategori, tv_deskripsi;
        String id_pembelajaran;
        CardView cv_pembelajaranku;
        public ViewItemPembelajaranku(@NonNull View itemView) {
            super(itemView);

            tv_judul = itemView.findViewById(R.id.tv_judul);
            tv_kategori = itemView.findViewById(R.id.tv_kategori);
            tv_deskripsi = itemView.findViewById(R.id.tv_deskripsi);


            cv_pembelajaranku = itemView.findViewById(R.id.cv_pembelajaranku_list);

            cv_pembelajaranku.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(context, KelolaPembelajaranActivity.class);
                    it.putExtra("id_pembelajaran",id_pembelajaran);
                    context.startActivity(it);
                }
            });
        }
    }
}
