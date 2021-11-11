package com.acdi.proyecto_final_daute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private Button btnUpdate, btnDelete;

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
        btnUpdate = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);

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


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = et_id.getText().toString();
                deleteProduct(getApplicationContext(), id);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = et_id.getText().toString();
                String nombre = et_nombre.getText().toString();
                String descripcion = et_descri.getText().toString();
                String stock = et_stock.getText().toString();
                String precio = et_precio.getText().toString();
                String unidad = et_unidad.getText().toString();
                String estado = "";

                if(id.length() == 0){
                    et_id.setError("Campo Obligatorio");

                }else if(nombre.length() == 0){
                    et_nombre.setError("Campo Obligatorio");

                }else if(descripcion.length() == 0){
                    et_descri.setError("Campo Obligatorio");

                }else if(stock.length() == 0){
                    et_stock.setError("Campo Obligatorio");

                }else if(precio.length() == 0){
                    et_precio.setError("Campo Obligatorio");

                }else if(unidad.length() == 0){
                    et_unidad.setError("Campo Obligatorio");

                }else if(sp_estado.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(), "Debe selecionar el estado eel producto", Toast.LENGTH_SHORT).show();

                }else if(sp_categoria.getSelectedItemPosition() > 0){

                    if (sp_estado.getSelectedItem().toString().equals("Activo")){
                        estado = "1";
                    }else{
                        estado = "0";
                    }

                    updateProduct(getApplicationContext(), id, nombre, descripcion, stock, precio, unidad, estado, id_categoria);

                }else{
                    Toast.makeText(getApplicationContext(), "Debe selecionar la categorpia", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void updateProduct(final Context context, final String id, final String nombre, final String descripcion, final String stock, final String precio, final String unidad, final String estado, final String categoria){

        String url = "http://acdi.freeoda.com/web_service/editar_producto.php ";

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
                parametros.put("descripcion", descripcion);
                parametros.put("stock", stock);
                parametros.put("precio", precio);
                parametros.put("unidad", unidad);
                parametros.put("estado", estado);
                parametros.put("categoria", categoria);

                return parametros;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }


    private void deleteProduct(final Context context, final String id){

        String url = "http://acdi.freeoda.com/web_service/eliminar_producto.php";

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

    public void back_listUser(View view) {
        finish();
    }
}