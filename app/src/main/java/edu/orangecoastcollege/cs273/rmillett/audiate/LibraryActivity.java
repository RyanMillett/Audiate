package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
    private Spinner filterBySpinner;

    // playback mode Radio Group
    private RadioButton mode1RadioButton;
    private RadioButton mode2RadioButton;
    private RadioButton mode3RadioButton;
    private RadioButton mode4RadioButton;

    // playback mode CheckBox group
    private CheckBox aux1CheckBox;
    private CheckBox aux2CheckBox;

    // playback buttons
    private Button testFundamentalButton;
    private Button playSelectionButton;

    // List adapter
    private LibraryListAdapter mLibraryListAdapter;

    // SoundObjects
    private Note fundamental;
    private ChordScale chord;

    // Player
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
        sortBySpinner = findViewById(R.id.sortMaterialSpinner);
        filterBySpinner = findViewById(R.id.filterMaterialSpinner);

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

        ArrayAdapter<String> filterMaterialSpinnerAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getAllFilters());
        filterBySpinner.setAdapter(filterMaterialSpinnerAdapter);


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
    private String[] getAllMusicalMaterials() {
        String[] musicalMaterials = new String[4];

        musicalMaterials[0] = getString(R.string.select_materials);
        musicalMaterials[1] = getString(R.string.select_intervals);
        musicalMaterials[2] = getString(R.string.select_chords);
        musicalMaterials[3] = getString(R.string.select_scales);

        return musicalMaterials;
    }

    // TODO: make this dynamic——currently hard-coded for testing purposes
    private String[] getAllSortCriteria() {
        String[] sortByCriteria = new String[10];

        // TODO: Change based on what material is selected
        sortByCriteria[0] = "[Sort " + "" + "]";

        return sortByCriteria;
    }

    // TODO: make this dynamic——currently hard-coded for testing purposes
    private String[] getAllFilters() {
        String[] filters = new String[5];

        // TODO: Change based on what material is selected
        filters[0] = "[Filter " + "" + "]";

        return filters;
    }

    /**
     * Handles audio playback
     *
     * @param view
     */
    public void playbackHandler(View view) {

        // TODO: consider adding this to an OnChangeListener if possible
        // Get fundamental frequency
        if (!TextUtils.isEmpty(setFundamentalEditText.getText())) {
            fundamental.setPitchFrequency(Double.parseDouble(setFundamentalEditText.getText().toString()));
        }


        // Determine button ID
        switch (view.getId()) {
            case R.id.testFundamentalFreqButton:
                fundamental.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG);
                mSoundObjectPlayer.playSoundObject(fundamental);
                break;
            case R.id.playSelectionButton: // TODO: AND something has been selected!!!!
                // TODO: make dynamic, currently hard-coded for testing purposes only
                // Build chord
                chord.clearAllChordMembers();
                chord.addChordMember(fundamental);
                chord.addChordMember(new Note(fundamental.getPitchFrequency()
                                * Music.convertRatioToDecimal("5/4")));
                chord.addChordMember(new Note(fundamental.getPitchFrequency()
                                * Music.convertRatioToDecimal("3/2")));
                chord.addChordMember(new Note(fundamental.getPitchFrequency()
                                * Music.convertRatioToDecimal("7/4")));
                detectPlaybackMode();
                // Load and Play SoundObject
                mSoundObjectPlayer.playSoundObject(chord);
                break;
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
            chord.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_SHORT);
        }
        else if (mode3RadioButton.isChecked()) {
            chord.setPlayBackMode(ChordScale.PLAYBACK_MODE_ARP_DOWN);
            chord.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_SHORT);
        }
        else {
            chord.setPlayBackMode(ChordScale.PLAYBACK_MODE_BLOCK_CHORD);
            chord.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG);
        }
    }
}
