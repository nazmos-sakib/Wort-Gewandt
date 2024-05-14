package com.wortgewandt.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.wortgewandt.DataStorage.Data;
import com.wortgewandt.Interface.ChangeFragment;
import com.wortgewandt.Model.Wort;
import com.wortgewandt.R;
import com.wortgewandt.Utility.Pronunciation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private ArrayList<Map.Entry<String, Wort>> recArrayList = new ArrayList<>();
    private final WeakReference<Context> context;

    private final ChangeFragment changeFragment;

    public RecyclerAdapter(Context context, ChangeFragment changeFragment) {
        // Private constructor to prevent instantiation
        this.context = new WeakReference<>(context);
        this.changeFragment = changeFragment;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_word_list,parent,false);

        return  new ViewHolder(view);    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int index = position;

        //set values accordingly
        Wort wort = recArrayList.get(index).getValue();
        holder.tv_germanWord.setText(wort.getGermanWord());
        holder.tv_englishMeaning.setText(wort.getEnglishMeaning());
        holder.checkBox.setChecked(wort.isSelected());

        byte[] pronunciation = wort.getPronunciation();
        //if pronunciation is available change color to green
        holder.playAudio.setImageTintList(ColorStateList.valueOf(Color.argb(255,0,
                //toggle green color
                pronunciation!=null?255:0,
                0)));

        //set functionality
        holder.playAudio.setOnClickListener(View->{
            if (pronunciation!=null){
                Pronunciation.playPronunciationMp3(context.get(), pronunciation);
            }
        });

        holder.deleteBtn.setOnClickListener(View->{
            if (Data.getInstance().deleteEntry(recArrayList.get(index))){
                setAdapterData(new ArrayList<>(Data.getInstance().getWordList().entries()));
                //notifyDataSetChanged();
            }
        });

        holder.editBtn.setOnClickListener(View->{
            changeFragment.changeToAddFragment(recArrayList.get(index));
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                wort.setSelected(b);
                Data.getInstance().editEntry(recArrayList.get(index),
                        wort.getGermanWord(),
                        wort);
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return recArrayList.size();
    }

    //getting clicked data
    public Map.Entry<String,Wort> getItemData(int position) {
        return recArrayList.get(position);
    }



    //updating the data of the recView array
    @SuppressLint("NotifyDataSetChanged")
    public void setAdapterData(ArrayList<Map.Entry<String, Wort>> object) {
        this.recArrayList = object;
        notifyDataSetChanged();
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_germanWord,tv_englishMeaning;
        public ImageButton playAudio,editBtn,deleteBtn;

        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tv_germanWord = itemView.findViewById(R.id.tv_germanWord_cardView);
            tv_englishMeaning = itemView.findViewById(R.id.tv_englishMeaning_cardView);
            playAudio = itemView.findViewById(R.id.btn_playAudio_cardView);
            deleteBtn = itemView.findViewById(R.id.img_delete_cardView);
            editBtn = itemView.findViewById(R.id.img_editButton_cardView);
            checkBox = itemView.findViewById(R.id.checkBox_cardView);
        }
    }
}
