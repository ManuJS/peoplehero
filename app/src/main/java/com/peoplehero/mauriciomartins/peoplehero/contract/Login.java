package com.peoplehero.mauriciomartins.peoplehero.contract;

/**
 * Created by mauriciomartins on 15/07/17.
 */

public interface Login {
    static interface View{

        void showMessage(String message);
    }

    static interface Presenter{
        void login();
    }

    static interface Interactor{

        void login();
    }
}
