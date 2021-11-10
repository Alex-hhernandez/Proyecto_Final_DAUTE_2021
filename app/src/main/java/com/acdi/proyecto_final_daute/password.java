package com.acdi.proyecto_final_daute;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class password extends AppCompatActivity {

    private Window window;
    private EditText et_email;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        this.window = getWindow();
        cambiarcolor();

        et_email = findViewById(R.id.rec_email);
        ImageView back = findViewById(R.id.back_pass);
        Button send = findViewById(R.id.btn_send);
        auth = FirebaseAuth.getInstance();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString().trim();

                if (email.length() == 0){
                    et_email.setError("Debe ingresar su Email.");

                }else{

                    recuperarPass(email);
                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void cambiarcolor(){
        String color = "#FF000000";

        window.setStatusBarColor(Color.parseColor(color));

    }

    private void recuperarPass(String email){

        auth.setLanguageCode("es");

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    auth.setLanguageCode("es");
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Revisa tu correo", Toast.LENGTH_SHORT).show();
                    finish();

                }else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}