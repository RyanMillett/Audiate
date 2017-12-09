package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSelectionMenuActivity extends AppCompatActivity {

    private String mExerciseType;

    private ExerciseSelectionListAdapter mExerciseSelectionListAdapter;
    private List<ExerciseActivityType> mAllExerciseList;
    private List<ExerciseActivityType> mFilteredExerciseList;

    private ImageView mEarsButton;
    private ImageView mSingingButton;

    private Button mIntervalsButton;
    private Button mChordsButton;
    private Button mScalesButton;

    private ListView mExercisesListView;
    private TextView mExerciseDescriptionTextView;


    List<ExerciseActivityType> allEarIntervalExercises;
    List<ExerciseActivityType> allEarChordExercises;
    List<ExerciseActivityType> allEarScaleExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_selection_menu);

        // TEST LISTS //

        allEarIntervalExercises = new ArrayList<>();
        allEarIntervalExercises.add(new ExerciseActivityType(ExerciseActivityType.EXERCISE_TYPE_INTERVALS,ExerciseActivityType.EAR_TRAINING_EXERCISE,"Easy", "Desc."));
        allEarChordExercises = new ArrayList<>();
        allEarChordExercises.add(new ExerciseActivityType(ExerciseActivityType.EXERCISE_TYPE_CHORDS,ExerciseActivityType.EAR_TRAINING_EXERCISE,"Easy", "Desc."));
        allEarScaleExercises = new ArrayList<>();
        allEarScaleExercises.add(new ExerciseActivityType(ExerciseActivityType.EXERCISE_TYPE_SCALES,ExerciseActivityType.EAR_TRAINING_EXERCISE,"Easy", "Desc."));

        List<ExerciseActivityType> allSingingIntervalExercises = new ArrayList<>();
        List<ExerciseActivityType> allSingingChordExercises = new ArrayList<>();
        List<ExerciseActivityType> allSingingScaleExercises = new ArrayList<>();


        // --------- //

        mAllExerciseList = new ArrayList<>();
        mFilteredExerciseList = new ArrayList<>(mAllExerciseList);

        mExerciseSelectionListAdapter = new ExerciseSelectionListAdapter(this,
                R.layout.library_list_item, mFilteredExerciseList);

        mEarsButton = findViewById(R.id.earsImageView);
        mSingingButton = findViewById(R.id.singingImageView);

        mIntervalsButton = findViewById(R.id.exercise1Button);
        mIntervalsButton.setEnabled(false);
        mChordsButton = findViewById(R.id.exercise2Button);
        mChordsButton.setEnabled(false);
        mScalesButton = findViewById(R.id.exercise3Button);
        mScalesButton.setEnabled(false);

        mExercisesListView = findViewById(R.id.exercisesCategoriesListView);
        mExercisesListView.setAdapter(mExerciseSelectionListAdapter);

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
            case R.id.exercise1Button: // update listview
            case R.id.exercise2Button:
            case R.id.exercise3Button:
                setExerciseSelectionListAdapter(view);
                break;
        }
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

    private void setExerciseButtons(boolean enabled, View view) {
        mIntervalsButton.setEnabled(enabled);
        mChordsButton.setEnabled(enabled);
        mScalesButton.setEnabled(enabled);
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
            mIntervalsButton.setText("");
            mChordsButton.setText("");
            mScalesButton.setText("");
        }
    }

    private void setExerciseSelectionListAdapter(View view) {
        mExerciseSelectionListAdapter.clear();
        switch (view.getId()) {
            case R.id.exercise1Button:
                // update listview
                mFilteredExerciseList.clear();
                mFilteredExerciseList = allEarIntervalExercises;
//                mExerciseType.equalsIgnoreCase(ExerciseActivityType.EAR_TRAINING_EXERCISE)
//                        ? mFilteredExerciseList = db.getAllIntervalEarExercises()
//                        : mFilteredExerciseList = db.getAllIntervalSingingExercises;
                break;
            case R.id.exercise2Button:
                // update listview
                mFilteredExerciseList.clear();
                mFilteredExerciseList = allEarChordExercises;
//                mExerciseType.equalsIgnoreCase(ExerciseActivityType.EAR_TRAINING_EXERCISE)
//                        ? mFilteredExerciseList = db.getAllChordEarExercises()
//                        : mFilteredExerciseList = db.getAllChordSingingExercises;
                break;
            case R.id.exercise3Button:
                // update listview
                mFilteredExerciseList.clear();
                mFilteredExerciseList = allEarScaleExercises;
//                mExerciseType.equalsIgnoreCase(ExerciseActivityType.EAR_TRAINING_EXERCISE)
//                        ? mFilteredExerciseList = db.getAllScaleEarExercises()
//                        : mFilteredExerciseList = db.getAllScaleSingingExercises;
                break;
        }
        mExerciseSelectionListAdapter.addAll(mFilteredExerciseList);
        mExerciseSelectionListAdapter.notifyDataSetChanged();
    }
}
