package com.example.artixspy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private EditText email,pass;
    private Button reg,log;
    private TextView chenge;
    private ProgressBar loading;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        reg = findViewById(R.id.reg);
        loading = findViewById(R.id.loading);
       log = findViewById(R.id.log);
       chenge = findViewById(R.id.registerd);
       chenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg.setVisibility(View.GONE);
                chenge.setVisibility(View.GONE);
                log.setVisibility(View.VISIBLE);
            }

        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reg();

            }
        });

    }

    private void login() {
        loading.setVisibility(View.VISIBLE);
        final String mail, passw;
        mail = email.getText().toString().trim();
        passw = pass.getText().toString().trim();
        if (mail.isEmpty()) {
            email.setError("Please Enter A email");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            email.setError("Please Enter A valid Email");
            email.requestFocus();
            return;
        } else if (passw.isEmpty()) {
            pass.setError("Please Enter A 8 Dig Passwoard");
            pass.requestFocus();
            return;
        }
        loading.setVisibility(View.VISIBLE);
        mauth = FirebaseAuth.getInstance();

        mauth.signInWithEmailAndPassword(mail, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), HomeAvc.class));
                    loading.setVisibility(View.GONE);
                } else {

                    Toast.makeText(Register.this, "Can't Login", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                }

            }
        });

    }

    private void Reg() {
        final String mail, passw;
        mail = email.getText().toString().trim();
        passw = pass.getText().toString().trim();
        if (mail.isEmpty()) {
            email.setError("Please Enter A email");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            email.setError("Please Enter A valid Email");
            email.requestFocus();
            return;
        } else if (passw.isEmpty()) {
            pass.setError("Please Enter A 8 Dig Passwoard");
            pass.requestFocus();
            return;
        }
        loading.setVisibility(View.VISIBLE);
        mauth = FirebaseAuth.getInstance();
        mauth.createUserWithEmailAndPassword(mail, passw).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uid = mauth.getCurrentUser().getUid();
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Data");
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("Data", "nulll");
                reference1.child(uid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loading.setVisibility(View.GONE);
                            Toast.makeText(Register.this, "You Havebeen registerd", Toast.LENGTH_SHORT).show();
                        } else {
                            loading.setVisibility(View.GONE);
                            Toast.makeText(Register.this, "Coould't register try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            } else {
                loading.setVisibility(View.GONE);
                Toast.makeText(Register.this, "Coould't register try again", Toast.LENGTH_SHORT).show();
            }

        });
        mauth.signInWithEmailAndPassword(mail, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), HomeAvc.class));
                    loading.setVisibility(View.GONE);
                } else {

                    Toast.makeText(Register.this, "Can't Login", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser !=null){
            startActivity(new Intent(getApplicationContext(), HomeAvc.class));
        }
}
}