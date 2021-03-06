package com.acdi.proyecto_final_daute;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class nav_Listar extends Fragment {

    private EditText et_id, et_nombre, et_apellido, et_correo, et_usuario, et_clave, et_pregunta, et_respuesta;
    private Spinner sp_estado, sp_tipo;
    private TextView tv_fecha;
    private Button btnNuevo, btnGuardar,btnVer;

    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nav__listar, container, false);

        et_id = view.findViewById(R.id.id_user);
        et_nombre = view.findViewById(R.id.et_des);
        et_apellido = view.findViewById(R.id.et_ap);
        et_correo = view.findViewById(R.id.et_correo);
        et_usuario = view.findViewById(R.id.et_us);
        et_clave = view.findViewById(R.id.et_cla);
        et_pregunta = view.findViewById(R.id.pre);
        et_respuesta = view.findViewById(R.id.res);
        sp_estado = view.findViewById(R.id.spinner2);
        sp_tipo = view.findViewById(R.id.spinner);
        tv_fecha = view.findViewById(R.id.textView10);
        btnGuardar = view.findViewById(R.id.button);
        btnNuevo = view.findViewById(R.id.button2);
        btnVer = view.findViewById(R.id.btn_Ver);


        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(getContext(), R.array.estadoUser, android.R.layout.simple_spinner_item);
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_estado.setAdapter(adapterEstado);

        ArrayAdapter<CharSequence> adaptertipo = ArrayAdapter.createFromResource(getContext(), R.array.tipoUser, android.R.layout.simple_spinner_item);
        adaptertipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_tipo.setAdapter(adaptertipo);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference(getString(R.string.Usuario));
        auth = FirebaseAuth.getInstance();

        verifi(MainActivity.correo);
        
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_id.setText("");
                et_nombre.setText("");
                et_apellido.setText("");
                et_correo.setText("");
                et_usuario.setText("");
                et_clave.setText("");
                et_pregunta.setText("");
                et_respuesta.setText("");
                sp_estado.setSelection(0);
                sp_tipo.setSelection(0);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
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

                }else if(clave.length() < 6){
                    et_clave.setError("Debe tener al menos 6 caracteres");

                } else if(sp_tipo.getSelectedItemPosition() == 0){
                    Toast.makeText(getContext(), "Debe selecionar el tipo", Toast.LENGTH_SHORT).show();

                }else if(sp_estado.getSelectedItemPosition() == 0){
                    Toast.makeText(getContext(), "Debe selecionar el estado", Toast.LENGTH_SHORT).show();

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
                    //check();
                    saveUser(id, nombre, apellidos, correo, usuario, clave, tipo, estado, pregunta, respuesta);
                }
            }
        });

        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), List_User.class);
                startActivity(i);
            }
        });

        return view;
    }

    private void saveUser(String id, String nombre, String apellidos, String correo, String usuario, String clave, String tipo, String estado, String pregunta, String respuesta) {

       auth.createUserWithEmailAndPassword(correo,clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Map<String, Object> datosUser = new HashMap<>();
                    datosUser.put("id", id);
                    datosUser.put("nombre", nombre);
                    datosUser.put("apellidos", apellidos);
                    datosUser.put("correo", correo);
                    datosUser.put("usuario", usuario);
                    datosUser.put("clave", clave);
                    datosUser.put("tipo", tipo);
                    datosUser.put("estado", estado);
                    datosUser.put("pregunta", pregunta);
                    datosUser.put("respuesta", respuesta);

                    String id = Objects.requireNonNull(auth.getCurrentUser()).getUid();

                    databaseReference.child("Usuario").child(id).setValue(datosUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {

                            if(task1.isSuccessful()){
                                check();
                                Toast.makeText(getContext(), "Registro guardado con exito!", Toast.LENGTH_SHORT).show();
                            }else{

                                Toast.makeText(getContext(), "No se pudo guardar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
       });

    }

    private void verifi(String correo){

        Query query = databaseReference1.orderByChild("correo").equalTo(correo);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    DtoUser user = snapshot.getValue(DtoUser.class);
                    String id = user.getId();
                    String nombre = user.getNombre();
                    String apellidos = user.getApellidos();
                    String correo = user.getCorreo();
                    String usuario = user.getUsuario();
                    String clave = user.getClave();
                    String tipo = user.getTipo();
                    String estado = user.getEstado();
                    String pregunta = user.getPregunta();
                    String respuesta = user.getRespuesta();

                    if(tipo.equals("0")){
                        btnGuardar.setEnabled(false);
                        btnNuevo.setEnabled(false);
                        btnVer.setEnabled(false);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void check() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();

        View vie = inflater.inflate(R.layout.dialog_check, null);

        builder.setView(vie);

        final AlertDialog dialog = builder.create();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },1800);

        dialog.show();
    }
}