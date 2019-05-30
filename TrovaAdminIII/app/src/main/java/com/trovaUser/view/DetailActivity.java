package com.trovaUser.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.trovaUser.Peluca;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.trovaAdminPack.R;


public class DetailActivity extends AppCompatActivity {


    public static Peluca peluca;
    Fragment fragment;
    TextView name, dist, rank;
    ImageView imagen;
    ProgressBar pB;

    public static ImageButton mapsBtn;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    public static int i;

    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        peluca = (Peluca) intent.getSerializableExtra("peluca");
        i = intent.getIntExtra("int", 0);

        mapsBtn = findViewById(R.id.maps_btn);
        name = findViewById(R.id.txt_toolbar_detail);
        dist = findViewById(R.id.txt_detail_activity_distancia);
        rank = findViewById(R.id.txt_detail_activity_rank);
        pB = findViewById(R.id.progress_bar);

        dist.setText(String.valueOf(peluca.getDist()));
        rank.setText(String.valueOf(peluca.getRank()));

        name.setText(peluca.getNom().toUpperCase());
        imagen = findViewById(R.id.image_promo);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(peluca.getNom()).child("Url");

        attachImage();

        setFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_second, fragment).commit();

        //pB.setVisibility(View.INVISIBLE);



    }

    public void onClickMaps(View view){


        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+String.valueOf(peluca.getLatitud())+","+String.valueOf(peluca.getLongitud())+"("+peluca.getNom().toUpperCase()+")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);


    }

    public void detailFragmentTransaction(Fragment fragment){

        Bundle bundle = new Bundle();
        bundle.putSerializable("peluca", peluca);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_second, fragment).commit();
    }

    public void attachImage(){

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                imageUrl=dataSnapshot.getValue(String.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });

        Glide.with(this)
                .load(peluca.getImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        pB.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pB.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(new RequestOptions().centerCrop())

                .into(imagen);






    }

    public void setFragment(){

        if(i==0)fragment = new DetallePeluqueriaFragment();
        else {

            mapsBtn.setVisibility(View.INVISIBLE);
            fragment = new DetallePromoFragment();
        }


    }





}
