package com.example.jvicentillo.digitalguestbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;

public class MainActivity extends AppCompatActivity {
    private FFmpeg ffmpeg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getApplicationContext(), GreetingFormatActivity.class);

        startActivity(intent);
    }
}
