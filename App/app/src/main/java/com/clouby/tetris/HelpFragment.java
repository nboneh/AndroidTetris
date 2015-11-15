package com.clouby.tetris;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by nboneh on 11/15/2015.
 */
public class HelpFragment extends Fragment implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_help, container, false);
        ((Button)v.findViewById(R.id.back_button)).setOnClickListener(this);
        return v;
    }

    @Override
    public  void onClick(View v){
        switch(v.getId()){
            case R.id.back_button:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }
}
