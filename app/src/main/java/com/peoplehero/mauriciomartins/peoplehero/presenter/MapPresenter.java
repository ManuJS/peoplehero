package com.peoplehero.mauriciomartins.peoplehero.presenter;


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
    public MapPresenter(Map.View view){
        this.view = view;
        this.interactor = new MapInteractor(this);
    }
    @Override
    public void setHelpAsk(Long latitude, Long longitude) {
        this.interactor.setHelpAsk(latitude,longitude);

    }

    @Override
    public void showMessage(String messsage) {
        this.view.showMessage(messsage);
    }

    @Override
    public void refresh(Long latitude, Long longitude,Long idUser) {
        this.interactor.refresh(latitude,longitude,idUser);
    }

    @Override
    public void updateHelpless(List<Helpless> help) {
        this.view.updateHelpless(help);
    }
}
