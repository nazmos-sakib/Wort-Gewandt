package com.wortgewandt.DataStorage;

import android.annotation.SuppressLint;

import com.wortgewandt.Model.Wort;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;

public class ReadData {
    @SuppressLint("CheckResult")
    public ReadData(File file){
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            List<Map.Entry<String, Wort>> wordEntries = (List<Map.Entry<String, Wort>>) in.readObject();
            in.close();
            fileIn.close();

            // Convert the list back to Multimap
            Multimap<String, Wort> wordList = ArrayListMultimap.create();
            for (Map.Entry<String, Wort> entry : wordEntries) {
                wordList.put(entry.getKey(), entry.getValue());
            }

            //
            Data.getInstance().setWordList(wordList);

            System.out.println("after reading number of words are ------------" + wordList.size());



        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
