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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.swg.MainActivity;
import com.example.swg.R;
import com.example.swg.contract.AppContract;
import com.example.swg.contract.typeFragment;
import com.example.swg.model.Entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewEntryFragment extends Fragment implements FragmentPhoto {

    private static final String TYPE = "type";
    private static final String CURRENT_ENTRY = "current_entry";

    private Entry newEntry=null;
    private Entry currentEntry;
    private List<String> currentImages;
    private typeFragment type;

    private EditText entryEditText;

    private LinearLayout photoLayout;
    public NewEntryFragment() {
    }


    public static NewEntryFragment newInstance(Bundle bundle) {
        NewEntryFragment fragment = new NewEntryFragment();
        Bundle args = new Bundle();
        args.putSerializable(TYPE,bundle.getSerializable(TYPE));
        if(bundle.getSerializable(TYPE)==typeFragment.EDIT){
            args.putSerializable(CURRENT_ENTRY,bundle.getSerializable(CURRENT_ENTRY));
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((MainActivity) requireActivity()).currentFragment=this;
        if (getArguments() != null) {
            type= (typeFragment) getArguments().getSerializable(TYPE);
            if(type==typeFragment.EDIT)
                currentEntry= (Entry) getArguments().getSerializable(CURRENT_ENTRY);
        }
        currentImages=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView dateTextView=view.findViewById(R.id.dateTextView);
        entryEditText=view.findViewById(R.id.entryEditText);
        photoLayout=view.findViewById(R.id.scrollLayout);
        if(type==typeFragment.EDIT){
            dateTextView.setText(currentEntry.date);
            entryEditText.setText(currentEntry.data);
            currentImages=currentEntry.images;
            viewImage();
        }
        else if(type==typeFragment.NEW){
            dateTextView.setText(makeDate());
        }
    }

    public void setImage(String image){
        if(currentImages==null)
            currentImages=new ArrayList<>();
        currentImages.add(image);
        viewImage(image);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_take_photo:
                ((MainActivity) requireActivity()).selectPhoto();
                return true;

            case R.id.action_edit:

                getAppContract().toListEntryScreen(this);
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;
            case R.id.action_submit:
                if(type==typeFragment.NEW){
                    createNewEntry(String.valueOf(entryEditText.getText()));
                    ((MainActivity) requireActivity()).saveLoader.writeNewEntry(newEntry);
                }
                if(type==typeFragment.EDIT) {
                    updateEntry();
                    ((MainActivity) requireActivity()).saveLoader.updateEntry(currentEntry);
                }
                getAppContract().toListEntryScreen(this);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void updateEntry(){
        currentEntry.data= String.valueOf(entryEditText.getText());
        currentEntry.images=currentImages;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar,menu);
        MenuItem item=menu.findItem(R.id.action_edit);
        item.setVisible(false);
    }

    private void createNewEntry(String data){
        newEntry=new Entry(data);
        newEntry.addImages(currentImages);
    }

    private void viewImage(){
        if(currentImages==null)
            return;
        for (String im:currentImages) {
            ImageView image=new ImageView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 20, 0, 20);
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
    private String makeDate(){
        Date date = Calendar.getInstance().getTime();
        String dateStr=date.getDay()+" ";
        switch (date.getMonth()){
            case 1:
                dateStr+="січня";
                break;
            case 2:
                dateStr+="лютого";
                break;
            case 3:
                dateStr+="березня";
                break;
            case 4:
                dateStr+="квітня";
                break;
            case 5:
                dateStr+="травня";
                break;
            case 6:
                dateStr+="червня";
                break;
            case 7:
                dateStr+="липня";
                break;
            case 8:
                dateStr+="серпня";
                break;
            case 9:
                dateStr+="вересня";
                break;
            case 10:
                dateStr+="жовтня";
                break;
            case 11:
                dateStr+="листопада";
                break;
            case 12:
                dateStr+="грудня";
                break;
        }
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat(" yyy");
        dateStr += dateFormat.format(Calendar.getInstance().getTime());
        return dateStr;
    }

    final AppContract getAppContract() {
        return (AppContract) getActivity();
    }
}