package com.peoplehero.mauriciomartins.peoplehero.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.peoplehero.mauriciomartins.peoplehero.R;
import com.peoplehero.mauriciomartins.peoplehero.contract.Map;
import com.peoplehero.mauriciomartins.peoplehero.presenter.MapPresenter;

public class MapActivity extends AbstractActivity  implements  Map.View{
    private Map.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        this.presenter = new MapPresenter(this);
    }


    public void logOutClick(View view){
        this.finish();
    }

    public void refreshClick(View view){
        this.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        this.presenter.refresh(123456L, 654321L);
    }

    public void helpClick(View view){
        this.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        this.presenter.setHelpAsk(123456L, 654321L);
    }



    @Override
    public void showMessage(String message) {
        this.findViewById(R.id.progressBar).setVisibility(View.GONE);
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
