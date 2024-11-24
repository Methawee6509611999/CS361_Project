package com.example.warewolf;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.voting_page);

        //UI get
        CardView cardP1 = findViewById(R.id.p1);
        CardView cardP2 = findViewById(R.id.p2);
        CardView cardP3 = findViewById(R.id.p3);
        CardView cardP4 = findViewById(R.id.p4);
        CardView cardP5 = findViewById(R.id.p5);
        CardView cardP6 = findViewById(R.id.p6);
        CardView cardP7 = findViewById(R.id.p7);
        CardView cardP8 = findViewById(R.id.p8);
        CardView cardP9 = findViewById(R.id.p9);

        TextView textP1 = findViewById(R.id.textViewP1);
        TextView textP2 = findViewById(R.id.textViewP2);
        TextView textP3 = findViewById(R.id.textViewP3);
        TextView textP4 = findViewById(R.id.textViewP4);
        TextView textP5 = findViewById(R.id.textViewP5);
        TextView textP6 = findViewById(R.id.textViewP6);
        TextView textP7 = findViewById(R.id.textViewP7);
        TextView textP8 = findViewById(R.id.textViewP8);
        TextView textP9 = findViewById(R.id.textViewP9);

        ArrayList<Player> PlayState = getIntent().getParcelableArrayListExtra("players");

        // List of TextViews for the player names
        List<TextView> textViews = Arrays.asList(
                textP1, textP2, textP3, textP4, textP5, textP6, textP7, textP8, textP9
        );

        // Check if PlayState is not null
        if (PlayState != null) {
            for (int i = 0; i < PlayState.size() && i < textViews.size(); i++) {
                Player player = PlayState.get(i); // Get the player from the list
                TextView textView = textViews.get(i); // Get the corresponding TextView
                textView.setText(player.getName()); // Set the player's name
            }
        } else {
            Log.e("PlayState", "No players received from Intent!");
        }

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);



    }

    private final Handler handler = new Handler();
    protected void setTimer(String timeType) {

        final int delay = 1000;
        final int[] counter = {1};

        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        int progress = counter[0] * 20;
                        progressBar.setProgress(progress);

                        if(progress == 100) {

                        }

                        counter[0]++;
                        handler.postDelayed(this, delay);
                    }
                }, delay
        );
    }
}
