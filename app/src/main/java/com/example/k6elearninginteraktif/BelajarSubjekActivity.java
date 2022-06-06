package com.example.k6elearninginteraktif;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class BelajarSubjekActivity extends AppCompatActivity {

    FirebaseFirestore db ;
    DocumentReference pembelajaran ;
    RecyclerView rv_listSubjek;
    String id_pembelajaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_belajar_subjek);


        id_pembelajaran = getIntent().getStringExtra("id_pembelajaran");
        db = FirebaseFirestore.getInstance();
        pembelajaran = db.collection("Pembelajaran").document(id_pembelajaran);


        rv_listSubjek = findViewById(R.id.subjek_container);

        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getApplicationContext());
        rv_listSubjek.setLayoutManager(linearLayout);
        ArrayList<SubjekModel> list_subjek = new ArrayList<SubjekModel>();
        SubjekAdaptor subjekAdaptor = new SubjekAdaptor(this, list_subjek,SubjekAdaptor.METHOD_ACCESS, id_pembelajaran);

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
                        SubjekModel subjek_item = new SubjekModel(document.getId(),document.get("Nama Subjek").toString(),document.get("Ringkasan").toString());
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
        startActivity(new Intent(BelajarSubjekActivity.this, HomeActivity.class));
    }
}