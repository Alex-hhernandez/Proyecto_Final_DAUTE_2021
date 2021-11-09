package com.acdi.proyecto_final_daute;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class password extends AppCompatActivity {

    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        this.window = getWindow();
        cambiarcolor();

        ImageView back = findViewById(R.id.back_pass);
        Button send = findViewById(R.id.btn_send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Enviado", Toast.LENGTH_SHORT).show();
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
}