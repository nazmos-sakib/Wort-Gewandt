package com.wortgewandt.DataStorage;

import android.content.Context;

import com.wortgewandt.Interface.Callback;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Objects;

public class DataManagement {
    private WeakReference<Context> context;
    private static DataManagement instance;
    String fileName = "wordList.ser";
    //String fileName = Objects.requireNonNull(getContext()).getString(R.string.local_file_name);
    private File file;
    private DataManagement() {
        // Private constructor to prevent instantiation
    }
    private DataManagement(Context context) {
        // Private constructor to prevent instantiation
        this.context = new WeakReference<>(context);
    }

    public static synchronized DataManagement getInstance(Context context) {
        if (instance == null) {
            instance = new DataManagement(context);
        }
        return instance;
    }

    private Context getContext() {
        return context != null ? context.get() : null;
    }

    public void readLocalData(Data dataInstance){
        this.file = new File(Objects.requireNonNull(getContext()).getFilesDir(), fileName);
        if (file.exists() && dataInstance.getWordList().isEmpty()){
            new ReadData(file);
        }
    }

    public void writeLocalData(Callback<Void> callback){
        new WriteData(new File(Objects.requireNonNull(getContext()).getFilesDir(), fileName),callback);
    }

}
