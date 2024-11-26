package com.example.warewolf;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
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

        String seerName = getIntent().getStringExtra("seerName");
        boolean wolfWin = getIntent().getBooleanExtra("wolfWin", false);
        ArrayList<String> wolfName = getIntent().getStringArrayListExtra("wolfName");

        // หา TextView โดยใช้ ID
        TextView winTeamTextView = findViewById(R.id.winTeam);
        TextView seerNameTextView = findViewById(R.id.seerName);
        TextView wolfNameTextView = findViewById(R.id.wolfName);

        // ตั้งค่าข้อความของ winTeam TextView ตามค่าของ wolfWin
        if (wolfWin) {
            winTeamTextView.setText(R.string.role_werewolf); // หาก wolfWin เป็น true จะใช้ข้อความ "Werewolf"
        } else {
            winTeamTextView.setText(R.string.role_villager); // หาก wolfWin เป็น false จะใช้ข้อความ "Villager"
        }

        seerNameTextView.setText(seerName);

        wolfNameTextView.setText(String.join(", ", wolfName));

    }
}
