package com.example.wortgewandt.DataStorage;


import android.annotation.SuppressLint;

import com.example.wortgewandt.Interface.DatenAnderungMitteilen;
import com.example.wortgewandt.Model.Wort;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Map;

public class Data {
    private Multimap<String, Wort> wordList ;
    private static Data instance;
    private DatenAnderungMitteilen callBack;
    private Data(){

    }
    private Data(DatenAnderungMitteilen callBack){
        wordList= ArrayListMultimap.create();
        this.callBack = callBack;
    }

    public static synchronized Data getInstance() {
        return instance;
    }
    public static synchronized Data getInstance(DatenAnderungMitteilen callBack) {
        if (instance==null){
            instance = new Data(callBack);
        }
        return instance;
    }

    public Multimap<String, Wort> getWordList() {
        return wordList;
    }

    public void setWordList(Multimap<String, Wort> wordList) {
        instance.wordList = wordList;
        //callBack.onAnderungen();
    }

    //@SuppressLint("CheckResult")
    public boolean addNewWord(String key, Wort word){
        boolean result = instance.wordList.put(key, word);
        callBack.onAnderungen();
        return result;
    }

    public boolean deleteEntry(Map.Entry<String,Wort> entry){
        boolean result = wordList.remove(entry.getKey(),entry.getValue());
        callBack.onAnderungen();
        return result;
    }
    public boolean editEntry(Map.Entry<String,Wort> entry,String key, Wort word){
        if(wordList.remove(entry.getKey(),entry.getValue())){
            return addNewWord(key,word);
        }
        return false;
    }

    public int getDataSize(){
        return instance.wordList.size();
    }
}
