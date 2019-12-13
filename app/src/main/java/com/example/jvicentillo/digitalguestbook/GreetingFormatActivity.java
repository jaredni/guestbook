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
                    Log.d("loaded correctly", "ffmpeg : correct Loaded");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            Log.d("not loaded", "not loaded again");
        } catch (Exception e) {
            Log.d("not loaded", "EXception no controlada : " + e);
        }
    }

    private void execFFmpegBinary(final String[] command) {
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onProgress(String message) {}

                @Override
                public void onFailure(String message) {
                    Log.d("Error", message);
                }

                @Override
                public void onSuccess(String message) {
                    Log.d("Success", message);
                }

                @Override
                public void onFinish() {
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }
    }

    public void onClick(View view) {
        File lmao = new File(getExternalFilesDir(null), "/NewDirectory/1.mp4");
        File picture = new File(getExternalFilesDir(null), "/NewDirectory/0.png");
        File output = new File(getExternalFilesDir(null), "/NewDirectory/output.mp4");
        String filePath = lmao.getAbsolutePath();
        String[] command = new String[]{
                "-loop", "1", "-framerate", "24", "-t", "10", "-i", picture.getAbsolutePath(), "-i", filePath, "-f",
                "lavfi", "-t", "0.1", "-i", "anullsrc=channel_layout=stereo:sample_rate=4410",
                "-filter_complex", "[0]scale=432:432,setsar=1[im];[1]scale=432:432,setsar=1[vid];[im][vid]concat=n=2:v=1:a=0",
                output.getAbsolutePath()

        };
        execFFmpegBinary(command);
    }
}
