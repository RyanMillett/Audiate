package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EarTrainingQuizActivity extends AppCompatActivity {

    private static final String TAG = "Ear Training Quiz";

    private DBHelper mDBHelper;

    private Context mContext;

    // Handles audio
    private SoundObjectPlayer mSoundObjectPlayer;

    private static final int QUESTIONS_IN_QUIZ = 10;

    private Button[] mButtons = new Button[4];

    // This list contains all materials
    private List<ChordScale> mAllChordScaleList;

    private List<ChordScale> mAllIntervalsList;
    private List<ChordScale> mAllChordsList;
    private List<ChordScale> mAllScalesList;

    private List<ChordScale> mQuizList;

    private String mQuizType; // Stores what quiz choice is selected (interval, chord, or mode)

    private ChordScale mCorrectChordScale;
    private String mCorrectAnswer;

    private int mTotalGuesses;
    private int mCorrectGuesses;

    private SecureRandom rng;
    private Handler handler; // used to delay loading the next question

    private TextView mQuestionNumberTextView; // shows the current question number
    private ImageView mEarTrainingImageView; // this loads a play button with an interval behind it
    private TextView mAnswerTextView; // displays the correct answer

    private TextView mGuessTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ear_training_quiz);

        // This displays the back button
        // The functionality is handled in the manifest
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: FINISH THIS
        // Receive the intent
        // Unpack information
        // String that finds what type of quiz they are taking
        // Assign QuizType to whatever is packed into Intent
        // Create a filtered list based on the String
        // i.e. Historical -->

        // Initializes a new SoundObjectPlayer
        mSoundObjectPlayer = new SoundObjectPlayer();

        // TODO: In the future delete
        //
        // Database handled for practice, will remove later
        //
        deleteDatabase(DBHelper.DATABASE_NAME);
        mDBHelper = new DBHelper(this);
        mDBHelper.importAllIntervalsFromCSV("OctaveAnatomy.csv");

        mAllIntervalsList = mDBHelper.getAllIntervals();
        mAllChordsList = new ArrayList<>(4); // TODO: get all chords
        // mAllScalesList = mDBHelper.getAllScalesFromSCL();
        //
        // Delete after learning how to make parcelable classes
        //


        // Sets all the chordScales to just intervals
        mAllChordScaleList = mAllIntervalsList;

        // The TextViews and ImageView
        mEarTrainingImageView = (ImageView) findViewById(R.id.earTrainingImageView);
        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mGuessTextView = (TextView) findViewById(R.id.guessTextView);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);


        // Sets the TextView
        mQuestionNumberTextView.setText(getString(R.string.question, 1, QUESTIONS_IN_QUIZ));

        // Instantiate the Buttons
        mButtons[0] = (Button) findViewById(R.id.button);
        mButtons[1] = (Button) findViewById(R.id.button2);
        mButtons[2] = (Button) findViewById(R.id.button3);
        mButtons[3] = (Button) findViewById(R.id.button4);

        // Add Condition Statements Later
        mQuizList = new ArrayList<>(QUESTIONS_IN_QUIZ);

        rng = new SecureRandom();
        handler = new Handler();

        resetQuiz();
    }

    private void resetQuiz() {

        mTotalGuesses = 0;
        mCorrectGuesses = 0;

        // Condition handled later
        mQuizList.clear();

        mGuessTextView.setText(getString(R.string.guess, mQuizType));

        /*
        if (mQuizType.equals(getString(R.string.ear_training_selection)))
        {
            mQuizType = new ArrayList<>(mAllSuperheroNamesList);
            mCorrectAnswer = mCorrectSuperhero.getName();
        } else if (mQuizType.equals(getString(R.string.power_type)))
        {
            mAllSuperheroTypeList = new ArrayList<>(mAllSuperheroPowersList);
            mCorrectAnswer = mCorrectSuperhero.getPower();
        } else if (mQuizType.equals(getString(R.string.one_thing_type)))
        {
            mAllSuperheroTypeList = new ArrayList<>(mAllSuperheroThingsList);
            mCorrectAnswer = mCorrectSuperhero.getThing();
        }
        */

        while(mQuizList.size() < QUESTIONS_IN_QUIZ) {
            int randomPosition = rng.nextInt(mAllIntervalsList.size());
            ChordScale randomChordScale = mAllChordScaleList.get(randomPosition);

            if (!mQuizList.contains(randomChordScale))
                mQuizList.add(randomChordScale);
        }

        // This loads the next ChordScale/SoundObject
        loadNextChordScale();
    }

    private void loadNextChordScale() {

        mCorrectChordScale = mQuizList.remove(0);

        mAnswerTextView.setText("");

        int questionNumber = QUESTIONS_IN_QUIZ - mQuizList.size();
        mQuestionNumberTextView.setText(getString(R.string.question, questionNumber, QUESTIONS_IN_QUIZ));

        // Assigning the correct ChordScale
        // mSoundObjectPlayer.loadSoundObject(mCorrectChordScale);

        // Check the correct answer
        // This will need to be changed later

        mQuizList = new ArrayList<>(mAllIntervalsList);
        mCorrectAnswer = mCorrectChordScale.getName();

        // Shuffles the Collection
        do {
            Collections.shuffle(mQuizList);
        }
        while (mQuizList.subList(0, mButtons.length).contains(mCorrectAnswer));

        // Sets the button to the correct ChordScale
        for(int i = 0; i < mButtons.length; i++)
        {
            mButtons[i].setEnabled(true);
            mButtons[i].setText(mQuizList.get(i).getName());
        }

        // makes one random button the correct answer
        mButtons[rng.nextInt(mButtons.length)].setText(mCorrectAnswer);
    }

    public void makeGuess(View v) {
        Button selectedButton = (Button) v;
        String guess = selectedButton.getText().toString();

        mTotalGuesses++;

        if(guess.equals(mCorrectAnswer)) {
            mCorrectGuesses++;
            for(Button b : mButtons)
                b.setEnabled(false);

            mAnswerTextView.setText(mCorrectAnswer);
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.correct_answer));

            if(mCorrectGuesses < QUESTIONS_IN_QUIZ) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextChordScale();
                    }

                }, 2000);

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.results, mTotalGuesses, 100.0 * mCorrectGuesses / mTotalGuesses));
                builder.setPositiveButton(getString(R.string.reset_quiz), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetQuiz();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
            }
        } else {
            selectedButton.setEnabled(false);
            mAnswerTextView.setText(getString(R.string.incorrect_answer));
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.incorrect_answer));
        }
    }

    /**
     * This will play the correct SoundObject
     * so the user can listen and make a correct guess.
     * @param v
     */
    public void playChordScale(View v) {
        mSoundObjectPlayer.playSoundObject(mCorrectChordScale);
    }
}
