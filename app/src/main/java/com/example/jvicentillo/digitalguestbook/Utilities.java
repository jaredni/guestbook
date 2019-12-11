package com.example.jvicentillo.digitalguestbook;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;

public final class Utilities {
    // Get the file URI
    public Uri getFileUri(Context context, int requestCode, int greetingCode) {
        /*
            requestCode - code if getting of new file or the current file
            mediaCode - code for the type of greeting
         */
        String directoryString = context.getExternalFilesDir(null) + "/NewDirectory";
        File directoryFile = new File(directoryString);
        int fileCount;
        if (requestCode == 1) {
            try {
                fileCount = directoryFile.list().length;
            } catch (NullPointerException e) {
                Log.d("Error", "Directory file does not exist");
                return null;
            }
        } else {
            fileCount = directoryFile.list().length - 1;
        }
        String extensionFilename = ".mp4";
        if (greetingCode == 1) {
            extensionFilename = ".png";
        }
        File file = new File( context.getExternalFilesDir(null) + "/NewDirectory", fileCount + extensionFilename);

        Uri imgUri = Uri.fromFile(file);

        return imgUri;
    }
}
