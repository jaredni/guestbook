package com.example.jvicentillo.digitalguestbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.jvicentillo.digitalguestbook.FormatViews.PhotoActivity;

public class GreetingFormatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting_format);
    }

    public void clickPhoto(View view) {
        Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);

        startActivity(intent);
    }
}
