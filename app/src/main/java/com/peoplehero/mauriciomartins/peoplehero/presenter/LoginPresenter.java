package com.peoplehero.mauriciomartins.peoplehero.presenter;


import android.util.Log;

import com.peoplehero.mauriciomartins.peoplehero.contract.Login;
import com.peoplehero.mauriciomartins.peoplehero.model.service.PeopleHeroService;
import com.peoplehero.mauriciomartins.peoplehero.presenter.interactor.LoginInteractor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    public void login() {
        this.interactor.login();

    }
}
