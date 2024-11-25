package com.example.warewolf;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
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

    // Class-level variables
    private int[] skipped = {0};
    private int[] voted;
    private int voteCount = 0;
    private List<CardView> cardViews; // Store CardViews if you need to reference them dynamically
    private ArrayList<Player> playState; // List of players for reference
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.voting_page);

        // UI references
        Button skipButton = findViewById(R.id.skipButton);

        // Initialize CardViews and player list
        cardViews = Arrays.asList(
                findViewById(R.id.p1), findViewById(R.id.p2), findViewById(R.id.p3),
                findViewById(R.id.p4), findViewById(R.id.p5), findViewById(R.id.p6),
                findViewById(R.id.p7), findViewById(R.id.p8), findViewById(R.id.p9)
        );

        playState = getIntent().getParcelableArrayListExtra("players");
        time = getIntent().getStringExtra("time");

        // Initialize vote tracking
        voted = new int[cardViews.size()];
        assert time != null;

        //set name on page
        for (int i = 0; i < playState.size(); i++) {
            Player player = playState.get(i);
            TextView playerTextView = findViewById(getResources().getIdentifier(
                    "textViewP" + (i + 1), "id", getPackageName())
            );
            playerTextView.setText(player.getName());
        }

        if(time.equals("night")){

            // Set up skip button listener
            skipButton.setOnClickListener(v -> {
                skipped[0]++;
                voteCount++;
                skipButton.setText("skip(" + skipped[0] + ')');
            });
            if (skipped[0]>playState.size()){
                Intent intent = new Intent(GameActivity.this, DeathActivity.class);
                intent.putExtra("time","day");
                intent.putExtra("votedName","none");
                startActivity(intent);
            }
            // Set up click listeners for the cards
            for (int i = 0; i < playState.size(); i++) {
                int index = i; // Capture the current index
                CardView cardView = cardViews.get(i);

                cardView.setOnClickListener(v -> {
                    voted[index]++;
                    voteCount++;

                    Log.d("Voting", playState.get(index).getName() + " has been voted " + voted[index] + " times!");


                    // Check if all players have voted
                    if (hasEveryoneVoted()) {
                        Player mostVotedPlayer = getMostVotedPlayer();
                        if (mostVotedPlayer != null) {

                            mostVotedPlayer.Die();
                            Log.d("Result", "Most voted player is: " + mostVotedPlayer.getName());
                        }
                        Intent intent = new Intent(GameActivity.this, DeathActivity.class);
                        intent.putExtra("time","day");
                        intent.putExtra("votedName",mostVotedPlayer.getName());
                        intent.putExtra("votedRole",mostVotedPlayer.getRole());
                        startActivity(intent);
                    }
                });
            }


        }
        else if (playState != null && time.equals("day")) {

            // Set up skip button listener
            skipButton.setOnClickListener(v -> {
                skipped[0]++;
                voteCount++;
                skipButton.setText("skip(" + skipped[0] + ')');
            });
            if (skipped[0]>playState.size()/2){
                Intent intent = new Intent(GameActivity.this, DeathActivity.class);
                intent.putExtra("time","day");
                intent.putExtra("votedName","none");
                startActivity(intent);
            }

            // Set up click listeners for the cards
            for (int i = 0; i < playState.size(); i++) {
                int index = i; // Capture the current index
                CardView cardView = cardViews.get(i);

                cardView.setOnClickListener(v -> {
                    voted[index]++;
                    voteCount++;

                    Log.d("Voting", playState.get(index).getName() + " has been voted " + voted[index] + " times!");


                    // Check if all players have voted
                    if (hasEveryoneVoted()) {
                        Player mostVotedPlayer = getMostVotedPlayer();
                        if (mostVotedPlayer != null) {

                            mostVotedPlayer.Die();
                            Log.d("Result", "Most voted player is: " + mostVotedPlayer.getName());
                        }
                        Intent intent = new Intent(GameActivity.this, DeathActivity.class);
                        intent.putExtra("time","day");
                        intent.putExtra("votedName",mostVotedPlayer.getName());
                        intent.putExtra("votedRole",mostVotedPlayer.getRole());
                        startActivity(intent);
                    }
                });
            }
        } else {
            Log.e("PlayState", "No players received from Intent!");
        }

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        setTimer("seer");
    }

    private boolean hasEveryoneVoted() {
        return voteCount == playState.size();
    }

    /**
     * Determine the most voted player.
     *
     * @return The Player with the most votes, or null if no votes.
     */
    private Player getMostVotedPlayer() {
        int maxVotes = 0;
        int maxIndex = -1;

        for (int i = 0; i < voted.length; i++) {
            if (voted[i] > maxVotes) {
                maxVotes = voted[i];
                maxIndex = i;
            }
        }
        if (maxIndex != -1) {
            return playState.get(maxIndex); // Return the most voted player
        } else {
            return null; // No votes yet
        }
    }

    // set timer
    protected void setTimer(String timeType) {

        // ตั้งค่า maxTime ตาม timeType
        if (timeType.equals("day")) {
            maxTime = 3 * 60; // 3 นาที
        } else if (timeType.equals("night")) {
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
