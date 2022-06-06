package com.example.k6elearninginteraktif;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class BelajarActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseStorage storage;

    ListView ll_containerMateri;
    Button bt_nextMateri;
    String id_pembelajaran, id_subjek;
    FragmentManager fm;

    public int currentSection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_belajar);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        fm = getSupportFragmentManager();
//        ll_containerMateri = findViewById(R.id.ll_container_materi);
        bt_nextMateri = findViewById(R.id.bt_nextMateri);

        id_pembelajaran = getIntent().getStringExtra("id_pembelajaran");
        id_subjek = getIntent().getStringExtra("id_subjek");

        ArrayList<DocumentSnapshot> section_pembelajaran = new ArrayList<>();

        db.collection("Pembelajaran").document(id_pembelajaran).collection(id_subjek).orderBy("no urut", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult().getDocuments()){
                    section_pembelajaran.add(document);
                }
            }
        });

        currentSection = 0;
        bt_nextMateri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSection<section_pembelajaran.size()){
                    DocumentSnapshot data = section_pembelajaran.get(currentSection);
                    switch (data.get("tipe").toString()){
                        case "Title Text Field":
                            fm.beginTransaction().add(R.id.ll_container_materi,new DisplayTextTitleField(data, id_subjek)).commit();
                            break;
                        case "Text Field":
                            fm.beginTransaction().add(R.id.ll_container_materi,new DisplayTextField(data, id_subjek)).commit();
                            break;
                        case "Upload Image Field":
                            fm.beginTransaction().add(R.id.ll_container_materi,new DisplayImageField(data, id_subjek)).commit();
                            break;
                        case "Update Video Field":
                            fm.beginTransaction().add(R.id.ll_container_materi,new DisplayVideoField(BelajarActivity.this,data, id_subjek)).commit();
                            break;
                        case "Update Audio Field":
                            fm.beginTransaction().add(R.id.ll_container_materi,new DisplayAudioField(BelajarActivity.this,data, id_subjek)).commit();
                            break;
                    }
                    currentSection+=1;
                } else {
                    Toast.makeText(BelajarActivity.this, "Subjek Selesai", Toast.LENGTH_SHORT).show();
                    bt_nextMateri.setText("Kembali");
                    bt_nextMateri.setBackgroundColor(Color.RED);
                    bt_nextMateri.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent it = new Intent(BelajarActivity.this, BelajarSubjekActivity.class);
                            it.putExtra("id_pembelajaran",id_pembelajaran);
                            startActivity(it);
                            onStop();
                        }
                    });
                }
                    Toast.makeText(BelajarActivity.this, currentSection+"/"+section_pembelajaran.size(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onStop();
    }
}