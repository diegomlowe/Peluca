package com.trovaUser.view;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.trovaUser.ConectionSQLiteHelper;
import com.trovaUser.Peluca;

import com.trovaUser.adapters.AdapterFavoritas;
import com.trovaUser.adapters.AdapterPeluquerias;
import com.trovaUser.adapters.AdapterPromos;
import com.trovaUser.adapters.PagerAdapter;
import com.trovaUser.interfaces.IComunicaFragments;
import com.trovaUser.utils.Utils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trovaAdminPack.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements PromosFragment.OnFragmentInteractionListener,
        FavoritosFragment.OnFragmentInteractionListener, CercanasFragment.OnFragmentInteractionListener,
        IComunicaFragments {

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    Button btnClicAqui;
    Calendar calendar = new GregorianCalendar();

    public static AdapterPeluquerias adapterPeluquerias;
    public static AdapterFavoritas adapterFavoritas;
    public static AdapterPromos adapterPromos;

    public static ArrayList<String> favList;
    public static ArrayList<Peluca> promoList;
    public static ArrayList<Peluca> pList;
    public static ArrayList<Peluca> favoritasList;


    public static DatabaseReference mDB;

    public static LocationManager locationManager;
    public static LocationListener locationListener;
    public static Location location;

    TextView loadingLocation;
    ProgressBar pB;

    String s1, s2, s3, s4;

    GoogleApiClient mGoogleApiClient;

    GoogleMap mMap;

    GpsTracker gpsTracker;

    public static ConectionSQLiteHelper conn;

    SQLiteDatabase db;

    private FusedLocationProviderClient client;

    public static int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        settingLocation();


       // pB = findViewById(R.id.progress_bar_main_activity);
        loadingLocation = findViewById(R.id.txt_view_loading_location);



        btnClicAqui = findViewById(R.id.btn_click_aqui);


        pList = new ArrayList<>();
        favList = new ArrayList<>();
        promoList = new ArrayList<>();
        favoritasList = new ArrayList<>();


        gpsTracker = new GpsTracker(this);

        viewPager = findViewById(R.id.view_pager);

        settingTabLAyout();

        //settingViewPager();
/*

        conn = new ConectionSQLiteHelper(getApplicationContext(), "db_peluquerias", null, 1);
        db = openOrCreateDatabase("db_peluquerias", Context.MODE_PRIVATE, null);
*/
        //llenarListaFromSQL();

        adapterPeluquerias = new AdapterPeluquerias(pList);
        adapterFavoritas = new AdapterFavoritas(favoritasList);
        adapterPromos = new AdapterPromos(promoList);


        if (isNetworkAvailable()) {


            settingViewPager();
            settingFireBase();
            loadingLocation.setVisibility(View.GONE);

        }

        Toast.makeText(this, "Created", Toast.LENGTH_LONG).show();


        id=1;


    }


    public void validarLista(){

        int count=0;



        for(int i=0; i<pList.size(); i++){

            try {

                if (pList.get(i - count).getValid() == 0) {

                    count++;
                    //deletePelucaFromDB(pList.get(i - count));
                    pList.remove(pList.get(i - count));

                    if (favoritasList.contains(pList.get(i - count)))
                        favoritasList.remove(pList.get(i - count));
                    if (promoList.contains(pList.get(i - count)))
                        promoList.remove(pList.get(i - count));


                    notifyAdapters();


                }

            }catch(IndexOutOfBoundsException e){

                break;

            }

        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                //location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                /*
                Criteria criteria = new Criteria();

                locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria, false), 0, 0, locationListener);
                //if(location==null)locationManager.requestLocationUpdates();
*/
            }

        }
    }

    @Override
    public void enviarPeluca(Peluca peluca, int i) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("peluca", peluca);
        bundle.putInt("int", i);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void onClickSumar(View view) {

        Intent intent = new Intent(this, SumarActivity.class);
        startActivity(intent);

    }

    public void onClickAqui(View view) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "apptrova@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Peticion de suscripcion de peluqueria");
        startActivity(Intent.createChooser(emailIntent, null));

    }

    public void settingLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


