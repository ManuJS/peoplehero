package com.peoplehero.mauriciomartins.peoplehero.model.service.api;

import com.peoplehero.mauriciomartins.peoplehero.model.domain.Helpless;
import com.peoplehero.mauriciomartins.peoplehero.model.domain.User;
import com.peoplehero.mauriciomartins.peoplehero.model.dto.HelpDTO;
import com.peoplehero.mauriciomartins.peoplehero.model.dto.HelplessListDTO;
import com.peoplehero.mauriciomartins.peoplehero.model.dto.UserDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mauriciomartins on 16/07/17.
 */

public interface PeopleHeroServiceInterfaceApi {

    @Headers("Content-Type: application/json")
    @POST("setLogin.php")
    Call<UserDTO> setLogin(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("setHelp.php")
    Call<HelplessListDTO> setHelp(@Body Helpless helpless);

    @POST("setLocation.php")
    Call<ResponseBody> setLocation(@Query("latitude") Long latitude, @Query("longitude") Long longitude, @Query("iduser") Long iduser);

    @POST("setHelpAsk.php")
    Call<HelpDTO> setHelpAsk(@Query("latitude") double latitude, @Query("longitude") double longitude);

    @Headers("Content-Type: application/json")
    @POST("confirmHelp.php")
    Call<Helpless> setConfirmHelp(@Body Helpless helpless);
}
