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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

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
        String directoryString = getExternalFilesDir(null) + "/NewDirectory";
        File directoryFile = new File(directoryString);
        int fileCount = directoryFile.list().length - 1;
        File lmao = new File(getExternalFilesDir(null), "/NewDirectory/1.mp4");
        File picture = new File(getExternalFilesDir(null), "/NewDirectory/0.mp4");
        File output = new File(getExternalFilesDir(null), "/NewDirectory/output.mp4");
        String filePath = lmao.getAbsolutePath();
        String list = generateList(new String[] {picture.getAbsolutePath(), filePath});
        String[] command = new String[] {
                "-f",
                "concat",
                "-safe",
                "0",
                "-i",
                list,
                "-c",
                "copy",
                output.getAbsolutePath()
        };
        execFFmpegBinary(command);
    }

    private static String generateList(String[] inputs) {
        File list;
        Writer writer = null;
        try {
            list = File.createTempFile("ffmpeg-list", ".txt");
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(list)));
            for (String input: inputs) {
                writer.write("file '" + input + "'\n");
                Log.d("Writing to File", "Writing to list file: file '" + input + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "/";
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        Log.d("Wrote list file to", "Wrote list file to " + list.getAbsolutePath());
        return list.getAbsolutePath();
    }
}
