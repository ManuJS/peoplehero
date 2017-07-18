package com.peoplehero.mauriciomartins.peoplehero.model.domain;

/**
 * Created by mauriciomartins on 16/07/17.
 */

public class User {
    private String iduser;
    private String nome;
    private String email;
    private String urlimage;
    private String STATUS;
    private String UID;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getUrlimage() {
        return urlimage;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getUID() {
        return UID;
    }

    @Override
    public String toString() {
        return "Helpless{" +
                "id='" + iduser + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", STATUS='" + STATUS + '\'' +
                ", UID='" + UID + '\'' +
                '}';
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}


