package com.peoplehero.mauriciomartins.peoplehero.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.peoplehero.mauriciomartins.peoplehero.R;
import com.peoplehero.mauriciomartins.peoplehero.contract.Login;
import com.peoplehero.mauriciomartins.peoplehero.model.domain.User;
import com.peoplehero.mauriciomartins.peoplehero.presenter.LoginPresenter;

public class LoginActivity extends AbstractActivity implements Login.View {
    private Login.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login_activity);
        this.presenter = new LoginPresenter(this);
        super.onCreate(savedInstanceState);
    }

    public void fbClick(View view){
        this.showProgress(true);
        this.presenter.login(12345L,"MSM","mrs@gmail.com","http://www.peoplehero.com.br/mendigo.png");
    }

    @Override
    public void showMessage(String  message){
        this.showProgress(false);
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void connectToFacebook(User user){
        this.showProgress(false);
        this.showMessage("Usuário cadastrado com sucesso!!!"+user);
        Intent intent = new Intent(this,MapActivity.class);
        startActivity(intent);
        this.finish();
    }
}
