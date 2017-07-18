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

import com.peoplehero.mauriciomartins.peoplehero.R;
import com.peoplehero.mauriciomartins.peoplehero.contract.Login;
import com.peoplehero.mauriciomartins.peoplehero.model.dto.UserDTO;
import com.peoplehero.mauriciomartins.peoplehero.presenter.LoginPresenter;

import java.util.UUID;

public class LoginActivity extends AbstractActivity implements Login.View {
    private static final String PREFS = "PEOPLE_HERO";
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private Login.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login_activity);
        this.presenter = new LoginPresenter(this);
        super.onCreate(savedInstanceState);
    }

    public void fbClick(View view){
        this.showProgress(true);
        final String UID =  this.getUserID(this);
        this.presenter.login(UID,"MSM","mrs@gmail.com","http://www.peoplehero.com.br/mendigo.png");
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
        intent.putExtra(MapActivity.UID,user.getUid());
        startActivity(intent);
        this.finish();
    }


    public  String getUserID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        uniqueID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);//telephonyManager.getDeviceId();
//        if (uniqueID == null) {
//            SharedPreferences sharedPrefs = context.getSharedPreferences(
//                    LoginActivity.PREFS, Context.MODE_PRIVATE);
//            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
//            if (uniqueID == null) {
//                uniqueID = UUID.randomUUID().toString();
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putString(PREF_UNIQUE_ID, uniqueID);
//                editor.commit();
//
//                //backup the changes
//                BackupManager mBackupManager = new BackupManager(context);
//                mBackupManager.dataChanged();
//            }
//        }

        return uniqueID;
    }
}
