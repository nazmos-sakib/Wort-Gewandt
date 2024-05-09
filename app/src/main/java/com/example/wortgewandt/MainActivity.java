package com.example.wortgewandt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.example.wortgewandt.DataStorage.Data;
import com.example.wortgewandt.DataStorage.DataManagement;
import com.example.wortgewandt.ExternalRequest.VolleyNetworkRequest;
import com.example.wortgewandt.Fragments.AddNewWordFragment;
import com.example.wortgewandt.Fragments.HomeFragment;
import com.example.wortgewandt.Fragments.WordListFragment;
import com.example.wortgewandt.Interface.ChangeFragment;
import com.example.wortgewandt.Interface.DatenAnderungMitteilen;
import com.example.wortgewandt.Model.Wort;
import com.example.wortgewandt.databinding.ActivityMainBinding;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements ChangeFragment, DatenAnderungMitteilen {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private AddNewWordFragment addNewWordFragment;
    private WordListFragment wordListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init Data and DataManagement
        DataManagement.getInstance(this).readLocalData(
                Data.getInstance(this));
        //init volley
        VolleyNetworkRequest.getInstance(this);

        //fragment manager init
        fragmentManager = getSupportFragmentManager();


        initViews();
    }

    private void initViews() {
        homeFragment = new HomeFragment();
        addNewWordFragment = new AddNewWordFragment(this);
        wordListFragment = new WordListFragment(this);
        initBottomNavView();
    }
    private void initBottomNavView(){
        Log.d(TAG, "initBottomNavView: started");
        //initial selected item
        binding.bottomNavigationViewMainActivity.setSelectedItemId(R.id.navigation_home);

        binding.bottomNavigationViewMainActivity.setOnItemSelectedListener(item ->{
            int id = item.getItemId();

            if (id==R.id.navigation_home){
                Log.d(TAG, "initBottomNavView: home button is press");
                toggleFragments(homeFragment);

            } else if (id == R.id.add_new_word){
                Log.d(TAG, "initBottomNavView: Add_New_Word button is press->");
                toggleFragments(addNewWordFragment);
            } else if (id == R.id.show_all_word){
                Log.d(TAG, "initBottomNavView: WordList button is press");
                toggleFragments(wordListFragment);

            } else if (id == R.id.profile){
                //
            }

            return true;
        });
    } //end of initBottomNavView()





    private int toggleFragments(Fragment fragment){
        return this.fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView_mainActivity, fragment, null)
                .setReorderingAllowed(true)
                .commit();
    }

    @Override
    public void changeToHomeFragment() {
        toggleFragments(homeFragment);
        binding.bottomNavigationViewMainActivity.setSelectedItemId(R.id.navigation_home);

    }

    @Override
    public void changeToAddFragment(Map.Entry<String, Wort> entry){
        addNewWordFragment.presetValues(entry);
        binding.bottomNavigationViewMainActivity.setSelectedItemId(R.id.add_new_word);
    }

    @Override
    public void onAnderungen(){
        DataManagement.getInstance(this).writeLocalData( (Void)->{
            Log.d(TAG, "onAnderungen: called");
            //updateSaveWordSizeTextView();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //cancel all ongoing request
        RequestQueue requestQueue =  VolleyNetworkRequest.getInstance(this).getQueue();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}