package com.example.jvicentillo.digitalguestbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.jvicentillo.digitalguestbook.FormatViews.PhotoActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getApplicationContext(), GreetingFormatActivity.class);

        startActivity(intent);
    }
}
