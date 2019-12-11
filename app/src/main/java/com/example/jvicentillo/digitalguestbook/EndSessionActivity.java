package com.example.jvicentillo.digitalguestbook;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EndSessionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_session);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent greeting_intent = new Intent(getApplicationContext(), GreetingFormatActivity.class);
                startActivity(greeting_intent);
            }
        }, 4000);
    }
}
