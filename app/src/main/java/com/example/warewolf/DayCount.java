package com.example.warewolf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DayCount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.noon_page);

        // กำหนด View Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sunlight), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // กำหนด VideoView
        VideoView sunVideoView = findViewById(R.id.sunlight);
        sunVideoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sun_light));
        sunVideoView.setMediaController(new MediaController(this));
        sunVideoView.requestFocus();

        // สั่งให้ Video เล่นอัตโนมัติ
        sunVideoView.start();

        // เพิ่ม animation
        animateVideoView(sunVideoView);

        // ตั้งเวลา animation
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(DayCount.this, SetupActivity.class);
            startActivity(intent);
            finish(); // ปิด Activity ปัจจุบัน
        }, 3000); // 4000ms (3 วิ)
    }

    private void animateVideoView(VideoView videoView) {
        // สร้าง Animation (เคลื่อนที่ขึ้นลง)
        TranslateAnimation animation = new TranslateAnimation(
                0, 0,          // จาก X -> X (ไม่เปลี่ยนตำแหน่ง X)
                -100, 100      // จาก Y -> Y (เคลื่อนที่ขึ้นและลง)
        );
        animation.setDuration(2000); // ระยะเวลา 2 วินาที
        animation.setRepeatCount(1); // เล่น 1 รอบ
        animation.setRepeatMode(Animation.REVERSE); // เล่นกลับด้าน
        videoView.startAnimation(animation); // เริ่มแอนิเมชัน
    }
}


