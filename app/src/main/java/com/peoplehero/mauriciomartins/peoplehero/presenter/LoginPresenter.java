package com.peoplehero.mauriciomartins.peoplehero.presenter;


import com.peoplehero.mauriciomartins.peoplehero.contract.Login;
import com.peoplehero.mauriciomartins.peoplehero.model.dto.UserDTO;
import com.peoplehero.mauriciomartins.peoplehero.presenter.interactor.LoginInteractor;

/**
 * Created by mauriciomartins on 15/07/17.
 */

public class LoginPresenter implements Login.Presenter {
    private final Login.Interactor interactor;
    private Login.View view;
    public LoginPresenter(Login.View view){
        this.view = view;
        this.interactor = new LoginInteractor(this);
    }


    @Override
    public void login(String uid, String nome, String email, String urlimage) {
        this.interactor.login(uid, nome, email, urlimage);
    }

    @Override
    public void showMessage(String message) {
        this.view.showMessage(message);
    }

    @Override
    public void setUser(UserDTO user) {
        this.view.connectToFacebook(user);
    }
}
