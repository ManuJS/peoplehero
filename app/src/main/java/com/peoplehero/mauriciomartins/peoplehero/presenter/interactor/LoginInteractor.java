package com.peoplehero.mauriciomartins.peoplehero.presenter.interactor;

import com.peoplehero.mauriciomartins.peoplehero.contract.Login;
import com.peoplehero.mauriciomartins.peoplehero.presenter.LoginPresenter;


/**
 * Created by mauriciomartins on 15/07/17.
 */

public class LoginInteractor implements Login.Interactor {

    private final Login.Presenter presenter;

    public LoginInteractor(Login.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void login() {

    }
}
