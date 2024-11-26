package com.example.warewolf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    public  static final  String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CheckBox reveal = findViewById(R.id.revealCheckBox);

        // Restore checkbox state from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isRevealChecked = sharedPreferences.getBoolean("reveal_checked", false);
        reveal.setChecked(isRevealChecked);

        // Listener for checkbox toggle
        reveal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the checkbox state
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("reveal_checked", isChecked);
            editor.apply(); // Apply changes
        });


        final Button start_btn = findViewById(R.id.startButton);
        EditText playerCount = findViewById(R.id.playerCount);
        String input = playerCount.getText().toString().trim();  // Get the input and trim leading/trailing spaces

        // Check if the input is not empty and is a valid integer
        if (!input.isEmpty() && input.matches("-?\\d+")) {
            int playerCountInt = Integer.parseInt(input);
            // Use playerCountInt here
        } else {
            // Handle invalid input, maybe show an error message
            Toast.makeText(this, "Please enter a valid number.", Toast.LENGTH_SHORT).show();
        }

        start_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, SetupActivity.class);
                intent.putExtra("playerAmount",playerCount.getText());
                intent.putExtra("time","day");
                startActivity(intent);
            }
        });
    }
}