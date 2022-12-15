package com.example.swg.contract;

import androidx.fragment.app.Fragment;

public interface AppContract {
    void toListEntryScreen(Fragment target);
    void toEntryScreen(Fragment target);
    void toNewEntryScreen(Fragment target);
    void cancel();
}
