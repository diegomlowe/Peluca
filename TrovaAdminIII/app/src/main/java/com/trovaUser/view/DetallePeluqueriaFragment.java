package com.trovaUser.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trovaUser.Peluca;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trovaAdminPack.R;

public class DetallePeluqueriaFragment extends Fragment {



    TextView contacto, precio, dir, estado, horarios, name, codigo;
    ImageButton wats, fb, insta;
    ImageView estrellaNegra, estrellaMarcada;
    DatabaseReference mDB;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_peluqueria, container, false);

        contacto = view.findViewById(R.id.txt_detalle_pelu_contacto);
        precio = view.findViewById(R.id.txt_detalle_pelu_precio);
        dir = view.findViewById(R.id.txt_detalle_pelu_dir);
        estado = view.findViewById(R.id.txt_detalle_pelu_abierto_cerrado);
        horarios = view.findViewById(R.id.txt_detalle_pelu_horarios);
        wats = view.findViewById(R.id.btn_wp);
        fb = view.findViewById(R.id.btn_fb);
        insta = view.findViewById(R.id.btn_insta);
        estrellaMarcada = view.findViewById(R.id.image_estrella_marcada_detalle);
        estrellaNegra = view.findViewById(R.id.image_estrella_negra_detalle);
        codigo = view.findViewById(R.id.text_view_peluca_id_detalle);


        final Peluca peluca = DetailActivity.peluca;

        codigo.setText(peluca.getCodigo());


        if(peluca.getHorariosBool()==true){

            estado.setText("ABIERTO");
            estado.setTextColor(Color.parseColor("#1ab612"));

        } else {

            estado.setText("CERRADO");
            estado.setTextColor(Color.parseColor("#ff0000"));

        }

        contacto.setText(peluca.getContacto());
        precio.setText("$"+peluca.getPrecio());
        dir.setText(peluca.getDir());

        horarios.setText(peluca.getHorarios());

        wats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://api.whatsapp.com/send?phone="+peluca.getWhats();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);


            }

        });


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;

                if(appInstalledOrNot(getContext(), "com.facebook.katana")&&peluca.getFbId().length()>0){


                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"+peluca.getFbId()));

                    intent.setPackage("com.facebook.katana");

                } else intent = new Intent(Intent.ACTION_VIEW, Uri.parse(peluca.getFb()));

                tryCatchSocial(intent, "Facebook");

            }


        });


        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;

                if(appInstalledOrNot(getContext(), "com.instagram.android")){

                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(peluca.getInsta()));

                    intent.setPackage("com.instagram.android");


                }else intent = new Intent(Intent.ACTION_VIEW, Uri.parse(peluca.getInsta()));

                tryCatchSocial(intent, "Instagram");


            }
        });


        if(MainActivity.favList.contains(peluca.getNom())){

            estrellaNegra.setVisibility(View.GONE);
            estrellaMarcada.setVisibility(View.VISIBLE);
        } else {

            estrellaNegra.setVisibility(View.VISIBLE);
            estrellaMarcada.setVisibility(View.GONE);

        }

        mDB = FirebaseDatabase.getInstance().getReference().child(DetailActivity.peluca.getCodigo());

        estrellaNegra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MainActivity.favList.add(peluca.getNom());
                estrellaNegra.setVisibility(View.GONE);
                estrellaMarcada.setVisibility(View.VISIBLE);

                mDB.child("Rank").setValue(peluca.getRank()+1);

                peluca.setRank(peluca.getRank()+1);

                //MainActivity.setFavOnStarClick(peluca, 1);


            }
        });

        estrellaMarcada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.favList.remove(peluca.getNom());
                estrellaMarcada.setVisibility(View.GONE);
                estrellaNegra.setVisibility(View.VISIBLE);

                mDB.child("Rank").setValue(peluca.getRank()-1);
                peluca.setRank(peluca.getRank()-1);

                //MainActivity.setFavOnStarClick(peluca, 0);


            }
        });



        return view;

    }

    public boolean appInstalledOrNot(Context context, String uri) {

        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch(PackageManager.NameNotFoundException e) {

        }

        return false;
    }

    public void tryCatchSocial(Intent intent, String rs){

        try {

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(getContext(), "No existe la pagina de "+rs, Toast.LENGTH_SHORT).show();
        }



    }



}
