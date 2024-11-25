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

    private final Handler handler = new Handler();
    private ProgressBar progressBar;
    private int elapsedTime = 0; // เก็บเวลาที่ผ่านไป
    private int maxTime = 0;
    private boolean isPaused = false;

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
        progressBar.setMax(100);

        setTimer("seer");


    }

    // set timer
    protected void setTimer(String timeType) {

        // ตั้งค่า maxTime ตาม timeType
        if (timeType.equals("villager")) {
            maxTime = 3 * 60; // 3 นาที
        } else if (timeType.equals("wolf")) {
            maxTime = 1 * 60; // 1 นาที
        } else if (timeType.equals("seer")) {
            maxTime = 30; // 30 วินาที
        } else {
            Log.e("setTimer", "Unknown timeType: " + timeType);
            return;
        }
        elapsedTime = 0; // รีเซ็ตเวลาเริ่มต้น
        startTimer();

    }

    private void startTimer() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isPaused) return;

                int progress = (int) (((double) elapsedTime / maxTime) * 100);
                progressBar.setProgress(progress);

                if (elapsedTime >= maxTime) {
                    handler.removeCallbacks(this);
                } else {
                    elapsedTime++;
                    handler.postDelayed(this, 1000); // เรียกใหม่หลังจาก 1 วินาที
                }
            }
        }, 1000);
    }

    private void pauseTimer() {
        isPaused = true;
    }

    private void resumeTimer() {
        isPaused = false;
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeTimer();
    }

}
