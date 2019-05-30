package com.trovaUser.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trovaUser.Peluca;

import com.trovaUser.view.MainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trovaAdminPack.R;
import java.util.ArrayList;
public class AdapterPromos extends RecyclerView.Adapter<AdapterPromos.PromoViewHolder> implements View.OnClickListener {

    ArrayList<Peluca> pList;
    private View.OnClickListener listener;

    private DatabaseReference mDB;

    public AdapterPromos(ArrayList<Peluca> pList) {

        this.pList=pList;
    }



    @NonNull
    @Override
    public PromoViewHolder onCreateViewHolder(@NonNull  ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.celda_peluqueria, viewGroup, false);

        view.setOnClickListener(this);


        return new PromoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final PromoViewHolder holder, int i) {

        holder.peluca=pList.get(i);

        holder.nom.setText(String.valueOf(pList.get(i).getNom()).toUpperCase());

        if(pList.get(i).getHorariosBool())holder.nom.setTextColor(Color.parseColor("#1ab612"));
        else holder.nom.setTextColor(Color.parseColor("#ff0000"));

        holder.dir.setText(pList.get(i).getPromo().toUpperCase());
        holder.linearLayout.setVisibility(View.GONE);

        if(MainActivity.favList.contains(pList.get(i).getNom())){

            holder.estrellaNegra.setVisibility(View.GONE);
            holder.estrellaMarcada.setVisibility(View.VISIBLE);
        } else {

            holder.estrellaNegra.setVisibility(View.VISIBLE);
            holder.estrellaMarcada.setVisibility(View.GONE);

        }

        holder.pB.setVisibility(View.INVISIBLE);



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

    public void setOnClickListener(View.OnClickListener listener){

        this.listener=listener;


    }

    @Override
    public void onClick(View view) {
        if(this.listener!=null){

            listener.onClick(view);
        }
    }

    public class PromoViewHolder extends RecyclerView.ViewHolder{

        TextView nom, dir;
        LinearLayout linearLayout;
        ImageView estrellaMarcada, estrellaNegra;
        ProgressBar pB;
        Peluca peluca;

        public PromoViewHolder(@NonNull View itemView) {

            super(itemView);

            nom = itemView.findViewById(R.id.txt_view_nom);
            dir = itemView.findViewById(R.id.txt_view_dir);
            linearLayout = itemView.findViewById(R.id.linear_layout_celda_texto_medio);
            estrellaMarcada = itemView.findViewById(R.id.image_estrella_marcada);
            estrellaNegra = itemView.findViewById(R.id.image_estrella_negra);

            pB = itemView.findViewById(R.id.progress_bar_celda);


        }
    }

}
