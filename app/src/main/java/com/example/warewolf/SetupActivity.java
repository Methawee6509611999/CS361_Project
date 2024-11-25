package com.example.warewolf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SetupActivity extends AppCompatActivity implements SensorEventListener {

    private ArrayList<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0; // Tracks which player is being set up
    private List<String> roles; // Holds the shuffled roles

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private boolean isFacingDown = false;
    private boolean isFacingUp = false;

    private Vibrator vibrator;

    private ImageView playerRoleImage;
    private TextView roleText;
    private EditText inputName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_announcement);

        // Variables from Intent
        final int maxPlayers = getIntent().getIntExtra("playerAmount", 5);

        // UI Elements
        Button nextButton = findViewById(R.id.next_btn);
        inputName = findViewById(R.id.deathName);
        roleText = findViewById(R.id.role);
        playerRoleImage = findViewById(R.id.playerRole);

        // Sensor and Vibrator initialization
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Generate roles
        roles = generateRoles(maxPlayers);

        // Set initial UI for the first player
        updateUIForPlayer(roleText, playerRoleImage);

        // Next button logic
        nextButton.setOnClickListener(v -> {
            // Change image to question mark
            playerRoleImage.setImageResource(R.drawable.questionmark_img);
            roleText.setText("?");
            // Start listening for orientation changes
            isFacingDown = false; // Reset states
            isFacingUp = false;
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        });
    }


    private List<String> generateRoles(int maxPlayers) {
        List<String> roles = new ArrayList<>();

        int werewolves = maxPlayers / 3; // At least 1 Werewolf
        int seers = 1; // Always 1 Seer
        int villagers = maxPlayers - werewolves - seers;

        for (int i = 0; i < werewolves; i++) roles.add("Werewolf");
        for (int i = 0; i < seers; i++) roles.add("Seer");
        for (int i = 0; i < villagers; i++) roles.add("Villager");

        Collections.shuffle(roles);
        return roles;
    }

    private void updateUIForPlayer(TextView roleText, ImageView playerRoleImage) {
        String role = roles.get(currentPlayerIndex);

        // Set role description
        roleText.setText("Role: " + role);

        // Set role-specific image
        switch (role) {
            case "Werewolf":
                playerRoleImage.setImageResource(R.drawable.werewolf_img); // Replace with your actual drawable
                break;
            case "Seer":
                playerRoleImage.setImageResource(R.drawable.seer_img); // Replace with your actual drawable
                break;
            case "Villager":
                playerRoleImage.setImageResource(R.drawable.villager_img); // Replace with your actual drawable
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float z = event.values[2]; // Acceleration along the Z-axis

        if (z < -9 && !isFacingDown) { // Phone facing down
            isFacingDown = true;
            vibrator.vibrate(200); // Vibrate for 200ms
        }

        if (z > 9 && isFacingDown && !isFacingUp) { // Phone facing up
            isFacingUp = true;

            // Stop listening to the sensor
            sensorManager.unregisterListener(this);

            // Process current player
            String playerName = inputName.getText().toString().trim();

            if (!playerName.isEmpty() && currentPlayerIndex < roles.size()) {
                String assignedRole = roles.get(currentPlayerIndex);
                players.add(new Player(playerName, assignedRole));

                currentPlayerIndex++;
                if (currentPlayerIndex < roles.size()) {
                    updateUIForPlayer(roleText, playerRoleImage);
                    inputName.setText(""); // Clear input for the next player
                } else {
                    // All players set up, start the game
                    Intent intent = new Intent(SetupActivity.this, GameActivity.class);
                    intent.putParcelableArrayListExtra("players", players);
                    intent.putExtra("time","day");
                    startActivity(intent);
                }
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No action needed
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }
}
