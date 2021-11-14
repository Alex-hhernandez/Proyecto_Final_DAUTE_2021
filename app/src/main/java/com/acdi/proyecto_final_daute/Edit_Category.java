package com.acdi.proyecto_final_daute;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Edit_Category extends AppCompatActivity {

    private EditText et_id, et_nombre;
    private Spinner sp_estado;
    private Button btnDelete, btnUpdate;
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        et_id = findViewById(R.id.idcat_edit);
        et_nombre = findViewById(R.id.namecat_edit);
        sp_estado = findViewById(R.id.spinner_cat);
        btnDelete = findViewById(R.id.btn_delete);
        btnUpdate = findViewById(R.id.btn_save);

        this.window = getWindow();

        cambiarcolor();


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(Edit_Category.this);
                alerta.setIcon(android.R.drawable.ic_dialog_alert);
                alerta.setTitle("¿Eliminar categoría?");
                alerta.setMessage("Los datos se eliminaran permanentemente");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      confirmDelete();


                    }
                });
                alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });

        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(getApplicationContext(), R.array.estadoUser, android.R.layout.simple_spinner_item);
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_estado.setAdapter(adapterEstado);

        String nombre = getIntent().getStringExtra("nombre");

        Toast.makeText(getApplicationContext(), ""+nombre, Toast.LENGTH_SHORT).show();

        traerDatosCat(getApplicationContext(), nombre);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = et_id.getText().toString();
                String nombre = et_nombre.getText().toString();
                String estado = "";

                if (sp_estado.getSelectedItem().toString().equals("Activo")){
                    estado = "1";
                }else{
                    estado = "0";
                }

                updateCategory(getApplicationContext(), id, nombre, estado);
            }
        });
    }

    private void traerDatosCat(final Context context, final String nombre){

        String url = "http://acdi.freeoda.com/web_service/traerCat.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{

                    JSONArray respuesta = new JSONArray(response);

                    for(int i = 0; i < respuesta.length(); i++){

                        JSONObject respuestaJSON = respuesta.getJSONObject(i);

                        String id = respuestaJSON.getString("id_categoria");
                        String nombre = respuestaJSON.getString("nom_categoria");
                        String estado = respuestaJSON.getString("estado_categoria");

                        et_id.setText(id);
                        et_nombre.setText(nombre);

                        if (estado.equals("1")){
                            sp_estado.setSelection(1);
                        }else{
                            sp_estado.setSelection(2);
                        }
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    e.getMessage();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected HashMap<String,String> getParams(){
                HashMap<String,String> parametros = new HashMap<>();
                parametros.put("nombre", nombre);

                return parametros;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void deleteCategory(final Context context, final String id){

        String url = "http://acdi.freeoda.com/web_service/eliminar_categoria.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject requestJSON = null;

                try {

                    requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");

                    if(estado.equals("1")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                        finish();

                    }else if (estado.equals("2")){
                        Toast.makeText(context, "" + mensaje, Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    e.getMessage();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected HashMap<String,String> getParams(){

                HashMap<String,String> parametros = new HashMap<>();
                parametros.put("id", id);

                return parametros;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }


    private void updateCategory(final Context context, final String id, final String nombre, final String estado){

        String url = "http://acdi.freeoda.com/web_service/editar_categoria.php ";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject requestJSON = null;

                try {

                    requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");

                    if(estado.equals("1")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                        finish();

                    }else if (estado.equals("2")){
                        Toast.makeText(context, "" + mensaje, Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    e.getMessage();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error, Revise su conexión", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected HashMap<String,String> getParams(){

                HashMap<String,String> parametros = new HashMap<>();
                parametros.put("id", id);
                parametros.put("nombre", nombre);
                parametros.put("estado", estado);

                return parametros;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void back_listUser(View view) {
        finish();
    }

    private void cambiarcolor(){
        String color = "#FF000000";

        window.setStatusBarColor(Color.parseColor(color));

    }
    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        View vie = inflater.inflate(R.layout.dialog_confirm_delete, null);

        builder.setView(vie);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        Button btnok = vie.findViewById(R.id.btn_okDelete);
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = et_id.getText().toString();
                deleteCategory(getApplicationContext(), id);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}