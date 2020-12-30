package com.example.alzheimersapp.PatientModule.GameModule;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import com.example.alzheimersapp.CaregiverModule.CaregiverDashboard;
import com.example.alzheimersapp.PatientModule.PatientDashboard;
import com.example.alzheimersapp.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PlayGames extends AppCompatActivity {

    GridLayout gridLayout;

    @Override
            protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_play_games);

            gridLayout = findViewById(R.id.mainGrid);

            //Set Event
            setSingleEvent(gridLayout);
    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(finalI == 0) {
                        String url = "https://www.brightfocus.org/alzheimers/memory-games/match-pictures";
                        goToGame(url);
                    }

                    else if(finalI == 1) {
                        String url = "https://www.brightfocus.org/alzheimers/memory-games/universal-crossword-puzzle";
                        goToGame(url);
                    }

                    else if(finalI == 2) {
                        String url = "https://www.brightfocus.org/alzheimers/memory-games/match-words";
                        goToGame(url);
                    }
                    else if(finalI == 3) {
                        String url = "https://www.brightfocus.org/alzheimers/memory-games/sudoku-daily";
                        goToGame(url);
                    }

                }
            });
        }
    }


    public void goToGame(String url){
        Intent goToGame = new Intent(Intent.ACTION_VIEW);
        goToGame.setData(Uri.parse(url));
        startActivity(goToGame);
    }

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(this, PatientDashboard.class));
//        finish();
//    }

}