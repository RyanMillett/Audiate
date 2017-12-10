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

    private static final String TAG = "LibraryListAdapter";

    private Context mContext;
    private List<ChordScale> mChordScales = new ArrayList<>();
    private int mResourceId;


    public LibraryListAdapter(Context context, int rId, List<ChordScale> chordScales) {
        super(context, rId, chordScales);
        mContext = context;
        mResourceId = rId;
        mChordScales = chordScales;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ChordScale chordScale = mChordScales.get(position);

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

        libraryListLinearLayout.setTag(chordScale);

        switch (chordScale.getSize()) {
            case ChordScale.CHORDSCALE_DEFAULT_INITIAL_SIZE: // intervals
                libraryListNameTextView.setText(chordScale.getName());
                libraryListDescription1TextView.setText("Ratio: ");
                libraryListDescription1TextView.append(chordScale.getChordMemberAtPos(1).getRatio() + " ");
                libraryListDescription1TextView.append("Size in cents: ");
                libraryListDescription1TextView.append(String.valueOf(chordScale.getChordMemberAtPos(1).getSizeInCents()));

                libraryListDescription2TextView.setText(chordScale.getDescription());

                break;
            default: // chords/scales
                libraryListNameTextView.setText(chordScale.getName());
                libraryListDescription1TextView.setText(chordScale.getDescription());

                libraryListDescription2TextView.setText(R.string.chordScale_size + chordScale.getSize());
        }





        return view;
    }
}
