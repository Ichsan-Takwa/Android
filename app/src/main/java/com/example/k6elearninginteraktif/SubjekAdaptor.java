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

public class SubjekAdaptor extends RecyclerView.Adapter<SubjekAdaptor.ViewItemSubjek> {
    Context context;
    ArrayList<SubjekModel> list_data;
    int method;
    String id_pembelajaran;

    public final static int METHOD_EDIT = 0;
    public final static int METHOD_ACCESS = 1;

    public SubjekAdaptor(Context context, ArrayList<SubjekModel> list_data, int method, String id_pembelajaran){
        this.context = context;
        this.list_data = list_data;
        this.method = method;
        this.id_pembelajaran = id_pembelajaran;
    }

    @NonNull
    @Override
    public SubjekAdaptor.ViewItemSubjek onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_daftar_subjek,parent , false);
        return  new SubjekAdaptor.ViewItemSubjek(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjekAdaptor.ViewItemSubjek holder, int position) {
        SubjekModel subjek = list_data.get(position);
        holder.tv_namaSubjek.setText(subjek.getNamaSubjek());
        holder.tv_deskSubjek.setText(subjek.getDeskripsi());

        holder.containerItemSubjek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (method == 0){
                    context.startActivity(new Intent(context,KelolaSubjekActivity.class).putExtra("aksi", KelolaSubjekActivity.AKSI_EDIT).putExtra("id_pembelajaran",id_pembelajaran).putExtra("id_subjek", subjek.getId()));
                } else if(method == 1){
                    context.startActivity(new Intent(context,BelajarActivity.class).putExtra("id_pembelajaran", id_pembelajaran).putExtra("id_subjek",subjek.getId()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    class ViewItemSubjek extends  RecyclerView.ViewHolder{
        TextView tv_namaSubjek, tv_deskSubjek;
        CardView containerItemSubjek;
        public ViewItemSubjek(@NonNull View itemView) {
            super(itemView);

            tv_namaSubjek = itemView.findViewById(R.id.tv_namaSubjek);
            tv_deskSubjek = itemView.findViewById(R.id.tv_deskripsi);
            containerItemSubjek = itemView.findViewById(R.id.container_itemSubjek);


        }
    }
}
