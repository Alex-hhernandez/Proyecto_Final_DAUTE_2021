package com.acdi.proyecto_final_daute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.window = getWindow();

        CustomLogin();
        IniciarSeccion();
        cambiarcolor();

    }

    private void IniciarSeccion(){
        Button btnsuccess = findViewById(R.id.btn_getstarted);

        btnsuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(home);
            }
        });
    }

    private void CustomLogin(){
        TextView lb1 = findViewById(R.id.lb_login);

        String txt = lb1.getText().toString();

        SpannableString text = new SpannableString(txt);

        ForegroundColorSpan cgreen = new ForegroundColorSpan(Color.GREEN);

        text.setSpan(cgreen,2, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        lb1.setText(text);
    }

    private void cambiarcolor(){
        String color = "#FF000000";

        window.setStatusBarColor(Color.parseColor(color));

    }
}