package com.example.wortgewandt.Utility;

import android.content.Context;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DownloadFileThread extends Thread{
    private static final String TAG = "DownloadThread";
    Context context;
    String url;
    String word;
    File mp3;

    public DownloadFileThread(Context context, String url, String word) {
        this.context = context;
        this.url = url;
        this.word = word;
    }

    public File getFile(){
        return mp3;
    }

    @Override
    public void run() {
        super.run();

        try {
            File outputDir = context.getCacheDir();
            this.mp3 = File.createTempFile(word, ".mp3", outputDir);

            FileUtils.copyURLToFile(new URL(url),mp3);


        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "download: "+e.getMessage());
            //throw new RuntimeException(e);
        }
    }
}
