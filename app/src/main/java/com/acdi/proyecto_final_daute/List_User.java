package com.acdi.proyecto_final_daute;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class List_User extends AppCompatActivity {

    ListView listaUsers;
    private List<DtoUser> lista = new ArrayList<DtoUser>();
    ArrayAdapter<DtoUser> adaptador;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    DtoUser datos = new DtoUser();
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        this.window = getWindow();
        cambiarcolor();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference(getString(R.string.Usuario));

        listaUsers = findViewById(R.id.ListaUser);
        listaUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent edit = new Intent(getApplicationContext(), Edit_User.class);
                startActivity(edit);
            }
        });

        listarDatos();
    }

    private void listarDatos(){

        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //lista.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                    DtoUser user = snapshot1.getValue(DtoUser.class);

                    lista.add(user);
                    adaptador = new ArrayAdapter<DtoUser>(List_User.this, android.R.layout.simple_list_item_1, lista);
                    listaUsers.setAdapter(adaptador);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(List_User.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void back_aggUser(View view) {
        Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void cambiarcolor(){
        String color = "#FF000000";

        window.setStatusBarColor(Color.parseColor(color));

    }
}