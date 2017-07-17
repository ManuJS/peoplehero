package com.peoplehero.mauriciomartins.peoplehero.model.domain;

/**
 * Created by mauriciomartins on 16/07/17.
 */

public class User {
    private String iduser;
    private String nome;
    private String email;
    private String urlimage;
    private Long uid;

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

    @Override
    public String toString() {
        return "Helpless{" +
                "id='" + iduser + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}


