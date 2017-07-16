package com.peoplehero.mauriciomartins.peoplehero.model.service;

import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;

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

    private PeopleHeroServiceInterface service = retrofit.create(PeopleHeroServiceInterface.class);

    public Call<ResponseBody> setHelpAsk(Long latitude, Long longitude){
        Call<ResponseBody> repos = service.setHelpAsk(latitude, longitude);
        return repos;

    }
}
