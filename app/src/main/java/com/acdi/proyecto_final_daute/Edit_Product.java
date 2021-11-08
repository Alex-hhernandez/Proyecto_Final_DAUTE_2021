package com.acdi.proyecto_final_daute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Edit_Product extends AppCompatActivity {

    private EditText et_id, et_nombre, et_descri, et_stock, et_precio, et_unidad;
    private Spinner sp_estado, sp_categoria;
    private TextView tv_fecha;

    ArrayList<String> lista = null;
    ArrayList<dto_categorias> listaCategorias;

    String id_categoria = "";
    String nombre_categoria = "";
    int cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        et_id = findViewById(R.id.idprod_edit);
        et_nombre = findViewById(R.id.nombreprod_edit);
        et_descri = findViewById(R.id.desprod_edit);
        et_stock = findViewById(R.id.stockprod_edit);
        et_precio = findViewById(R.id.precioprod_edit);
        et_unidad = findViewById(R.id.uniprod_edit);
        sp_estado = findViewById(R.id.spinner);
        sp_categoria = findViewById(R.id.spinner2);
        tv_fecha = findViewById(R.id.textView10);

        String nombre = getIntent().getStringExtra("nombre");
        Toast.makeText(getApplicationContext(), ""+nombre, Toast.LENGTH_SHORT).show();

        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(getApplicationContext(), R.array.estadoUser, android.R.layout.simple_spinner_item);
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_estado.setAdapter(adapterEstado);

        fk_categorias(getApplicationContext());

        sp_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(cont >= 1 && sp_categoria.getSelectedItemPosition() > 0){
                    String itemSpinner = sp_categoria.getSelectedItem().toString();
                    String s[] = itemSpinner.split("~");
                    id_categoria = s[0].trim();
                    nombre_categoria = s[1];
                    Toast.makeText(getApplicationContext(), "Id cat: " + id_categoria + "\nNombre: "+ nombre_categoria, Toast.LENGTH_SHORT).show();

                }else{
                    id_categoria = "";
                    nombre_categoria = "";
                }
                cont++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        traerDatosProd(getApplicationContext(), nombre);
    }

    public void fk_categorias(final Context context){

        listaCategorias = new ArrayList<dto_categorias>();
        lista = new ArrayList<String>();
        lista.add("Selecione la categoria");

        String url = "http://acdi.freeoda.com/web_service/buscar_categorias.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray array = new JSONArray(response);
                    int totalencontrados = array.length();
                    dto_categorias obj_categorias = null;

                    for(int i = 0; i < array.length(); i++){

                        JSONObject categoriasObjet = array.getJSONObject(i);

                        int id_categoria = categoriasObjet.getInt("id_categoria");
                        String nombre = categoriasObjet.getString("nom_categoria");
                        int estado = Integer.parseInt(categoriasObjet.getString("estado_categoria"));

                        obj_categorias = new dto_categorias(id_categoria, nombre, estado);
                        listaCategorias.add(obj_categorias);
                        lista.add(listaCategorias.get(i).getId_categoria() + " ~ " + listaCategorias.get(i).getNom_categoria());

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lista);
                        sp_categoria.setAdapter(adapter);

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

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void traerDatosProd(final Context context, final String nombre){

        String url = "http://acdi.freeoda.com/web_service/traerProd.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{

                    JSONArray respuesta = new JSONArray(response);

                    for(int i = 0; i < respuesta.length(); i++){

                        JSONObject respuestaJSON = respuesta.getJSONObject(i);

                        String id = respuestaJSON.getString("id_producto");
                        String nombre = respuestaJSON.getString("nom_producto");
                        String descripcion = respuestaJSON.getString("des_producto");
                        String stock = respuestaJSON.getString("stock");
                        String precio = respuestaJSON.getString("precio");
                        String unidad = respuestaJSON.getString("unidad_medida");
                        String estado = respuestaJSON.getString("estado_producto");
                        String categoria = respuestaJSON.getString("categoria");
                        String fecha = respuestaJSON.getString("fecha_entrada");

                        Toast.makeText(getApplicationContext(), ""+id+descripcion, Toast.LENGTH_SHORT).show();

                        et_id.setText(id);
                        et_nombre.setText(nombre);
                        et_descri.setText(descripcion);
                        et_stock.setText(stock);
                        et_precio.setText(precio);
                        et_unidad.setText(unidad);
                        tv_fecha.setText(fecha);

                        if (estado.equals("1")){
                            sp_estado.setSelection(1);
                        }else{
                            sp_estado.setSelection(2);
                        }

                        //sp_categoria.setSelection(Integer.parseInt(String.valueOf(estado.equals(id_categoria))));
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