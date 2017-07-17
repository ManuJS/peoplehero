package com.peoplehero.mauriciomartins.peoplehero.contract;

import com.peoplehero.mauriciomartins.peoplehero.model.domain.User;

/**
 * Created by mauriciomartins on 15/07/17.
 */

public interface Login {
    static interface View{
        void showMessage(String message);
        void connectToFacebook(User user);
        void showProgress(boolean visible);
    }

    static interface Presenter{
        void login(Long uid, String nome, String email,String urlimage);
        void showMessage(String message);
        void setUser(User user);
    }

    static interface Interactor{
        void login(Long uid, String nome, String email,String urlimage);
    }
}
