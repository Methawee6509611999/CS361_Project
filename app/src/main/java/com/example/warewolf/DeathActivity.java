package com.example.warewolf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DeathActivity extends AppCompatActivity
{

    private ArrayList<Player> playState;
    private String role;
    ImageView deadManImg;
    ArrayList<String> wolfName;
    int wolfCount;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.death_announcement);

        playState = getIntent().getParcelableArrayListExtra("playState");
        String time = getIntent().getStringExtra("time");
        String deadManName = getIntent().getStringExtra("votedName");

        TextView reasonOfDeath = findViewById(R.id.kill);
        TextView deadMan = findViewById(R.id.deathName);
        TextView deadRole = findViewById(R.id.roleDead);
        deadManImg = findViewById(R.id.imageViewdead);
        Button next_btn = findViewById(R.id.NextButtondead);


        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        // Retrieve the boolean value for "reveal_checked", defaulting to false if not set
        boolean isChecked = sharedPreferences.getBoolean("reveal_checked", false);


        if(deadManName.equals("none")){
            reasonOfDeath.setText(R.string.safe);
        }else if(time.equals("day")){
            reasonOfDeath.setText(R.string.hang);
            deadMan.setText(deadManName);
            role=getIntent().getStringExtra("votedRole");
            if(isChecked){
                setImage(role);
                deadRole.setText(role);
            }
        }else if(time.equals("night")){
            reasonOfDeath.setText(R.string.kill);
            deadMan.setText(deadManName);
            role=getIntent().getStringExtra("votedRole");
            if(isChecked){
                setImage(role);
                deadRole.setText(role);
            }
        }

        next_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //check Endgame wolfwin
                findwolf();
                if(wolfCount>=playState.size()){
                    String seerName = findSeer();
                    Intent intent = new Intent(DeathActivity.this, Ending.class);
                    if (!seerName.equals("")){
                        intent.putExtra("seerName",seerName);
                    }
                    intent.putExtra("wolfWin",true);
                    intent.putStringArrayListExtra("wolfNames",wolfName);
                    startActivity(intent);
                } else if (wolfCount==0) {
                    String seerName = findSeer();
                    Intent intent = new Intent(DeathActivity.this, Ending.class);
                    if (!seerName.equals("")){
                        intent.putExtra("seerName",seerName);
                    }

                    intent.putExtra("wolfWin",false);
                    intent.putStringArrayListExtra("wolfNames",wolfName);
                    startActivity(intent);
                }

                Intent intent = new Intent(DeathActivity.this, GameActivity.class);
                if(time.equals("day")){
                    intent.putExtra("time","night");
                }else{
                    intent.putExtra("time","day");
                }
                intent.putParcelableArrayListExtra("playState",playState);
                startActivity(intent);
            }
        });
    }

    private String findSeer(){
        String SeerName = "";
        for(int i=0;i<playState.size();i++){
            if(playState.get(i).getRole().equals("Seer")){
                SeerName = (playState.get(i).getName());
            }
        }
        return SeerName;
    }

    private void findwolf(){
        for(int i=0;i<playState.size();i++){
            if( playState.get(i).getRole().equals("Werewolf") && playState.get(i).isAlive() ){
                wolfName.add(playState.get(i).getName());
                wolfCount++;
            }
        }
    }



    private void setImage(String role){
        switch (role){
            case "Werewolf":
                deadManImg.setImageResource(R.drawable.werewolf_img); // Replace with your actual drawable
                break;
            case "Seer":
                deadManImg.setImageResource(R.drawable.seer_img); // Replace with your actual drawable
                break;
            case "Villager":
                deadManImg.setImageResource(R.drawable.villager_img); // Replace with your actual drawable
                break;
        }
    }
}
