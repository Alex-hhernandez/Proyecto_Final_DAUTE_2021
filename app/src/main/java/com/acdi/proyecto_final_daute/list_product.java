package com.acdi.proyecto_final_daute;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class list_product extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
    }
    public void back_aggProduct(View view) {
        Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show();
        finish();
    }
}