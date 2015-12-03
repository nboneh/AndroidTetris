package com.clouby.tetris;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clouby.tetris.game.GameView;

import java.util.Random;
/**
 * Created by Shirong on 11/19/2015.
 */
public class GameFragment extends Fragment implements View.OnClickListener{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        //pause button
        (v.findViewById(R.id.pause_button)).setOnClickListener(this);
        //left button
        (v.findViewById(R.id.left_button)).setOnClickListener(this);
        //right button
        (v.findViewById(R.id.right_button)).setOnClickListener(this);
        //down button
        (v.findViewById(R.id.down_button)).setOnClickListener(this);
        //transform
        (v.findViewById(R.id.transform_button)).setOnClickListener(this);

        return v;
    }


    @Override
    public  void onClick(View v){
        switch(v.getId()){
            case R.id.pause_button:
                //TODO change later
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.left_button:
                ((GameView)(getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().moveLeft();
                break;
            case R.id.right_button:
                ((GameView)(getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().moveRight();
                break;
            case R.id.down_button:
                ((GameView)(getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().moveDown();
                break;
            case R.id.transform_button:
                ((GameView)(getActivity().findViewById(R.id.game_surfaceView))).getGamePanel().turnNext();
                break;

        }
    }

    private void gameOver() {
        //Popping up to main menu screen to
        getActivity().getSupportFragmentManager().popBackStack();

        //Transition to game over fragment
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
