package com.peoplehero.mauriciomartins.peoplehero.view;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.peoplehero.mauriciomartins.peoplehero.R;
import com.peoplehero.mauriciomartins.peoplehero.contract.Login;
import com.peoplehero.mauriciomartins.peoplehero.model.dto.UserDTO;
import com.peoplehero.mauriciomartins.peoplehero.presenter.LoginPresenter;

import com.facebook.FacebookSdk;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.peoplehero.mauriciomartins.peoplehero.view.MapActivity.UID;

public class LoginActivity extends AbstractActivity implements Login.View {
    private CallbackManager callbackManager;

    private static final String PREFS = "PEOPLE_HERO";
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private Login.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login_activity);
        this.presenter = new LoginPresenter(this);
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                final String url = profile.getProfilePictureUri(200,200).toString();
                final GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String id    = object.getString("id");
                                    String name  = object.getString("name");
                                    String email = object.getString("email");
                                    presenter.login(id, name, email, url);
                               } catch (JSONException error) {
                                    showMessage("onError()"+error.getMessage());
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                showMessage("Login cancelado");
            }

            @Override
            public void onError(FacebookException error) {
                showMessage("onError()"+error.getMessage());
            }
        });

    }

    public void fbClick(View view){
        this.showProgress(true);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
    }

    @Override
    public void showMessage(String  message){
        this.showProgress(false);
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void connectToFacebook(UserDTO user){
        this.showProgress(false);
        Log.i("LoginActivity","Usuário cadastrado com sucesso!!!"+user);
        Intent intent = new Intent(this,MapActivity.class);
        final Bundle params = new Bundle();
        intent.putExtra(MapActivity.IDUSER,Integer.valueOf(user.getIduser()));
        intent.putExtra(UID,user.getUid());
        startActivity(intent);
        this.finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
