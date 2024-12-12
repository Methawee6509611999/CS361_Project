package com.example.warewolf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.loading_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linearLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ใช้ Vibrator เพื่อสั่น
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(500); // สั่นเป็นเวลา 500 มิลลิวินาที
        }

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Loading.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 5000
        );

    }
}
