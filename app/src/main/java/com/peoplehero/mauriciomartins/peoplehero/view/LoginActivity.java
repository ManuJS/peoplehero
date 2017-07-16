package com.peoplehero.mauriciomartins.peoplehero.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.peoplehero.mauriciomartins.peoplehero.R;
import com.peoplehero.mauriciomartins.peoplehero.contract.Login;
import com.peoplehero.mauriciomartins.peoplehero.presenter.LoginPresenter;

public class LoginActivity extends AbstractActivity implements Login.View {
    private Login.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        this.presenter = new LoginPresenter(this);
    }

    public void fbClick(View view){
//        this.presenter.setHelpAsk();
        this.connectToFacebook();
    }

    @Override
    public void showMessage(String  message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void connectToFacebook(){
        Intent intent = new Intent(this,MapActivity.class);
        startActivity(intent);
        this.finish();
    }
}
