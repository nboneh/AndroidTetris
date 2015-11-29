package com.clouby.tetris;

/**
 * Created by nboneh on 11/28/2015.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameOverFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gameover, container, false);
        ((Button) v.findViewById(R.id.main_menu_button)).setOnClickListener(this);
        ((Button) v.findViewById(R.id.new_game_button)).setOnClickListener(this);
        ((Button) v.findViewById(R.id.highscores_button)).setOnClickListener(this);

        int score = getArguments().getInt("score");
        ((TextView) v.findViewById(R.id.score_text)).append(score + "");
        checkHighscores(score);
        return v;
    }

    private void checkHighscores(final int score) {
        final DatabaseHandler db = DatabaseHandler.getInstance(getActivity());
        final Settings settings = Settings.getInstance(getActivity());
        if (!db.madeLocalHighscores(score))
            return;

        final EditText aliastext = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        //Setting max length for alias
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(10);
        aliastext.setFilters(filterArray);
        aliastext.setLayoutParams(lp);
        aliastext.setMaxLines(1);
        aliastext.setSingleLine(true);

        aliastext.setText(settings.getAlias());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You made the local highscores please enter your name: ")
                .setTitle("Congratulations")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String alias = aliastext.getText().toString();
                        db.insetScore(score, aliastext.getText().toString());
                        db.sendHighscoresToWebApp();
                        settings.setAlias(alias);
                    }
                })
                .setCancelable(false);
        // Create the AlertDialog object and return it
        AlertDialog alert = builder.create();
        alert.setView(aliastext);
        alert.show();
    }

    @Override
    public void onClick(View v) {
        //Popping off to main menu
        getActivity().getSupportFragmentManager().popBackStack();

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.main_menu_button:
                return;
            case R.id.highscores_button:
                fragment = new HighScoresFragment();
                break;
            case R.id.new_game_button:
                fragment = new GameFragment();
                break;
        }
        fragmentTransaction
                .replace(R.id.fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}