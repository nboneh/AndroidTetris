package com.clouby.tetris;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

/**
 * Created by nboneh on 11/15/2015.
 */
public class SettingsFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, Spinner.OnItemSelectedListener {

    private Settings settings;
    private Spinner themeSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = Settings.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        SeekBar musicBar = ((SeekBar) v.findViewById((R.id.music_seekbar)));
        musicBar.setProgress((int) (musicBar.getMax() * settings.getMusicVolume()));
        musicBar.setOnSeekBarChangeListener(this);

        SeekBar soundBar = ((SeekBar) v.findViewById((R.id.sound_seekbar)));
        soundBar.setProgress((int) (soundBar.getMax() * settings.getSoundVolume()));
        soundBar.setOnSeekBarChangeListener(this);

        themeSpinner = ((Spinner) v.findViewById(R.id.theme_spinner));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.themes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapter);

        themeSpinner.setSelection(settings.getTheme());

        themeSpinner.setOnItemSelectedListener(this);


        return v;
    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.music_seekbar:
                settings.setMusicVolume(progress / (float) (seekBar.getMax()));
                break;
            case R.id.sound_seekbar:
                settings.setSoundVolume(progress / (float) (seekBar.getMax()));
                break;
        }

    }

    public void onItemSelected(AdapterView<?> parent, final View view,
                               final int pos, long id) {
        if (settings.getTheme() == pos)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You will need to restart the App for the theme to change ")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Restarting the app and changing to the new theme
                        settings.setTheme(pos);

                        settings.save();

                        Context context = getActivity();
                        Intent mStartActivity = new Intent(context, MainActivity.class);
                        int mPendingIntentId = 123456;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        themeSpinner.setSelection(settings.getTheme());
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }
}

