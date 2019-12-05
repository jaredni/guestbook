package com.example.jvicentillo.digitalguestbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.jvicentillo.digitalguestbook.FormatViews.PhotoActivity;
import com.example.jvicentillo.digitalguestbook.FormatViews.VideoActivity;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;

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

    public static void convertToGif(String originVideoFilePath, String destinationGifFilePath,
                                    int width, int height, FFmpegExecuteResponseHandler ffmpegExecuteResponseHandler) {

    }

    private ExecuteBinaryResponseHandler executeBinaryResponseHandler() {
        return new ExecuteBinaryResponseHandler() {
            @Override
            public void onStart() {}

            @Override
            public void onProgress(String message) {}

            @Override
            public void onFailure(String message) {}

            @Override
            public void onSuccess(String message) {}

            @Override
            public void onFinish() {}
        };
    }

    /*public void loadBinary(Context context) {
        String[] cmd = new String[]{"-y", "-i", originVideoFilePath, "-s", width + "x" + height,
                "-pix_fmt", "rgb24", "-preset", "ultrafast",
                destinationGifFilePath};
        try {
            CustomFFmpeg.getInstance().execute(originVideoFilePath, cmd, ffmpegExecuteResponseHandler);
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }

        FFmpeg ffmpeg = FFmpeg.getInstance(context);
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(cmd, executeBinaryResponseHandler());
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }
    }*/
}
