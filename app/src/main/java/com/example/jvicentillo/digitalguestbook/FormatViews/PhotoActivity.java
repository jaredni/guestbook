package com.example.jvicentillo.digitalguestbook.FormatViews;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jvicentillo.digitalguestbook.EndSessionActivity;
import com.example.jvicentillo.digitalguestbook.GreetingFormatActivity;
import com.example.jvicentillo.digitalguestbook.R;
import com.example.jvicentillo.digitalguestbook.Utilities;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_NEW_FILE = 1;
    static final int REQUEST_CURRENT_FILE = 2;

    Utilities util;
    FFmpeg ffmpeg;

    public void getPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (hasAllPermissionsGranted(grantResults)){
            getPhoto();
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
        setContentView(R.layout.activity_photo);
        loadFFMpegBinary();
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            getPhoto();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = findViewById(R.id.realPhoto);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public void clearDrawing(View view) {
        DrawableImageView div = findViewById(R.id.background);
        div.removeDrawing();
    }

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void saveImage(View view) {
        ViewGroup templateLayout = findViewById(R.id.templateLayout);
        String directory = createDirectory();
        Bitmap bitmap = viewToBitmap(templateLayout);
        util = new Utilities();
        try {
            FileOutputStream output = new FileOutputStream(util.getFileUri(getApplicationContext(), REQUEST_NEW_FILE, 1).getPath());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.close();
            Uri video_output = util.getFileUri(getApplicationContext(), REQUEST_CURRENT_FILE, 0);
            Uri image_output = util.getFileUri(getApplicationContext(), REQUEST_CURRENT_FILE, 1);
            File video = new File(video_output.getPath());
            File picture = new File(image_output.getPath());
            String[] command = new String[] {
                "-loop", "1", "-i", picture.getAbsolutePath(), "-c:v", "libx264", "-t", "15", "-pix_fmt", "yuv420p", "-vf", "scale=320:240", video.getAbsolutePath()
            };
            execFFmpegBinary(command, picture);
            Intent end_session_intent = new Intent(getApplicationContext(), EndSessionActivity.class);
            startActivity(end_session_intent );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String createDirectory() {
        File folder = new File(getExternalFilesDir(null), "NewDirectory");

        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            return folder.toString();
        } else {
            Log.i("status", Environment.getExternalStorageDirectory().toString());
        }
        return folder.toString();
    }

    public void clicKBack(View view) {
        Intent greeting_intent = new Intent(getApplicationContext(), GreetingFormatActivity.class);

        startActivity(greeting_intent);
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

    private void execFFmpegBinary(final String[] command, final File picture) {
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
                    boolean deleted = picture.delete();
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
}
