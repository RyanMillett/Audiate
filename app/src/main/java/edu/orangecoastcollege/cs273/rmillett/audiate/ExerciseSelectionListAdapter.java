package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ryan Millett
 * @version 1.0
 */
public class ExerciseSelectionListAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> mExerciseModesList = new ArrayList<>();
    private int mResourceId;

    public ExerciseSelectionListAdapter(Context context, int rId, List<String> exerciseModesList) {
        super(context, rId, exerciseModesList);
        mContext = context;
        mResourceId = rId;
        mExerciseModesList = exerciseModesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);

        // TODO: get exercise modes and descriptions
    }
}
