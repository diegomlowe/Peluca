package com.trovaUser.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.trovaUser.Peluca;
import com.trovaAdminPack.R;


public class DetallePromoFragment extends Fragment {

    ImageButton imageButton;
    TextView promo;
    DetallePeluqueriaFragment detallePeluqueriaFragment;
    //Peluca peluca;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_promo, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Peluca peluca = DetailActivity.peluca;

        imageButton = getView().findViewById(R.id.btn_local);
        promo = getView().findViewById(R.id.txt_detalle_promo);
        promo.setText(peluca.getPromo().toUpperCase());


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new DetallePeluqueriaFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_second, newFragment);
                ft.commit();
                DetailActivity.mapsBtn.setVisibility(View.VISIBLE);

            }
        });


    }
}
