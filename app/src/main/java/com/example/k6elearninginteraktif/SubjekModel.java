package com.example.k6elearninginteraktif;

public class SubjekModel {
    String id,tipe, namaSubjek, deskripsi;

    public SubjekModel(){

    }

    public SubjekModel(String id, String namaSubjek, String deskripsi   ) {
        this.id = id;
        this.namaSubjek = namaSubjek;
        this.deskripsi = deskripsi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaSubjek() {
        return namaSubjek;
    }

    public void setNamaSubjek(String namaSubjek) {
        this.namaSubjek = namaSubjek;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }


    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

}
