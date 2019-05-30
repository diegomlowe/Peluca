package com.trovaUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Peluca implements Serializable {

    String nom, precio, promo, dir, contacto, fb, fbId, insta, whats, image, horarios;

    Integer rank;

    int dist;

    int valid;

    String codigo;

    //Location locationPeluca;

    Integer fav;

    Integer h1, h2, h3, h4;

    Double latitud, longitud;

    Boolean  lun, mart, mie, jue, vie, sab, dom;

    Calendar calendar = new GregorianCalendar();


    public Peluca(String nom){

        this.nom=nom;
        this.valid=0;

    }


    public Peluca(String nom, String precio, int dist, String dir, String imagen, Integer rank, String promo, String whats,
                  String fb, /*String fbId,*/ String insta, Double longitud, Double latitud, ArrayList<Boolean> dias, ArrayList<Integer> horarios, String horariosS) {

        this.nom = nom;
        this.dir = dir;
        this.image = imagen;
        this.longitud=longitud;
        this.latitud=latitud;
        this.rank=rank;
        this.promo=promo;
        this.whats=whats;
        this.fb=fb;
        this.insta=insta;

        this.precio=precio;

        this.h1=horarios.get(0);
        this.h2=horarios.get(1);
        this.h3=horarios.get(2);
        this.h4=horarios.get(3);

        this.lun=dias.get(0);
        this.mart=dias.get(1);
        this.mie=dias.get(2);
        this.jue=dias.get(3);
        this.vie=dias.get(4);
        this.sab=dias.get(5);
        this.dom=dias.get(6);

        this.horarios=horariosS;

        //this.locationPeluca = new Location (LocationManager.GPS_PROVIDER);

        //      Location locationPelu = new Location(LocationManager.PASSIVE_PROVIDER);
/*
        locationPelu.setLongitude(this.longitud);
        locationPelu.setLatitude(this.latitud);
        */

/*
        try{
            locationPelu.setLongitude(this.longitud);
            locationPelu.setLatitude(this.latitud);
        } catch (NullPointerException e){

        }

        MainActivity.adapterPeluquerias.notifyDataSetChanged();


        this.dist = ((int) MainActivity.location.distanceTo(locationPelu));
        */

        this.dist=dist;


    }


    public void setHorarios(ArrayList<Integer> horarios, ArrayList<Boolean> dias){

        this.h1=horarios.get(0);
        this.h2=horarios.get(1);
        this.h3=horarios.get(2);
        this.h4=horarios.get(3);

        this.lun=dias.get(0);
        this.mart=dias.get(1);
        this.mie=dias.get(2);
        this.jue=dias.get(3);
        this.vie=dias.get(4);
        this.sab=dias.get(5);
        this.dom=dias.get(6);


    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public Integer getH1() {
        return h1;
    }

    public Integer getH2() {
        return h2;
    }

    public Integer getH3() {
        return h3;
    }

    public Integer getH4() {
        return h4;
    }

    public boolean getHorariosBool(){

        boolean abiertoDia = false, abiertoHorario = false, abierto = false;

        Integer dia = calendar.get(Calendar.DAY_OF_WEEK);
        Integer hora = calendar.get(Calendar.HOUR_OF_DAY);

        switch (dia){

            case 2: if(lun)abiertoDia=true;
            break;

            case 3: if(mart)abiertoDia=true;
            break;

            case 4: if(mie)abiertoDia=true;
            break;

            case 5: if(jue)abiertoDia=true;
            break;

            case 6: if(vie)abiertoDia=true;
            break;

            case 7: if(sab)abiertoDia=true;
            break;

            case 1: if(dom)abiertoDia=true;
            break;

        }

        if ((hora>=h1&&hora<h2)||(hora>=h3&&hora<h4)) abiertoHorario = true;

        if(abiertoDia && abiertoHorario) abierto=true;

        return abierto;

    }

    public Integer getFav() {
        return fav;
    }

    public void setFav(Integer fav) {
        this.fav = fav;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getInsta() {
        return insta;
    }

    public void setInsta(String insta) {
        this.insta = insta;
    }

    public String getWhats() {
        return whats;
    }

    public void setWhats(String whats) {
        this.whats = whats;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public Boolean getLun() {
        return lun;
    }

    public Boolean getMart() {
        return mart;
    }

    public Boolean getMie() {
        return mie;
    }

    public Boolean getJue() {
        return jue;
    }

    public Boolean getVie() {
        return vie;
    }

    public Boolean getSab() {
        return sab;
    }

    public Boolean getDom() {
        return dom;
    }




}
