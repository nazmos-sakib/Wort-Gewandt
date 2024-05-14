package com.wortgewandt.Interface;

import com.wortgewandt.Model.Wort;

import java.util.Map;

public interface ChangeFragment {
    void changeToHomeFragment();
    void changeToAddFragment(Map.Entry<String, Wort> entry);
}
