package com.example.k6elearninginteraktif;

import android.content.Context;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ListPembelajaranAdaptor extends RecyclerView.Adapter<ListPembelajaranAdaptor.ViewDaftarPembelajaran>{
    FirebaseAuth auth;
    FirebaseFirestore db;
    DocumentSnapshot userInfo;
    FirebaseStorage storage;
    Context context;
    AlertDialog.Builder adBuilder_pembelajaran;
    AlertDialog  ad_pembelajaran;
    // komponen view dari fragment  dialog detail pembelajaran
    TextView tv_namaPengajarAD, tv_judulPembelajaranAD, tv_deskripsiAD;
    Button bt_daftarAD;

    String profilPicsName;

    ArrayList<PembelajarankuModel> listItem;
    public ListPembelajaranAdaptor(Context context, ArrayList<PembelajarankuModel> listItem){
        this.context = context;
        this.listItem = listItem;
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }
    @NonNull
    @Override
    public ListPembelajaranAdaptor.ViewDaftarPembelajaran onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.item_list_pembelajaran, parent,false);
        return new ListPembelajaranAdaptor.ViewDaftarPembelajaran(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPembelajaranAdaptor.ViewDaftarPembelajaran holder, int position) {
        PembelajarankuModel item = listItem.get(position);
//        holder.tv_namaPengajar.setText(item.getId_pengajar());
        holder.tv_judul.setText(item.getJudul());
        holder.tv_kategori.setText(item.getKategori());
        holder.tv_deskripsi.setText(item.getDeskripsi());

        db.collection("Pengguna").document(item.getId_pengajar()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null){
                    userInfo =  task.getResult();
                    String namaFileFoto  = userInfo.get("Nama Pengguna").toString();
                    holder.tv_namaPengajar.setText(namaFileFoto);
                    tv_namaPengajarAD.setText(namaFileFoto);
                }
            }
        });



        tv_judulPembelajaranAD.setText(item.getJudul());
        tv_deskripsiAD.setText(item.getDeskripsi());

        // Menambahkan fungsi daftar
        CollectionReference pembelajaranTerdaftar = db.collection("Pengguna").document(auth.getCurrentUser().getUid()).collection("Pembelajaran Terdaftar");
        bt_daftarAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pembelajaranTerdaftar.document(item.getId_pembelajaran()).set(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Berhasil mendaftar pembelajaran!!", Toast.LENGTH_SHORT).show();
                            ad_pembelajaran.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewDaftarPembelajaran extends RecyclerView.ViewHolder {
        TextView tv_namaPengajar, tv_judul, tv_kategori, tv_deskripsi;
        ImageView iv_profilePics;
        CardView container_itemPembelajaran;
        public ViewDaftarPembelajaran(@NonNull View itemView) {
            super(itemView);

            tv_namaPengajar = itemView.findViewById(R.id.tv_namaPengajar);
            tv_judul = itemView.findViewById(R.id.tv_judul);
            tv_kategori = itemView.findViewById(R.id.tv_kategori);
            tv_deskripsi = itemView.findViewById(R.id.tv_deskripsi);
            iv_profilePics = itemView.findViewById(R.id.iv_fotoProfil);
            container_itemPembelajaran = itemView.findViewById(R.id.cv_pembelajaranku_list);

            createAlertDialogPembelajaran();

            container_itemPembelajaran.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ad_pembelajaran.show();
                }
            });
        }
    }
    private void createAlertDialogPembelajaran(){
        adBuilder_pembelajaran = new AlertDialog.Builder(context);
        View popup = LayoutInflater.from(context).inflate(R.layout.popup_deskripsi_pembelajaran, null);
//            Inisialisasi  komponen dari fragment

        tv_namaPengajarAD = popup.findViewById(R.id.tv_namaPengajar);
        tv_judulPembelajaranAD = popup.findViewById(R.id.tv_judulPembelajaran);
        tv_deskripsiAD = popup.findViewById(R.id.tv_deskripsi);
        bt_daftarAD = popup.findViewById(R.id.bt_daftar);

        adBuilder_pembelajaran.setView(popup);
        adBuilder_pembelajaran.setCancelable(true);
        ad_pembelajaran = adBuilder_pembelajaran.create();
    }
}
