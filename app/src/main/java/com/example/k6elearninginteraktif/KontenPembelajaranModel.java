package com.example.k6elearninginteraktif;

import android.net.Uri;

public class KontenPembelajaranModel {
    int noUrut ;
    String tipeKonten, id_dokumen;
    Uri data;

    public KontenPembelajaranModel(int noUrut, String tipeKonten, String id_dokumen, Uri data) {
        this.noUrut = noUrut;
        this.tipeKonten = tipeKonten;
        this.id_dokumen = id_dokumen;
        this.data = data;
    }

    public int getNoUrut() {
        return noUrut;
    }

    public void setNoUrut(int noUrut) {
        this.noUrut = noUrut;
    }

    public String getTipeKonten() {
        return tipeKonten;
    }

    public void setTipeKonten(String tipeKonten) {
        this.tipeKonten = tipeKonten;
    }

    public String getId_dokumen() {
        return id_dokumen;
    }

    public void setId_dokumen(String id_dokumen) {
        this.id_dokumen = id_dokumen;
    }

    public Uri getData() {
        return data;
    }

    public void setData(Uri data) {
        this.data = data;
    }
}
