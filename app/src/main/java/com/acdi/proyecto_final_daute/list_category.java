package com.acdi.proyecto_final_daute;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class list_category extends AppCompatActivity {

    private ListView listaCat;
    private Window window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);

        this.window = getWindow();


        cambiarcolor();
        traerCategorias();

        listaCat = findViewById(R.id.ListaCat);
    }

    private void traerCategorias(){

        ArrayList<String> lista = new ArrayList<>();
        ArrayList<dto_categorias> listaCategorias = new ArrayList<>();

        String url = "http://acdi.freeoda.com/web_service/buscar_categorias.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray array = new JSONArray(response);
                    dto_categorias obj_categorias = null;

                    for(int i = 0; i < array.length(); i++){

                        JSONObject categoriasObjet = array.getJSONObject(i);

                        int id_categoria = categoriasObjet.getInt("id_categoria");
                        String nombre = categoriasObjet.getString("nom_categoria");
                        int estado = Integer.parseInt(categoriasObjet.getString("estado_categoria"));

                        obj_categorias = new dto_categorias(id_categoria, nombre, estado);
                        listaCategorias.add(obj_categorias);
                        lista.add(listaCategorias.get(i).getNom_categoria());

                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, lista);
                        listaCat.setAdapter(adapter);
                        listaCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                Intent intent = new Intent(getApplicationContext(), Edit_Category.class);
                                String nombres = (String) listaCat.getItemAtPosition(i);
                                intent.putExtra("nombre", nombres);
                                startActivity(intent);
                            }
                        });

                        Log.i("id_categoria", String.valueOf(obj_categorias.getId_categoria()));
                        Log.i("nombre_categoria", String.valueOf(obj_categorias.getId_categoria()));
                        Log.i("estado_categoria", String.valueOf(obj_categorias.getId_categoria()));

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Error: compruebe su conexión a internet", Toast.LENGTH_SHORT).show();

            }
        });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void back_aggCat(View view) {
        finish();
    }

    private void cambiarcolor(){
        String color = "#FF000000";

        window.setStatusBarColor(Color.parseColor(color));

    }
}