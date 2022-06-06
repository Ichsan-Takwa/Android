package com.example.k6elearninginteraktif;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KelolaSubjekActivity extends AppCompatActivity {
    final static int AKSI_BUAT_BARU = 0;
    final static int AKSI_EDIT = 1;

    Button bt_inputTextTitle,bt_inputText, bt_inputDocs, bt_inputImage, bt_inputVideo, bt_inputAudio;
    Button bt_simpanSubjek;

    FirebaseFirestore db;
    DocumentReference pembelajaran;
    DocumentReference subjek_detail;
    FirebaseStorage storage;
    FirebaseAuth auth;
    int fieldOfNumber;
    String id_pembelajaran, id_subjek;
    int userAction;

    EditText et_namaSubjek, et_ringkasan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_subjek);
        db = FirebaseFirestore.getInstance();

        id_pembelajaran = getIntent().getStringExtra("id_pembelajaran");
        pembelajaran = db.collection("Pembelajaran").document(id_pembelajaran);
        userAction = getIntent().getIntExtra("aksi",0);

        id_subjek = "subjek - "+ UUID.randomUUID().toString();
        if (userAction == AKSI_EDIT){
            id_subjek = getIntent().getStringExtra("id_subjek");
        }
        subjek_detail = pembelajaran.collection("Daftar Subjek").document(id_subjek);

        FragmentManager fm = getSupportFragmentManager();

        fieldOfNumber = 0;
        pembelajaran.collection(id_subjek).orderBy("no urut").get().addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                fieldOfNumber = task.getResult().getDocuments().size();
            }
        });
//        userStorage = storage;

        bt_inputTextTitle  = findViewById(R.id.bt_inputTextJudul);
        bt_inputText = findViewById(R.id.bt_inputText);
        bt_inputImage = findViewById(R.id.bt_inputImage);
        bt_inputVideo = findViewById(R.id.bt_inputVideo);
        bt_inputAudio = findViewById(R.id.bt_inputAudio);
        bt_simpanSubjek = findViewById(R.id.bt_simpan_subjek);

        et_namaSubjek = findViewById(R.id.et_namaSubjek);
        et_ringkasan = findViewById(R.id.et_ringkasanSubjek);


        if(userAction == AKSI_EDIT){
            ProgressDialog loading = new ProgressDialog(KelolaSubjekActivity.this);
            loading.setTitle("Loading..");
            loading.setMessage("sedang mengakses data!");
            loading.show();
            pembelajaran.collection("Daftar Subjek").document(id_subjek).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        et_namaSubjek.setText(task.getResult().get("Nama Subjek").toString());
                        et_ringkasan.setText(task.getResult().get("Ringkasan").toString());
                    }
                }
            });

            pembelajaran.collection(id_subjek).orderBy("no urut").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for(DocumentSnapshot document: task.getResult().getDocuments()){
                            switch (document.get("tipe").toString()){
                                case "Title Text Field":
                                    fm.beginTransaction().add(R.id.ll_subjectItemContainer,new InputTextTitleField(document, id_pembelajaran, id_subjek)).commit();
                                    break;
                                case "Text Field":
                                    fm.beginTransaction().add(R.id.ll_subjectItemContainer,new InputTextField(document, id_pembelajaran, id_subjek)).commit();
                                    break;
                                case "Upload Image Field":
                                    fm.beginTransaction().add(R.id.ll_subjectItemContainer,new InputImageField(document, id_pembelajaran, id_subjek)).commit();
                                    break;
                                case "Update Video Field":
                                    fm.beginTransaction().add(R.id.ll_subjectItemContainer,new InputVideoField(document, id_pembelajaran, id_subjek)).commit();
                                    break;
                                case "Update Audio Field":
                                    fm.beginTransaction().add(R.id.ll_subjectItemContainer,new InputAudioField(document, id_pembelajaran, id_subjek)).commit();
                                    break;
                            }
                        }
                        loading.dismiss();
                    } else {
                        loading.dismiss();
                    }
                }
            });
        }


        bt_inputTextTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().add(R.id.ll_subjectItemContainer, new InputTextTitleField(++fieldOfNumber, id_pembelajaran, id_subjek)).commit();
            }
        });

        bt_inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().add(R.id.ll_subjectItemContainer, new InputTextField(++fieldOfNumber, id_pembelajaran,id_subjek)).commit();
            }
        });

        bt_inputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().add(R.id.ll_subjectItemContainer, new InputImageField(++fieldOfNumber, id_pembelajaran,id_subjek)).commit();
            }
        });

        bt_inputVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().add(R.id.ll_subjectItemContainer, new InputVideoField(++fieldOfNumber, id_pembelajaran,id_subjek)).commit();
            }
        });

        bt_inputAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().add(R.id.ll_subjectItemContainer, new InputAudioField(++fieldOfNumber, id_pembelajaran,id_subjek)).commit();
            }
        });

        bt_simpanSubjek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaSubjek = et_namaSubjek.getText().toString();
                String ringkasan = et_ringkasan.getText().toString();


                // memastikan field text terakhir tersimpan
                et_namaSubjek.requestFocus();


                if(!(TextUtils.isEmpty(namaSubjek) || TextUtils.isEmpty(ringkasan))){
                    Map<String,Object> detail = new HashMap<>();
                    detail.put("Nama Subjek", namaSubjek);
                    detail.put("Ringkasan", ringkasan);

//                    Mengambil no subjek terakhir
                    pembelajaran.collection("Daftar Subjek").orderBy("urutan subjek", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            String lastestSubject;
                            if (task.isSuccessful()){
                                if (!task.getResult().getDocuments().isEmpty()){
                                    lastestSubject =  task.getResult().getDocuments().get(0).get("urutan subjek").toString();
                                    Toast.makeText(KelolaSubjekActivity.this, task.getResult().getDocuments().get(0).get("Nama Subjek").toString(), Toast.LENGTH_LONG).show();
                                } else{
                                    lastestSubject = "0";
                                }
                            } else {
                                lastestSubject = "0";
                            }
                            detail.put("urutan subjek", (Integer.parseInt(lastestSubject) +1));
                            subjek_detail.set(detail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(KelolaSubjekActivity.this, "Subjek berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),KelolaPembelajaranActivity.class).putExtra("id_pembelajaran",id_pembelajaran));
                                    }else{
                                        Toast.makeText(KelolaSubjekActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                } else {
                    Toast.makeText(KelolaSubjekActivity.this, "field nama dan ringkasan wajib diisi", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void sebuahFieldDihapus(){
        fieldOfNumber--;
    }

    @Override
    public void onBackPressed() {
        bt_simpanSubjek.callOnClick();
        startActivity(new Intent(getApplicationContext(), KelolaPembelajaranActivity.class).putExtra("id_pembelajaran", id_pembelajaran));
    }
}
