package com.example.warewolf;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class NightCount extends AppCompatActivity {

    private ArrayList<Player> playState;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false); // เปิดใช้งาน Edge-to-Edge UI
        setContentView(R.layout.night_page);

        // กำหนด View Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.moonlightGif), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        playState = getIntent().getParcelableArrayListExtra("playState");
        time = getIntent().getStringExtra("time");

        GifView gifView = findViewById(R.id.moonlightGif);
        gifView.setGifResource(R.raw.moon_light); // ใช้ไฟล์ GIF จาก res/raw

        // ตั้งเวลา animation
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(NightCount.this, GameActivity.class);
            intent.putParcelableArrayListExtra("playState", playState);
            intent.putExtra("time","night");
            startActivity(intent);
            finish(); // ปิด Activity ปัจจุบัน
        }, 3000); // 4000ms (3 วิ)
    }
}
