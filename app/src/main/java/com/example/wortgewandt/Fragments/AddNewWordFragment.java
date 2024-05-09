package com.example.wortgewandt.Fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wortgewandt.DataStorage.Data;
import com.example.wortgewandt.ExternalRequest.MakeInternetRequest;
import com.example.wortgewandt.Interface.ChangeFragment;
import com.example.wortgewandt.Model.ResponseCodes;
import com.example.wortgewandt.Model.Wort;
import com.example.wortgewandt.R;
import com.example.wortgewandt.Utility.Pronunciation;

import java.util.Map;


public class AddNewWordFragment extends Fragment {
    private final String TAG = "AddNewWordFragment";
    private ProgressBar progressBar;
    private EditText word,editText_englishMeaning;
    private TextView notFoundWarning,shoUrl;
    private Button btn_search, btn_add, btn_cancel, btn_enterManually, btn_enterEnglishMeaningManually;
    private LinearLayout warningSection,mainDiv;

    private ImageButton playPronunciation, downloadPronunciation;

    private String audioUrlMain;

    byte[] aMp3;

    private final ChangeFragment changeFragment;

    private boolean isPreset;
    private Map.Entry<String, Wort> presetEntry;


    public AddNewWordFragment(ChangeFragment changeFragment) {
        this.changeFragment = changeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_word, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews(requireView());
    }

    private void initViews(View view){
        word = view.findViewById(R.id.edTv_germanWord_addNewWordFragment);
        btn_search = view.findViewById(R.id.btn_searchEngMeaning_addNewWordFragment);
        notFoundWarning = view.findViewById(R.id.tv_englishMeaningNotFoundWarning_addNewWordFragment);
        editText_englishMeaning = view.findViewById(R.id.edTv_englishMeaning_addNewWordFragment);
        progressBar = view.findViewById(R.id.progressBar_addNewWordFragment);
        btn_add = view.findViewById(R.id.btn_add_addNewWordFragment);
        btn_cancel = view.findViewById(R.id.btn_cancel_addNewWordFragment);
        shoUrl = view.findViewById(R.id.shoUrl);
        btn_enterEnglishMeaningManually = view.findViewById(R.id.btn_englishWordEnterManually_addNewWordFragment);
        warningSection = view.findViewById(R.id.warningDiv_addNewWordFragment);
        playPronunciation = view.findViewById(R.id.imgB_playPronunciation_addNewWordFragment);
        downloadPronunciation = view.findViewById(R.id.imgB_downloadPronunciation_addNewWordFragment);
        mainDiv = view.findViewById(R.id.mainDiv_addNewWordFragment);


        addFunctionalityToButton();
    }

