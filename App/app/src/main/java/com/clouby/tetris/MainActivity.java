package com.clouby.tetris;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


public class MainActivity extends FragmentActivity {


    public interface OnBackPressedListener {
        void onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting theme from Settings
        switch(Settings.getInstance(this).getTheme()) {
            case Settings.LIGHT_THEME:
                setTheme(R.style.AppThemeLight);
                break;
            case Settings.DARK_THEME:
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
        Settings.getInstance(this).save();
    }

    @Override
    protected  void onResume(){
        super.onResume();
        //Incase phone gained connectivity while the app was in background, send score to the web app
        DatabaseHandler.getInstance(this).sendHighscoresToWebApp();
    }
}
