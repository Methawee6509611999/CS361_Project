package com.example.warewolf;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
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


    private List<ImageView> imageViewPersonIds;
    private List<ImageView> imageViewCountIds;


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

        // Initialize ImageView for each player
        imageViewPersonIds = new ArrayList<>();
        imageViewPersonIds = Arrays.asList(
                findViewById(R.id.imageViewP1), findViewById(R.id.imageViewP2),findViewById(R.id.imageViewP3),
                findViewById(R.id.imageViewP4), findViewById(R.id.imageViewP5), findViewById(R.id.imageViewP6),
                findViewById(R.id.imageViewP7), findViewById(R.id.imageViewP8), findViewById(R.id.imageViewP9)
        );

        // Add voting count for each player
        imageViewCountIds = new ArrayList<>();
        imageViewCountIds = Arrays.asList(
                findViewById(R.id.imageViewCountP1), findViewById(R.id.imageViewCountP2),findViewById(R.id.imageViewCountP3),
                findViewById(R.id.imageViewCountP4), findViewById(R.id.imageViewCountP5), findViewById(R.id.imageViewCountP6),
                findViewById(R.id.imageViewCountP7), findViewById(R.id.imageViewCountP8), findViewById(R.id.imageViewCountP9)
        );

        playState = getIntent().getParcelableArrayListExtra("playState");
        time = getIntent().getStringExtra("time");

        // Initialize vote tracking
        voted = new int[cardViews.size()];
        assert time != null;

        //set name on page
        for (int i = 0; i < playState.size(); i++) {
            Player player = playState.get(i);

            // Set the name to the TextView
            TextView playerTextView = findViewById(getResources().getIdentifier(
                    "textViewP" + (i + 1), "id", getPackageName())
            );
            playerTextView.setText(player.getName());

            // Set the avatar to imageView
            if(!playState.get(i).isAlive()){
                imageViewPersonIds.get(i).setImageResource(R.drawable.default_img);
            }
            else {
                int imageResourceId = getResources().getIdentifier("user_" + (i+1), "drawable", getPackageName());
                imageViewPersonIds.get(i).setImageResource(imageResourceId);
            }
        }

        // Let's vote.
        if(playState != null && time.equals("night")){

            // Set up skip button listener
            skipButton.setOnClickListener(v -> {
                Intent intent = new Intent(GameActivity.this, DeathActivity.class);
                intent.putExtra("time","day");
                intent.putExtra("votedName","none");
                intent.putParcelableArrayListExtra("playState", playState);
                startActivity(intent);
            });
            // Set up click listeners for the cards
            for (int i = 0; i < playState.size() ; i++) {
                int index = i; // Capture the current index
                CardView cardView = cardViews.get(i);

                if(!playState.get(index).isAlive()){
                    continue;
                }
                cardView.setOnClickListener(v -> {

                    Log.d("Voting", playState.get(index).getName() + " has been voted " + voted[index] + " times!");
                    playState.get(index).Die();
                    Intent intent = new Intent(GameActivity.this, DeathActivity.class);
                    intent.putExtra("time","night");
                    intent.putExtra("votedName",playState.get(index).getName());
                    intent.putExtra("votedRole",playState.get(index).getRole());
                    intent.putParcelableArrayListExtra("playState", playState);
                    startActivity(intent);

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
                intent.putParcelableArrayListExtra("playState", playState);
                startActivity(intent);
            }

            // Set up click listeners for the cards
            for (int i = 0; i < playState.size() ; i++) {
                int index = i; // Capture the current index
                CardView cardView = cardViews.get(i);

                if(!playState.get(index).isAlive()){
                    continue;
                }
                cardView.setOnClickListener(v -> {
                    voted[index]++;
                    voteCount++;

                    if(voted[index]>=1) {
                        int imageClickCount = getResources().getIdentifier("number_" + (voted[index]), "drawable", getPackageName());
                        switch (index) {
                            case 0:
                                imageViewCountIds.get(0).setImageResource(imageClickCount);
                                break;
                            case 1:
                                imageViewCountIds.get(1).setImageResource(imageClickCount);
                                break;
                            case 2:
                                imageViewCountIds.get(2).setImageResource(imageClickCount);
                                break;
                            case 3:
                                imageViewCountIds.get(3).setImageResource(imageClickCount);
                                break;
                            case 4:
                                imageViewCountIds.get(4).setImageResource(imageClickCount);
                                break;
                            case 5:
                                imageViewCountIds.get(5).setImageResource(imageClickCount);
                                break;
                            case 6:
                                imageViewCountIds.get(6).setImageResource(imageClickCount);
                                break;
                            case 7:
                                imageViewCountIds.get(7).setImageResource(imageClickCount);
                                break;
                            case 8:
                                imageViewCountIds.get(8).setImageResource(imageClickCount);
                                break;
                            default:
                                // Default image for other indices
                                break;
                        }

                    }


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
                        intent.putParcelableArrayListExtra("playState", playState);
                        startActivity(intent);
                    }
                });
            }
        } else if (time.equals("seer")) {
            //set wolf image
            for (int i = 0; i < playState.size(); i++) {
                Player player = playState.get(i);

                // Set the avatar to imageView
                if(player.getRole().equals("Werewolf")){
                    imageViewPersonIds.get(i).setImageResource(R.drawable.werewolf_img);
                }
            }
        } else {
            Log.e("PlayState", "No players received from Intent!");
        }


        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        setTimer(time);
    }

    private boolean hasEveryoneVoted() {
        return voteCount == aliveCount();
    }

    private int aliveCount(){
        int count = 0;
        for(int i=0;i<playState.size();i++){
            if(playState.get(i).isAlive()){
                count++;
            }
        }
        return count;
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
            maxTime = 10; // 10 วินาที
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
                    handleTimerExpiration();
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

    private void handleTimerExpiration() {
        Log.d("Timer", "Timer expired. Completing votes...");

        // Force all votes to complete
        int haventVote = aliveCount()-voteCount;
        voteCount = aliveCount(); // Assume all alive players have cast their votes
        skipped[0] +=haventVote;
        Player mostVotedPlayer = getMostVotedPlayer();

        Intent intent = new Intent(GameActivity.this, DeathActivity.class);
        intent.putExtra("time", time); // Pass the current time type (day/night)
        if (mostVotedPlayer != null) {
            mostVotedPlayer.Die();
            intent.putExtra("votedName", mostVotedPlayer.getName());
            intent.putExtra("votedRole", mostVotedPlayer.getRole());
        } else {
            intent.putExtra("votedName", "none"); // No votes if no majority
        }
        intent.putParcelableArrayListExtra("playState", playState);
        startActivity(intent);
    }


}
