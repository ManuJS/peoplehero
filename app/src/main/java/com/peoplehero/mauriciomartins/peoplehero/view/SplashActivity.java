package com.peoplehero.mauriciomartins.peoplehero.view;

import android.content.Intent;
import android.icu.util.TimeUnit;
import android.os.Bundle;
import android.os.Handler;

import com.peoplehero.mauriciomartins.peoplehero.R;

public class SplashActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showLoginActivity();
            }
        }, 1000);
    }
    private void showLoginActivity() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

}
