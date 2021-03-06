package com.example.jvicentillo.digitalguestbook.FormatViews;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_NEW_FILE = 1;

    Utilities util;

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
}
