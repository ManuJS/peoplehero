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
        void setHelpAsk(Long latitude, Long longitude);
        void showMessage(String messsage);
        void refresh(Long latitude, Long longitude,Long idUser);
        void updateHelpless(List<Helpless> help);
    }

    static interface Interactor{

        void setHelpAsk(Long latitude, Long longitude);

        void refresh(Long latitude, Long longitude,Long idUser);
    }
}
