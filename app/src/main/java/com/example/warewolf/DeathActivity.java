package com.example.warewolf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DeathActivity extends AppCompatActivity {

    private ArrayList<Player> playState;
    private String role;
    private ImageView deadManImg;
    private ArrayList<String> wolfNames = new ArrayList<>();
    private int wolfCount = 0;
    private int aliveCount = 0;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.death_announcement);

        initializeData();
        updateDeathUI();
        countPlayers();
        setupNextButton();
    }

    private void initializeData() {
        playState = getIntent().getParcelableArrayListExtra("playState");
        logPlayerStates();

        deadManImg = findViewById(R.id.imageViewdead);
    }

    private void logPlayerStates() {
        for (Player player : playState) {
            Log.d("DeathActivity", "Player: " + player.getName() + ", Role: " + player.getRole() + ", isAlive: " + player.isAlive());
        }
    }

    private void updateDeathUI() {
        String time = getIntent().getStringExtra("time");
        String deadManName = getIntent().getStringExtra("votedName");
        role = getIntent().getStringExtra("votedRole");

        TextView reasonOfDeath = findViewById(R.id.kill);
        TextView deadMan = findViewById(R.id.deathName);
        TextView deadRole = findViewById(R.id.roleDead);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        boolean isRevealChecked = sharedPreferences.getBoolean("reveal_checked", false);

        if ("none".equals(deadManName)) {
            reasonOfDeath.setText(R.string.safe);
        } else {
            reasonOfDeath.setText(time.equals("day") ? R.string.hang : R.string.kill);
            deadMan.setText(deadManName);

            if (isRevealChecked) {
                setImage(role);
                deadRole.setText(role);
            }
        }
    }

    private void countPlayers() {
        for (Player player : playState) {
            if (player.isAlive()) {
                aliveCount++;
                if ("Werewolf".equals(player.getRole())) {
                    wolfCount++;
                    wolfNames.add(player.getName());
                }
            }
        }
    }

    private void setupNextButton() {
        Button nextButton = findViewById(R.id.NextButtondead);
        nextButton.setOnClickListener(view -> handleNextAction());
    }

    private void handleNextAction() {
        Log.d("DeathActivity", "Next button clicked");

        if (isGameOver()) {
            navigateToEnding();
        } else {
            navigateToNextPhase();
        }
    }

    private boolean isGameOver() {
        return wolfCount >= (aliveCount - wolfCount) || wolfCount == 0;
    }

    private void navigateToEnding() {
        Log.d("DeathActivity", "Game over logic executed");

        Intent intent = new Intent(DeathActivity.this, Ending.class);
        intent.putExtra("wolfWin", wolfCount > 0);
        intent.putParcelableArrayListExtra("playState",playState);

        String seerName = findSeer();
        if (!seerName.isEmpty()) {
            intent.putExtra("seerName", seerName);
        }

        startActivity(intent);
    }

    private void navigateToNextPhase() {
        Log.d("DeathActivity", "Proceeding to next phase");

        String time = getIntent().getStringExtra("time");

        if(time.equals("night")){
            intent = new Intent(DeathActivity.this, DayCount.class);
        } else if (time.equals("day")) {
            intent = new Intent(DeathActivity.this, NightCount.class);
        }

        intent.putExtra("time", time.equals("day") ? "night" : "day");
        intent.putParcelableArrayListExtra("playState", playState);

        startActivity(intent);
    }

    private String findSeer() {
        for (Player player : playState) {
            if ("Seer".equals(player.getRole())) {
                return player.getName();
            }
        }
        return "";
    }

    private void setImage(String role) {
        int imageResource;
        switch (role) {
            case "Werewolf":
                imageResource = R.drawable.werewolf_img;
                break;
            case "Seer":
                imageResource = R.drawable.seer_img;
                break;
            case "Villager":
                imageResource = R.drawable.villager_img;
                break;
            default:
                imageResource = R.drawable.default_img; // Default fallback image
        }
        deadManImg.setImageResource(imageResource);
    }
}
