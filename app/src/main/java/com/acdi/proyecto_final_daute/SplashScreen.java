package com.acdi.proyecto_final_daute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Window;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        this.window = getWindow();

        CustomLogin();
        cambiarcolor();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
            }
        },4000);

    }

    private void CustomLogin(){
        TextView lb1 = findViewById(R.id.lb1);

        String txt = lb1.getText().toString();

        SpannableString text = new SpannableString(txt);

        ForegroundColorSpan cgreen = new ForegroundColorSpan(Color.GREEN);

        text.setSpan(cgreen,4, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        lb1.setText(text);
    }

    private void cambiarcolor(){
        String color = "#FF000000";

        window.setStatusBarColor(Color.parseColor(color));

    }

}