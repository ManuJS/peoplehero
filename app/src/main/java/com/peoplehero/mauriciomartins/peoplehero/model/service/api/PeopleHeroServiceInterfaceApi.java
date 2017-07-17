package com.peoplehero.mauriciomartins.peoplehero.model.service.api;

import com.peoplehero.mauriciomartins.peoplehero.model.dto.HelpDTO;
import com.peoplehero.mauriciomartins.peoplehero.model.dto.HelplessListDTO;
import com.peoplehero.mauriciomartins.peoplehero.model.dto.UserDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mauriciomartins on 16/07/17.
 */

public interface PeopleHeroServiceInterfaceApi {

    @POST("setLogin.php")
    Call<UserDTO> setLogin(@Query("uid") Long uid, @Query("nome") String nome, @Query("email") String email, @Query("urlimage") String urlimage);

    @POST("setHelp.php")
    Call<HelplessListDTO> setHelp(@Query("latitude") Long latitude, @Query("longitude") Long longitude, @Query("iduser") Long iduser);

    @POST("setLocation.php")
    Call<ResponseBody> setLocation(@Query("latitude") Long latitude, @Query("longitude") Long longitude, @Query("iduser") Long iduser);

    @POST("setUser.php")
    Call<HelpDTO> setHelpAsk(@Query("latitude") Long latitude, @Query("longitude") Long longitude);



}
