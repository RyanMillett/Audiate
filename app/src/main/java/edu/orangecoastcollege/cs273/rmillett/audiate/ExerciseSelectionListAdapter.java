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
public class ExerciseSelectionListAdapter extends ArrayAdapter<ExerciseActivityType> {

    private Context mContext;
    private List<ExerciseActivityType> mExerciseActivityList = new ArrayList<>();
    private int mResourceId;

    public ExerciseSelectionListAdapter(Context context, int rId, List<ExerciseActivityType> exerciseActivityTypeList) {
        super(context, rId, exerciseActivityTypeList);
        mContext = context;
        mResourceId = rId;
        mExerciseActivityList = exerciseActivityTypeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ExerciseActivityType selectedExerciseActivityType = mExerciseActivityList.get(position);

        // TODO: get exercise modes and descriptions
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        LinearLayout exerciseDetailsListLinearLayout
                = view.findViewById(R.id.libraryListItemLinearLayout);
        TextView exerciseDetailsListNameTextView
                = view.findViewById(R.id.libraryListNameTextView);
        TextView exerciseDetailsListDescription1TextView
                = view.findViewById(R.id.libraryListDescription1TextView);
        TextView exerciseDetailsListDescription2TextView
                = view.findViewById(R.id.libraryListDescription2TextView);

        exerciseDetailsListLinearLayout.setTag(selectedExerciseActivityType);

        exerciseDetailsListNameTextView.setText(selectedExerciseActivityType.getExerciseName());
        exerciseDetailsListDescription1TextView.setText(selectedExerciseActivityType.getExerciseType());
        exerciseDetailsListDescription2TextView.setText(selectedExerciseActivityType.getDifficulty());

        return view;
    }
}
