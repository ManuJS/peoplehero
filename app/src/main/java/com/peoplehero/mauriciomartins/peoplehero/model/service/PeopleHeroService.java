package com.peoplehero.mauriciomartins.peoplehero.model.service;

import android.util.Log;

import com.peoplehero.mauriciomartins.peoplehero.model.domain.User;
import com.peoplehero.mauriciomartins.peoplehero.model.dto.HelpDTO;
import com.peoplehero.mauriciomartins.peoplehero.model.dto.HelplessListDTO;
import com.peoplehero.mauriciomartins.peoplehero.model.dto.UserDTO;
import com.peoplehero.mauriciomartins.peoplehero.model.service.api.PeopleHeroServiceInterfaceApi;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mauriciomartins on 16/07/17.
 */

public class PeopleHeroService {
    // Trailing slash is needed
    public static final String BASE_URL = "https://www.peoplehero.com.br/webservice/";
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private PeopleHeroServiceInterfaceApi service = retrofit.create(PeopleHeroServiceInterfaceApi.class);

    public Call<HelpDTO> setHelpAsk(Long latitude, Long longitude){
        Call<HelpDTO> repos = service.setHelpAsk(latitude, longitude);
        return repos;
    }

    public Call<HelplessListDTO> setHelp(Long latitude, Long longitude, Long idUser){
        Call<HelplessListDTO> repos = service.setHelp(latitude, longitude,idUser);
        return repos;
    }

    public Call<UserDTO> setLogin(Long uid, String nome, String email,String urlimage){
        Call<UserDTO> repos = service.setLogin(uid, nome, email,urlimage);
        return repos;
    }

    public Call<UserDTO> setLogin(User user){
        Log.i("Login","Üser"+user);
        Call<UserDTO> repos = service.setLoginBody(user);
        return repos;
    }
}
