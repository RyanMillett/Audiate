package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSelectionMenuActivity extends AppCompatActivity {

    private static final String TAG = "ExerciseSelection";

    private DBHelper db;

    private ExerciseActivityType mExerciseActivityType;

    private String mExerciseType;

    private ExerciseSelectionListAdapter mExerciseSelectionListAdapter;
    private List<ExerciseActivityType> mAllExerciseList;

    private List<ExerciseActivityType> mFilteredExerciseList;
    private ImageView mEarsImageView;
    private ImageView mSingingImageView;

    private ImageView[] mTypeImageViews;
    private Button mIntervalsButton;
    private Button mChordsButton;
    private Button mScalesButton;


    private Button[] mExerciseButtons;
    private ListView mExercisesListView;

    private TextView mExerciseDescriptionTextView;
    // TEST LISTS //
    List<ExerciseActivityType> allEarIntervalExercises;
    List<ExerciseActivityType> allEarChordExercises;
    List<ExerciseActivityType> allEarScaleExercises;
    // --------- //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_selection_menu);

        mExerciseActivityType = new ExerciseActivityType();

        // Lists and DB
        deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);
        mFilteredExerciseList = new ArrayList<>();

        // TEST LISTS //
        allEarIntervalExercises = new ArrayList<>();
        allEarIntervalExercises.add(new ExerciseActivityType("Harmonic" + ExerciseActivityType.EXERCISE_TYPE_INTERVALS,ExerciseActivityType.EAR_TRAINING_EXERCISE,"Easy", "Desc."));
        allEarIntervalExercises.add(new ExerciseActivityType("Historical" + ExerciseActivityType.EXERCISE_TYPE_INTERVALS,ExerciseActivityType.EAR_TRAINING_EXERCISE,"Intermediate", "Desc."));
        allEarChordExercises = new ArrayList<>();
        allEarChordExercises.add(new ExerciseActivityType(ExerciseActivityType.EXERCISE_TYPE_CHORDS,ExerciseActivityType.EAR_TRAINING_EXERCISE,"Easy", "Desc."));
        allEarScaleExercises = new ArrayList<>();
        allEarScaleExercises.add(new ExerciseActivityType(ExerciseActivityType.EXERCISE_TYPE_SCALES,ExerciseActivityType.EAR_TRAINING_EXERCISE,"Easy", "Desc."));

        List<ExerciseActivityType> allSingingIntervalExercises = new ArrayList<>();
        List<ExerciseActivityType> allSingingChordExercises = new ArrayList<>();
        List<ExerciseActivityType> allSingingScaleExercises = new ArrayList<>();
        // --------- //


        // ImageViews
        mEarsImageView = findViewById(R.id.earsImageView);
        mSingingImageView = findViewById(R.id.singingImageView);

        // ImageView array
        mTypeImageViews = new ImageView[]{mEarsImageView, mSingingImageView};

        // Buttons
        mIntervalsButton = findViewById(R.id.exercise1Button);
        mIntervalsButton.setEnabled(false);
        mChordsButton = findViewById(R.id.exercise2Button);
        mChordsButton.setEnabled(false);
        mScalesButton = findViewById(R.id.exercise3Button);
        mScalesButton.setEnabled(false);

        // Buttons array
        mExerciseButtons = new Button[]{mIntervalsButton, mChordsButton, mScalesButton};

        // ListAdapter
        mExerciseSelectionListAdapter = new ExerciseSelectionListAdapter(this,
                R.layout.library_list_item, mFilteredExerciseList);

        // ListView
        mExercisesListView = findViewById(R.id.exercisesCategoriesListView);
        mExercisesListView.setAdapter(mExerciseSelectionListAdapter);

        // Info TextView
        mExerciseDescriptionTextView = findViewById(R.id.exerciseDescriptionTextView);

        // This should create the return button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void exerciseSelectionHandler(View view) {
        switch (view.getId()) {
            case R.id.earsImageView: // ear training exercises
                mExerciseType = ExerciseActivityType.EAR_TRAINING_EXERCISE;
                setExerciseButtons(true, view);
                break;
            case R.id.singingImageView: // singing exercises
                mExerciseType = ExerciseActivityType.SIGHT_SINGING_EXERCISE;
                setExerciseButtons(true, view);
                break;
            case R.id.exerciseListLinearLayout:
                updateDescriptionTextView(view);
                break;
        }
        updateListView(view);
    }

    public void startActivity(View view) {
        // TODO: this method
        // starts an exercise activity based on selection
        // button is enabled only if a quiz type and option is selected

        // If (Button Clicked ??? && List Type clicked ???) send x String
        // Unpack string in EarTrainingQuizActivity


        // This will be changed later
        Intent launchEarTrainingQuiz = new Intent(this, EarTrainingQuizActivity.class);

        // Will putExtra which contains a string

        startActivity(launchEarTrainingQuiz);
    }

    public void help(View view) {
        // TODO: this method
        Intent launchHelp = new Intent(this, HelpActivity.class);
        startActivity(launchHelp);
        // displays a toast or launches text activity with help information
    }

    // PRIVATE HELPER METHODS //

    private void setExerciseButtons(boolean enabled, View view) {
        updateImageViewColors(view);
        updateButtonColors(view);

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
        //if (view instanceof LinearLayout) return;

        // handle button colors
        updateButtonColors(view);

        // handle lists, update list view
        mFilteredExerciseList.clear();
        switch (view.getId()) {
            case R.id.exercise1Button:
                mFilteredExerciseList = new ArrayList<>(allEarIntervalExercises);
//                mExerciseType.equalsIgnoreCase(ExerciseActivityType.EAR_TRAINING_EXERCISE)
//                        ? mFilteredExerciseList = db.getAllIntervalEarExercises()
//                        : mFilteredExerciseList = db.getAllIntervalSingingExercises;
                break;
            case R.id.exercise2Button:
                mFilteredExerciseList = new ArrayList<>(allEarChordExercises);
//                mExerciseType.equalsIgnoreCase(ExerciseActivityType.EAR_TRAINING_EXERCISE)
//                        ? mFilteredExerciseList = db.getAllChordEarExercises()
//                        : mFilteredExerciseList = db.getAllChordSingingExercises;
                break;
            case R.id.exercise3Button:
                mFilteredExerciseList = new ArrayList<>(allEarScaleExercises);
//                mExerciseType.equalsIgnoreCase(ExerciseActivityType.EAR_TRAINING_EXERCISE)
//                        ? mFilteredExerciseList = db.getAllScaleEarExercises()
//                        : mFilteredExerciseList = db.getAllScaleSingingExercises;
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
            ExerciseActivityType selectedExerciseActivity = (ExerciseActivityType) selectedLayout.getTag();
            Log.i(TAG, selectedExerciseActivity.getExerciseName());
            mExerciseActivityType = selectedExerciseActivity;
            mExerciseDescriptionTextView.setText(selectedExerciseActivity.getExerciseDescription());
        }
        // exercise buttons
        else if (view instanceof Button) {
            // TODO: clean up conditions
            switch (view.getId()) {
                case R.id.exercise1Button:
                    if (mExerciseType.equalsIgnoreCase(ExerciseActivityType.EAR_TRAINING_EXERCISE)) {
                        mExerciseDescriptionTextView.setText("Ear Intervals...");
                    }
                    else {
                        mExerciseDescriptionTextView.setText("Singing Intervals...");
                    }
                    break;
                case R.id.exercise2Button:
                    if (mExerciseType.equalsIgnoreCase(ExerciseActivityType.EAR_TRAINING_EXERCISE)) {
                        mExerciseDescriptionTextView.setText("Ear Chords...");
                    }
                    else {
                        mExerciseDescriptionTextView.setText("Singing Chords...");
                    }
                    break;
                case R.id.exercise3Button:
                    if (mExerciseType.equalsIgnoreCase(ExerciseActivityType.EAR_TRAINING_EXERCISE)) {
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
}
