package com.wortgewandt.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wortgewandt.Fragments.sub.GameFragment;
import com.wortgewandt.Fragments.sub.PlayAudioFragment;
import com.example.wortgewandt.R;


public class HomeFragment extends Fragment {

    private Button btn_game, btn_listen;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        initView(requireView());
    }

    private void initView(View view){
        btn_game = view.findViewById(R.id.btn_wortSpiel_homeFragment);
        btn_listen = view.findViewById(R.id.btn_listen_homeFragment);


        btn_listen.setOnClickListener(View->{
            Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag("play-pronunciation");

            if(fragment != null && fragment.isAdded())
                requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            else {
                fragment = requireActivity().getSupportFragmentManager().findFragmentByTag("play-game");
                if (fragment != null && fragment.isAdded()) {
                    requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentContainer_homeFragment, new PlayAudioFragment(), "play-pronunciation")
                        .setReorderingAllowed(true)
                        .commit();

            }
        });

        btn_game.setOnClickListener(View->{
            Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag("play-game");

            if(fragment != null && fragment.isAdded())
                requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            else {
                fragment = requireActivity().getSupportFragmentManager().findFragmentByTag("play-pronunciation");
                if (fragment != null && fragment.isAdded()) {
                    requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentContainer_homeFragment, new GameFragment(), "play-game")
                        .setReorderingAllowed(true)
                        .commit();

            }
        });


    }
}