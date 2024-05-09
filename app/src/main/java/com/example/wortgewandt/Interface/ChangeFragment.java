package com.example.wortgewandt.Interface;

import com.example.wortgewandt.Model.ResponseCodes;
import com.example.wortgewandt.Model.Wort;

import java.util.Map;

public interface ChangeFragment {
    void changeToHomeFragment();
    void changeToAddFragment(Map.Entry<String, Wort> entry);
}
