package com.example.k6elearninginteraktif;

public class PembelajarankuModel {
    private String id_pengajar, id_pembelajaran,judul, kategori, deskripsi;

    public  PembelajarankuModel(){

    }
    public PembelajarankuModel( String id_user, String id_pembelajaran, String judul, String kategori, String deskripsi) {
        this.id_pengajar = id_user;
        this.id_pembelajaran = id_pembelajaran;
        this.judul = judul;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
    }


    public String getJudul() {
        return judul;
    }

    public void setJudul(String juduln) {
        this.judul = juduln;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getId_pembelajaran() {
        return id_pembelajaran;
    }
    public String getId_pengajar() {
        return id_pengajar;
    }

    public void setId_pengajar(String id_pengajar) {
        this.id_pengajar = id_pengajar;
    }

    public void setId_pembelajaran(String id) {
        this.id_pembelajaran = id;
    }
}
