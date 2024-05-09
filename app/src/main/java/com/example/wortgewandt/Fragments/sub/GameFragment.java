package com.example.wortgewandt.Fragments.sub;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;


import com.example.wortgewandt.DataStorage.Data;
import com.example.wortgewandt.Model.Wort;
import com.example.wortgewandt.R;
import com.example.wortgewandt.Utility.Pronunciation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class GameFragment extends Fragment {

    private ArrayList<Map.Entry<String, Wort>> wordList ;
    HashMap<Integer,Wort> words;

    private Button btn11,btn12, btn21,btn22,btn31,btn32,btn41,btn42,btn51,btn52,btn61,btn62;
    Button leftSelectedButton = null;
    Button rightSelectedButton = null;

    int[] leftColumns = {11,21,31,41,51,61};
    int[] rightColumns = {12,22,32,42,52,62};
    int numberOfCorrectAns = 0;


    public GameFragment() {
        // Required empty public constructor
    }

    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }

        wordList = new ArrayList<>(Data.getInstance().getWordList().entries());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        //start story point1 (one button select other not select) -----------
        initViews(view);
        // end story point1

        return view;
    }

    private void initViews(View view){
        btn11 = view.findViewById(R.id.button11);
        btn12 = view.findViewById(R.id.button12);
        btn21 = view.findViewById(R.id.button21);
        btn22 = view.findViewById(R.id.button22);
        btn31 = view.findViewById(R.id.button31);
        btn32 = view.findViewById(R.id.button32);
        btn41 = view.findViewById(R.id.button41);
        btn42 = view.findViewById(R.id.button42);
        btn51 = view.findViewById(R.id.button51);
        btn52 = view.findViewById(R.id.button52);
        btn61 = view.findViewById(R.id.button61);
        btn62 = view.findViewById(R.id.button62);
    }

    @Override
    public void onStart() {
        super.onStart();

        initGameButtons();
        doMagic();
    }
    public void initButtons(){

        // left columns -----------------------------
        // right columns ----------------------------
        initGameButtons();
    }

    void initGameButtons(){
        //left column
        addClickListenerToButton(btn11,true);
        addClickListenerToButton(btn21,true);
        addClickListenerToButton(btn31,true);
        addClickListenerToButton(btn41,true);
        addClickListenerToButton(btn51,true);
        addClickListenerToButton(btn61,true);

        //right column
        addClickListenerToButton(btn12,false);
        addClickListenerToButton(btn22,false);
        addClickListenerToButton(btn32,false);
        addClickListenerToButton(btn42,false);
        addClickListenerToButton(btn52,false);
        addClickListenerToButton(btn62,false);
    }

    private void doMagic(){
        //AudioDownloadTracker.reset();

        //step 1: shuffle columns
        Integer[] leftColumn = shuffleArray(new Integer[]{11,21,31,41,51,61});
        Integer[] rightColumn = shuffleArray(new Integer[]{12,22,32,42,52,62});

        List<Integer> randomEntries = generateUniqueRandomNumbers(Math.max(wordList.size(), 6));

        words = new HashMap<>();
        //Object[] key = words.keySet().toArray();
        for (int i =0;i<leftColumn.length;i++){
            words.put(leftColumn[i],wordList.get(randomEntries.get(i)).getValue());
            getButtonByNumber(leftColumn[i]).setText(wordList.get(randomEntries.get(i)).getValue().getGermanWord() );
            getButtonByNumber(rightColumn[i]).setText(wordList.get(randomEntries.get(i)).getValue().getEnglishMeaning());
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkCorrectAnswer()  {
        if (leftSelectedButton!=null  && rightSelectedButton!=null){

            if (getClickedButtonId(leftSelectedButton.getId())!=null){

                Wort word = words.get(getClickedButtonId(leftSelectedButton.getId()));

                // correct word mapping
                if(word.getGermanWord().equals(leftSelectedButton.getText()) &&
                        word.getEnglishMeaning()
                                .equals(rightSelectedButton.getText())){

                    ++numberOfCorrectAns;

                    leftSelectedButton.setBackground(requireContext().getDrawable(R.drawable.button_style_complete));
                    rightSelectedButton.setBackground(requireContext().getDrawable(R.drawable.button_style_complete));
                    leftSelectedButton.setOnClickListener(null);
                    rightSelectedButton.setOnClickListener(null);

                } else {// wrong word mapping

                    AlertDialog wrongAnswerDialog = new AlertDialog.Builder(requireContext())
                            .create();

                    // Inflate the view containing the EditText and Button
                    LayoutInflater inflater = LayoutInflater.from(requireContext());
                    View view = inflater.inflate(R.layout.alartdialog_warning, null);

                    wrongAnswerDialog.setView(view);
                    // Create and show the AlertDialog
                    wrongAnswerDialog.show();
                    //step 7: halt all execution for 800ms
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // do stuff
                            wrongAnswerDialog.dismiss();
                        }
                    }, 800);

                    //step 8: set button background to default
                    leftSelectedButton.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
                    rightSelectedButton.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));

                }
                //step 8: deselect both button
                //for both case wrong answer or right answer reset selected button
                leftSelectedButton= null;
                rightSelectedButton=null;

                //step 10:
                if (numberOfCorrectAns>5){
                    numberOfCorrectAns = 0;
                    buttonResetBackgroundColor();
                    doMagic();
                    initGameButtons();
                }
            }


        }
    }

    private Button getButtonByNumber(int n){
        return switch (n) {
            case 11 -> btn11;
            case 12 -> btn12;
            case 21 -> btn21;
            case 22 -> btn22;
            case 31 -> btn31;
            case 32 -> btn32;
            case 41 -> btn41;
            case 42 -> btn42;
            case 51 -> btn51;
            case 52 -> btn52;
            case 61 -> btn61;
            case 62 -> btn62;
            default -> null;
        };
    }

    @SuppressLint("NonConstantResourceId")
    private Integer getClickedButtonId(int n){
        return switch (n) {
            case R.id.button11 -> 11;
            case R.id.button21 -> 21;
            case R.id.button31 -> 31;
            case R.id.button41 -> 41;
            case R.id.button51 -> 51;
            case R.id.button61 -> 61;
            default -> null;
        };
    }

    private Integer[] shuffleArray(Integer [] intArray){
        List<Integer> intList = Arrays.asList(intArray);
        Collections.shuffle(intList);
        return intList.toArray(intArray);
    }

    private List<Integer> generateUniqueRandomNumbers(int maxValue){
        Set<Integer> uniqueNumbers = new HashSet<>();
        Random random = new Random();

        while (uniqueNumbers.size() < 6) {
            int randomValue = random.nextInt(maxValue) ;
            uniqueNumbers.add(randomValue);
        }
        return new ArrayList<>(uniqueNumbers);
    }

    private void generateRandomWords(List<Integer> randomEntries){
        Map<Integer,Wort> words = new HashMap<>();
        for (int i=0;i<randomEntries.size();i++){
            words.put(i,wordList.get(randomEntries.get(i)).getValue());
        }
        //RandomWordDataHolder.setWordList(words);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void buttonResetBackgroundColor(){
        btn11.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
        btn12.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
        btn21.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
        btn22.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
        btn31.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
        btn32.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
        btn41.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
        btn42.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
        btn51.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
        btn52.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
        btn61.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
        btn62.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    void addClickListenerToButton(Button btn, boolean isLeftBtn){
        btn.setOnClickListener(View-> {
            if ((btn == leftSelectedButton) || (btn == rightSelectedButton)) {
                // Deselect the button
                btn.setSelected(false);
                btn.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));

                leftSelectedButton = isLeftBtn ? null : leftSelectedButton;
                rightSelectedButton = !isLeftBtn ? null : rightSelectedButton;
            } else {
                // Deselect the previously selected button (if any)
                if (leftSelectedButton != null && isLeftBtn) {
                    leftSelectedButton.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
                    leftSelectedButton.setSelected(false);
                } else if (rightSelectedButton != null && !isLeftBtn) {
                    rightSelectedButton.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
                    rightSelectedButton.setSelected(false);
                }


                // Select the clicked button
                btn.setSelected(true);
                btn.setBackground(requireContext().getDrawable(R.drawable.button_style_on_select));

                leftSelectedButton = isLeftBtn ? btn : leftSelectedButton;
                rightSelectedButton = !isLeftBtn ? btn : rightSelectedButton;
                //look for correct solution
                checkCorrectAnswer();

            }
            //story point 4
            playSound(btn.getId());
        });

    }

    private void playSound(int id) {
        //step 1: get row from button id
        if (getClickedButtonId(id)!=null){
            Wort word = words.get(getClickedButtonId(id));
            if (word.getPronunciation()!=null){
                Pronunciation.playPronunciationMp3(requireContext(),word.getPronunciation());
            }
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void toggleBackground(Button button){
        Drawable normal = requireContext().getDrawable(R.drawable.button_style_normal);
        Drawable currentDrawable = button.getBackground();
        if (areDrawablesIdentical(normal,currentDrawable)){
            button.setBackground(requireContext().getDrawable(R.drawable.button_style_on_select));
        } else {
            button.setBackground(requireContext().getDrawable(R.drawable.button_style_normal));
        }
    }

    //compare drawable
    public static boolean areDrawablesIdentical(Drawable drawableA, Drawable drawableB) {
        Drawable.ConstantState stateA = drawableA.getConstantState();
        Drawable.ConstantState stateB = drawableB.getConstantState();
        // If the constant state is identical, they are using the same drawable resource.
        // However, the opposite is not necessarily true.
        return (stateA != null && stateB != null && stateA.equals(stateB))
                || getBitmap(drawableA).sameAs(getBitmap(drawableB));
    }

    //helper function to compare drawable
    public static Bitmap getBitmap(Drawable drawable) {
        Bitmap result;
        if (drawable instanceof BitmapDrawable) {
            result = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            // Some drawables have no intrinsic width - e.g. solid colours.
            if (width <= 0) {
                width = 1;
            }
            if (height <= 0) {
                height = 1;
            }

            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return result;
    }




}