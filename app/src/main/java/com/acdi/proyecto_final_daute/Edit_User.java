package com.acdi.proyecto_final_daute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Edit_User extends AppCompatActivity {

    private EditText et_id, et_nombre, et_apellido, et_correo, et_usuario, et_clave, et_pregunta, et_respuesta;
    private Spinner sp_estado, sp_tipo;
    private TextView tv_fecha;
    private Button btnEditar, btnBorrar;

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

    }

    public void back_listUser(View view) {
        finish();
    }
}