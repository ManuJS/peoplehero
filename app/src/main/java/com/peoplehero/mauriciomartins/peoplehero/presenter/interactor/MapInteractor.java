package com.peoplehero.mauriciomartins.peoplehero.presenter.interactor;

import android.util.Log;

import com.peoplehero.mauriciomartins.peoplehero.contract.Login;
import com.peoplehero.mauriciomartins.peoplehero.contract.Map;
import com.peoplehero.mauriciomartins.peoplehero.model.service.PeopleHeroService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mauriciomartins on 15/07/17.
 */

public class MapInteractor implements Map.Interactor {

    private final Map.Presenter presenter;

    public MapInteractor(Map.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setHelpAsk(Long latitude, Long longitude) {
        PeopleHeroService peopleHeroService = new PeopleHeroService();
        Call<ResponseBody> repos = peopleHeroService.setHelpAsk(latitude,longitude);
        repos.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response!=null&&response.raw()!=null&& response.raw().code()==200){
                    Log.i("Retrofit Service","Sucesso");
                    presenter.showMessage("Retrofit Service - Sucesso");
                }
                else{
                    Log.i("Retrofit Service","Erro : "+response.raw().code());
                    presenter.showMessage("Retrofit Service - Erro : "+response.raw().code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Retrofit Service","Response:"+t.getMessage());
                presenter.showMessage("Retrofit Service - Response:"+t.getMessage());
            }
        });
    }

    @Override
    public void refresh(Long latitude, Long longitude) {
        PeopleHeroService peopleHeroService = new PeopleHeroService();
        Call<ResponseBody> repos = peopleHeroService.setHelpAsk(latitude,longitude);
        repos.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response!=null&&response.raw()!=null&& response.raw().code()==200){
                    Log.i("Retrofit Service","Sucesso");
                    presenter.showMessage("Retrofit Service - Sucesso");
                }
                else{
                    Log.i("Retrofit Service","Erro : "+response.raw().code());
                    presenter.showMessage("Retrofit Service - Erro : "+response.raw().code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Retrofit Service","Response:"+t.getMessage());
                presenter.showMessage("Retrofit Service - Response:"+t.getMessage());
            }
        });
    }
}
