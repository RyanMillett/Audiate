package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;

public class EarTrainingQuizActivity extends AppCompatActivity {

    private static final String TAG = "Ear Training Quiz";

    private Context mContext;

    private static final int QUESTIONS_IN_QUIZ = 10;

    private Button[] mButtons = new Button[4]; // this can be changed in the future

    private List<ChordScale> mAllChordScaleList;
    private List<ChordScale> mQuizChordScaleList;


    private List<String> mQuizIntervalsList;

    private List<String> mAllIntervalsList;
    private List<String> mAllChordsList;
    private List<String> mAllModesList;

    private List<String> mAllQuizTypeList;

    private String mCorrectInterval; // This might be wrong, it could potentially need to be a note
    private String mCorrectAnswer;

    private int mTotalGuesses;
    private int mCorrectGuesses;

    private SecureRandom rng;
    private Handler handler; // used to delay loading the next question

    private TextView mQuestionNumberTextView; // shows the current question number
    private ImageView mEarTrainingImageView; // this loads a play button with an interval behind it
    private TextView mAnswerTextView; // displays the correct answer
    private TextView mGuessTextView;

    private String mQuizType; // Stores what quiz choice is selected (interval, chord, or mode)

    private SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

        // This is where the sharedPreferences are changed.

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            mQuizType = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_default));
            resetQuiz();

            Toast.makeText(EarTrainingQuizActivity.this, R.string.restarting_quiz, Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ear_training_quiz);

        // This should set up the back button to be displayed
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // This should fill out the content in the ear training settings.
        // getFragmentManager().beginTransaction().replace(android.R.id.content, new EarTrainingSettingsActivityFragment()).commit();

        mQuizIntervalsList = new ArrayList<>(QUESTIONS_IN_QUIZ);

        rng = new SecureRandom();

        // TODO: THIS has issues
        // handler = new Handler();

        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mEarTrainingImageView = (ImageView) findViewById(R.id.earTrainingImageView);
        mGuessTextView = (TextView) findViewById(R.id.guessTextView);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);

        mButtons[0] = (Button) findViewById(R.id.button);
        mButtons[1] = (Button) findViewById(R.id.button2);
        mButtons[2] = (Button) findViewById(R.id.button3);
        mButtons[3] = (Button) findViewById(R.id.button4);


        mQuestionNumberTextView.setText(getString(R.string.question, 1, QUESTIONS_IN_QUIZ));


        mAllIntervalsList = new ArrayList<>();

        // Already handled in the DBHelper
        /*
        AssetManager am = mContext.getAssets();
        InputStream inStream;

        try {
            inStream = am.open("intervals.csv");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 4) {
                    Log.d("Audiate", "Skipping Bad CSV Row: " + Arrays.toString(fields));
                    continue;
                }
                int id = Integer.parseInt(fields[0].trim());
                String name = fields[1].trim();
                String ratio = fields[2].trim();
                double cents = Double.parseDouble(fields[3].trim());

                // TODO: Figure out what this is referred to in our program
               // mAllIntervalsList.add(new Interval(id, name, ratio, cents));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        */





        // TODO: Finish Looping through intervals
        // TODO: go to GitHub copy/paste DBHelper
        // List of all intervals
        // For Each loop List of FilteredIntervals
        // List of allIntervals

        // Call the import from CSV method from db
        // make a list populate with getAllIntervals()



        mAllChordsList = new ArrayList<>();
        mAllModesList = new ArrayList<>();

        // TODO: Fix this
        /*
        Need help with this

        for (ChordScale c : mAllChordScaleList) {
            mAllIntervalsList.add(c.getName());
            mAllChordsList.add(c.getName());
            mAllModesList.add(c.getName());
        }
        */


        // TODO: Fix this
        /*
        HANDLER?
        Different from Superheroes

        handler = new Handler() {
            @Override
            public void publish(LogRecord logRecord) {

            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        };
        */

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(mPreferenceChangeListener);

        mQuizType = preferences.getString(getString(R.string.pref_key), getString(R.string.pref_default));

        resetQuiz();

    }


    private void resetQuiz() {

        mTotalGuesses = 0;
        mCorrectGuesses = 0;
        mQuizIntervalsList.clear();

        mGuessTextView.setText(getString(R.string.guess, mQuizType));

        while(mQuizIntervalsList.size() < QUESTIONS_IN_QUIZ) {
            int randomPosition = rng.nextInt(mAllChordScaleList.size()); // Not sure if this is right
            if (!mQuizIntervalsList.contains(mAllIntervalsList.get(randomPosition)))
                mQuizIntervalsList.add(mAllIntervalsList.get(randomPosition));
        }

        loadNextQuestion();
    }

    private void loadNextQuestion() {

        // TODO: Need to remove the first question from the list
        mCorrectInterval = mQuizIntervalsList.remove(0);

        mAnswerTextView.setText("");

        int questionNumber = QUESTIONS_IN_QUIZ - mQuizIntervalsList.size();

        mQuestionNumberTextView.setText(getString(R.string.question, questionNumber, QUESTIONS_IN_QUIZ));


        if(mQuizType.equals(getString(R.string.intervals)))
        {
            mAllQuizTypeList = new ArrayList<>(mAllIntervalsList);
            // TODO: Get the name of the correct interval
            // mCorrectAnswer = mCorrectInterval
        }
        else if(mQuizType.equals(getString(R.string.chord_type)))
        {
            mAllQuizTypeList = new ArrayList<>(mAllChordsList);
            // TODO: similar to interval_type
        }
        else if(mQuizType.equals(getString(R.string.mode_type)))
        {
            mAllQuizTypeList = new ArrayList<>(mAllModesList);
            // TODO: similar to the two above
        }

        do {
            Collections.shuffle(mAllQuizTypeList);
        }
        while(mAllQuizTypeList.subList(0, mButtons.length).contains(mCorrectAnswer));

        for(int i = 0; i < mButtons.length; i++)
        {
            mButtons[i].setEnabled(true);
            mButtons[i].setText(mAllQuizTypeList.get(i));
        }

        mButtons[rng.nextInt(mButtons.length)].setText(mCorrectAnswer);

    }

    public void makeGuess(View v) {

        Button clickedButton = (Button) v;
        String guess = clickedButton.getText().toString();

        mTotalGuesses++;

        if(guess.equals(mCorrectAnswer)) {
            mCorrectGuesses++;
            for(Button b : mButtons)
                b.setEnabled(false);

            mAnswerTextView.setText(mCorrectAnswer);
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.correct_answer));

            if(mCorrectGuesses < QUESTIONS_IN_QUIZ) {

                // TODO: Fix the handler
                // Not sure why this has issues

                /*
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextQuestion();
                    }
                }, 2000); // This sets how long between the two questions

                */
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
            clickedButton.setEnabled(false);
            mAnswerTextView.setText(getString(R.string.incorrect_answer));
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.incorrect_answer));
        }

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.ear_training_quiz_settings, menu); // Accesses the menu
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent earTrainingSettingsIntent = new Intent(this, EarTrainingQuizSettingsActivity.class);
        startActivity(earTrainingSettingsIntent);

        return super.onOptionsItemSelected(item);
    }

    public static class EarTrainingSettingsActivityFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
        }
    }
    */

    /**
     * This will play the interval so the user can guess correctly
     * @param v
     */
    public void playInterval(View v)
    {

    }
}
