package com.peoplehero.mauriciomartins.peoplehero.model.domain;

/**
 * Created by mauriciomartins on 16/07/17.
 */

public class Helpless {
    private String idmendingo;
    private String iduser;
    private String latitude;
    private String longitude;

    public String getIdmendingo() {
        return idmendingo;
    }

    public void setIdmendingo(String idmendingo) {
        this.idmendingo = idmendingo;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Helpless{" +
                "idmendingo='" + idmendingo + '\'' +
                ", iduser='" + iduser + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}