/*
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

*/
            return;

        }



        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                MainActivity.location = location;
                locationManager.removeUpdates(locationListener);
                updateListOnLocationChange();
                notifyAdapters();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //notifyAdapters();

            }

            @Override
            public void onProviderEnabled(String s) {
/*
                Criteria criteria = new Criteria();

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria, false), 0, 0, locationListener);
*/
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };


        location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        if(location==null)locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER,0,0,locationListener);


    }

    private void getLocation() {
    }

    public void changePelucaFromFirebase(Peluca p, DataSnapshot dataSnapshot){

        addFromFB(p, dataSnapshot);

        //editPelucaInDB(p);

        orderPelucaList(pList);

        if(!promoList.contains(p))addPelucaToPromoList(p, promoList);

        if(!favoritasList.contains(p)){

            addPelucaToFavoritasList(p, favoritasList);
            orderFavList(favoritasList);
        }

        notifyAdapters();

    }

    public void settingViewPager() {

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void settingFireBase() {

        mDB = FirebaseDatabase.getInstance().getReference();

        mDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //String nom = dataSnapshot.getKey();

                boolean add=true;
/*
                for(Peluca p: pList){

                    if(p.getNom().equals(nom)){

                        //p.setValid(1);
                        //changePelucaFromFirebase(p, dataSnapshot);
                        add=false;
                        break;

                    }

                }*/

                if(add) {

                    String codigo = dataSnapshot.getKey().toString();

                    String nom = (String) dataSnapshot.child("Nombre").getValue();

                    Peluca peluca = new Peluca(nom);

                    peluca.setCodigo(codigo);

                    //peluca.setValid(1);

                    addFromFB(peluca, dataSnapshot);

                    //addPelucaToDB(peluca);

                    pList.add(peluca);

                    addPelucaToPromoList(peluca, promoList);

                    addPelucaToFavoritasList(peluca, favoritasList);

                    orderFavList(favoritasList);

                    orderPelucaList(pList);

                    notifyAdapters();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (Peluca p : pList) {

                    if (p.getNom().equals((String)dataSnapshot.child("Nombre").getValue())) {

                       changePelucaFromFirebase(p, dataSnapshot);


                       break;

                    }

                }

            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                for (Peluca p : pList) {

                    if (p.getNom().equals((String)dataSnapshot.child("Nombre").getValue())) {

                        pList.remove(p);
                        favList.remove(p);
                        promoList.remove(p);
                        //deletePelucaFromDB(p);
                        notifyAdapters();
                        break;

                    }

                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void settingTabLAyout() {

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("CERCANAS"));
        tabLayout.addTab(tabLayout.newTab().setText("PROMOS"));
        tabLayout.addTab(tabLayout.newTab().setText("FAVORITAS"));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);


    }

    public void waitToRefreshTime() {

        while (true) {
            try {

                Thread.sleep(1000);

            } catch (InterruptedException ex) {

                Thread.currentThread().interrupt();

            }

           notifyAdapters();
        }

    }

    public void addFromFB(Peluca p, DataSnapshot dataSnapshot) {

        p.setDir(dataSnapshot.child("Direccion").getValue(String.class));


        Double latitud = (Double) dataSnapshot.child("Latitud").getValue(Double.class);
        Double longitud = (Double) dataSnapshot.child("Longitud").getValue(Double.class);
        Integer rank = dataSnapshot.child("Rank").getValue(Integer.class);
        String promo = dataSnapshot.child("Promo").getValue(String.class);
        String whats = dataSnapshot.child("Whatsapp").getValue(String.class);
        String image = dataSnapshot.child("Url").getValue(String.class);
        String contacto = dataSnapshot.child("Contacto").getValue(String.class);

        Integer id = dataSnapshot.child("ID").getValue(Integer.class);

        if(id>=this.id)this.id=id+1;

        String precio = (String) dataSnapshot.child("Precio").getValue();
        String horariosS = (String) dataSnapshot.child("HorariosString").getValue();

        String fb = (String) dataSnapshot.child("Facebook").getValue();

        String ig = (String) dataSnapshot.child("Instagram").getValue();

        Boolean lun = (Boolean) dataSnapshot.child("Dias").child("Lun").getValue();
        Boolean mar = (Boolean) dataSnapshot.child("Dias").child("Mar").getValue();
        Boolean mie = (Boolean) dataSnapshot.child("Dias").child("Mie").getValue();
        Boolean jue = (Boolean) dataSnapshot.child("Dias").child("Jue").getValue();
        Boolean vie = (Boolean) dataSnapshot.child("Dias").child("Vie").getValue();
        Boolean sab = (Boolean) dataSnapshot.child("Dias").child("Sab").getValue();
        Boolean dom = (Boolean) dataSnapshot.child("Dias").child("Dom").getValue();


        ArrayList<Boolean> dias = new ArrayList<>();
        dias.add(lun);
        dias.add(mar);
        dias.add(mie);
        dias.add(jue);
        dias.add(vie);
        dias.add(sab);
        dias.add(vie);
        dias.add(sab);
        dias.add(dom);

        Integer h1 = dataSnapshot.child("Horarios").child("H1").getValue(Integer.class);
        Integer h2 = dataSnapshot.child("Horarios").child("H2").getValue(Integer.class);
        Integer h3 = dataSnapshot.child("Horarios").child("H3").getValue(Integer.class);
        Integer h4 = dataSnapshot.child("Horarios").child("H4").getValue(Integer.class);


        ArrayList<Integer> horarios = new ArrayList<>();
        horarios.add(h1);
        horarios.add(h2);
        horarios.add(h3);
        horarios.add(h4);

        int dist=setDist(longitud, latitud);

        p.setLatitud(latitud);
        p.setLongitud(longitud);
        p.setFb(fb);
        p.setInsta(ig);
        p.setRank(rank);
        p.setPromo(promo);
        p.setContacto(contacto);
        p.setWhats(whats);
        p.setHorarios(horarios, dias);
        p.setHorarios(horariosS);
        p.setPrecio(precio);
        p.setImage(image);
        p.setDist(dist);


        notifyAdapters();


    }

    private void notifyAdapters() {

        adapterPeluquerias.notifyDataSetChanged();
        adapterFavoritas.notifyDataSetChanged();
        adapterPromos.notifyDataSetChanged();

    }

    public void orderPelucaList(ArrayList<Peluca> pList) {

        Peluca aux;

        for (int i = 0; i < pList.size() - 1; i++) {
            for (int z = 0; z < pList.size() - 1; z++) {

                if (pList.get(z).getDist() > pList.get(z + 1).getDist()) {

                    aux = pList.get(z + 1);
                    pList.set(z + 1, pList.get(z));
                    pList.set(z, aux);

                }

            }

        }


    }

    public void orderFavList(ArrayList<Peluca> pList){

        Peluca aux;

        for (int i = 0; i < pList.size() - 1; i++) {
            for (int z = 0; z < pList.size() - 1; z++) {

                if (pList.get(z).getRank() < pList.get(z + 1).getRank()) {

                    aux = pList.get(z + 1);
                    pList.set(z + 1, pList.get(z));
                    pList.set(z, aux);

                }

            }

        }


    }

    public int setDist(Double longitud, Double latitud){

        Location locationPelu = new Location("");


        locationPelu.setLongitude(longitud);
        locationPelu.setLatitude(latitud);


        try {

            return (int) location.distanceTo(locationPelu);

         }catch(NullPointerException e){

            return 0;
        }

            /*
        }catch(NullPointerException e){



        }
*/


    }

    public void addPelucaToPromoList(Peluca peluca, ArrayList<Peluca> promoList){

        try {
            if (peluca.getPromo().length() > 1) promoList.add(peluca);
        } catch (NullPointerException e) {

        }

    }

    public void addPelucaToFavoritasList(Peluca peluca, ArrayList<Peluca> favoritasList){

        if(peluca.getRank()>0 && peluca.getDist()<25000) favoritasList.add(peluca);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    protected void onDestroy() {

        db.close();

        super.onDestroy();



    }

    public void updateListOnLocationChange(){

        for(Peluca p : pList){

            p.setDist(setDist(p.getLongitud(), p.getLatitud()));

        }

        orderPelucaList(pList);
        notifyAdapters();

    }
    /*
    public void setFavOnStarClick(Peluca p, int i){

        pList.get(i).setFav(pList.get(i));


    }*/



    /////////// METODOS SQLite ////////////


    public void updateListOnLocationChangeDB(){


        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+Utils.TABLA_PELUQUERIAS, null);


        while(cursor.moveToNext()){

            for(Peluca p: pList){

                if(p.getNom().equals(cursor.getString(0))){

                    int dist = setDist(p.getLongitud(), p.getLatitud());
                    p.setDist(dist);
                    break;

                }

            }

            orderPelucaList(pList);

        }

    }
    /*

    public void llenarListaFromSQL() {

        SQLiteDatabase db = conn.getReadableDatabase();

        Peluca p = null;

        Cursor cursor = db.rawQuery("SELECT * FROM "+Utils.TABLA_PELUQUERIAS, null);


        while(cursor.moveToNext()){

            addPelucaFromCursor(p, cursor);

            orderPelucaList(pList);


        }


    }


    public void addPelucaFromCursor(Peluca p, Cursor cursor){

        p = new Peluca(cursor.getString(0));

        p.setPrecio(cursor.getString(1));
        p.setPromo(cursor.getString(2));
        p.setDir(cursor.getString(3));
        p.setContacto(cursor.getString(4));
        p.setFb(cursor.getString(5));
        p.setFbId(cursor.getString(6));
        p.setInsta(cursor.getString(7));
        p.setWhats(cursor.getString(8));
        p.setImage(cursor.getString(9));
        p.setHorarios(cursor.getString(10));
        p.setRank(cursor.getInt(11));
        p.setFav(cursor.getInt(25));



        p.setLatitud(Double.valueOf(cursor.getString(16)));
        p.setLongitud(Double.valueOf(cursor.getString(17)));

        Integer h1, h2, h3, h4;
        h1 = cursor.getInt(12);
        h2 = cursor.getInt(13);
        h3 = cursor.getInt(14);
        h4 = cursor.getInt(15);

        ArrayList<Integer> horarios = new ArrayList<>();
        horarios.add(h1);
        horarios.add(h2);
        horarios.add(h3);
        horarios.add(h4);

        Boolean lun, mar, mie, jue, vie, sab, dom;


        if(cursor.getInt(18)==0) lun = false;
        else lun = true;

        if(cursor.getInt(19)==0) mar = false;
        else mar = true;

        if(cursor.getInt(20)==0) mie = false;
        else mie = true;

        if(cursor.getInt(21)==0) jue = false;
        else jue = true;

        if(cursor.getInt(22)==0) vie = false;
        else vie = true;

        if(cursor.getInt(23)==0) sab = false;
        else sab = true;

        if(cursor.getInt(24)==0) dom = false;
        else dom = true;

        ArrayList<Boolean> dias = new ArrayList<>();

        dias.add(lun);
        dias.add(mar);
        dias.add(mie);
        dias.add(jue);
        dias.add(vie);
        dias.add(sab);
        dias.add(dom);

        p.setHorarios(horarios, dias);

        int dist = setDist(p.getLongitud(), p.getLatitud());
        p.setDist(dist);


        pList.add(p);

        addPelucaToPromoList(p, promoList);
        addPelucaToFavoritasList(p, favoritasList);

        orderFavList(favoritasList);

        if (p.getFav() == 1) favList.add(p.getNom());

    }

    public void addPelucaToDB(Peluca p){


        SQLiteDatabase db = conn.getWritableDatabase();
        ArrayList<String> dias = diasBoolToString(p);


        String insert="INSERT INTO "+Utils.TABLA_PELUQUERIAS+ " ( "+Utils.CAMPO_NOMBRE+", "+Utils.CAMPO_PRECIO+ " ,"+Utils.CAMPO_PROMO+
                ", "+Utils.CAMPO_DIR+", "+Utils.CAMPO_CONTACTO+", "+Utils.CAMPO_FACEBOOK+", "+Utils.CAMPO_FACEBOOKID+", "+
                Utils.CAMPO_INSTAGRAM+", "+Utils.CAMPO_WHATSAPP+", "+Utils.CAMPO_IMAGEN+", "+Utils.CAMPO_HORARIOS+", "+Utils.CAMPO_RANK+
                ", "+Utils.CAMPO_H1+", "+Utils.CAMPO_H2+", "+Utils.CAMPO_H3+", "+Utils.CAMPO_H4+", "+
                Utils.CAMPO_LATITUD+", "+Utils.CAMPO_LONGITUD+", "+ Utils.CAMPO_LUN+", "+Utils.CAMPO_MAR+", "+Utils.CAMPO_MIE+", "+
                Utils.CAMPO_JUE+", "+Utils.CAMPO_VIE+", "+Utils.CAMPO_SAB+", "+Utils.CAMPO_DOM+", "+Utils.CAMPO_FAV+")";

        String values="";

        values = " VALUES ( '" + p.getNom() + "', '" + p.getPrecio() + "', '" + p.getPromo() + "', '" + p.getDir() + "', '" + p.getContacto() + "', '" +
                p.getFb() + "', '" + p.getFbId() + "', '" + p.getInsta() + "', '" + p.getWhats() + "', '" + p.getImage() + "', '" + p.getHorarios() + "', " +
                String.valueOf(p.getRank()) + ", " + String.valueOf(p.getH1()) + ", " + String.valueOf(p.getH2()) + ", " + String.valueOf(p.getH3()) +
                ", " + String.valueOf(p.getH4()) + ", '" + String.valueOf(p.getLatitud()) + "', '" + String.valueOf(p.getLongitud()) + "', " + dias.get(0) +
                ", " + dias.get(1) + ", " + dias.get(2) + ", " + dias.get(3) + ", " + dias.get(4) + ", " + dias.get(5) + ", " + dias.get(6) + ", 0 ) ";


        db.execSQL(insert+values);


        //db.close();



    }

    public void deletePelucaFromDB(Peluca p){

        SQLiteDatabase db = conn.getReadableDatabase();

        String[] parametros = {p.getNom()};

        db.delete(Utils.TABLA_PELUQUERIAS, Utils.CAMPO_NOMBRE+"=?", parametros);

        //db.close();



    }

    public static void setFavOnStarClick(Peluca p, int i){

        SQLiteDatabase db = conn.getReadableDatabase();

        String[] parametros = {p.getNom()};

        ContentValues values = new ContentValues();

        values.put(Utils.CAMPO_FAV, i);

        db.update(Utils.TABLA_PELUQUERIAS, values, Utils.CAMPO_NOMBRE+"=?", parametros);

    }

    public void editPelucaInDB(Peluca p){

        SQLiteDatabase db = conn.getReadableDatabase();

        String[] parametros = {p.getNom()};

        ContentValues values = new ContentValues();

        values.put(Utils.CAMPO_PRECIO, p.getPrecio());
        values.put(Utils.CAMPO_PROMO, p.getPromo());
        values.put(Utils.CAMPO_DIR, p.getDir());
        values.put(Utils.CAMPO_CONTACTO, p.getContacto());
        values.put(Utils.CAMPO_FACEBOOK, p.getFb());
        values.put(Utils.CAMPO_FACEBOOKID, p.getFbId());
        values.put(Utils.CAMPO_INSTAGRAM, p.getInsta());
        values.put(Utils.CAMPO_WHATSAPP, p.getWhats());
        values.put(Utils.CAMPO_IMAGEN, p.getImage());
        values.put(Utils.CAMPO_HORARIOS, p.getHorarios());
        values.put(Utils.CAMPO_RANK, String.valueOf(p.getRank()));
        values.put(Utils.CAMPO_H1, String.valueOf(p.getH1()));
        values.put(Utils.CAMPO_H2, String.valueOf(p.getH2()));
        values.put(Utils.CAMPO_H3, String.valueOf(p.getH3()));
        values.put(Utils.CAMPO_H4, String.valueOf(p.getH4()));
        values.put(Utils.CAMPO_LATITUD, String.valueOf(p.getLatitud()));
        values.put(Utils.CAMPO_LONGITUD, String.valueOf(p.getLongitud()));

        ArrayList<String> dias = diasBoolToString(p);

        values.put(Utils.CAMPO_LUN, dias.get(0));
        values.put(Utils.CAMPO_MAR, dias.get(1));
        values.put(Utils.CAMPO_MIE, dias.get(2));
        values.put(Utils.CAMPO_JUE, dias.get(3));
        values.put(Utils.CAMPO_VIE, dias.get(4));
        values.put(Utils.CAMPO_SAB, dias.get(5));
        values.put(Utils.CAMPO_DOM, dias.get(6));


        db.update(Utils.TABLA_PELUQUERIAS, values, Utils.CAMPO_NOMBRE+"=?", parametros);

        //db.close();

    }

    public ArrayList<String> diasBoolToString(Peluca p){

        String lun, mar, mie, jue, vie, sab, dom;

        ArrayList<String> dias = new ArrayList<>();


            if (p.getLun()) lun = "1";
            else lun = "0";

            if (p.getMart()) mar = "1";
            else mar = "0";

            if (p.getMie()) mie = "1";
            else mie = "0";

            if (p.getJue()) jue = "1";
            else jue = "0";

            if (p.getVie()) vie = "1";
            else vie = "0";

            if (p.getSab()) sab = "1";
            else sab = "0";

            if (p.getDom()) dom = "1";
            else dom = "0";

            dias.add(lun);
            dias.add(mar);
            dias.add(mie);
            dias.add(jue);
            dias.add(vie);
            dias.add(sab);
            dias.add(dom);


        return dias;


    }

    public void saveSharedPreferences(){

        SharedPreferences sharedPreferences = getSharedPreferences("db_peluquerias", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();


    }*/

}

