package com.peoplehero.mauriciomartins.peoplehero.presenter;


import com.peoplehero.mauriciomartins.peoplehero.ViewModel.MapModelView;
import com.peoplehero.mauriciomartins.peoplehero.contract.Map;
import com.peoplehero.mauriciomartins.peoplehero.model.domain.Helpless;
import com.peoplehero.mauriciomartins.peoplehero.presenter.interactor.MapInteractor;

import java.util.List;

/**
 * Created by mauriciomartins on 15/07/17.
 */

public class MapPresenter implements Map.Presenter {
    private final Map.Interactor interactor;
    private Map.View view;
    private double latitude;
    private double longitude;
    private int iduser;
    private String uid;

    public MapPresenter(Map.View view){
        this.view = view;
        this.interactor = new MapInteractor(this);
    }
    @Override
    public void setHelpAsk() {
        this.interactor.setHelpAsk(latitude,longitude);

    }

    @Override
    public void showMessage(String messsage) {
        this.view.showMessage(messsage);
    }

    @Override
    public void refresh() {
        this.interactor.refresh(latitude,longitude,iduser);
    }

    @Override
    public void updateHelpless(List<Helpless> helplessList) {
        this.view.updateHelpless(new MapModelView(this.view.getContext(),helplessList));
    }


    @Override
    public void confirmHelp(String idmendingo) {
        this.view.showProgress(true);
        this.interactor.confirmHelp(idmendingo, String.valueOf(iduser));
    }

    @Override
    public void saveLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void saveUserInfo(int iduser, String uid) {
        this.iduser = iduser;
        this.uid    = uid;
    }
}
