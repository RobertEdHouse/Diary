package com.example.swg.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.swg.MainActivity;
import com.example.swg.R;
import com.example.swg.contract.AppContract;
import com.example.swg.contract.typeFragment;
import com.example.swg.model.Entry;
import com.example.swg.model.SaveLoader;
import com.example.swg.ui.EntriesAdapter;


public class ListEntryFragment extends Fragment {
    private static final String CURRENT_ENTRY = "current_entry";

    private ListView listView;
    public ListEntryFragment() {
    }

    public static ListEntryFragment newInstance() {
        ListEntryFragment fragment = new ListEntryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView=view.findViewById(R.id.listEntry);
        fillList();
        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            EntriesAdapter e= (EntriesAdapter) adapterView.getAdapter();
            Entry currentEntry=e.getItem(i);
            toDiaryEntry(currentEntry);
        });
        ImageButton newEntryButton=view.findViewById(R.id.newEntryButton);
        newEntryButton.setOnClickListener(v-> {
                Bundle arg=getArguments();
            assert arg != null;
            arg.putSerializable("type", typeFragment.NEW);
                getAppContract().toNewEntryScreen(this);
            });
    }
    private void toDiaryEntry(Entry entry){
        Bundle arg=new Bundle();
        arg.putSerializable(CURRENT_ENTRY,entry);
        setArguments(arg);
        getAppContract().toEntryScreen(this);
    }

    public void fillList(){
        SaveLoader saveLoader=((MainActivity) requireActivity()).saveLoader;
        saveLoader.loadEntry(listView,getActivity());
    }

    final AppContract getAppContract() {
        return (AppContract) getActivity();
    }
}