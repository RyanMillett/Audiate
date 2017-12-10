package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ryan Millett
 * @version 1.0
 */
public class LibraryListAdapter extends ArrayAdapter<SoundObject> {

    private static final String TAG = "LibraryListAdapter";

    private Context mContext;
    private List<SoundObject> mSoundObjectList = new ArrayList<>();
    private int mResourceId;


    public LibraryListAdapter(Context context, int rId, List<SoundObject> soundObjects) {
        super(context, rId, soundObjects);
        mContext = context;
        mResourceId = rId;
        mSoundObjectList = soundObjects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final SoundObject selectedSoundObject = mSoundObjectList.get(position);

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        LinearLayout libraryListLinearLayout
                = view.findViewById(R.id.libraryListItemLinearLayout);
        TextView libraryListNameTextView
                = view.findViewById(R.id.libraryListNameTextView);
        TextView libraryListDescription1TextView
                = view.findViewById(R.id.libraryListDescription1TextView);
        TextView libraryListDescription2TextView
                = view.findViewById(R.id.libraryListDescription2TextView);

        libraryListLinearLayout.setTag(selectedSoundObject);

        // Name TextView
        libraryListNameTextView.setText(selectedSoundObject.getName());

        // TODO: handle different ChordScale types (Interval, Chord, Scale)
        // For intervals
        if (selectedSoundObject instanceof Note) {

        }
        // For chords and scales
        else {

        }

        return view;
    }
}
