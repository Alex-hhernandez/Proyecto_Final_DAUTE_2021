package com.acdi.proyecto_final_daute.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.acdi.proyecto_final_daute.MySingleton;
import com.acdi.proyecto_final_daute.R;
import com.acdi.proyecto_final_daute.databinding.FragmentGalleryBinding;
import com.acdi.proyecto_final_daute.list_category;
import com.acdi.proyecto_final_daute.list_product;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private Spinner snpcat;
    private Button viewcat, btnSave, btnNew;
    private TextView et_id, et_nombre;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        et_id = view.findViewById(R.id.et_idcat);
        et_nombre = view.findViewById(R.id.et_namecat);
        viewcat = view.findViewById(R.id.btn_viewCat);
        snpcat = view.findViewById(R.id.spinner_cat);
        btnSave = view.findViewById(R.id.btn_saveCat);
        btnNew = view.findViewById(R.id.btn_newCat);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (getContext(), R.array.estadoUser, R.layout.support_simple_spinner_dropdown_item);
        snpcat.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = et_id.getText().toString();
                String nombre = et_nombre.getText().toString();
                String estado = "";

                if (id.length() == 0){
                    et_id.setError("Campo obligatorio");

                }else if(nombre.length() == 0){
                    et_nombre.setError("Campo obligatorio");

                }else if(snpcat.getSelectedItemPosition() > 0){

                    if (snpcat.getSelectedItem().toString().equals("Activo")){
                        estado = "1";
                    }else{
                        estado = "0";
                    }

                    saveCategory(getContext(), Integer.parseInt(id), nombre, Integer.parseInt(estado));

                }else{
                    Toast.makeText(getContext(), "Debe selecionar un estado de categoría", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_id.setText(null);
                et_nombre.setText(null);
                snpcat.setSelection(0);
            }
        });


        viewcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Hola", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), list_category.class);
                startActivity(i);
            }
        });

        return view;
    }

    private void saveCategory(final Context context, final int id, final String nombre, final int estado){

        String url = "http://acdi.freeoda.com/web_service/guardar_categorias.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject requestJSON = null;

                try{

                    requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");

                    if(estado.equals("1")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();

                    }else if (estado.equals("2")){
                        Toast.makeText(context, "" + mensaje, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    e.getMessage();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                error.getMessage();
                Toast.makeText(getContext(), "No se pudo guardar.\nIntentelo mas tarde", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type","application/json; charset=utf-8");
                map.put("Accept","application/json");
                map.put("id", String.valueOf(id));
                map.put("nombre", nombre);
                map.put("estado", String.valueOf(estado));
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}