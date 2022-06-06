package com.example.k6elearninginteraktif;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListPembelajaranActivity extends AppCompatActivity {
    FirebaseFirestore db;
    CollectionReference pembelajaran_ref;

    RecyclerView rv_list_pembelajaranku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pembelajaran);

        db = FirebaseFirestore.getInstance();
        pembelajaran_ref = db.collection("Pembelajaran");
        rv_list_pembelajaranku = findViewById(R.id.pembelajaranku_container);

//        Recyclerview daftar pembelajaranku
        RecyclerView.LayoutManager linear_layout = new LinearLayoutManager(ListPembelajaranActivity.this);
        rv_list_pembelajaranku.setLayoutManager(linear_layout);
        ArrayList<PembelajarankuModel> list_pembelajaranku= new ArrayList<PembelajarankuModel>();
        ListPembelajaranAdaptor listPembelajaranAdaptor = new ListPembelajaranAdaptor(ListPembelajaranActivity.this, list_pembelajaranku);

//        pembelajaran.notify();
        pembelajaran_ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    Log.e("Database error: ", error.getLocalizedMessage());
                    Toast.makeText(ListPembelajaranActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                }
                listPembelajaranAdaptor.notifyDataSetChanged();
            }
        });
        rv_list_pembelajaranku.setAdapter(listPembelajaranAdaptor);


    }
}