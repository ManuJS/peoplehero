package com.peoplehero.mauriciomartins.peoplehero.contract;

import com.peoplehero.mauriciomartins.peoplehero.model.domain.Helpless;

import java.util.List;

/**
 * Created by mauriciomartins on 15/07/17.
 */

public interface Map {
    static interface View{
        void showMessage(String message);
        void updateHelpless(List<Helpless> help);
    }

    static interface Presenter{
        void setHelpAsk();
        void showMessage(String messsage);
        void refresh();
        void updateHelpless(List<Helpless> help);
        void confirmHelp(String idmendingo, String iduser);
        void saveLocation(double latitude, double longitude);
        void saveUserInfo(int iduser, String uid);
    }

    static interface Interactor{

        void setHelpAsk(double latitude, double longitude);

        void refresh(double latitude, double longitude, double idUser);

        void confirmHelp(Long latitude, Long longitude, Long idUser);

        void confirmHelp(String idmendingo, String iduser);
    }
}
