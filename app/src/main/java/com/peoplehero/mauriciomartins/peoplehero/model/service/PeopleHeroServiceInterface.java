package com.peoplehero.mauriciomartins.peoplehero.model.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by mauriciomartins on 16/07/17.
 */

public interface PeopleHeroServiceInterface {

    @POST("setHelpAsk.php")
    Call<ResponseBody> setHelpAsk(@Header("latitude") Long latitude, @Header("longitude") Long longitude);
}
