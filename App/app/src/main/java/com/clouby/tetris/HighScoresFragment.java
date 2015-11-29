package com.clouby.tetris;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by nboneh on 11/1/2015.
 */
public class HighScoresFragment extends Fragment implements View.OnClickListener {

    private LinearLayout localHighScoresView;
    private LinearLayout onlineHighScoresView;
    private DatabaseHandler db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db =  DatabaseHandler.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_highscores, container, false);
        ((Button) v.findViewById(R.id.delete_local_high_scores)).setOnClickListener(this);

        localHighScoresView = (LinearLayout) v.findViewById(R.id.local_high_scores);
        printHighScores(localHighScoresView,db.getLocalHighScores());

        onlineHighScoresView = (LinearLayout) v.findViewById(R.id.online_high_scores);



        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        //Incase user regained internet or online highscores updates while in background
        //Sending an observer to the database function since networking must happen outside the main thread
        Handler onlineScoreHandler = new Handler(getActivity().getMainLooper()){
            public void handleMessage(Message msg) {
                //Checks if fragment is still attached
                if(isAdded()) {
                    printHighScores(onlineHighScoresView, (List<HighScoreContainer>) msg.obj);
                }
            }
        };

        if(!db.getOnlineHighScores(onlineScoreHandler)){
            Toast.makeText(getActivity(), "Phone is not connected to the internet!", Toast.LENGTH_LONG).show();
        }



    }

    private void printHighScores(LinearLayout highscoresview,List<HighScoreContainer> highscoreList){
        highscoresview.removeAllViews();
        for(int i = 0; i < highscoreList.size(); i++){
            HighScoreContainer container = highscoreList.get(i);
            RelativeLayout entryView = new RelativeLayout(getActivity());

            int sizeInDP = 10;

            int marginInDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, sizeInDP, getResources()
                            .getDisplayMetrics());

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, marginInDp , 0, 0);
            entryView.setLayoutParams(lp);

            TextView aliasView= new TextView(getActivity());
            aliasView.setText((i + 1) + "." + container.getAlias());
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                     RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            aliasView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
            aliasView.setLayoutParams(rlp);

            TextView scoreView= new TextView(getActivity());
            scoreView.setText(container.getScore() + "");
            rlp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            scoreView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
           scoreView.setLayoutParams(rlp);

            entryView.addView(aliasView);
            entryView.addView(scoreView);
            highscoresview.addView(entryView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_local_high_scores:
                final Context context = getActivity();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete local highscores");
                builder.setMessage("Are you sure? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.clearLocalHighScores();
                                printHighScores(localHighScoresView,db.getLocalHighScores());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();
                break;
        }
    }

}
