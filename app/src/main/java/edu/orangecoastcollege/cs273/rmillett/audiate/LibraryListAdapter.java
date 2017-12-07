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
public class LibraryListAdapter extends ArrayAdapter<ChordScale> {

    private Context mContext;
    private List<ChordScale> mChordScaleList = new ArrayList<>();
    private int mResourceId;


    public LibraryListAdapter(Context context, int rId, List<ChordScale> chordScales) {
        super(context, rId, chordScales);
        mContext = context;
        mResourceId = rId;
        mChordScaleList = chordScales;
    }

    public LibraryListAdapter(Context context, int rId) {
        super(context, rId);
        mContext = context;
        mResourceId = rId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ChordScale selectedChordScale = mChordScaleList.get(position);

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        LinearLayout libraryListLinearLayout
                = view.findViewById(R.id.libraryListLinearLayout);
        TextView libraryListNameTextView
                = view.findViewById(R.id.libraryListNameTextView);
        TextView libraryListDescription1TextView
                = view.findViewById(R.id.libraryListDescription1TextView);
        TextView libraryListDescription2TextView
                = view.findViewById(R.id.libraryListDescription2TextView);

        libraryListLinearLayout.setTag(selectedChordScale);

        libraryListNameTextView.setText(selectedChordScale.getName());

        // TODO: handle different ChordScale types (Interval, Chord, Scale)

        return view;
    }
}
