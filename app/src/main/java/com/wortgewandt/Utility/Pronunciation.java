package com.wortgewandt.Utility;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.wortgewandt.ExternalRequest.VolleyNetworkRequest;
import com.wortgewandt.Interface.Callback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;


public class Pronunciation {

    private static final String TAG = "Pronunciation";

    public static void downloadPronunciationPage(Context context,String word, Callback<String> callback){

        if (InternetConnection.isConnectedToInternet(context)) {
            Log.d(TAG, "getPronunciationUrl: " + word);
            String url = "https://www.howtopronounce.com/german/"+word;
            VolleyNetworkRequest.getInstance(context).makeWebRequest(
                    url, callback
            );
        } else callback.onCallback(null);
    }

    public static void getPronunciationUrl(Context context, String page, Callback<String> callback){

        Document doc = Jsoup.parse(page);
        Element content = doc.getElementById("pronouncedContents");

        if (content!=null){
            Elements audios= content.getElementsByTag("audio");
            if (!audios.isEmpty()){
                Element audio = audios.get(0);
                callback.onCallback(audio.attr("src"));
            }else {
                callback.onCallback(null);
            }
        } else {
            callback.onCallback(null);
        }


        if (InternetConnection.isConnectedToInternet(context)){

        }
    }

    public static void downloadToFile(Context context,String url,String word,Callback<byte[]> callback){
        DownloadFileThread thread = new DownloadFileThread(context,url,word);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File file = thread.getFile();

        try {
            if (thread.isAlive()){
                thread = null;
            }
            callback.onCallback(getBytesFromFile(file));
        } catch (Exception e){
            e.printStackTrace();
        }

        Log.d(TAG, "download: downloaded size: "+file.length());
        Log.d(TAG, "download: downloaded file: "+file.toString());

    }

    private static byte[] getBytesFromFile(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        inputStream.read(bytes);
        inputStream.close();
        return bytes;
    }

    public static byte[] downloadPronunciation(Context context,String url,String word){

        byte[] buffer = new byte[4096];
        Log.d(TAG, "downloadPronunciation: buffer values-----------------"+Arrays.hashCode(buffer));
        Log.d(TAG, "downloadPronunciation: buffer value"+ Arrays.toString(buffer));

        new  Thread(new Runnable(){

            @Override
            public void run() {
                File outputDir = context.getCacheDir(); // context being the Activity pointer
                try {
                    URLConnection conn = new URL(url).openConnection();
                    InputStream is = conn.getInputStream();

                    //OutputStream outStream = new FileOutputStream(new File("/tmp/file.mp3"));
                    OutputStream outStream = new FileOutputStream(
                            File.createTempFile(word, ".mp3", outputDir));


                    int len;
                    while ((len = is.read(buffer)) > 0) {
                        outStream.write(buffer, 0, len);
                    }
                    outStream.close();

                    Log.d("Thread","Pronunciation downloaded");

                } catch (Exception e){
                    e.printStackTrace();

                }
            }
        }).start();

        Log.d(TAG, "downloadPronunciation: buffer values-----------------"+Arrays.hashCode(buffer));
        Log.d(TAG, "downloadPronunciation: buffer value"+ Arrays.toString(buffer));


        return buffer;

    }



    public static void playPronunciationOnline(String src){
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        try {
            mediaPlayer.setDataSource(src);
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playPronunciationRaw(Context context,int raw){
        MediaPlayer mediaPlayer = MediaPlayer.create(context, raw);
        mediaPlayer.start();

    }


    public static void playPronunciationMp3(Context context,byte[] mp3SoundByteArray,Callback<Void> c){
        playPronunciationMp3(context,mp3SoundByteArray);
        c.onCallback(null);
    }
    public static synchronized void  playPronunciationMp3(Context context,byte[] mp3SoundByteArray){

        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("word", "mp3", context.getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            // resetting mediaplayer instance to evade problems
            mediaPlayer.reset();

            // In case you run into issues with threading consider new instance like:
            // MediaPlayer mediaPlayer = new MediaPlayer();

            // Tried passing path directly, but kept getting
            // "Prepare failed.: status=0x1"
            // so using file descriptor instead
            FileInputStream fis = new FileInputStream(tempMp3);
            //ByteArrayMediaDataSource a = new ByteArrayMediaDataSource(mp3SoundByteArray);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            //String s = ex.toString();
            ex.printStackTrace();
        }

        Log.d(TAG, "playPronunciationMp3: length: "+mp3SoundByteArray.length);

    }
}
