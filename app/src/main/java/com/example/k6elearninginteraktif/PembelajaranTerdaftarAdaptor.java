package com.example.k6elearninginteraktif;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class PembelajaranTerdaftarAdaptor extends RecyclerView.Adapter<PembelajaranTerdaftarAdaptor.ViewPembelajaranTerdaftar> {
    FirebaseAuth auth;
    FirebaseFirestore db;
    DocumentSnapshot userInfo;
    Context context;
    // komponen view dari fragment  dialog detail pembelajaran

    String profilPicsName;

    ArrayList<PembelajarankuModel> listItem;
    public PembelajaranTerdaftarAdaptor(Context context, ArrayList<PembelajarankuModel> listItem){
        this.context = context;
        this.listItem = listItem;
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }
    @NonNull
    @Override
    public ViewPembelajaranTerdaftar onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.item_list_pembelajaran, parent,false);
        return new PembelajaranTerdaftarAdaptor.ViewPembelajaranTerdaftar(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPembelajaranTerdaftar holder, int position) {
        PembelajarankuModel item = listItem.get(position);
//        holder.tv_namaPengajar.setText(item.getId_pengajar());
        holder.tv_judul.setText(item.getJudul());
        holder.tv_kategori.setText(item.getKategori());
        holder.tv_deskripsi.setText(item.getDeskripsi());
        holder.id_pembelajaran = item.getId_pembelajaran();

        db.collection("Pengguna").document(item.getId_pengajar()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && (task.getResult()!=null)){
                    userInfo =  task.getResult();
                    String namaFileFoto  = userInfo.get("Nama Pengguna").toString();
                    holder.tv_namaPengajar.setText(namaFileFoto);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewPembelajaranTerdaftar extends RecyclerView.ViewHolder{
        TextView tv_namaPengajar, tv_judul, tv_kategori, tv_deskripsi;
        ImageView iv_profilePics;
        String id_pembelajaran;
        CardView container_itemPembelajaran;
        public ViewPembelajaranTerdaftar(@NonNull View itemView) {
            super(itemView);

            tv_namaPengajar = itemView.findViewById(R.id.tv_namaPengajar);
            tv_judul = itemView.findViewById(R.id.tv_judul);
            tv_kategori = itemView.findViewById(R.id.tv_kategori);
            tv_deskripsi = itemView.findViewById(R.id.tv_deskripsi);
            iv_profilePics = itemView.findViewById(R.id.iv_fotoProfil);
            container_itemPembelajaran = itemView.findViewById(R.id.cv_pembelajaranku_list);

//            TODO : fungsi mengakses pembelajaran
            container_itemPembelajaran.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ad_pembelajaran.show();
                    Toast.makeText(context, "Pembelajaran diakses", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(context, BelajarSubjekActivity.class);
                    it.putExtra("id_pembelajaran",id_pembelajaran);
                    context.startActivity(it);
                }
            });
        }
    }
}
