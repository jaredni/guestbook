package com.example.jvicentillo.digitalguestbook.FormatViews;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.jvicentillo.digitalguestbook.EndSessionActivity;
import com.example.jvicentillo.digitalguestbook.GreetingFormatActivity;
import com.example.jvicentillo.digitalguestbook.R;
import com.example.jvicentillo.digitalguestbook.Utilities;

import java.io.File;

public class VideoActivity extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int REQUEST_NEW_FILE = 1;
    static final int REQUEST_CURRENT_FILE = 2;
    Utilities util = new Utilities();

    public void getVideo() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, util.getFileUri(getApplicationContext(), REQUEST_NEW_FILE, 0));
        takePictureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (hasAllPermissionsGranted(grantResults)){
            getVideo();
        }
    }

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            getVideo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            VideoView videoView= findViewById(R.id.videoGreeting);
            videoView.setMediaController(new MediaController(this));
            videoView.setVideoURI(util.getFileUri(getApplicationContext(), REQUEST_CURRENT_FILE, 0));
            videoView.requestFocus();
            videoView.start();
        }
    }

    public void saveVideo(View view) {
        Intent end_session_intent = new Intent(getApplicationContext(), EndSessionActivity.class);
        startActivity(end_session_intent );
    }

    public void clicKBack(View view) {
        File file = new File(util.getFileUri(getApplicationContext(), REQUEST_CURRENT_FILE, 0).getPath());
        file.delete();

        Intent greeting_intent = new Intent(getApplicationContext(), GreetingFormatActivity.class);

        startActivity(greeting_intent);
    }
}
