package com.peoplehero.mauriciomartins.peoplehero.contract;

import android.content.Context;

import com.peoplehero.mauriciomartins.peoplehero.ViewModel.MapModelView;
import com.peoplehero.mauriciomartins.peoplehero.model.domain.Helpless;

import java.util.List;

/**
 * Created by mauriciomartins on 15/07/17.
 */

public interface Map {
    static interface View{
        void showMessage(String message);
        void updateHelpless(MapModelView help);
        Context getContext();
        void showProgress(boolean show);

        void route(String latitude, String longitude);
    }

    static interface Presenter{
        void setHelpAsk();
        void showMessage(String messsage);
        void refresh();
        void updateHelpless(List<Helpless> helplessList);
        void confirmHelp(String idmendingo);
        void saveLocation(double latitude, double longitude);
        void saveUserInfo(int iduser, String uid);
        void route(String latitude, String longitude);
    }

    static interface Interactor{

        void setHelpAsk(double latitude, double longitude);

        void refresh(double latitude, double longitude, double idUser);

        void confirmHelp(Long latitude, Long longitude, Long idUser);

        void confirmHelp(String idmendingo, String iduser);
    }
}
