package com.example.k6elearninginteraktif;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class KelolaPembelajaranActivity extends AppCompatActivity {

    FirebaseFirestore db ;
    DocumentReference pembelajaran ;
    RecyclerView rv_listSubjek;
    String id_pembelajaran;
    Button bt_tambahSubjek, bt_editPembelajaran, bt_judulPembelajaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_pembelajaran);

        id_pembelajaran = getIntent().getStringExtra("id_pembelajaran");
        db = FirebaseFirestore.getInstance();
        pembelajaran = db.collection("Pembelajaran").document(id_pembelajaran);

//        createAlertDialogSubjek();
        bt_tambahSubjek = findViewById(R.id.bt_tambah_subjek);
        bt_editPembelajaran = findViewById(R.id.bt_buatPembelajaran);
        bt_judulPembelajaran = findViewById(R.id.bt_judul_pembelajaran);

        pembelajaran.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String judulPembelajaran = task.getResult().get("judul").toString();
                bt_judulPembelajaran.setText(judulPembelajaran);
            }
        });

        bt_tambahSubjek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KelolaPembelajaranActivity.this, KelolaSubjekActivity.class).putExtra("id_pembelajaran", id_pembelajaran).putExtra("aksi", KelolaSubjekActivity.AKSI_BUAT_BARU));
            }
        });

        rv_listSubjek = findViewById(R.id.subjek_container);

        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getApplicationContext());
        rv_listSubjek.setLayoutManager(linearLayout);
        ArrayList<SubjekModel> list_subjek = new ArrayList<SubjekModel>();
        SubjekAdaptor subjekAdaptor = new SubjekAdaptor(this, list_subjek,SubjekAdaptor.METHOD_EDIT, id_pembelajaran);

        pembelajaran.collection("Daftar Subjek").orderBy("urutan subjek", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    Log.e("Database error: ", error.getLocalizedMessage());
                    Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DocumentChange data_subjek: value.getDocumentChanges()){
                    if(data_subjek!= null){
                        QueryDocumentSnapshot document = data_subjek.getDocument();
                        SubjekModel subjek_item = new SubjekModel(document.getId(), document.get("Nama Subjek").toString(),document.get("Ringkasan").toString());
                        list_subjek.add(subjek_item);
                    }
                }
                subjekAdaptor.notifyDataSetChanged();
            }
        });

        rv_listSubjek.setAdapter(subjekAdaptor);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),PembelajarankuActivity.class));
    }
}


//    private void createAlertDialogSubjek(){
//        adBuilder_subjek = new AlertDialog.Builder(this);
//        View popup = getLayoutInflater().inflate(R.layout.fragment_buat_subjek, null);
////            Inisialisasi  komponen dari fragment
//        et_namaSubjek = popup.findViewById(R.id.et_namaSubjek);
//        et_deskripsiSubjek = popup.findViewById(R.id.et_deskripsi);
//        bt_buatSubjek = popup.findViewById(R.id.bt_buat);
//        bt_batalBuatSubjek = popup.findViewById(R.id.bt_batal);
//
//        adBuilder_subjek.setView(popup);
//        adBuilder_subjek.setCancelable(true);
//        ad_buatSubjek = adBuilder_subjek.create();

        //         Button BUAT pembelajaran
//        bt_buatSubjek.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                String judul = et_namaSubjek.getText().toString();
//                String deskripsi = et_deskripsiSubjek.getText().toString();
//                if (!(TextUtils.isEmpty(judul) || TextUtils.isEmpty(deskripsi))){
//                    Map<String,Object> data = new HashMap<String, Object>();
//                    data.put("judul", judul);
//                    data.put("deskripsi", deskripsi);


//                    pembelajaran.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                             @Override
//                             public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                 if(value.isSuccessful()){
//                                    Toast.makeText(KelolaSubjekActivity.this, "Pembelajaran berhasil dibuat!", Toast.LENGTH_SHORT).show();
//                                    ad_buatSubjek.dismiss();
//                                }else{
//                                    Toast.makeText(KelolaSubjekActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
////
//                    });

//                            new OnCompleteListener<DocumentReference>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                            if(task.isSuccessful()){
//                                Toast.makeText(KelolaSubjekActivity.this, "Pembelajaran berhasil dibuat!", Toast.LENGTH_SHORT).show();
//                                ad_buatSubjek.dismiss();
//                            }else{
//                                Toast.makeText(KelolaSubjekActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

//                } else{
//
//                    Toast.makeText(KelolaSubjekActivity.this, "Silahkan isi semua field!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//         Button BATAL buat pembelajaran
//        bt_batalBuatSubjek.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ad_buatSubjek.dismiss();
//            }
//        });
//    }
//}