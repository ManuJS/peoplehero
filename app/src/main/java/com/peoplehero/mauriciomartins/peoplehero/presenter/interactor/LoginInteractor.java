package com.peoplehero.mauriciomartins.peoplehero.presenter.interactor;

import android.util.Log;

import com.peoplehero.mauriciomartins.peoplehero.contract.Login;
import com.peoplehero.mauriciomartins.peoplehero.model.domain.User;
import com.peoplehero.mauriciomartins.peoplehero.model.dto.UserDTO;
import com.peoplehero.mauriciomartins.peoplehero.model.service.PeopleHeroService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mauriciomartins on 15/07/17.
 */

public class LoginInteractor implements Login.Interactor {

    private final Login.Presenter presenter;

    public LoginInteractor(Login.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void login(String uid, String nome, String email, String urlimage) {
        PeopleHeroService peopleHeroService = new PeopleHeroService();
        User user = new User();
        user.setUid(uid);
        user.setEmail(email);
        user.setNome(nome);
        user.setUrlimage(urlimage);
        Call<UserDTO> repos = peopleHeroService.setLogin(user);

        repos.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {

                if(response!=null&&response.raw()!=null&& response.raw().code()==200){
                    UserDTO user = response.body();
                    presenter.setUser(user);
                }
                else{
                    Log.i("Retrofit Service","Erro : "+response.raw().code());
                    presenter.showMessage("Retrofit Service - Erro : "+response.raw().code());
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Log.i("Retrofit Service","Response:"+t.getMessage());
                presenter.showMessage("Retrofit Service - Response:"+t.getMessage());
            }
        });
    }
}
