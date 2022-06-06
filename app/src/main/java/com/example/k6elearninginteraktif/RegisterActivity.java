package com.example.k6elearninginteraktif;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText te_username ,te_email, te_password;
    Button bt_register;

    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseStorage storage;

    ProgressDialog progressDialog;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(getApplicationContext());
        loading = progressDialog.show(this,"Loading", "Tunggu sebentar");
        loading.setProgressStyle(2);
        loading.dismiss();

        te_username = findViewById(R.id.et_username);
        te_email = findViewById(R.id.et_email);
        te_password = findViewById(R.id.et_password);

        bt_register = findViewById(R.id.bt_register);



        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val_username = te_username.getText().toString();
                String val_email = te_email.getText().toString();
                String val_password = te_password.getText().toString();

                if(!TextUtils.isEmpty(val_email) || !TextUtils.isEmpty(val_password)){
                    if (val_password.length() >=8) {
                        registerUser(val_username ,val_email ,val_password);
                    } else {
                        Toast.makeText(RegisterActivity.this, "kata sandi minimal 8 karakter!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "email atau kata sandi salah!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void registerUser(String username, String email, String password) {
        loading.show();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && (task.getResult() != null)){
                    FirebaseUser fb_user = task.getResult().getUser();

                    if(fb_user != null){
                        // update user information | insert username
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                        fb_user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(RegisterActivity.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                                addUserToDatabase();
//                                auth.signOut();
                            }
                        });

                    }else{
                        Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                loading.dismiss();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        loading.dismiss();
    }

    public void addUserToDatabase(){
        CollectionReference user_ref = db.collection("Pengguna");
        Map<String,Object> user = new HashMap<String,Object>();
        user.put("Nama Pengguna", auth.getCurrentUser().getDisplayName());
        user.put("Email ",auth.getCurrentUser().getEmail());
        user.put("File Foto", "default.jpg");

        Toast.makeText(this, "Adding User to DB", Toast.LENGTH_SHORT).show();
        user_ref.document(auth.getCurrentUser().getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "berhasil memperbarui informasi akun", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    auth.signOut();
                } else {
                    Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, StartActivity.class));
    }
}