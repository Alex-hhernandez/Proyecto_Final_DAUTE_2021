package com.acdi.proyecto_final_daute.ui.gallery;

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

import com.acdi.proyecto_final_daute.R;
import com.acdi.proyecto_final_daute.databinding.FragmentGalleryBinding;
import com.acdi.proyecto_final_daute.list_category;
import com.acdi.proyecto_final_daute.list_product;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private Spinner snpcat;
    private Button viewcat;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        viewcat = view.findViewById(R.id.btn_viewCat);
        snpcat = view.findViewById(R.id.spinner_cat);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (getContext(), R.array.estadoUser, R.layout.support_simple_spinner_dropdown_item);

        snpcat.setAdapter(adapter);

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
}