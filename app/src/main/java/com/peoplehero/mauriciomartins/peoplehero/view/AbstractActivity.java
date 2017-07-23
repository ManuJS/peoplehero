package com.peoplehero.mauriciomartins.peoplehero.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.peoplehero.mauriciomartins.peoplehero.R;

public class AbstractActivity extends FragmentActivity {
    private ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        if(progressBar!=null) {
            progressBar.setIndeterminate(true);
        }
    }

    public void showMessage(String message) {
        if(progressBar!=null) {
            this.progressBar.setVisibility(View.GONE);
        }
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void showProgress(boolean visible){
        if(progressBar!=null) {
            if (visible) {
                this.progressBar.setVisibility(View.VISIBLE);
            } else {
                this.progressBar.setVisibility(View.GONE);
            }
        }
    }

    public Context getContext() {
        return this;
    }

    public boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }
}
