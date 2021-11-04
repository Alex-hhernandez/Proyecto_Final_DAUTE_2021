package com.acdi.proyecto_final_daute.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.acdi.proyecto_final_daute.List_User;
import com.acdi.proyecto_final_daute.R;
import com.acdi.proyecto_final_daute.databinding.FragmentSlideshowBinding;
import com.acdi.proyecto_final_daute.list_product;

public class SlideshowFragment extends Fragment {

    private Button btnVer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);

        btnVer = view.findViewById(R.id.btn_Ver);

        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), list_product.class);
                startActivity(i);
            }
        });

        return view;
    }
}