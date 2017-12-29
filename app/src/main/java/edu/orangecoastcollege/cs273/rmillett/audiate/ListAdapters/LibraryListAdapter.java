package edu.orangecoastcollege.cs273.rmillett.audiate.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.orangecoastcollege.cs273.rmillett.audiate.Models.ChordScale;
import edu.orangecoastcollege.cs273.rmillett.audiate.Models.Note;
import edu.orangecoastcollege.cs273.rmillett.audiate.R;

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
        final ChordScale selectedChordScale = mChordScales.get(position);

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

        libraryListLinearLayout.setTag(selectedChordScale);

        libraryListNameTextView.setText(selectedChordScale.getName());

        // Determine Interval or ChordScale
        switch (selectedChordScale.getSize()) {
            case 2: // interval
                libraryListDescription1TextView.setText(selectedChordScale.getDescription());

                // Determine limit (if applicable)
                libraryListLinearLayout.setBackgroundColor(mContext.getResources()
                        .getColor(findLimitColorID(selectedChordScale.getChordMemberAt(1).getLimit())));

                // Determine if Meantone or Superparticular
                libraryListDescription2TextView.setText(determineMeanOrSuper(selectedChordScale.getChordMemberAt(1)));

                if (selectedChordScale.getChordMemberAt(1).isMeantone()) {
                    libraryListLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.meantone));
                }

                // Determine if ET
                if (selectedChordScale.getChordMemberAt(1).getTET()[0] != 0) {
                    libraryListLinearLayout
                            .setBackgroundColor(mContext.getResources().getColor(R.color.x_tone_et));
                }
                break;

            default: // ChordScale
                libraryListDescription1TextView.setText(R.string.scale_size);
                libraryListDescription1TextView.append(String.valueOf(selectedChordScale.getSize())
                        + " | " + selectedChordScale.getDescription());

                // Determine limit (if applicable)
                if (selectedChordScale.getDescription().contains("-limit")) {
                    //Log.i(TAG, "Limit Found");
                    libraryListLinearLayout.setBackgroundColor(mContext.getResources().getColor(findScaleLimitColorID(selectedChordScale)));
                }

                // Determine if ET
                if (selectedChordScale.getDescription().toUpperCase().contains("ET")
                        || selectedChordScale.getDescription().toUpperCase().contains("EQUAL")
                        || selectedChordScale.getDescription().toUpperCase().contains("E.T.")) {
                    libraryListLinearLayout
                            .setBackgroundColor(mContext.getResources().getColor(R.color.x_tone_et));
                }

        }

        return view;
    }

    private int findScaleLimitColorID(ChordScale chordScale) {
        int i = chordScale.getDescription().indexOf("-limit");
        int j = i;
        while (chordScale.getDescription().charAt(i) != ' ' && i > 0) {
            i--;
        }
        return findLimitColorID(Integer.parseInt(chordScale.getDescription().substring(i,j).trim()));
    }

    private int findLimitColorID(int limit) {
        switch (limit) {
            case -1:
                return R.color.white;
            case 3:
                return (R.color.limit_3);
            case 5:
                return (R.color.limit_5);
            case 7:
                return (R.color.limit_7);
            case 11:
                return (R.color.limit_11);
            case 13:
                return (R.color.limit_13);
            case 17:
                return (R.color.limit_17);
            case 19:
                return (R.color.limit_19);
            default:
                return (R.color.high_limit);
        }
    }

    private String determineMeanOrSuper(Note note) {
        if (note.isMeantone()) {
            return "Meantone";
        }
        else if (note.isSuperparticular()) {
            return "Superparticular";
        }
        else {
            return "";
        }
    }
}
