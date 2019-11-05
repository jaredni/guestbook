package com.example.jvicentillo.digitalguestbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.jvicentillo.digitalguestbook.FormatViews.PhotoActivity;
import com.example.jvicentillo.digitalguestbook.FormatViews.VideoActivity;

public class GreetingFormatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting_format);
    }

    public void clickPhoto(View view) {
        Intent photo_intent = new Intent(getApplicationContext(), PhotoActivity.class);

        startActivity(photo_intent);
    }

    public void clickVideo(View view) {
        Intent video_intent = new Intent(getApplicationContext(), VideoActivity.class);

        startActivity(video_intent);
    }
}
