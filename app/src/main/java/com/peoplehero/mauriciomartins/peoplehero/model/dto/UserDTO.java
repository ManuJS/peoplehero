package com.peoplehero.mauriciomartins.peoplehero.model.dto;

import com.peoplehero.mauriciomartins.peoplehero.model.domain.User;

import java.util.List;

/**
 * Created by mauriciomartins on 16/07/17.
 */

public class UserDTO {
    private String STATUS;
    private String UID;

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
                ", STATUS='" + STATUS + '\'' +
                ", UID='" + UID + '\'' +
                '}';
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}


