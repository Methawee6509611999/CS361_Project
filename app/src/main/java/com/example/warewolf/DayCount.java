package com.example.warewolf;

import android.net.Uri;
import android.os.Bundle;
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sunlight), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        VideoView sunVideoView = findViewById(R.id.sunlight);
        sunVideoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sun_light));
        sunVideoView.setMediaController(new MediaController(this));
        sunVideoView.requestFocus();

    }
}


