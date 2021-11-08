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

public class list_product extends AppCompatActivity {

    private ListView ListaProduct;
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        this.window = getWindow();

        cambiarcolor();
        traerProductos();

        ListaProduct = findViewById(R.id.ListaProduct);
    }

    private void traerProductos(){

        ArrayList<String> lista = new ArrayList<>();
        ArrayList<dto_productos> listaProductos = new ArrayList<>();

        String url = "http://acdi.freeoda.com/web_service/buscar_productos.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONArray array = new JSONArray(response);
                    dto_productos obj_productos = null;

                    for(int i = 0; i < array.length(); i++){

                        JSONObject productoObjet = array.getJSONObject(i);

                        String id_producto = productoObjet.getString("id_producto");
                        String nombre = productoObjet.getString("nom_producto");
                        String descripcion = productoObjet.getString("des_producto");
                        double stock = Double.parseDouble(productoObjet.getString("stock"));
                        double precio = Double.parseDouble(productoObjet.getString("precio"));
                        String unidad_medida = productoObjet.getString("unidad_medida");
                        int estado = Integer.parseInt(productoObjet.getString("estado_producto"));
                        int categoria = Integer.parseInt(productoObjet.getString("categoria"));

                        obj_productos = new dto_productos(id_producto, nombre, descripcion, stock, precio, unidad_medida, estado ,categoria);
                        listaProductos.add(obj_productos);
                        lista.add(listaProductos.get(i).getNom_producto());

                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, lista);
                        ListaProduct.setAdapter(adapter);
                        ListaProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                Intent intent = new Intent(getApplicationContext(), Edit_Product.class);
                                String nombres = (String) ListaProduct.getItemAtPosition(i);
                                intent.putExtra("nombre", nombres);
                                startActivity(intent);
                            }
                        });

                        Log.i("id_producto", String.valueOf(obj_productos.getId_producto()));
                        Log.i("nombre_producto", String.valueOf(obj_productos.getId_producto()));
                        Log.i("estado_producto", String.valueOf(obj_productos.getId_producto()));

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

    public void back_aggProduct(View view) {
        Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void cambiarcolor(){
        String color = "#FF000000";

        window.setStatusBarColor(Color.parseColor(color));

    }
}