package com.example.k6elearninginteraktif;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button bt_pembelajaranku, bt_kelolaPembelajaran, bt_signout;
    RecyclerView rv_pembelajaranTerdaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        bt_pembelajaranku = findViewById(R.id.bt_pembelajaranku);
        bt_kelolaPembelajaran = findViewById(R.id.bt_kelolaPembelajaran);
        bt_signout = findViewById(R.id.bt_signout);

        rv_pembelajaranTerdaftar = findViewById(R.id.rv_container);
        RecyclerView.LayoutManager rvLayout = new LinearLayoutManager(getApplicationContext());
        rv_pembelajaranTerdaftar.setLayoutManager(rvLayout);
        ArrayList<PembelajarankuModel> listPembelajaranTerdaftar = new ArrayList<>();
        PembelajaranTerdaftarAdaptor adaptorListPembelajaran = new PembelajaranTerdaftarAdaptor(HomeActivity.this, listPembelajaranTerdaftar);
        rv_pembelajaranTerdaftar.setAdapter(adaptorListPembelajaran);
        db.collection("Pengguna").document(auth.getCurrentUser().getUid()).collection("Pembelajaran Terdaftar").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(HomeActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                for (DocumentChange data_pembelajaran: value.getDocumentChanges()){
                    if(data_pembelajaran != null){
                        DocumentSnapshot document = data_pembelajaran.getDocument();
                        PembelajarankuModel item_pembelajaran = new PembelajarankuModel(
                                document.get("id_pengajar").toString(),
                                document.getId(),
                                document.get("judul").toString(),
                                document.get("kategori").toString(),
                                document.get("deskripsi").toString()
                        );
                        listPembelajaranTerdaftar.add(item_pembelajaran);
                    }
                }

                adaptorListPembelajaran.notifyDataSetChanged();
            }
        });


        bt_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Toast.makeText(HomeActivity.this, "Sign-out berhasil!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

        bt_kelolaPembelajaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, PembelajarankuActivity.class) );
            }
        });

        bt_pembelajaranku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ListPembelajaranActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(auth.getCurrentUser().isAnonymous()){
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
}