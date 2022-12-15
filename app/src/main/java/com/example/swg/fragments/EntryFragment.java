package com.example.swg.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.swg.MainActivity;
import com.example.swg.R;
import com.example.swg.contract.AppContract;
import com.example.swg.contract.typeFragment;
import com.example.swg.model.Entry;

import java.util.ArrayList;
import java.util.List;


public class EntryFragment extends Fragment implements FragmentPhoto{

    private static final String CURRENT_ENTRY = "current_entry";

    private Entry currentEntry;

    private List<String> currentImages;
    private LinearLayout photoLayout;
    public EntryFragment() {
    }

    public static EntryFragment newInstance(Bundle bundle) {
        EntryFragment fragment = new EntryFragment();
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_ENTRY, bundle.getSerializable(CURRENT_ENTRY));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) requireActivity()).currentFragment=this;
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            currentEntry = (Entry) getArguments().getSerializable(CURRENT_ENTRY);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        dateTextView.setText(currentEntry.date);
        TextView dataTextView = view.findViewById(R.id.entryTextView);
        dataTextView.setText(currentEntry.data);
        currentImages=currentEntry.images;
        photoLayout=view.findViewById(R.id.scrollLayout);
        viewImage();
    }

    private void viewImage(){
        if(currentImages==null)
            return;
        for (String im: currentImages) {
            ImageView image=new ImageView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 10, 0, 10);
            image.setLayoutParams(lp);
            ((MainActivity) requireActivity()).saveLoader.loadImageFromStorage(image,im);
            photoLayout.addView(image);
        }
    }
    private void viewImage(String im){
        ImageView image=new ImageView(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 10, 0, 10);
        image.setLayoutParams(lp);
        ((MainActivity) requireActivity()).saveLoader.loadImageFromStorage(image,im);
        photoLayout.addView(image);

    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_take_photo:
                ((MainActivity) requireActivity()).selectPhoto();
                return true;

            case R.id.action_edit:
                Bundle arg=getArguments();
                assert arg != null;
                arg.putSerializable("type", typeFragment.EDIT);
                getAppContract().toNewEntryScreen(this);
                return true;
            case R.id.action_submit:
                currentEntry.images=currentImages;
                ((MainActivity) requireActivity()).saveLoader.updateEntry(currentEntry);
                getAppContract().toListEntryScreen(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar,menu);
    }

    final AppContract getAppContract() {
        return (AppContract) getActivity();
    }

    @Override
    public void setImage(String image) {
        if(currentImages==null)
            currentImages=new ArrayList<>();
        currentImages.add(image);
        viewImage(image);
    }
}