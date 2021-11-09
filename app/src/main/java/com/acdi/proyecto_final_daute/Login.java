package com.acdi.proyecto_final_daute;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private Window window;
    private EditText et_email, et_pass;
    private Button btnLogin;
    DatabaseReference databaseReference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_getstarted);
        databaseReference1 = FirebaseDatabase.getInstance().getReference(getString(R.string.Usuario));

        this.window = getWindow();

        //IniciarSeccion();

        cambiarcolor();
        CustomLogin();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = et_email.getText().toString();
                String pass = et_pass.getText().toString();

                if (email.length() == 0){
                    et_email.setError("Campo obligatorio");

                }else if(pass.length() == 0){
                    et_pass.setError("Campo obligatorio");

                }else{
                    login(getApplicationContext(), email, pass);
                }
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

    private void welcome() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        View vie = inflater.inflate(R.layout.dialog_welcome, null);

        builder.setView(vie);

        final AlertDialog dialog = builder.create();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(home);
                dialog.dismiss();
                finish();
            }
        },1500);

        dialog.show();
    }

    private void login(final Context context, final String email, final String pass){

        Query query = databaseReference1.orderByChild("correo").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        DtoUser user = dataSnapshot.getValue(DtoUser.class);

                        String clave = user.getClave();

                        if (pass.equals(clave)){
                            welcome();
                        }else{
                            Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}