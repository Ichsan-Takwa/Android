package com.example.k6elearninginteraktif;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PembelajarankuActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth auth;
    CollectionReference pembelajaran_ref;
    Button bt_buat_pembelajaran;
    // komponen view dari fragment  dialog detail pembelajaran
    EditText et_judul, et_kategory, et_deskripsi;
    AlertDialog.Builder adBuilder_pembelajaran;
    AlertDialog  ad_pembelajaran;
    Button bt_buat, bt_batal;

    RecyclerView rv_list_pembelajaranku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembelajaranku);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        pembelajaran_ref = db.collection("Pembelajaran");
        rv_list_pembelajaranku = findViewById(R.id.pembelajaranku_container);
        bt_buat_pembelajaran = findViewById(R.id.bt_buatPembelajaran);
        createAlertDialogPembelajaran();

        bt_buat_pembelajaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    ad_pembelajaran.dismiss();
                    ad_pembelajaran.show();
            }
        });

//        Recyclerview daftar pembelajaranku
        RecyclerView.LayoutManager linear_layout = new LinearLayoutManager(getApplicationContext());
        rv_list_pembelajaranku.setLayoutManager(linear_layout);
        ArrayList<PembelajarankuModel> list_pembelajaranku= new ArrayList<PembelajarankuModel>();
        PembelajarankuAdaptor adaptor_pembelajaranku = new PembelajarankuAdaptor(PembelajarankuActivity.this, list_pembelajaranku);
//

//        pembelajaran.notify();
        pembelajaran_ref.whereEqualTo("id_pengajar",auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    Log.e("Database error: ", error.getLocalizedMessage());
                    Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DocumentChange data_pembelajaran: value.getDocumentChanges()){
                    if(data_pembelajaran!= null){
                        QueryDocumentSnapshot document = data_pembelajaran.getDocument();
                        PembelajarankuModel pembelajaranku = new PembelajarankuModel(
                                document.get("id_pengajar").toString(),
                                document.getId(),
                                document.get("judul").toString(),
                                document.get("kategori").toString(),
                                document.get("deskripsi").toString());

                        list_pembelajaranku.add(pembelajaranku);
                    }
//
                }
                adaptor_pembelajaranku.notifyDataSetChanged();
            }
        });
        rv_list_pembelajaranku.setAdapter(adaptor_pembelajaranku);



    }

    private void createAlertDialogPembelajaran(){
        adBuilder_pembelajaran = new AlertDialog.Builder(this);
         View popup = getLayoutInflater().inflate(R.layout.popup_buat_pembelajaran, null);
//            Inisialisasi  komponen dari fragment
            et_judul = popup.findViewById(R.id.et_judul);
            et_kategory = popup.findViewById(R.id.et_kategori);
            et_deskripsi = popup.findViewById(R.id.et_deskripsi);
            bt_buat = popup.findViewById(R.id.bt_buat);
            bt_batal = popup.findViewById(R.id.bt_batal);

         adBuilder_pembelajaran.setView(popup);
        adBuilder_pembelajaran.setCancelable(true);
        ad_pembelajaran = adBuilder_pembelajaran.create();

        //         Button BUAT pembelajaran
        bt_buat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String judul = et_judul.getText().toString();
                String kategori = et_kategory.getText().toString();
                String deskripsi = et_deskripsi.getText().toString();
                if (!(TextUtils.isEmpty(judul) || TextUtils.isEmpty(kategori)||TextUtils.isEmpty(deskripsi))){
                    Map<String,Object> data = new HashMap<String, Object>();
                    data.put("id_pengajar", auth.getCurrentUser().getUid());
                    data.put("judul", judul);
                    data.put("kategori", kategori);
                    data.put("deskripsi", deskripsi);

                    pembelajaran_ref.add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Pembelajaran berhasil dibuat!", Toast.LENGTH_SHORT).show();
                                ad_pembelajaran.dismiss();
                            }else{
                                Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else{

                    Toast.makeText(getApplicationContext(), "Silahkan isi semua field!", Toast.LENGTH_SHORT).show();
                }
            }
        });
//         Button BATAL buat pembelajaran
        bt_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad_pembelajaran.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }
}