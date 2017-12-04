package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

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

    private Button testFundamentalButton;
    private Button playSelectionButton;

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

        // TODO: get full library from MainMenu, initialize lists

        setFundamentalEditText = findViewById(R.id.setFundamentalFreqEditText);
        intervalDisplayTextView = findViewById(R.id.libraryListNameTextView);
        selectionDisplayTextView = findViewById(R.id.selectionDescriptionTextView);
        selectMaterialSpinner = findViewById(R.id.materialSelectionSpinner);
        sortBySpinner = findViewById(R.id.sortMaterialSelectionSpinner);

        libraryListView = (ListView) findViewById(R.id.libraryListView);

//        filteredChordScaleList = new ArrayList<>(allIntervalsList);

//        mLibraryListAdapter = new LibraryListAdapter(this,
//                R.layout.audition_room_list_item, filteredChordScaleList);
//        libraryListView.setAdapter(mLibraryListAdapter);

        // spinner adapters
        ArrayAdapter<String> selectMaterialSpinnerAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getAllMusicalMaterials());
        selectMaterialSpinner.setAdapter(selectMaterialSpinnerAdapter);

        ArrayAdapter<String> sortMaterialBySpinnerAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getAllSortCriteria());
        sortBySpinner.setAdapter(sortMaterialBySpinnerAdapter);

        // playback settings group
        // TODO: add OnCheckedListener
        mode1RadioButton = findViewById(R.id.mode1RadioButton);
        mode1RadioButton.setChecked(true); // Default playback mode
        mode2RadioButton = findViewById(R.id.mode2RadioButton);
        mode3RadioButton = findViewById(R.id.mode3RadioButton);
        mode4RadioButton = findViewById(R.id.mode4RadioButton);

        aux1CheckBox = findViewById(R.id.aux1CheckBox);
        aux2CheckBox = findViewById(R.id.aux2CheckBox);

        testFundamentalButton = findViewById(R.id.testFundamentalFreqButton);
        playSelectionButton = findViewById(R.id.playSelectionButton);

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
     * Handles audio playback
     *
     * @param view
     */
    public void playbackHandler(View view) {

        // TODO: consider adding this to an OnChangeListener if possible
        // Get fundamental frequency
        if (setFundamentalEditText.getText().toString().trim().length() > 0)
            fundamental.setPitchFrequency(Double.parseDouble(setFundamentalEditText.getText().toString()));

        // Determine button ID
        Button selectedButton = (Button) view;
        if (selectedButton == testFundamentalButton) {
            fundamental.setDurationMilliseconds(DEFAULT_BLOCK_LENGTH_MILLISECONDS);
            mSoundObjectPlayer.loadSoundObject(fundamental);

            // Play fundamental frequency
            mSoundObjectPlayer.play();
        }
        else if (selectedButton == playSelectionButton) {  // AND something has been selected!!!!
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
        }

        // TODO: disable/change color of button on play, re-enable on stop

    }

    public void selectionDetailsHandler(View view) {
        // TODO: this method
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
