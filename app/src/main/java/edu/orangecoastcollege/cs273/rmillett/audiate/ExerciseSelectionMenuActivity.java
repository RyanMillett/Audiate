package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSelectionMenuActivity extends AppCompatActivity {

    private static final String TAG = "ExerciseSelection";

    private DBHelper db;

    private Exercise mExerciseActivity;

    private String mExerciseMode;

    private ExerciseSelectionListAdapter mExerciseSelectionListAdapter;
    private List<Exercise> mAllExerciseList;

    private List<Exercise> mFilteredExerciseList;
    private ImageView mEarsImageView;
    private ImageView mSingingImageView;

    private EditText mSearchEditText;

    private ImageView[] mTypeImageViews;
    private Button mIntervalsButton;
    private Button mChordsButton;
    private Button mScalesButton;

    private LinearLayout mListItem;

    private Button[] mExerciseButtons;
    private ListView mExercisesListView;

    private TextView mExerciseDescriptionTextView;

    private Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_selection_menu);

        mExerciseActivity = new Exercise();

        // Lists and DB
        deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);
        mFilteredExerciseList = new ArrayList<>();

        mListItem = findViewById(R.id.exerciseListLinearLayout);

        // ImageViews
        mEarsImageView = findViewById(R.id.earsImageView);
        mSingingImageView = findViewById(R.id.singingImageView);

        // Search EditText
        mSearchEditText = findViewById(R.id.searchExercisesEditText);

        // ImageView array
        mTypeImageViews = new ImageView[]{mEarsImageView, mSingingImageView};

        // Buttons
        mIntervalsButton = findViewById(R.id.exercise1Button);
        mIntervalsButton.setEnabled(false);
        mChordsButton = findViewById(R.id.exercise2Button);
        mChordsButton.setEnabled(false);
        mScalesButton = findViewById(R.id.exercise3Button);
        mScalesButton.setEnabled(false);
        mStartButton = findViewById(R.id.startExerciseButton);
        mStartButton.setEnabled(false);


        // Buttons array
        mExerciseButtons = new Button[]{mIntervalsButton, mChordsButton, mScalesButton};

        // ListAdapter
        mExerciseSelectionListAdapter = new ExerciseSelectionListAdapter(this,
                R.layout.exercise_list_item, mFilteredExerciseList);

        // ListView
        mExercisesListView = findViewById(R.id.exercisesCategoriesListView);
        mExercisesListView.setAdapter(mExerciseSelectionListAdapter);
        mExercisesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise selectedItem = (Exercise) parent.getItemAtPosition(position);
                mExerciseDescriptionTextView.setText(selectedItem.getExerciseDescription());
            }
        });

        // Info TextView
        mExerciseDescriptionTextView = findViewById(R.id.exerciseDescriptionTextView);

        // This should create the return button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void startActivity(View view) {

        if (!mExerciseActivity.getExerciseName().equals("")) {

            // Declare intent
            Intent exerciseIntent;

            // Determine Exercise Mode
            switch (mExerciseActivity.getExerciseMode()) {
                case Exercise.EAR_TRAINING_EXERCISE_MODE:
                    exerciseIntent = new Intent(this, EarTrainingExerciseActivity.class);
                    break;
                case Exercise.SIGHT_SINGING_EXERCISE_MODE:
                    exerciseIntent = new Intent(this, SingingExerciseActivity.class);
                    break;
                default:
                    exerciseIntent = new Intent();
            }

            // Parcel Exercise information
            exerciseIntent.putExtra("SelectedExercise",mExerciseActivity);

            // Launch
            startActivity(exerciseIntent);
        }
        else {
            Toast.makeText(this, "Select an exercise.", Toast.LENGTH_LONG);
        }
    }

    public void exerciseSelectionHandler(View view) {
        if (view instanceof Button) updateButtonColors(view);

        mExerciseActivity.reset();

        switch (view.getId()) {
            case R.id.earsImageView: // ear training exercises
                mExerciseActivity.setExerciseMode(Exercise.EAR_TRAINING_EXERCISE_MODE);
                setExerciseButtons(true, view);
                break;
            case R.id.singingImageView: // singing exercises
                mExerciseActivity.setExerciseMode(Exercise.SIGHT_SINGING_EXERCISE_MODE);
                setExerciseButtons(true, view);
                break;
            case R.id.exerciseListLinearLayout:
                updateDescriptionTextView(view);
                return;
        }

        // update search bar
        mSearchEditText.setHint(getString(R.string.search_exercises).toLowerCase()
                + " " + mExerciseActivity.getExerciseMode().toLowerCase()
                + " " + getString(R.string.exercises).toLowerCase());

        updateListView(view);
    }


    // PRIVATE HELPER METHODS //

    private void setExerciseButtons(boolean enabled, View view) {
        updateImageViewColors(view);
        resetButtonColors();

        for (Button button : mExerciseButtons) {
            button.setEnabled(enabled);
        }

        if (enabled) {
            switch (view.getId()){
                case R.id.earsImageView:
                    mIntervalsButton.setText(getString(R.string.interval_identification_exercises_button));
                    mChordsButton.setText(getString(R.string.chord_quality_exercises_button));
                    mScalesButton.setText(getString(R.string.modeScale_exercises_button));
                    break;
                case R.id.singingImageView:
                    mIntervalsButton.setText(getString(R.string.interval_singing_exercises_button));
                    mChordsButton.setText(getString(R.string.chord_arpeggio_exercises_button));
                    mScalesButton.setText(getString(R.string.melody_singing_exercises_button));
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
                mFilteredExerciseList =
                        new ArrayList<>(db.getAllIntervalExercisesByMode(mExerciseMode));
                break;
            case R.id.exercise2Button:
                mFilteredExerciseList =
                        new ArrayList<>(db.getAllChordExercisesByMode(mExerciseMode));
                break;
            case R.id.exercise3Button:
                mFilteredExerciseList =
                        new ArrayList<>(db.getAllScaleExercisesByMode(mExerciseMode));
                break;
        }

        // update description text view
        updateDescriptionTextView(view);

        Log.i(TAG, "mFilteredExerciseList.size()->" + mFilteredExerciseList.size());
        mExerciseSelectionListAdapter.clear();
        mExerciseSelectionListAdapter.addAll(mFilteredExerciseList);
        mExerciseSelectionListAdapter.notifyDataSetChanged();
        Log.i(TAG, "mExerciseSelectionListAdapter.getCount()->" + mExerciseSelectionListAdapter.getCount());
    }

    private void updateDescriptionTextView(View view) {
        // list view items
        if (view instanceof LinearLayout) {
            LinearLayout selectedLayout = (LinearLayout) view;
            Exercise selectedExerciseActivity = (Exercise) selectedLayout.getTag();
            Log.i(TAG, selectedExerciseActivity.getExerciseName());
            mExerciseActivity = selectedExerciseActivity;
            mExerciseDescriptionTextView.setText(selectedExerciseActivity.getExerciseDescription());
        }
        // exercise buttons
        else if (view instanceof Button) {
            // TODO: clean up conditions
            switch (view.getId()) {
                case R.id.exercise1Button:
                    if (mExerciseMode.equalsIgnoreCase(Exercise.EAR_TRAINING_EXERCISE_MODE)) {
                        mExerciseDescriptionTextView.setText("Ear Intervals...");
                    }
                    else {
                        mExerciseDescriptionTextView.setText("Singing Intervals...");
                    }
                    break;
                case R.id.exercise2Button:
                    if (mExerciseMode.equalsIgnoreCase(Exercise.EAR_TRAINING_EXERCISE_MODE)) {
                        mExerciseDescriptionTextView.setText("Ear Chords...");
                    }
                    else {
                        mExerciseDescriptionTextView.setText("Singing Chords...");
                    }
                    break;
                case R.id.exercise3Button:
                    if (mExerciseMode.equalsIgnoreCase(Exercise.EAR_TRAINING_EXERCISE_MODE)) {
                        mExerciseDescriptionTextView.setText("Ear Scales...");
                    }
                    else {
                        mExerciseDescriptionTextView.setText("Singing Scales...");
                    }
                    break;
            }
        }
        // exercise type image views
        else if (view instanceof ImageView) {
            switch (view.getId()) {
                case R.id.earsImageView: // ear training exercises
                    mExerciseDescriptionTextView.setText("Ear Training...");
                    break;
                case R.id.singingImageView: // singing exercises
                    mExerciseDescriptionTextView.setText("Singing Exercises...");
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
}
