package com.acdi.proyecto_final_daute.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.acdi.proyecto_final_daute.List_User;
import com.acdi.proyecto_final_daute.MySingleton;
import com.acdi.proyecto_final_daute.R;
import com.acdi.proyecto_final_daute.databinding.FragmentSlideshowBinding;
import com.acdi.proyecto_final_daute.dto_categorias;
import com.acdi.proyecto_final_daute.list_product;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SlideshowFragment extends Fragment {

    private EditText et_id, et_nombre, et_descri, et_stock, et_precio, et_unidad;
    private Spinner sp_estado, sp_categoria;
    private Button btnVer, btnSave, btnNew;

    ArrayList<String> lista = null;
    ArrayList<dto_categorias> listaCategorias;

    String id_categoria = "";
    String nombre_categoria = "";
    int cont = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);

        et_id = view.findViewById(R.id.id_produc);
        et_nombre = view.findViewById(R.id.et_des);
        et_descri = view.findViewById(R.id.et_desc);
        et_stock = view.findViewById(R.id.et_correo);
        et_precio = view.findViewById(R.id.et_us);
        et_unidad = view.findViewById(R.id.et_cla);
        sp_estado = view.findViewById(R.id.spinner);
        sp_categoria = view.findViewById(R.id.spinner2);
        btnVer = view.findViewById(R.id.btn_Ver);
        btnSave = view.findViewById(R.id.button);
        btnNew = view.findViewById(R.id.button2);

        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(getContext(), R.array.estadoUser, android.R.layout.simple_spinner_item);
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_estado.setAdapter(adapterEstado);

        fk_categorias(getContext());

        sp_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(cont >= 1 && sp_categoria.getSelectedItemPosition() > 0){
                    String itemSpinner = sp_categoria.getSelectedItem().toString();
                    String s[] = itemSpinner.split("~");
                    id_categoria = s[0].trim();
                    nombre_categoria = s[1];
                    Toast.makeText(getContext(), "Id cat: " + id_categoria + "\nNombre: "+ nombre_categoria, Toast.LENGTH_SHORT).show();

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

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newProduct();
            }
        });


        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), list_product.class);
                startActivity(i);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(getContext(), "Debe selecionar el estado eel producto", Toast.LENGTH_SHORT).show();

                }else if(sp_categoria.getSelectedItemPosition() > 0){

                    if (sp_estado.getSelectedItem().toString().equals("Activo")){
                        estado = "1";
                    }else{
                        estado = "0";
                    }

                    //check();

                    save_productos(getContext(), Integer.parseInt(id), nombre, descripcion, stock, precio, unidad, estado, id_categoria);

                }else{
                    Toast.makeText(getContext(), "Debe selecionar la categorpia", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
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

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lista);
                        sp_categoria.setAdapter(adapter);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "Error: compruebe su conexión a internet", Toast.LENGTH_SHORT).show();

            }
        });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void newProduct(){
        et_id.setText(null);
        et_nombre.setText(null);
        et_descri.setText(null);
        et_stock.setText(null);
        et_precio.setText(null);
        et_unidad.setText(null);
        sp_estado.setSelection(0);
        sp_categoria.setSelection(0);
    }

    private void save_productos(final Context context, final int id_prod, final String nom_prod, final String des_prod, final String stock, final String precio, final String uni_medida, final String estado_prod, final String categoria){

        String url = "http://acdi.freeoda.com/web_service/guardar_productos.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject requestJSON = null;

                try{
                    requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");

                    if(estado.equals("1")){
                        check();
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();

                    }else if(estado.equals("2")){
                        check();
                        Toast.makeText(context, "Registro guardado corectamente", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No se pudo guardar. \nIntentelo más tarde.", Toast.LENGTH_SHORT).show();
            }

        }){

            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id_prod", String.valueOf(id_prod));
                map.put("nom_prod", nom_prod);
                map.put("des_prod", des_prod);
                map.put("stock", stock);
                map.put("precio", precio);
                map.put("uni_medida", uni_medida);
                map.put("estado_prod", estado_prod);
                map.put("categoria", categoria);

                return map;
            }

        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
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