    private void addFunctionalityToButton(){
        //search functionality
        btn_search.setOnClickListener(View->{
            searchEnglishMeaning();
        });

        //close
        btn_cancel.setOnClickListener(View->{
            //closeBottomSheet();
            this.changeFragment.changeToHomeFragment();
        });

        //word edit view
        word.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*btn_search.setEnabled(true);
                editText_englishMeaning.setText("");
                editText_englishMeaning.setEnabled(false);
                hideWarning();*/
                downloadPronunciation.setImageTintList(ColorStateList.valueOf(Color.argb(255,0,0,0)));
                playPronunciation.setImageTintList(ColorStateList.valueOf(Color.argb(255,0,0,0)));
                playPronunciation.setEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!word.getText().toString().trim().isEmpty()){
                    showMainDiv();
                    editText_englishMeaning.setText("");
                    editText_englishMeaning.setEnabled(false);
                } else hideMainDiv();
            }
        });

        //english word edit view
        editText_englishMeaning.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btn_add.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn_add.setEnabled(!editText_englishMeaning.getText().toString().trim().isEmpty());
            }
        });

        //
        btn_enterEnglishMeaningManually.setOnClickListener(View->{
            editText_englishMeaning.setEnabled(true);
            hideWarning();
            hideProgressbar();
        });



        //image button download sound
        //download pronunciation
        downloadPronunciation.setOnClickListener(View->{
            downloadPronunciation();
        });

        //play audion
        // image button play sound
        //
        playPronunciation.setOnClickListener(View->{
            playPronunciation.setImageTintList(ColorStateList.valueOf(Color.argb(255,0,250,0)));
            if (aMp3!=null && aMp3.length!=0){
                //Pronunciation.playPronunciationOnline(audioUrlMain);
                Pronunciation.playPronunciationMp3(requireContext(),aMp3);
                //Pronunciation.playPronunciationRaw(getContext(),R.raw.schon);
            }
        });

        //
        btn_add.setOnClickListener(View->{
            addNewWord();
        });

        if (isPreset && presetEntry!=null){
            setValues();
        }

    } //end of addFunctionalityToButton()


    private void addNewWord() {
        if (isPreset){
            Log.d(TAG, "addNewWord: "+"preset is set");
            if (Data.getInstance().editEntry(presetEntry,
                    word.getText().toString().trim(),
                    new Wort(
                            word.getText().toString().trim(),
                            editText_englishMeaning.getText().toString().trim(),
                            audioUrlMain,
                            aMp3
                    )
            )){
                this.isPreset = false;
                this.presetEntry = null;
                this.changeFragment.changeToHomeFragment();
            }
        } else {
            if (Data.getInstance().addNewWord(
                    word.getText().toString().trim(),
                    new Wort(
                            word.getText().toString().trim(),
                            editText_englishMeaning.getText().toString().trim(),
                            audioUrlMain,
                            aMp3
                    )
            )){
                this.isPreset = false;
                this.presetEntry = null;
                this.changeFragment.changeToHomeFragment();
            }
        }

    }
    
    //1.1 volley is downloading the whole web-page
    //1.2 Jsoup is finding the pronunciation url in the downloaded web-page
    private void searchEnglishMeaning(){
        //check if word text view is empty
        showProgressbar();
        hideWarning();
        editText_englishMeaning.setText("");
        MakeInternetRequest.makeInternetRequestToGetEnglishMeaning(
                word.getText().toString().trim(),
                result->{
                    Log.d(TAG, "searchEnglishMeaning: "+result.toString());
                    if (result.isSuccess() && result.getResponseCode() == ResponseCodes.SUCCESS){
                        //content downloaded successfully
                        hideProgressbar();
                        //setting english meaning
                        editText_englishMeaning.setText(result.getMsg());
                    } else {
                        showWarning();
                    }
                });
    }


    //1 download url
    //1.1 download web page
    //1.2 parse web page and find pronunciation url
    //2 from .mp3 url download sound
    //2.1 download and convert
    //2.2 convert
    //2.3 call afterPronunciationDownloadSuccess() function
    private void downloadPronunciation(){
        showProgressbar();
        //1
        Pronunciation.downloadPronunciationPage(getContext(),
                word.getText().toString().trim(),
                //1.1 returns whole web page
                response->{
                    if (response!=null){
                        // 1.2 find pronunciation url
                        Pronunciation.getPronunciationUrl(getContext(),
                                response,
                                //2 download audio file convert it in byte
                                audioUrl->{
                                    if (!audioUrl.isEmpty()){
                                        shoUrl.setText(audioUrl);
                                        shoUrl.setVisibility(android.view.View.VISIBLE);
                                        audioUrlMain = audioUrl;

                                        //2.1 download
                                        Pronunciation.downloadToFile(getContext(),audioUrl,word.getText().toString().trim(),
                                                //2.2 converted byte received
                                                bytes -> {
                                                    //downloaded successfully
                                                    aMp3 = bytes;
                                                    //2.3
                                                    afterPronunciationDownloadSuccess();
                                                });

                                        //aMp3 = Pronunciation.downloadPronunciation(getContext(),audioUrl,word.getText().toString().trim());

                                    }else {
                                        hideProgressbar();
                                        shoUrl.setText(R.string.no_audio);
                                        shoUrl.setVisibility(android.view.View.VISIBLE);
                                    }
                                });
                    } else {
                        hideProgressbar();
                        shoUrl.setText(R.string.no_audio);
                        shoUrl.setVisibility(android.view.View.VISIBLE);
                    }
                });
    }

    //1 change color
    //2 enable play sound image button
    private void afterPronunciationDownloadSuccess(){
        hideProgressbar();
        downloadPronunciation.setImageTintList(ColorStateList.valueOf(Color.argb(255,0,250,0)));
        playPronunciation.setImageTintList(ColorStateList.valueOf(Color.argb(255,0,250,0)));
        //2
        playPronunciation.setEnabled(true);
    }


    private void setValues(){
        if (isPreset && presetEntry!=null){
            Wort wort = presetEntry.getValue();
            word.setText(wort.getGermanWord());
            editText_englishMeaning.setText(wort.getEnglishMeaning());
            aMp3 = wort.getPronunciation();
            if (wort.getPronunciation()!=null){
                playPronunciation.setImageTintList(ColorStateList.valueOf(Color.argb(255,0,250,0)));
                playPronunciation.setEnabled(true);
            }
        }
    }

    public void presetValues(Map.Entry<String, Wort> entry){
        this.isPreset = true;
        this.presetEntry = entry;
    }


    private void showMainDiv(){
        mainDiv.setVisibility(View.VISIBLE);
    }
    private void hideMainDiv(){
        mainDiv.setVisibility(View.GONE);
    }

    private void showProgressbar(){
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgressbar(){
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showWarning(){warningSection.setVisibility(View.VISIBLE);}
    private void hideWarning(){warningSection.setVisibility(View.GONE);}

}