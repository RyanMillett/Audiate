package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
public class ExerciseSelectionListAdapter extends ArrayAdapter<Exercise> {

    private static final String TAG = "ExerciseListAdapter";

    private Context mContext;
    private List<Exercise> mExerciseActivityList = new ArrayList<>();
    private int mResourceId;

    public ExerciseSelectionListAdapter(Context context, int rId, List<Exercise> exerciseActivityTypeList) {
        super(context, rId, exerciseActivityTypeList);
        mContext = context;
        mResourceId = rId;
        mExerciseActivityList = exerciseActivityTypeList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Exercise selectedExerciseActivity = mExerciseActivityList.get(position);
        //Log.i(TAG, "selectedExerciseActivity->" + selectedExerciseActivity.getExerciseName());

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        LinearLayout exerciseDetailsListLinearLayout
                = (LinearLayout) view.findViewById(R.id.exerciseListLinearLayout);
        TextView exerciseDetailsListNameTextView
                = view.findViewById(R.id.exerciseListNameTextView);
        TextView exerciseDetailsListDescription1TextView
                = view.findViewById(R.id.exerciseListDescription1TextView);
        TextView exerciseDetailsListDescription2TextView
                = view.findViewById(R.id.exerciseListDescription2TextView);

        // set tag
        exerciseDetailsListLinearLayout.setTag(selectedExerciseActivity);

        // Name
        exerciseDetailsListNameTextView.setText(selectedExerciseActivity.getExerciseName());

        // Mode / Materials
        exerciseDetailsListDescription1TextView.setText(selectedExerciseActivity.getExerciseMode());
        exerciseDetailsListDescription1TextView.append(" / " + selectedExerciseActivity.getExerciseMaterial());

        // Difficulty
        exerciseDetailsListDescription2TextView.setText(R.string.exercise_difficulty);
        exerciseDetailsListDescription2TextView.append(selectedExerciseActivity.getExerciseDifficultyString());

        return view;
    }
}
