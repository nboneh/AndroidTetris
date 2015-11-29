package com.clouby.tetris;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.FragmentTransaction;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener {

    public MainMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ((Button)v.findViewById(R.id.play_button)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.highscores_button)).setOnClickListener(this);
        ((Button) v.findViewById(R.id.help_button)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.settings_button)).setOnClickListener(this);
        return v;
    }

    @Override
    public  void onClick(View v){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        Fragment fragment = null;
        switch(v.getId()){
            case R.id.play_button:
                fragment = new GameFragment();
                break;
            case R.id.highscores_button:
                fragment = new HighScoresFragment();
                break;
            case R.id.help_button:
                fragment = new HelpFragment();
                break;
            case R.id.settings_button:
                fragment = new SettingsFragment();
                break;
        }
        fragmentTransaction
                .replace(R.id.fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
