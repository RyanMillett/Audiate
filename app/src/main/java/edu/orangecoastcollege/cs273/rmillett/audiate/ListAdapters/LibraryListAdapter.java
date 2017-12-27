package edu.orangecoastcollege.cs273.rmillett.audiate.ListAdapters;

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

import edu.orangecoastcollege.cs273.rmillett.audiate.Models.ChordScale;
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
                switch (selectedChordScale.getChordMemberAtPos(1).getLimit()) {
                    case -1:
                        break;
                    case 3:
                        libraryListLinearLayout
                                .setBackgroundColor(mContext.getResources().getColor(R.color.limit_3));
                        break;
                    case 5:
                        libraryListLinearLayout
                                .setBackgroundColor(mContext.getResources().getColor(R.color.limit_5));
                        break;
                    case 7:
                        libraryListLinearLayout
                                .setBackgroundColor(mContext.getResources().getColor(R.color.limit_7));
                        break;
                    case 11:
                        libraryListLinearLayout
                                .setBackgroundColor(mContext.getResources().getColor(R.color.limit_11));
                        break;
                    case 13:
                        libraryListLinearLayout
                                .setBackgroundColor(mContext.getResources().getColor(R.color.limit_13));
                        break;
                    case 17:
                        libraryListLinearLayout
                                .setBackgroundColor(mContext.getResources().getColor(R.color.limit_17));
                        break;
                    case 19:
                        libraryListLinearLayout
                                .setBackgroundColor(mContext.getResources().getColor(R.color.limit_19));
                        break;
                    default:
                        libraryListLinearLayout
                                .setBackgroundColor(mContext.getResources().getColor(R.color.high_limit));
                }

                // Determine if Meantone or Superparticular
                if (selectedChordScale.getChordMemberAtPos(1).isMeantone()) {
                    libraryListDescription2TextView.setText("Meantone");
                    libraryListLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.meantone));

                }
                else if (selectedChordScale.getChordMemberAtPos(1).isSuperparticular()) {
                    libraryListDescription2TextView.setText("Superparticular");
                }
                else {
                    libraryListDescription2TextView.setText("");
                }

                // Determine if ET
                if (selectedChordScale.getChordMemberAtPos(1).getTET()[0] != 0) {
                    libraryListLinearLayout
                            .setBackgroundColor(mContext.getResources().getColor(R.color.x_tone_et));
                }
                break;

            default: // ChordScale
                libraryListDescription1TextView.setText(R.string.scale_size);
                libraryListDescription1TextView.append(String.valueOf(selectedChordScale.getSize())
                        + " | " + selectedChordScale.getDescription());
        }

        return view;
    }
}
