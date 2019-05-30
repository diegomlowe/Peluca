package com.trovaUser.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.trovaUser.Peluca;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.trovaAdminPack.R;

import java.io.IOException;
import java.util.HashMap;


public class SumarActivity extends AppCompatActivity {

    CheckBox lun, mar, mie, jue, vie, sab, dom;
    EditText nom, dir, cont, precio, fb, fbId, insta, whats, promo, h1, h2, h3, h4, latitud, longitud, horario;
    Button enter;
    ImageButton image;


    String nombre;

    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 1;
    private Uri filepath;

    String imageUrl;
    Uri downloadLink;

    private boolean imagePass = false, pass = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sumar);


        enter = findViewById(R.id.btn_agregar_enter);
        image = findViewById(R.id.image_button_agregar);

        lun = findViewById(R.id.cb_lun);
        mar = findViewById(R.id.cb_mar);
        mie = findViewById(R.id.cb_mie);
        jue = findViewById(R.id.cb_jue);
        vie = findViewById(R.id.cb_vie);
        sab = findViewById(R.id.cb_sab);
        dom = findViewById(R.id.cb_dom);

        nom = findViewById(R.id.et_nom_agregar);
        dir = findViewById(R.id.et_dir_agregar);
        cont = findViewById(R.id.et_contacto_agregar);
        precio = findViewById(R.id.et_precio_agregar);
        fb = findViewById(R.id.et_facebook_agregar);
        fbId = findViewById(R.id.et_facebook_id_agregar);
        insta = findViewById(R.id.et_instagram_agregar);
        whats = findViewById(R.id.et_whatsapp_agregar);
        promo = findViewById(R.id.et_promo_agregar);
        latitud = findViewById(R.id.et_latitud_agregar);
        longitud = findViewById(R.id.et_longitud_agregar);
        horario = findViewById(R.id.et_horario_agregar);

        h1 = findViewById(R.id.et_hora1);
        h2 = findViewById(R.id.et_hora2);
        h3 = findViewById(R.id.et_hora3);
        h4 = findViewById(R.id.et_hora4);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();



        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean paso = true;

                /*
                for(Peluca p : MainActivity.pList){

                    if(p.getNom().equals(nom.getText().toString())){



                        Toast.makeText(getApplicationContext(), "Ya existe una peluqueria con el mismo nombre", Toast.LENGTH_SHORT).show();
                        paso=false;
                        break;

                    }

                }
                */

                if(paso) {

                    if(nom.getText().length()==0)

                        Toast.makeText(getApplicationContext(), "Primero ingresa al nombre", Toast.LENGTH_SHORT).show();

                    else {

                        Intent intent = new Intent(/*Intent.ACTION_PICK*/);
                        intent.setType("image/*"); // abarca todas las extensiones de imagenes
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), GALLERY_INTENT);
                        imagePass = true;
                    }


                }



            }
        });


    }

    public void onClickEnter(View view){

        checkForErrors();

        if(pass&&imagePass){

            addToFB();
            Toast.makeText(this, nom.getText().toString().toUpperCase()+" agregada correctamente a la DB",
                    Toast.LENGTH_SHORT).show();


        } else if(!imagePass) Toast.makeText(this, "No se puede agregar una peluqueria sin imagen!",
                Toast.LENGTH_SHORT).show();


    }

    public void checkForErrors(){

        if(!(nom.getText().toString().length()>0)){

            nom.setError("Campo obligatorio");
            pass=false;

        }

        if(!(dir.getText().toString().length()>0)){

            dir.setError("Campo obligatorio");
            pass=false;

        }

        if(!(precio.getText().toString().length()>0)){

            precio.setError("Campo obligatorio");
            pass=false;

        }

        if(!(latitud.getText().toString().length()>0)){

            latitud.setError("Campo obligatorio");
            pass=false;

        }

        if(!(longitud.getText().toString().length()>0)){

            longitud.setError("Campo obligatorio");
            pass=false;

        }

        if(!(cont.getText().toString().length()>0)){

            cont.setError("Campo obligatorio");
            pass=false;

        }

        if(!(horario.getText().toString().length()>0)){

            horario.setError("Campo obligatorio");
            pass=false;

        }


        try {

            if (Integer.valueOf(h1.getText().toString()) < 0) {

                h1.setError("El horario de apertura no puede ser menor que 0");
                pass = false;
            }

            if (Integer.valueOf(h1.getText().toString()) > 24) {

                h1.setError("El horario de apertura no puede ser mayor que 24");
                pass = false;
            }

        }catch(NumberFormatException e){

            h1.setError("Campo obligatorio");
            pass=false;

        }

        try {


            if(Integer.valueOf(h2.getText().toString())<Integer.valueOf(h1.getText().toString())){

                h2.setError("El horario de cierre no puede ser menor que la apertura");
                pass=false;
            }


            if(Integer.valueOf(h2.getText().toString())>24){

                h2.setError("El horario de apertura no puede ser mayor que 24");
                pass=false;
            }


        }catch (NumberFormatException e){

            h2.setError("Campo obligatorio");
            pass=false;

        }


        try {


            if (Integer.valueOf(h3.getText().toString()) < Integer.valueOf(h2.getText().toString())) {

                h3.setError("El horario de la segunda apertura no puede ser menor que el primer cierre");
                pass = false;
            }

            if (Integer.valueOf(h3.getText().toString()) > 24) {

                h3.setError("El horario de apertura no puede ser mayor que 24");
                pass = false;
            }

        } catch(NumberFormatException e){

            h3.setError("Campo obligatorio");
            pass = false;

        }




        try {

            if (Integer.valueOf(h4.getText().toString()) < Integer.valueOf(h3.getText().toString())) {

                h4.setError("El horario de apertura no puede ser menor que el cierre");
                pass = false;
            }

            if (Integer.valueOf(h4.getText().toString()) > 24) {

                h4.setError("El horario de apertura no puede ser mayor que 24");
                pass = false;
            }

        }catch (NumberFormatException e){

            h4.setError("Campo obligatorio");
            pass=false;


        }



    }

    public void addToFB(){

        HashMap map = new HashMap();
        HashMap dias = new HashMap();
        HashMap horarios = new HashMap();

        dias.put("Lun",lun.isChecked());
        dias.put("Mar",mar.isChecked());
        dias.put("Mie",mie.isChecked());
        dias.put("Jue",jue.isChecked());
        dias.put("Vie",vie.isChecked());
        dias.put("Sab",sab.isChecked());
        dias.put("Dom",dom.isChecked());

        horarios.put("H1",Integer.valueOf(h1.getText().toString()));
        horarios.put("H2",Integer.valueOf(h2.getText().toString()));
        horarios.put("H3",Integer.valueOf(h3.getText().toString()));
        horarios.put("H4",Integer.valueOf(h4.getText().toString()));


        map.put("Dias", dias);
        map.put("Horarios", horarios);

        map.put("Direccion", dir.getText().toString() );
        map.put("Contacto",cont.getText().toString());
        map.put("Precio", precio.getText().toString());
        map.put("Facebook", precio.getText().toString() );
        map.put("FacebookId", fbId.getText().toString());
        map.put("Instagram", insta.getText().toString());
        map.put("Whatsapp", whats.getText().toString());
        map.put("Promo", promo.getText().toString());
        map.put("Url", downloadLink.toString() );
        map.put("Rank", 0);
        map.put("Longitud", Double.valueOf(longitud.getText().toString()));
        map.put("Latitud", Double.valueOf(latitud.getText().toString()));
        map.put("HorariosString", horario.getText().toString());
        map.put("ID", MainActivity.id);
        MainActivity.id++;
        map.put("Nombre", nom.getText().toString());


        mDatabase = FirebaseDatabase.getInstance().getReference().push();

        mDatabase.setValue(map);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK && data!=null && data.getData()!=null){

            filepath = data.getData();

            try {
                Bitmap bitmapImagen = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                image.setImageBitmap(bitmapImagen);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (filepath != null) {

                final StorageReference fotoRef = mStorage.child(String.valueOf(MainActivity.id));



                fotoRef.putFile(filepath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()) throw new Exception();

                        return fotoRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){

                            downloadLink = task.getResult();

                            imagePass=true;


                        }
                    }
                });
            }

        }

    }
}
