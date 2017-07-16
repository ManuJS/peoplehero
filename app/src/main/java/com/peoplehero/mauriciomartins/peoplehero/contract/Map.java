package com.peoplehero.mauriciomartins.peoplehero.contract;

/**
 * Created by mauriciomartins on 15/07/17.
 */

public interface Map {
    static interface View{

        void showMessage(String message);
    }

    static interface Presenter{
        void setHelpAsk(Long latitude, Long longitude);
        void showMessage(String messsage);
        void refresh(Long latitude, Long longitude);
    }

    static interface Interactor{

        void setHelpAsk(Long latitude, Long longitude);

        void refresh(Long latitude, Long longitude);
    }
}
