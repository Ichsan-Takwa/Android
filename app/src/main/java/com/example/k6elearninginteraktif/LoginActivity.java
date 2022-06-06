package com.example.k6elearninginteraktif;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText te_email, te_password;
    Button bt_login;
    ProgressDialog progressDialog;
    ProgressDialog loading;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getApplicationContext());
        loading = progressDialog.show(this,"Loading..", "Tunggu sebentar!");
        loading.dismiss();
        te_email = findViewById(R.id.et_email);
        te_password = findViewById(R.id.et_password);

        bt_login = findViewById(R.id.bt_login);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val_email = te_email.getText().toString();
                String val_password = te_password.getText().toString();

                if(!TextUtils.isEmpty(val_email) || !TextUtils.isEmpty(val_password)){
                    login(val_email,val_password);
                }else{
                    Toast.makeText(LoginActivity.this, "email atau kata sandi salah!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void login(String val_email, String val_password) {
        loading.show();
        auth.signInWithEmailAndPassword(val_email,val_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful() && (task.getResult() != null)){
                    if(task.getResult().getUser() != null){
                        loginUser();
                        loading.dismiss();
                    }else{
                        Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                } else{
                    Toast.makeText(LoginActivity.this, "email atau kata sandi salah", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        loginUser();
    }

    private void loginUser(){
        if (auth.getCurrentUser()!= null ){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        loading.dismiss();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, StartActivity.class));

    }
}