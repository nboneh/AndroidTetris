package com.clouby.tetris;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting theme from Settings
        switch(Settings.getInst(this).getTheme()) {
            case 0:
                setTheme(R.style.AppThemeLight);
                break;
            case 1:
                setTheme(R.style.AppThemeDark);
                break;
        }

        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction =getSupportFragmentManager()
                .beginTransaction();
        Fragment fragment = new MainMenuFragment();
        fragmentTransaction
                .replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPause(){
         super.onPause();
        Settings.getInst(this).save();
    }
}
