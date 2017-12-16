package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExerciseBuilderActivity extends AppCompatActivity {

    private static final String TAG = "ExerciseBuilder";

    private Exercise mExerciseActivity;

    private DBExercises mDBExercises;

    private List<Exercise> mAllListeningExercises;
    private List<Exercise> mAllSingingExercises;
    private List<Exercise> mFilteredExerciseList;

    private ExerciseBuilderListAdapter mExerciseBuilderListAdapter;

    private ImageView mEarsImageView;
    private ImageView mSingingImageView;
    private ImageView[] mTypeImageViews;

    private EditText mSearchEditText;

    private Button mIntervalsButton;
    private Button mChordsButton;
    private Button mScalesButton;
    private Button[] mExerciseButtons;

    private ListView mExercisesListView;
    private LinearLayout mListItem;

    private TextView mExerciseDescriptionTextView;

    private Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_selection_menu);

        // Exercise to build
        mExerciseActivity = new Exercise();

        // DB
//        deleteDatabase(DBHelper.DATABASE_NAME);

        mDBExercises = new DBExercises(this);

//        mDBExercises.deleteAllExercies();
//        mDBExercises.importAllExercisesFromCSV("exercises.csv");

        // Lists
        mAllListeningExercises = new ArrayList<>(mDBExercises.getAllExercisesByMode(Exercise.EXERCISE_MODE_LISTENING));
        mAllSingingExercises = new ArrayList<>(mDBExercises.getAllExercisesByMode(Exercise.EXERCISE_MODE_SINGING));

        // Filtered List
        mFilteredExerciseList = new ArrayList<>();

        // ImageViews
        mEarsImageView = findViewById(R.id.earsImageView);
        mSingingImageView = findViewById(R.id.singingImageView);

        // ImageView array
        mTypeImageViews = new ImageView[]{mEarsImageView, mSingingImageView};

        // Search EditText
        mSearchEditText = findViewById(R.id.searchExercisesEditText);

        // Category Buttons
        mIntervalsButton = findViewById(R.id.exercise1Button); mIntervalsButton.setEnabled(false);
        mChordsButton = findViewById(R.id.exercise2Button); mChordsButton.setEnabled(false);
        mScalesButton = findViewById(R.id.exercise3Button); mScalesButton.setEnabled(false);

        // Start button
        mStartButton = findViewById(R.id.startExerciseButton);
        mStartButton.setVisibility(View.INVISIBLE);

        // Buttons array
        mExerciseButtons = new Button[]{mIntervalsButton, mChordsButton, mScalesButton};

        // ListAdapter
        mExerciseBuilderListAdapter = new ExerciseBuilderListAdapter(this,
                R.layout.exercise_list_item, mFilteredExerciseList);

        // ListView
        mExercisesListView = findViewById(R.id.exercisesCategoriesListView);
        mExercisesListView.setAdapter(mExerciseBuilderListAdapter);
        mExercisesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mExercisesListView.setSelector(R.color.colorPrimary);

                exerciseSelectionHandler(view);
            }
        });

        // List items
        mListItem = findViewById(R.id.exerciseListLinearLayout);

        // Info TextView
        mExerciseDescriptionTextView = findViewById(R.id.exerciseDescriptionTextView);
        mExerciseDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());

        // This should create the return button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    // SELECTION HANDLERS //

    public void exerciseSelectionHandler(View view) {
        if (view instanceof Button) {
            updateButtonColors(view);
            mStartButton.setVisibility(View.INVISIBLE);
            mExercisesListView.setSelector(R.color.white);
        }

        //mExerciseActivity.reset();

        switch (view.getId()) {
            case R.id.earsImageView: // ear training exercises
                mExerciseActivity.setExerciseMode(Exercise.EXERCISE_MODE_LISTENING);
                setExerciseButtons(true, view);
                break;
            case R.id.singingImageView: // singing exercises
                mExerciseActivity.setExerciseMode(Exercise.EXERCISE_MODE_SINGING);
                setExerciseButtons(true, view);
                break;
            case R.id.exerciseListLinearLayout:
                mStartButton.setVisibility(View.VISIBLE);
                LinearLayout selectedLayout = (LinearLayout) view;
                Exercise selectedExercise = (Exercise) selectedLayout.getTag();
                mExerciseActivity.setExerciseName(selectedExercise.getExerciseName());
                updateDescriptionTextView(view);
                return;
        }

        // update search bar
        mSearchEditText.setHint(getString(R.string.search_exercises).toLowerCase()
                + " " + mExerciseActivity.getExerciseMode().toLowerCase()
                + " " + getString(R.string.exercises).toLowerCase());

        updateListView(view);
    }

    public void startActivity(View view) {
        // Declare intent
        Intent exerciseIntent;

        // Determine Exercise Mode
        switch (mExerciseActivity.getExerciseMode()) {
            case Exercise.EXERCISE_MODE_LISTENING:
                exerciseIntent = new Intent(this, ExerciseActivity.class);
                break;
            default:
                exerciseIntent = new Intent();
        }

        Log.i(TAG, mExerciseActivity.getExerciseName()
                + " | " + mExerciseActivity.getExerciseMode()
                + " | "+ mExerciseActivity.getExerciseMaterial());

        // Parcel Exercise information
        exerciseIntent.putExtra("SelectedExercise",mExerciseActivity);

        // Launch Exercise
        startActivity(exerciseIntent);
    }


    // PRIVATE HELPER METHODS //

    private String getDescriptionFromTxt(Exercise exercise, String fileName) {
        String description = "Description Not Found.";
        AssetManager manager = this.getAssets();
        InputStream inputStream;
        try {
            inputStream = manager.open(fileName);
        }
        catch (IOException e) {
            e.printStackTrace();
            return description;
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();

            StringBuilder stringBuilder = new StringBuilder();

            while (line != null) {
                if (line.contains(exercise.getExerciseName())) {
                    line = bufferedReader.readLine();
                    if (line.contains(exercise.getExerciseMode())) {
                        while (!line.contains("Description")) {
                            // eat non-description lines
                            line = bufferedReader.readLine();
                        }
                        while (!line.equals("//")) {
                            stringBuilder.append(line);
                            stringBuilder.append("\n");
                            stringBuilder.append("\n");
                            line = bufferedReader.readLine();
                        }
                        return stringBuilder.toString();
                    }
                }
                else {
                    line = bufferedReader.readLine();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return description;
    }

    private void setExerciseButtons(boolean enabled, View view) {
        updateImageViewColors(view);

        mStartButton.setVisibility(View.INVISIBLE);

        if (enabled) {
            switch (view.getId()){
                case R.id.earsImageView:
                    enableAllButtons();
                    resetButtonColors();
                    mIntervalsButton.setText(getString(R.string.interval_identification_exercises_button));
                    mChordsButton.setText(getString(R.string.chord_quality_exercises_button));
                    mScalesButton.setText(getString(R.string.modeScale_exercises_button));
                    break;
                case R.id.singingImageView:
                    mIntervalsButton.setText(getString(R.string.interval_singing_exercises_button));
                        mIntervalsButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mChordsButton.setText(getString(R.string.chord_arpeggio_exercises_button));
                        mChordsButton.setEnabled(false);
                        mChordsButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    mScalesButton.setText(getString(R.string.melody_singing_exercises_button));
                        mScalesButton.setEnabled(false);
                        mScalesButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    break;
            }
        }
        else {
            for (Button button : mExerciseButtons) {
                button.setText("");
            }
        }
        updateListView(view);
    }

    private void updateListView(View view) {
        // handle lists, update list view
        mFilteredExerciseList.clear();
        switch (view.getId()) {
            case R.id.exercise1Button:
                mExerciseActivity.setExerciseMaterial(Exercise.EXERCISE_MATERIAL_INTERVALS);
                mFilteredExerciseList =
                        new ArrayList<>(mDBExercises.getAllIntervalExercisesByModeAndMaterial(
                                mExerciseActivity.getExerciseMode(),
                                Exercise.EXERCISE_MATERIAL_INTERVALS));
                break;
            case R.id.exercise2Button:
                mExerciseActivity.setExerciseMaterial(Exercise.EXERCISE_MATERIAL_CHORDS);
                mFilteredExerciseList =
                        new ArrayList<>(mDBExercises.getAllIntervalExercisesByModeAndMaterial(
                                mExerciseActivity.getExerciseMode(),
                                Exercise.EXERCISE_MATERIAL_CHORDS));
                break;
            case R.id.exercise3Button:
                mExerciseActivity.setExerciseMaterial(Exercise.EXERCISE_MATERIAL_SCALES);
                mFilteredExerciseList =
                        new ArrayList<>(mDBExercises.getAllIntervalExercisesByModeAndMaterial(
                                mExerciseActivity.getExerciseMode(),
                                Exercise.EXERCISE_MATERIAL_SCALES));
                break;
        }

        // update description text view
        updateDescriptionTextView(view);

        Log.i(TAG, "mFilteredExerciseList.size()->" + mFilteredExerciseList.size());
        mExerciseBuilderListAdapter.clear();
        mExerciseBuilderListAdapter.addAll(mFilteredExerciseList);
        mExerciseBuilderListAdapter.notifyDataSetChanged();
        Log.i(TAG, "mExerciseBuilderListAdapter.getCount()->" + mExerciseBuilderListAdapter.getCount());
    }

    private void updateDescriptionTextView(View view) {
        mExercisesListView.scrollTo(0,0);

        // list view items
        if (view instanceof LinearLayout) {
            LinearLayout selectedLayout = (LinearLayout) view;
            Exercise selectedExerciseActivity = (Exercise) selectedLayout.getTag();
            Log.i(TAG, selectedExerciseActivity.getExerciseName());
            mExerciseActivity = selectedExerciseActivity;
            mExerciseDescriptionTextView.setText(getDescriptionFromTxt(mExerciseActivity, "exercise_descriptions.txt"));
        }
        // exercise buttons
        else if (view instanceof Button) {
            // TODO: clean up conditions
            mExerciseDescriptionTextView.setText(mExerciseActivity.getExerciseMode());
            switch (view.getId()) {
                case R.id.exercise1Button:
                    mExerciseDescriptionTextView.append(getString(R.string.intervals_desc_text));
                    break;
                case R.id.exercise2Button:
                    mExerciseDescriptionTextView.append(getString(R.string.chords_desc_text));
                    break;
                case R.id.exercise3Button:
                    mExerciseDescriptionTextView.append(getString(R.string.scales_desc_text));
                    break;
            }
        }
        // exercise type image views
        else if (view instanceof ImageView) {
            switch (view.getId()) {
                case R.id.earsImageView: // ear training exercises
                    mExerciseDescriptionTextView.setText(getString(R.string.listening_exercises_description_text));
                    break;
                case R.id.singingImageView: // singing exercises
                    mExerciseDescriptionTextView.setText(getString(R.string.singing_exercises_description_text));
                    break;
            }
        }



    }

    private void updateImageViewColors(View view) {
        // handle type buttons
        for (ImageView imageView : mTypeImageViews) {
            if (imageView.getId() == view.getId()) {
                imageView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            else {
                imageView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }

    private void updateButtonColors(View view) {
        // handle exercise buttons
        for (Button button : mExerciseButtons) {
            if (button.getId() == view.getId()) {
                button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            else {
                button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }

    private void resetButtonColors() {
        for (Button button : mExerciseButtons) {
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void enableAllButtons() {
        for (Button button : mExerciseButtons) {
            button.setEnabled(true);
        }
    }
}
