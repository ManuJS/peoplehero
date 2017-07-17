package com.peoplehero.mauriciomartins.peoplehero.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.peoplehero.mauriciomartins.peoplehero.R;
import com.peoplehero.mauriciomartins.peoplehero.contract.Map;
import com.peoplehero.mauriciomartins.peoplehero.model.domain.Helpless;
import com.peoplehero.mauriciomartins.peoplehero.presenter.MapPresenter;

import java.util.List;

public class MapActivity extends AbstractActivity  implements  Map.View{
    private Map.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.map_activity);
        this.presenter = new MapPresenter(this);
        super.onCreate(savedInstanceState);
    }


    public void logOutClick(View view){
        this.finish();
    }

    public void refreshClick(View view){
        this.showProgress(true);
        this.presenter.refresh(123456L, 654321L,12L);
    }

    public void helpClick(View view){
        this.showProgress(true);
        this.presenter.setHelpAsk(123456L, 654321L);
    }


    @Override
    public void updateHelpless(List<Helpless> helpList) {
        if(helpList!=null){
            for(Helpless help:helpList){
                Log.i("Helpless List","Helpless"+help);
            }
        }
        this.showProgress(false);
    }
}
