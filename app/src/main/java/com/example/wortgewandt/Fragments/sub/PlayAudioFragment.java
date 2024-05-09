package com.example.wortgewandt.Fragments.sub;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wortgewandt.DataStorage.Data;
import com.example.wortgewandt.Model.Wort;
import com.example.wortgewandt.R;
import com.example.wortgewandt.Utility.Pronunciation;
import com.google.common.collect.Multimap;

public class PlayAudioFragment extends Fragment {

    public static boolean status;
    Wort wort;
    TextView tvWord, tvMeaning;
    ImageButton next,play,previous;


    public PlayAudioFragment() {
        // Required empty public constructor
        status = true;
    }

    public static PlayAudioFragment newInstance(String param1, String param2) {
        PlayAudioFragment fragment = new PlayAudioFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_audio, container, false);
        //initViews(view);
        return view;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initViews(View view){
        tvWord = view.findViewById(R.id.tv_germanWord_fragmentPlayAudio);
        tvMeaning = view.findViewById(R.id.tv_englishMeaning_fragmentPlayAudio);

        previous = view.findViewById(R.id.img_previous_fragmentPlayAudio);
        play = view.findViewById(R.id.img_play_pause_fragmentPlayAudio);
        next = view.findViewById(R.id.img_next_fragmentPlayAudio);

        //previous.setOnClickListener();


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onStart() {
        super.onStart();

        initViews(requireView());

        play.setOnClickListener(View->{
            play.setImageDrawable(requireContext().getDrawable(R.drawable.baseline_pause_24));
            Thread t = new Thread(separateThread);
            t.start();
        });
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            tvWord.setText(b.getString("word"));
            tvMeaning.setText(b.getString("meaning"));
        }
    };

    private final Runnable separateThread = new Runnable() {
        @Override
        public void run() {
            updateUI();
        }

    };

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateUI()  {
        Multimap<String, Wort> wordList = Data.getInstance().getWordList();

        wordList.values().forEach((w)-> {

            if (w.getPronunciation()!=null && w.getPronunciation().length>0){
                Pronunciation.playPronunciationMp3(requireContext(),w.getPronunciation(),
                        nullResponse->{
                            Message msg = new Message();
                            Bundle b = new Bundle();
                            b.putString("word",w.getGermanWord());
                            b.putString("meaning",w.getEnglishMeaning());

                            msg.setData(b);
                            handler.sendMessage(msg);
                            Log.d("updateUI", w.getGermanWord());
                });
            }


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        play.setImageDrawable(requireContext().getDrawable(R.drawable.baseline_play_arrow_24));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setPlay(){
        Multimap<String, Wort> wordList = Data.getInstance().getWordList();

        for (Wort w: wordList.values()){
            tvWord.setText(w.getGermanWord());
            tvMeaning.setText(w.getEnglishMeaning());
            if (w.getPronunciation()!=null && w.getPronunciation().length>0){

                Pronunciation.playPronunciationMp3(requireContext(),w.getPronunciation());



                /*((MainActivity)requireContext()).runOnUiThread (
                        new Runnable() {
                            @Override
                            public void run() {
                                Pronunciation.playPronunciationMp3(requireContext(),w.getPronunciation());
                            }
                        }
                );*/

                /*Thread t = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Pronunciation.playPronunciationMp3(requireContext(),w.getPronunciation());
                    }
                };

                t.start();

                try {
                    while (t.isAlive()){
                        Thread.sleep(100);
                    }
                    t.join();
                } catch (Exception e){
                    e.printStackTrace();
                }*/

                /*AtomicBoolean playFinished = new AtomicBoolean(false);
                Pronunciation.playPronunciationMp3(requireContext(),w.getPronunciation(),
                        res->{
                            playFinished.set((boolean) res);
                        });
                while (!playFinished.get());*/

            }

            try {
                Thread.sleep(2*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        play.setImageDrawable(requireContext().getDrawable(R.drawable.baseline_play_arrow_24));

    }
}