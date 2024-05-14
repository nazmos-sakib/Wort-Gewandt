package com.wortgewandt.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wortgewandt.Adapter.RecyclerAdapter;
import com.wortgewandt.DataStorage.Data;
import com.wortgewandt.Interface.ChangeFragment;
import com.wortgewandt.Model.Wort;
import com.wortgewandt.R;

import java.util.ArrayList;
import java.util.Map;


public class WordListFragment extends Fragment {
    private final String TAG = "WordListFragment";

    RecyclerView recView;
    private RecyclerAdapter recAdapter;

    private final ChangeFragment changeFragment;

    public WordListFragment(ChangeFragment changeFragment) {
        this.changeFragment = changeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_word_list_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews(requireView());
    }

    private void initViews(View view){
        recView = view.findViewById(R.id.root_list_wordListFragment);
        recAdapter = new RecyclerAdapter(requireContext(),this.changeFragment);
        setRecViewAdapter();
    }


    @SuppressLint("NotifyDataSetChanged")
    private void setRecViewAdapter() {
        recView.setAdapter(recAdapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        //list approach
        //covert Multimap into List with multiList built in function entries()
        ArrayList<Map.Entry<String, Wort>> wordEntries = new ArrayList<>(Data.getInstance().getWordList().entries());
        recAdapter.setAdapterData(wordEntries);
        recAdapter.notifyDataSetChanged();
    }




}