package com.neal.android.upitplatforma;
/**
 * Created by Loredana on 09.02.2019.
 */
public class Postare {
    private String locatie;
    private int pozitieItem;
    private String descriere;
    private String photoUrl;
    private String data;
    private String adresa;
    private String recenzie;

    public Postare() {

    }

    public Postare(String locatie, String descriere) {
        this.locatie = locatie;

        this.descriere = descriere;

    }

    public Postare(String locatie, int pozitieItem, String descriere, String adresa, String data, String photoUrl, String recenzie) {
        this.locatie = locatie;
        this.pozitieItem = pozitieItem;
        this.descriere = descriere;
        this.adresa = adresa;
        this.data = data;
        this.photoUrl = photoUrl;
        this.recenzie = recenzie;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public int getPozitieItem() {
        return pozitieItem;
    }

    public void setPozitieItem(int pozitieItem) {
        this.pozitieItem = pozitieItem;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getRecenzie() {
        return recenzie;
    }

    public void setRecenzie(String recenzie) {
        this.recenzie = recenzie;
    }

    @Override
    public String toString() {
        return "Postare{" +
                "locatie='" + locatie + '\'' +
                ", pozitieItem=" + pozitieItem +
                ", descriere='" + descriere + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", data='" + data + '\'' +
                ", adresa='" + adresa + '\'' +
                '}';
    }
}
