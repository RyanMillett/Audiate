package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    private static final String TAG = "LibraryActivity";

    // DB
    private DBHelper db;

    // ChordScale Lists
    private List<ChordScale> mListOfPitchIntervals;
    private List<ChordScale> mKyleGannOctaveAnatomy;
    private List<ChordScale> mAllChordsList;
    private List<ChordScale> mScalaArchive;

    // Filtered List
    private List<ChordScale> filteredChordScalesList;

    // views
    private EditText setFundamentalEditText;
    private TextView displayNameTextView;
    private TextView selectionDisplayTextView;
    private ListView libraryListView;

    // spinners and adapters
    private Spinner selectMaterialSpinner;
    private Spinner sortBySpinner;
    private Spinner filterBySpinner;
    ArrayAdapter<String> filterMaterialSpinnerAdapter;

    // playback mode Radio Group
    private RadioButton mode1RadioButton;
    private RadioButton mode2RadioButton;
    private RadioButton mode3RadioButton;
    private RadioButton mode4RadioButton;
    private RadioButton[] radioButtonArray;

    // playback mode CheckBox group
    private CheckBox aux1CheckBox;
    private CheckBox aux2CheckBox;
    private CheckBox[] checkBoxArray;

    // playback buttons
    private Button testFundamentalButton;
    private Button playSelectionButton;

    // List adapter
    private LibraryListAdapter mLibraryListAdapter;

    // ChordScale
    private ChordScale mChordScale;

    // SoundObjectPlayer
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

        // DB
        deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);

        // Import materials
        //db.importPitchIntervalsFromCSV("pitch_intervals_redux.csv");
        // db.importKyleGannOctaveAnatomyFromCSV("OctaveAnatomy.csv");
        // TODO: import chords
        db.importScalaArchiveFromCSV("ScalaArchiveRedux.csv");

        // Lists
        filteredChordScalesList = new ArrayList<>();
        mListOfPitchIntervals = db.getAllIntervals();

        displayNameTextView = findViewById(R.id.selectionNameDisplayTextView);
        selectionDisplayTextView = findViewById(R.id.selectionDescriptionTextView);

        selectMaterialSpinner = findViewById(R.id.materialSelectionSpinner);
        sortBySpinner = findViewById(R.id.sortMaterialSpinner);
        filterBySpinner = findViewById(R.id.filterMaterialSpinner);

        libraryListView = (ListView) findViewById(R.id.libraryListView);

        mLibraryListAdapter = new LibraryListAdapter(this,
                R.layout.library_list_item, filteredChordScalesList);
        libraryListView.setAdapter(mLibraryListAdapter);

        // spinner adapters
        ArrayAdapter<String> selectMaterialSpinnerAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getAllMusicalMaterialTypes());
        selectMaterialSpinner.setAdapter(selectMaterialSpinnerAdapter);
        selectMaterialSpinner.setOnItemSelectedListener(selectMaterialSpinnerListener);
        selectMaterialSpinner.setSelection(0);

        ArrayAdapter<String> sortMaterialBySpinnerAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getAllSortCriteria());
        sortBySpinner.setAdapter(sortMaterialBySpinnerAdapter);
        // TODO: spinner listener

        filterMaterialSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        filterBySpinner.setAdapter(filterMaterialSpinnerAdapter);
        // TODO: spinner listener


        // playback settings group
        // TODO: add OnCheckedListener
        mode1RadioButton = findViewById(R.id.mode1RadioButton);
        mode1RadioButton.setChecked(true); // Default playback mode
        mode2RadioButton = findViewById(R.id.mode2RadioButton);
        mode3RadioButton = findViewById(R.id.mode3RadioButton);
        mode4RadioButton = findViewById(R.id.mode4RadioButton);
        radioButtonArray = new RadioButton[]{
                mode1RadioButton,
                mode2RadioButton,
                mode3RadioButton,
                mode4RadioButton
        };

        // CheckBoxes
        aux1CheckBox = findViewById(R.id.aux1CheckBox);
        aux2CheckBox = findViewById(R.id.aux2CheckBox);
        checkBoxArray = new CheckBox[]{aux1CheckBox,aux2CheckBox};

        // Set fundamental
        setFundamentalEditText = findViewById(R.id.setFundamentalFreqEditText);

        // Playback Buttons
        testFundamentalButton = findViewById(R.id.testFundamentalFreqButton);
        playSelectionButton = findViewById(R.id.playSelectionButton);
        playSelectionButton.setEnabled(false);

        // Sound Object
        //mChordScale = new ChordScale("Selected SoundObject");

        // Sound Object Player
        mSoundObjectPlayer = new SoundObjectPlayer();

    }

    // SPINNER LISTENERS //
    public AdapterView.OnItemSelectedListener selectMaterialSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> spinner, View view, int i, long l) {
            String materialType = String.valueOf(spinner.getItemAtPosition(i));

            // Update Library ListView
            mLibraryListAdapter.clear();
            if (materialType.equals(getString(R.string.select_materials))) {
                // Do nothing
//                mLibraryListAdapter.clear();
                disablePlaybackSettings();
            }
            else if (materialType.equals(getString(R.string.select_intervals))) {
                // All Intervals
                // TODO: add intervals
                filteredChordScalesList.addAll(mListOfPitchIntervals);
                mLibraryListAdapter.addAll(filteredChordScalesList);
                // Update playback options
                enablePlaybackSettings();
                mode1RadioButton.setText(getString(R.string.block_chord));
                    mode1RadioButton.setChecked(true);
                mode2RadioButton.setText(getString(R.string.arp_up));
                mode3RadioButton.setText(getString(R.string.arp_down));
                aux1CheckBox.setText(getString(R.string.aux_interval_invert));

            }
            else if (materialType.equals(getString(R.string.select_chords))) {
                // All Chords
                // TODO: add chords
                mLibraryListAdapter.addAll(filteredChordScalesList);
                // Update playback options
                enablePlaybackSettings();
                mode1RadioButton.setText(getString(R.string.block_chord));
                    mode1RadioButton.setChecked(true);
                mode2RadioButton.setText(getString(R.string.arp_up));
                mode3RadioButton.setText(getString(R.string.arp_down));
                aux1CheckBox.setText(getString(R.string.aux_chord_invert));
            }
            else if (materialType.equals(getString(R.string.select_scales))) {
                // All Scales
                // TODO: add scales
                filteredChordScalesList.addAll(mScalaArchive = new ArrayList<>(db.getAllScalaArchiveScales()));
                mLibraryListAdapter.addAll(filteredChordScalesList);
                Log.i(TAG + "Scl", "mLibraryListAdapter count->" + mLibraryListAdapter.getCount());
                // Update playback options
                enablePlaybackSettings();
                mode1RadioButton.setText(getString(R.string.cluster_chord));
                mode2RadioButton.setText(getString(R.string.scale_up));
                    mode2RadioButton.setChecked(true);
                mode3RadioButton.setText(getString(R.string.scale_down));
                aux1CheckBox.setText(getString(R.string.aux_scale_invert));
            }
            mLibraryListAdapter.notifyDataSetChanged();

            // Update "Filter" spinner adapter
            filterMaterialSpinnerAdapter.clear();
            filterMaterialSpinnerAdapter.addAll(getFilterCriteria());
            filterMaterialSpinnerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // UNUSED
        }
    };

    public AdapterView.OnItemSelectedListener filterMaterialSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> spinner, View view, int i, long l) {


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // UNUSED
        }
    };

    // SPINNER STRINGS //
    private String[] getAllMusicalMaterialTypes() {
        String[] musicalMaterials = new String[4];

        musicalMaterials[0] = getString(R.string.select_materials);
        musicalMaterials[1] = getString(R.string.select_intervals);
        musicalMaterials[2] = getString(R.string.select_chords);
        musicalMaterials[3] = getString(R.string.select_scales);

        return musicalMaterials;
    }
    private String[] getAllSortCriteria() {
        String[] sortByCriteria = new String[4];

        sortByCriteria[0] = getString(R.string.sort_small_large);
        sortByCriteria[1] = getString(R.string.sort_large_small);
        sortByCriteria[2] = getString(R.string.sort_name_a_z);
        sortByCriteria[3] = getString(R.string.sort_name_z_a);

        return sortByCriteria;
    }
    private String[] getFilterCriteria() {
        String[] filters;

        // TODO: implement in arrays.xml, currently hard-coded for expedience
        switch (selectMaterialSpinner.getSelectedItemPosition()) {
            case 1: // "Intervals" selected
                filters = new String[10];
                filters[0] = "List of Pitch Intervals";
                filters[1] = "Equal-Tempered (by approximation)";
                filters[2] = "Limit Intervals";
                filters[3] = "Meantone";
                filters[4] = "Superparticular ratios";
                filters[5] = "Harmonics (First 127)";
                filters[6] = "Ptolemy's intense diatonic scale";
                filters[7] = "Pythagorean";
                filters[8] = "Commas";
                filters[9] = "\"Anatomy of an Octave\" by Kyle Gann";
                break;
            case 2: // "Chords" selected
                filters = new String[3];
                filters[0] = "All Chords";
                filters[1] = "Triads";
                filters[2] = "7th-Chords";
                break;
            case 3: // "Scales" selected
                filters = new String[7];
                filters[0] = "Heptadiatonic Modes";
                filters[1] = "Pentatonic";
                filters[2] = "Equal-Tempered (by approximation)";
                filters[3] = "Octavating";
                filters[4] = "Non-Octavating";
                filters[5] = "Olivier Messiaen's \"Modes of Limited Transposition\"";
                filters[6] = "Full Scala Archive";
                break;
            default: // nothing selected
                filters = new String[1];
                filters[0] = "";
                break;
        }

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
            mChordScale.resetFundamentalFrequency(Double.parseDouble(setFundamentalEditText.getText().toString()));
        }

        // Determine button ID
        switch (view.getId()) {
            case R.id.testFundamentalFreqButton:
                mChordScale.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG);
                mSoundObjectPlayer.playSoundObject(mChordScale.getChordMemberAtPos(0));
                break;
            case R.id.playSelectionButton:
                detectPlaybackMode();
                mSoundObjectPlayer.playSoundObject(mChordScale);
                break;
        }

        // TODO: disable/change color of button on play, re-enable on stop

    }

    public void selectionDetailsHandler(View view) {
        // Build SoundObject
        if (view instanceof LinearLayout) {
            LinearLayout selectedLayout = (LinearLayout) view;
            ChordScale selectedChordScale = (ChordScale) selectedLayout.getTag();
            Log.i(TAG, selectedChordScale.getName() + ", size: " + selectedChordScale.getSize());



            mChordScale = new ChordScale(
                    selectedChordScale.getName(),
                    selectedChordScale.getSize(),
                    selectedChordScale.getDescription(),
                    selectedChordScale.getSCLfileName());
            mChordScale.buildChordScaleFromSCL(db.createScaleFromSCL(mChordScale, mChordScale.getSCLfileName()));

            Log.i(TAG,"mChordScale-> " + mChordScale.getName() + ", " + mChordScale.getSize());

//            int i = 0;
//            for (Note note : mChordScale.getAllChordMembers()) {
//                Log.i(TAG, "" + i++ + " " + note.getPitchFrequency());
//            }

            displayNameTextView.setText(selectedChordScale.getName());

            // Enable playback // TODO: add to listener
            playSelectionButton.setEnabled(true);
        }
    }

    // TODO: consider adding this to an OnChangeListener if possible
    private void detectPlaybackMode() {
        // Set PlayBack mode
        if (mode1RadioButton.isChecked()) {
            mChordScale.setPlayBackMode(ChordScale.PLAYBACK_MODE_CHORDSCALE_BLOCK_CLUSTER);
            mChordScale.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG * 2);
        }
        else if (mode2RadioButton.isChecked()) {
            mChordScale.setPlayBackMode(ChordScale.PLAYBACK_MODE_CHORDSCALE_UP);
            mChordScale.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_SHORT);
        }
        else if (mode3RadioButton.isChecked()) {
            mChordScale.setPlayBackMode(ChordScale.PLAYBACK_MODE_CHORDSCALE_DOWN);
            mChordScale.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_SHORT);
        }
    }

    private void disablePlaybackSettings() {
        for (RadioButton radioButton : radioButtonArray) {
            radioButton.setEnabled(false);
            radioButton.setText("");
        }

        for (CheckBox checkBox : checkBoxArray) {
            checkBox.setEnabled(false);
            checkBox.setText("");
        }
    }

    private void enablePlaybackSettings() {
        for (RadioButton radioButton : radioButtonArray) {
            radioButton.setEnabled(true);
        }

        // just for now
        mode4RadioButton.setEnabled(false);

        for (CheckBox checkBox : checkBoxArray) {
            checkBox.setEnabled(true);
        }

        // just for now
        aux2CheckBox.setEnabled(false);
    }
}
