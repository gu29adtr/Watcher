package com.example.gustavo.techsoybean.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Watcher {
    private String id;
    private String idusuario;
    private Double lat;
    private Double lng;
    private String praga;
    private String pragaurl;
    private int status;
    private Double temp;
    private Double umi;

    public Watcher() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Watcher (String id, String idusuario, Double lat, Double lng, String praga, String pragaurl,int status, Double temp, Double umi) {
        this.id = id;
        this.idusuario = idusuario;
        this.lat = lat;
        this.lng = lng;
        this.praga = praga;
        this.pragaurl = pragaurl;
        this.status = status;
        this.temp = temp;
        this.umi = umi;
    }
    public String getId() {
        return id;
    }
    public String getIdusuario() {
        return idusuario;
    }
    public Double getLat() {
        return lat;
    }
    public Double getLng() {
        return lng;
    }
    public String getPraga(){return praga; }
    public String getPragaUrl() {return pragaurl;}
    public int getStatus() {
        return status;
    }
    public Double getTemp() {
        return temp;
    }
    public Double getUmi() {
        return umi;
    }
}
