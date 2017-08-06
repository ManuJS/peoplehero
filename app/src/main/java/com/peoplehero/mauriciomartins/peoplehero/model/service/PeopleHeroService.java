package com.peoplehero.mauriciomartins.peoplehero.model.service;

import com.peoplehero.mauriciomartins.peoplehero.model.domain.Helpless;
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

    public Call<HelpDTO> setHelpAsk(Long idUser, double latitude, double longitude){
        PeopleHeroService peopleHeroService = new PeopleHeroService();
        User user = new User();
        user.setIduser(String.valueOf(idUser));
        user.setLatitude(String.valueOf(latitude));
        user.setLongitude(String.valueOf(longitude));
        Call<HelpDTO> repos = service.setHelpAsk(user);
        return repos;
    }

    public Call<HelplessListDTO> setHelp(double latitude, double longitude, double idUser){
        final Helpless helpless = new Helpless();
        helpless.setIduser(String.valueOf(idUser));
        helpless.setLatitude(String.valueOf(latitude));
        helpless.setLongitude(String.valueOf(longitude));
        Call<HelplessListDTO> repos = service.setHelp(helpless);
        return repos;
    }

    public Call<UserDTO> setLogin(User user){
        Call<UserDTO> repos = service.setLogin(user);
        return repos;
    }

    public Call<Helpless> confirmHelp(Helpless helpless){
        Call<Helpless> repos = service.setConfirmHelp(helpless);
        return repos;
    }
}
