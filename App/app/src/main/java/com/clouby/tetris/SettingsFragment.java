package com.clouby.tetris;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

/**
 * Created by nboneh on 11/15/2015.
 */
public class SettingsFragment  extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Settings settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = Settings.getInst(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        ((Button)v.findViewById(R.id.back_button)).setOnClickListener(this);

        SeekBar musicBar = ((SeekBar)v.findViewById((R.id.music_seekbar)));
        musicBar.setProgress((int)(musicBar.getMax() * settings.getMusicVolume()));
        musicBar.setOnSeekBarChangeListener(this);

        SeekBar soundBar = ((SeekBar)v.findViewById((R.id.sound_seekbar)));
        soundBar.setProgress((int)(soundBar.getMax() * settings.getSoundVolume()));
        soundBar.setOnSeekBarChangeListener(this);

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

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
        switch(seekBar.getId()){
            case R.id.music_seekbar:
                settings.setMusicVolume(progress/(float)(seekBar.getMax()));
                break;
            case R.id.sound_seekbar:
                settings.setSoundVolume(progress / (float) (seekBar.getMax()));
                break;
        }

    }
}

