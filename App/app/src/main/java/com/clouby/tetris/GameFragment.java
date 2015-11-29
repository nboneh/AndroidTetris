package com.clouby.tetris;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

/**
 * Created by nboneh on 11/15/2015.
 */
public class GameFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        gameOver();
        return v;
    }

    private void gameOver() {
        //Popping up to main menu screen to
        getActivity().getSupportFragmentManager().popBackStack();

        //Transation to game over fragment
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();

        Random rand = new Random();
        Fragment fragment = new GameOverFragment();
        Bundle args = new Bundle();
        args.putInt("score", rand.nextInt(100));
        fragment.setArguments(args);
        fragmentTransaction
                .replace(R.id.fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
