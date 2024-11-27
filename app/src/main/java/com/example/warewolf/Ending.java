package com.example.warewolf;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Ending extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ending_page);

        Button end_btn = findViewById(R.id.end_btn);

        end_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ending.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Get data passed via Intent
        String seerName = getIntent().getStringExtra("seerName");
        boolean wolfWin = getIntent().getBooleanExtra("wolfWin", false);
        ArrayList<Player> playState = getIntent().getParcelableArrayListExtra("playState");
        ArrayList<String> wolfName =new ArrayList<String>();

        for (Player player:playState) {
            if(player.getRole().equals("Werewolf")){
                wolfName.add(player.getName());
            }
        }

        // Find TextViews by ID
        TextView winTeamTextView = findViewById(R.id.winTeam);
        TextView seerNameTextView = findViewById(R.id.seerName);
        TextView wolfNameTextView = findViewById(R.id.wolfName);
        ImageView roleWinImageView = findViewById(R.id.roleWin);

        // Set text for winTeam TextView based on wolfWin value
        if (wolfWin) {
            winTeamTextView.setText(R.string.role_werewolf); // If wolfWin is true, display "Werewolf"
            roleWinImageView.setImageResource(R.drawable.werewolf_img); // แสดงรูปหมาป่า
        } else {
            winTeamTextView.setText(R.string.role_villager); // If wolfWin is false, display "Villager"
            roleWinImageView.setImageResource(R.drawable.villager_img); // แสดงรูปชาวบ้าน
        }

        // Display Seer name
        if (seerName != null && !seerName.isEmpty()) {
            seerNameTextView.setText(seerName);
        } else {
            seerNameTextView.setText("Unknown Seer");
        }

        // Display wolf names (if any)
        if (wolfName != null && !wolfName.isEmpty()) {
            wolfNameTextView.setText(String.join(", ", wolfName));
        } else {
            wolfNameTextView.setText("No Wolves");
        }
    }
}
