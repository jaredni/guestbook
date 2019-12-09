package com.example.jvicentillo.digitalguestbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.jvicentillo.digitalguestbook.FormatViews.PhotoActivity;
import com.example.jvicentillo.digitalguestbook.FormatViews.VideoActivity;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;

public class GreetingFormatActivity extends AppCompatActivity {

    FFmpeg ffmpeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting_format);
        loadFFMpegBinary();
    }

    public void clickPhoto(View view) {
        Intent photo_intent = new Intent(getApplicationContext(), PhotoActivity.class);
    
        startActivity(photo_intent);
    }

    public void clickVideo(View view) {
        Intent video_intent = new Intent(getApplicationContext(), VideoActivity.class);

        startActivity(video_intent);
    }

    private void loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {
                ffmpeg = FFmpeg.getInstance(this);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {

                }

                @Override
                public void onSuccess() {

                }
            });
        } catch (FFmpegNotSupportedException e) {

        } catch (Exception e) {

        }
    }

    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.i("REEEEE", "REEEEEEEEe");
                }

                @Override
                public void onSuccess(String s) {
                    Log.i("status", "read");
                }

                @Override
                public void onProgress(String s) {

                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }

    public void onClick(View view) {
        File lmao = new File(getExternalFilesDir(null), "/NewDirectory/something.mp4");
        String filePath = lmao.getAbsolutePath();
        Log.i("AbsPAth", filePath);
        String[] command = new String[]{"-i", filePath};
        execFFmpegBinary(command);
    }
}
