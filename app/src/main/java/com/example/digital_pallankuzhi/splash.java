package com.example.digital_pallankuzhi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.MediaController;
import android.widget.VideoView;

public class splash extends AppCompatActivity {

    VideoView videoView;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        videoView=findViewById(R.id.videoview);
        videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.splash);
        videoView.start();
        videoView.seekTo(0);
        mp = MediaPlayer.create(this, R.raw.samplesound);
        mp.start();
        videoView.animate().translationY(-1600).setDuration(2000).setStartDelay(7000);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splash.this,MainActivity.class);
                startActivity(i);
            }
        }, 7000);
    }
}