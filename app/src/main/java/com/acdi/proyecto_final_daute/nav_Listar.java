package com.acdi.proyecto_final_daute;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class nav_Listar extends Fragment {

    private EditText et_id, et_nombre, et_apellido, et_correo, et_usuario, et_clave, et_pregunta, et_respuesta;
    private Spinner sp_estado, sp_tipo;
    private TextView tv_fecha;
    private Button btnNuevo, btnGuardar;

    DatabaseReference databaseReference;

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

        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(getContext(), R.array.estadoUser, android.R.layout.simple_spinner_item);
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_estado.setAdapter(adapterEstado);

        ArrayAdapter<CharSequence> adaptertipo = ArrayAdapter.createFromResource(getContext(), R.array.tipoUser, android.R.layout.simple_spinner_item);
        adaptertipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_tipo.setAdapter(adaptertipo);

        databaseReference = FirebaseDatabase.getInstance().getReference();

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

                }else if(sp_tipo.getSelectedItemPosition() == 0){
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

                    saveUser(id, nombre, apellidos, correo, usuario, clave, tipo, estado, pregunta, respuesta);
                    Toast.makeText(getContext(), "¡Registro guardado!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void saveUser(String id, String nombre, String apellidos, String correo, String usuario, String clave, String tipo, String estado, String pregunta, String respuesta) {
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
        datosUser.put("fecha", ServerValue.TIMESTAMP);

        databaseReference.child("Usuario").push().setValue(datosUser);
    }
}