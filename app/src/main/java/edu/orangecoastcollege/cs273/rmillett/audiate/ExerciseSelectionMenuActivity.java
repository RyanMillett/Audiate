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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_selection_menu);

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
                setButtons(true, view);
                break;
            case R.id.singingImageView: // singing exercises
                setButtons(true, view);
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

    private void setButtons(boolean enabled, View view) {
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
}
