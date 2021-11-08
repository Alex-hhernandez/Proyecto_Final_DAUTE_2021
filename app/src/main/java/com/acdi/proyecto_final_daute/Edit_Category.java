package com.acdi.proyecto_final_daute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        et_id = findViewById(R.id.idcat_edit);
        et_nombre = findViewById(R.id.namecat_edit);
        sp_estado = findViewById(R.id.spinner_cat);

        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(getApplicationContext(), R.array.estadoUser, android.R.layout.simple_spinner_item);
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_estado.setAdapter(adapterEstado);

        String nombre = getIntent().getStringExtra("nombre");

        Toast.makeText(getApplicationContext(), ""+nombre, Toast.LENGTH_SHORT).show();

        traerDatosCat(getApplicationContext(), nombre);
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

    public void back_listUser(View view) {
        finish();
    }
}