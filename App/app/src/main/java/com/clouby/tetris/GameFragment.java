package com.clouby.tetris;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.widget.ImageView;

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

        return v;
    }


    @Override
    public  void onClick(View v){
        switch(v.getId()){
            case R.id.pause_button:
                //TODO change later
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }


}
