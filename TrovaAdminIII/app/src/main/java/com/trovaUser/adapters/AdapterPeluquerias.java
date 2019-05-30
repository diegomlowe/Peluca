package com.trovaUser.adapters;

import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trovaUser.Peluca;
import com.trovaAdminPack.R;
import com.trovaUser.view.MainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterPeluquerias extends RecyclerView.Adapter<AdapterPeluquerias.PeluqueriasViewHolder> implements View.OnClickListener {

    ArrayList<Peluca> pList;

    private View.OnClickListener listener;

    private DatabaseReference mDB;


    public AdapterPeluquerias(ArrayList<Peluca> pList) {
        this.pList = pList;

    }



    @NonNull
    @Override
    public PeluqueriasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.celda_peluqueria, viewGroup, false);

        view.setOnClickListener(this);


        return new PeluqueriasViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener){

        this.listener=listener;


    }

    @Override
    public void onBindViewHolder(@NonNull final PeluqueriasViewHolder holder,  int i) {

        holder.peluca = pList.get(i);

        holder.nom.setText(String.valueOf(pList.get(i).getNom()).toUpperCase());

        if(pList.get(i).getHorariosBool()) holder.nom.setTextColor(Color.parseColor("#1ab612"));
        else holder.nom.setTextColor(Color.parseColor("#ff0000"));


        Location locationPelu = new Location("");

        locationPelu.setLongitude(pList.get(i).getLongitud());
        locationPelu.setLatitude(pList.get(i).getLatitud());


        int dist = pList.get(i).getDist();

        if(dist==0){

            holder.dist.setVisibility(View.GONE);
            holder.distDerecha.setVisibility(View.GONE);

        } else {

            holder.pB.setVisibility(View.INVISIBLE);
            holder.dist.setVisibility(View.VISIBLE);
            holder.distDerecha.setVisibility(View.VISIBLE);


            if(pList.get(i).getDist()>999) {

                double d = pList.get(i).getDist();

                d /= 1000;

                StringBuilder distancia = new StringBuilder(String.valueOf(d));

                if (d % 1 != 0) distancia.delete(distancia.length() - 3, distancia.length() - 1);
                else distancia.delete(distancia.length() - 5, distancia.length() - 1);

                holder.dist.setText(distancia);

                holder.distDerecha.setText("KM");

            }else {


                holder.dist.setText(String.valueOf(dist));
                holder.distDerecha.setText("METROS");
            }


        }

        holder.dir.setText(String.valueOf(pList.get(i).getDir()).toUpperCase());


        if(MainActivity.favList.contains(pList.get(i).getNom())){

            holder.estrellaNegra.setVisibility(View.GONE);
            holder.estrellaMarcada.setVisibility(View.VISIBLE);
        } else {

            holder.estrellaNegra.setVisibility(View.VISIBLE);
            holder.estrellaMarcada.setVisibility(View.GONE);

        }

        //mDB = FirebaseDatabase.getInstance().getReference().child(pList.get(i).getNom());

        holder.estrellaNegra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDB = FirebaseDatabase.getInstance().getReference().child(holder.peluca.getCodigo());

                MainActivity.favList.add(holder.peluca.getNom());
                holder.estrellaNegra.setVisibility(View.GONE);
                holder.estrellaMarcada.setVisibility(View.VISIBLE);

                mDB.child("Rank").setValue(holder.peluca.getRank()+1);

                holder.peluca.setRank(holder.peluca.getRank()+1);

                //MainActivity.setFavOnStarClick(holder.peluca, 1);



            }
        });

        holder.estrellaMarcada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDB = FirebaseDatabase.getInstance().getReference().child(holder.peluca.getCodigo());
                MainActivity.favList.remove(holder.peluca.getNom());
                holder.estrellaMarcada.setVisibility(View.GONE);
                holder.estrellaNegra.setVisibility(View.VISIBLE);

                mDB.child("Rank").setValue(holder.peluca.getRank()-1);
                holder.peluca.setRank(holder.peluca.getRank()-1);

                //MainActivity.setFavOnStarClick(holder.peluca, 0);


            }
        });


    }

    @Override
    public int getItemCount() {
        return pList.size();
    }

    @Override
    public void onClick(View view) {

        if(this.listener!=null){

            listener.onClick(view);
        }

    }

    public class PeluqueriasViewHolder extends RecyclerView.ViewHolder{


        TextView nom, dir, dist, distDerecha;
        ImageView estrellaMarcada, estrellaNegra;
        Peluca peluca;
        ProgressBar pB;


        public PeluqueriasViewHolder(@NonNull View itemView) {

            super(itemView);

            nom = itemView.findViewById(R.id.txt_view_nom);
            dir = itemView.findViewById(R.id.txt_view_dir);
            dist = itemView.findViewById(R.id.txt_view_distance);
            estrellaMarcada = itemView.findViewById(R.id.image_estrella_marcada);
            estrellaNegra = itemView.findViewById(R.id.image_estrella_negra);
            distDerecha = itemView.findViewById(R.id.txt_view_distance_drch);
            pB = itemView.findViewById(R.id.progress_bar_celda);


        }

    }


}
