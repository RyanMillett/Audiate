package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    /**
     * int constant value representing the default duration in milliseconds for single-notes and
     * block chords
     */
    public static final int DEFAULT_BLOCK_LENGTH_MILLISECONDS
            = SoundObject.DEFAULT_DURATION_MILLISECONDS;

    /**
     * int constant value representing the defaulting duration in milliseconds for sequences (e.g.
     * arpeggios, scales, chord sequences, etc...)
     */
    public static final int DEFAULT_ARP_LENGTH_MILLISECONDS = 500;

    // DB and lists
    private DBHelper db;
    private List<ChordScale> allIntervalsList;
    private List<ChordScale> allChordsList; // TODO: construct from scratch
    private List<ChordScale> allScalesList;
    private List<ChordScale> filteredChordScaleList;

    private EditText setFundamentalEditText;
    private TextView intervalDisplayTextView;
    private TextView selectionDisplayTextView;
    private ListView libraryListView;
    private Spinner selectMaterialSpinner;
    private Spinner sortBySpinner;

    // playback mode Radio Group
    private RadioButton mode1RadioButton;
    private RadioButton mode2RadioButton;
    private RadioButton mode3RadioButton;
    private RadioButton mode4RadioButton;

    private CheckBox aux1CheckBox;
    private CheckBox aux2CheckBox;

    private LibraryListAdapter mLibraryListAdapter;

    private Note fundamental;
    private ChordScale chord;

    private SoundObjectPlayer mSoundObjectPlayer;

    /**
     * Creates an instance of <code>LibraryActivity</code> in the view
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);
        db.importIntervalsFromCSV("intervals.csv");

        allIntervalsList = db.getAllIntervals();
        allScalesList = db.importScalesFromSCL();

        setFundamentalEditText = findViewById(R.id.setFundamentalFreqEditText);
        intervalDisplayTextView = findViewById(R.id.libraryListNameTextView);
        selectionDisplayTextView = findViewById(R.id.selectionDescriptionTextView);
        selectMaterialSpinner = findViewById(R.id.materialSelectionSpinner);
        sortBySpinner = findViewById(R.id.sortMaterialSelectionSpinner);

        libraryListView = (ListView) findViewById(R.id.libraryListView);
        filteredChordScaleList = new ArrayList<>(allIntervalsList);
        mLibraryListAdapter = new LibraryListAdapter(this,
                R.layout.audition_room_list_item, filteredChordScaleList);
        libraryListView.setAdapter(mLibraryListAdapter);

        // spinner adapters
        ArrayAdapter<String> selectMaterialSpinnerAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getAllMusicalMaterials());
        selectMaterialSpinner.setAdapter(selectMaterialSpinnerAdapter);

        ArrayAdapter<String> sortMaterialBySpinnerAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getAllSortCriteria());
        sortBySpinner.setAdapter(sortMaterialBySpinnerAdapter);

        // playback settings group
        mode1RadioButton = findViewById(R.id.mode1RadioButton);
        mode1RadioButton.setChecked(true); // Default playback mode
        mode2RadioButton = findViewById(R.id.mode2RadioButton);
        mode3RadioButton = findViewById(R.id.mode3RadioButton);
        mode4RadioButton = findViewById(R.id.mode4RadioButton);

        aux1CheckBox = findViewById(R.id.aux1CheckBox);
        aux2CheckBox = findViewById(R.id.aux2CheckBox);

        fundamental = new Note("Fundamental");
        chord = new ChordScale();

        mSoundObjectPlayer = new SoundObjectPlayer();

    }

    // TODO: make this dynamic——currently hard-coded for testing purposes
    private String[] getAllSortCriteria() {
        String[] sortByCriteria = new String[10];

        sortByCriteria[0] = "[Sort Musical Material]";


        return sortByCriteria;
    }

    // TODO: make this dynamic——currently hard-coded for testing purposes
    private String[] getAllMusicalMaterials() {
        String[] musicalMaterials = new String[4];

        musicalMaterials[0] = "[Select Musical Material]";
        musicalMaterials[1] = "Intervals";
        musicalMaterials[2] = "Scales";
        musicalMaterials[3] = "Chords";

        return musicalMaterials;
    }

    /**
     * Plays a short audio sample of a frequency entered in the <code>setFundamentalEditText</code>.
     * If no frequency is entered, the default frequency of 440Hz will be used.
     *
     * @param view
     */
    public void testFundamental(View view) {

        // TODO: consider adding this to an OnChangeListener if possible
        if (setFundamentalEditText.getText().toString().trim().length() > 0)
            fundamental.setPitchFrequency(Double.parseDouble(setFundamentalEditText.getText().toString()));
        fundamental.setDurationMilliseconds(DEFAULT_BLOCK_LENGTH_MILLISECONDS);
        mSoundObjectPlayer.loadSoundObject(fundamental);

        // Play fundamental frequency
        mSoundObjectPlayer.play();

        // TODO: disable/change color of button on play, re-enable on stop

    }

    /**
     * Plays a short sample of the selected <code>ChordScale</code>
     *
     * @param view
     */
    public void playSelection(View view) {
        // TODO: consider adding this to an OnChangeListener if possible
        // Get fundamental frequency
        if (setFundamentalEditText.getText().toString().trim().length() > 0)
            fundamental.setPitchFrequency(Double.parseDouble(setFundamentalEditText.getText().toString()));

        // TODO: make dynamic, currently hard-coded for testing purposes only
        // Build chord
        chord.clearAllChordMembers();
        chord.addChordMember(fundamental);
        chord.addChordMember(new Note(
                fundamental.getPitchFrequency()
                        * IntervalHandler.convertRatioToDecimal("5/4")));
        chord.addChordMember(new Note(
                fundamental.getPitchFrequency()
                        * IntervalHandler.convertRatioToDecimal("3/2")));
        chord.addChordMember(new Note(
                fundamental.getPitchFrequency()
                        * IntervalHandler.convertRatioToDecimal("7/4")));

        detectPlaybackMode();

        // TODO: add to OnChangeListener
        // Load SoundObject into SoundObjectPlayer
        mSoundObjectPlayer.loadSoundObject(chord);

        // Play SoundObject
        mSoundObjectPlayer.play();

        // TODO: disable/change color of button on play, re-enable on stop

    }

    public void displaySelectionDetails(View view) {
        if (view instanceof LinearLayout) {
            LinearLayout selectedLayout = (LinearLayout) view;
            ChordScale selectedChordScale = (ChordScale) selectedLayout.getTag();
            Log.i("LibraryActivity", selectedChordScale.getName());
            intervalDisplayTextView.setText(selectedChordScale.getName());
            selectionDisplayTextView.setText(selectedChordScale.getDescription());
        }
    }

    // TODO: consider adding this to an OnChangeListener if possible
    private void detectPlaybackMode() {
        // Set PlayBack mode
        if (mode2RadioButton.isChecked()) {
            chord.setPlayBackMode(ChordScale.PLAYBACK_MODE_ARP_UP);
            chord.setDurationMilliseconds(DEFAULT_ARP_LENGTH_MILLISECONDS);
        }
        else if (mode3RadioButton.isChecked()) {
            chord.setPlayBackMode(ChordScale.PLAYBACK_MODE_ARP_DOWN);
            chord.setDurationMilliseconds(DEFAULT_ARP_LENGTH_MILLISECONDS);
        }
        else {
            chord.setPlayBackMode(ChordScale.PLAYBACK_MODE_BLOCK_CHORD);
            chord.setDurationMilliseconds(DEFAULT_BLOCK_LENGTH_MILLISECONDS);
        }
    }
}
