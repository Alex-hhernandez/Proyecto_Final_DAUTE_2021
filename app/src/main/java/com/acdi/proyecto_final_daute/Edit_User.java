package com.acdi.proyecto_final_daute;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Edit_User extends AppCompatActivity {

    private EditText et_id, et_nombre, et_apellido, et_correo, et_usuario, et_clave, et_pregunta, et_respuesta;
    private Spinner sp_estado, sp_tipo;
    private TextView tv_fecha;
    private Button btnEditar, btnBorrar;
    private Window window;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        et_id = findViewById(R.id.id_edit);
        et_nombre = findViewById(R.id.name_edit);
        et_apellido = findViewById(R.id.ap_edit);
        et_correo = findViewById(R.id.correo_edit);
        et_usuario = findViewById(R.id.us_edit);
        et_clave = findViewById(R.id.cla_edit);
        et_pregunta = findViewById(R.id.pre_edit);
        et_respuesta = findViewById(R.id.res_edit);
        sp_estado = findViewById(R.id.spinner2_edit);
        sp_tipo = findViewById(R.id.spinner1_edit);
        tv_fecha = findViewById(R.id.textView10);
        btnBorrar = findViewById(R.id.btn_delete);
        btnEditar = findViewById(R.id.btn_save);

        this.window = getWindow();

        cambiarcolor();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference(getString(R.string.Usuario));

        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(getApplicationContext(), R.array.estadoUser, android.R.layout.simple_spinner_item);
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_estado.setAdapter(adapterEstado);

        ArrayAdapter<CharSequence> adaptertipo = ArrayAdapter.createFromResource(getApplicationContext(), R.array.tipoUser, android.R.layout.simple_spinner_item);
        adaptertipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_tipo.setAdapter(adaptertipo);

        String senal = "";
        String id = "";
        String nombre = "";
        String apellidos = "";
        String correo = "";
        String usuario = "";
        String clave = "";
        String tipo = "";
        String estado = "";
        String pregunta = "";
        String respuesta = "";

        try{
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();

            if(bundle != null){

                senal = bundle.getString("senal");
                id = bundle.getString("id");
                nombre = bundle.getString("nombre");
                apellidos = bundle.getString("apellidos");
                correo = bundle.getString("correo");
                usuario = bundle.getString("usuario");
                clave = bundle.getString("clave");
                tipo = bundle.getString("tipo");
                estado = bundle.getString("estado");
                pregunta = bundle.getString("pregunta");
                respuesta = bundle.getString("respuesta");

                if(senal.equals("1")){
                    et_id.setText(id);
                    et_nombre.setText(nombre);
                    et_apellido.setText(apellidos);
                    et_correo.setText(correo);
                    et_usuario.setText(usuario);
                    et_clave.setText(clave);
                    et_pregunta.setText(pregunta);
                    et_respuesta.setText(respuesta);

                    if (tipo.equals("1")){
                        sp_tipo.setSelection(1);
                    }else{
                        sp_tipo.setSelection(2);
                    }

                    if (estado.equals("1")){
                        sp_estado.setSelection(1);
                    }else{
                        sp_estado.setSelection(2);
                    }
                }
            }

        }catch (Exception e){
            e.getMessage();
        }

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = et_id.getText().toString();
                String nombre = et_nombre.getText().toString();
                String apellidos = et_apellido.getText().toString();
                String correo = et_correo.getText().toString();
                String usuario = et_usuario.getText().toString();
                String clave = et_clave.getText().toString();
                String pregunta = et_pregunta.getText().toString();
                String respuesta = et_respuesta.getText().toString();
                String tipo = "";
                String estado = "";

                if(id.length() == 0){
                    et_id.setError("Campo obligatorio");

                }else if(nombre.length() == 0){
                    et_nombre.setError("Campo obligatorio");

                }else if(apellidos.length() == 0){
                    et_apellido.setError("Campo obligatorio");

                }else if(correo.length() == 0){
                    et_correo.setError("Campo obligatorio");

                }else if(usuario.length() == 0){
                    et_usuario.setError("Campo obligatorio");

                }else if(clave.length() == 0){
                    et_clave.setError("Campo obligatorio");

                }else if(sp_tipo.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(), "Debe selecionar el tipo", Toast.LENGTH_SHORT).show();

                }else if(sp_estado.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(), "Debe selecionar el estado", Toast.LENGTH_SHORT).show();

                }else if(pregunta.length() == 0){
                    et_pregunta.setError("Campo obligatorio");

                }else if(respuesta.length() == 0){
                    et_respuesta.setError("Campo obligatorio");

                }else{

                    if (sp_tipo.getSelectedItem().toString().equals("Administrador")){
                        tipo = "1";
                    }else{
                        tipo = "0";
                    }

                    if (sp_estado.getSelectedItem().toString().equals("Activo")){
                        estado = "1";
                    }else{
                        estado = "0";
                    }

                    updateUser(id, nombre, apellidos, correo, usuario, clave, tipo, estado, pregunta, respuesta);
                }
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(Edit_User.this);
                alerta.setIcon(android.R.drawable.ic_dialog_alert);
                alerta.setTitle("¿Eliminar usuario?");
                alerta.setMessage("Los datos se eliminaran permanentemente");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String id = et_id.getText().toString();

                        deleteUser(id);
                    }
                });
                alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

            }
        });
    }

    private void updateUser(String id, String nombre, String apellidos, String correo,
                            String usuario, String clave, String tipo, String estado,
                            String pregunta, String respuesta){

        Query query = databaseReference1.orderByChild("id").equalTo(id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String llave = dataSnapshot.getKey();

                    databaseReference1.child(llave).child("nombre").setValue(nombre);
                    databaseReference1.child(llave).child("apellidos").setValue(apellidos);
                    databaseReference1.child(llave).child("correo").setValue(correo);
                    databaseReference1.child(llave).child("usuario").setValue(usuario);
                    databaseReference1.child(llave).child("clave").setValue(clave);
                    databaseReference1.child(llave).child("tipo").setValue(tipo);
                    databaseReference1.child(llave).child("estado").setValue(estado);
                    databaseReference1.child(llave).child("pregunta").setValue(pregunta);
                    databaseReference1.child(llave).child("respuesta").setValue(respuesta);

                    Toast.makeText(getApplicationContext(), "¡Actualizado!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void deleteUser(String id){

        Query query = databaseReference1.orderByChild("id").equalTo(id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String llave = dataSnapshot.getKey();

                    databaseReference1.child(llave).removeValue();
                    Toast.makeText(getApplicationContext(), "Eliminado.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void back_listUser(View view) {
        finish();
    }

    private void cambiarcolor(){
        String color = "#FF000000";

        window.setStatusBarColor(Color.parseColor(color));

    }
}