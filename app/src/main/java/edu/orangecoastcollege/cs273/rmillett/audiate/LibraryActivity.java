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

    // SoundOject Lists
    private List<Note> mKyleGannOctaveAnatomy;
    private List<ChordScale> mAllChordsList;
    private List<ChordScale> mAllScalesList;

    // Scala scale archive
    private List<ChordScale> mScalaArchive;

    private List<SoundObject> filteredSoundObjectList;

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

    // playback mode CheckBox group
    private CheckBox aux1CheckBox;
    private CheckBox aux2CheckBox;

    // playback buttons
    private Button testFundamentalButton;
    private Button playSelectionButton;

    // List adapter
    private LibraryListAdapter mLibraryListAdapter;

    // SoundObject
    private SoundObject mSoundObject;

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
        db.importKyleGannOctaveAnatomyFromCSV("OctaveAnatomy.csv");
        // TODO: db.importMusicalIntervalsFromCSV("musical_intervals.csv");
        // TODO: import chords
        db.importScalaArchiveFromCSV("ScalaArchive.csv");

        // Lists
        filteredSoundObjectList = new ArrayList<>();


        displayNameTextView = findViewById(R.id.selectionNameDisplayTextView);
        selectionDisplayTextView = findViewById(R.id.selectionDescriptionTextView);

        selectMaterialSpinner = findViewById(R.id.materialSelectionSpinner);
        sortBySpinner = findViewById(R.id.sortMaterialSpinner);
        filterBySpinner = findViewById(R.id.filterMaterialSpinner);

        libraryListView = (ListView) findViewById(R.id.libraryListView);

        mLibraryListAdapter = new LibraryListAdapter(this,
                R.layout.library_list_item, filteredSoundObjectList);
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

        aux1CheckBox = findViewById(R.id.aux1CheckBox);
        aux2CheckBox = findViewById(R.id.aux2CheckBox);

        // Set fundamental
        setFundamentalEditText = findViewById(R.id.setFundamentalFreqEditText);

        // Playback Buttons
        testFundamentalButton = findViewById(R.id.testFundamentalFreqButton);
        playSelectionButton = findViewById(R.id.playSelectionButton);
        playSelectionButton.setEnabled(false);

        // Sound Object
        mSoundObject = new ChordScale("Selected SoundObject");

        // Sound Object Player
        mSoundObjectPlayer = new SoundObjectPlayer();

    }

    public AdapterView.OnItemSelectedListener selectMaterialSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> spinner, View view, int i, long l) {
            String materialType = String.valueOf(spinner.getItemAtPosition(i));

            // Update Library ListView
            mLibraryListAdapter.clear();
            if (materialType.equals(getString(R.string.select_materials))) {
                // Do nothing
//                mLibraryListAdapter.clear();
                mode1RadioButton.setEnabled(false);
                mode1RadioButton.setText("");
                mode2RadioButton.setEnabled(false);
                mode2RadioButton.setText("");
                mode3RadioButton.setEnabled(false);
                mode3RadioButton.setText("");
                mode4RadioButton.setEnabled(false);
                mode4RadioButton.setText("");
                aux1CheckBox.setEnabled(false);
                aux1CheckBox.setText("");
                aux2CheckBox.setEnabled(false);
                aux2CheckBox.setText("");
            }
            else if (materialType.equals(getString(R.string.select_intervals))) {
                // All Intervals
                //filteredSoundObjectList;
                mLibraryListAdapter.addAll(filteredSoundObjectList);
                //Log.i(TAG, "mLibraryListAdapter count->" + mLibraryListAdapter.getCount());
                // Update playback options
                mode1RadioButton.setEnabled(true);
                mode1RadioButton.setChecked(true);
                mode1RadioButton.setText(getString(R.string.block_chord));
                mode2RadioButton.setEnabled(true);
                mode2RadioButton.setText(getString(R.string.arp_up));
                mode3RadioButton.setEnabled(true);
                mode3RadioButton.setText(getString(R.string.arp_down));
                mode4RadioButton.setEnabled(false);
                mode4RadioButton.setText("");
                aux1CheckBox.setEnabled(true);
                aux1CheckBox.setText(getString(R.string.aux_interval_invert));
                aux2CheckBox.setEnabled(false);
                aux2CheckBox.setText("");
            }
            else if (materialType.equals(getString(R.string.select_chords))) {
                // All Chords
                // TODO: add chords
                // Update playback options
                mode1RadioButton.setEnabled(true);
                mode1RadioButton.setChecked(true);
                mode1RadioButton.setText(getString(R.string.block_chord));
                mode2RadioButton.setEnabled(true);
                mode2RadioButton.setText(getString(R.string.arp_up));
                mode3RadioButton.setEnabled(true);
                mode3RadioButton.setText(getString(R.string.arp_down));
                mode4RadioButton.setEnabled(false);
                mode4RadioButton.setText("");
                aux1CheckBox.setEnabled(true);
                aux1CheckBox.setText(getString(R.string.aux_chord_invert));
                aux2CheckBox.setEnabled(false);
                aux2CheckBox.setText("");
            }
            else if (materialType.equals(getString(R.string.select_scales))) {
                // All Scales
                // TODO: add scales
                //filteredSoundObjectList;
                mLibraryListAdapter.addAll(filteredSoundObjectList);
                Log.i(TAG + "Scl", "mLibraryListAdapter count->" + mLibraryListAdapter.getCount());
                // Update playback options
                mode1RadioButton.setEnabled(true);
                mode1RadioButton.setText(getString(R.string.cluster_chord));
                mode2RadioButton.setChecked(true);
                mode2RadioButton.setEnabled(true);
                mode2RadioButton.setText(getString(R.string.scale_up));
                mode3RadioButton.setEnabled(true);
                mode3RadioButton.setText(getString(R.string.scale_down));
                mode4RadioButton.setEnabled(false);
                mode4RadioButton.setText("");
                aux1CheckBox.setEnabled(true);
                aux1CheckBox.setText(getString(R.string.aux_scale_invert));
                aux2CheckBox.setEnabled(false);
                aux2CheckBox.setText("");
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

        // TODO: add to strings arrays, currently hard-coded for expedience
        switch (selectMaterialSpinner.getSelectedItemPosition()) {
            case 1: // "Intervals" selected
                filters = new String[9];
                filters[0] = "Heptatonic/Diatonic";
                filters[1] = "Dodecaphonic";
                filters[2] = "Equal-Tempered (by approximation)";
                filters[3] = "Harmonics (First 127)";
                filters[4] = "Historical";
                filters[5] = "Pythagorean";
                filters[6] = "Commas";
                filters[7] = "Misc./Unnamed";
                filters[8] = "All " + getString(R.string.select_intervals);
                break;
            case 2: // "Chords" selected
                filters = new String[5];
                filters[0] = "All " + getString(R.string.select_chords);
                filters[1] = "Major";
                filters[2] = "Minor";
                filters[3] = "Augmented";
                filters[4] = "Diminished";
                break;
            case 3: // "Scales" selected
                filters = new String[6];
                filters[0] = "Octavating";
                filters[1] = "Pentatonic";
                filters[2] = "Heptatonic";
                filters[3] = "Dodecaphonic";
                filters[4] = "Non-Octavating";
                filters[5] = "All " + getString(R.string.select_scales);
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
        detectPlaybackMode();

        // TODO: consider adding this to an OnChangeListener if possible
        // Get fundamental frequency
        if (!TextUtils.isEmpty(setFundamentalEditText.getText())) {
            mSoundObject.resetFundamentalFrequency(Double.parseDouble(setFundamentalEditText.getText().toString()));
        }

        // Determine button ID
        switch (view.getId()) {
            case R.id.testFundamentalFreqButton:
                mSoundObject.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG);
                mSoundObjectPlayer.playSoundObject(mSoundObject.getChordMemberAtPos(0));
                break;
            case R.id.playSelectionButton:
                mSoundObjectPlayer.playSoundObject(mSoundObject);
                break;
        }

        // TODO: disable/change color of button on play, re-enable on stop

    }

    public void selectionDetailsHandler(View view) {
        // Build SoundObject
        if (view instanceof LinearLayout) {
            LinearLayout selectedLayout = (LinearLayout) view;
            SoundObject selectedSoundObject = (SoundObject) selectedLayout.getTag();
            Log.i(TAG, selectedSoundObject.getName());




            // Enable playback
            playSelectionButton.setEnabled(true);
        }
    }

    // TODO: consider adding this to an OnChangeListener if possible
    private void detectPlaybackMode() {
        // Set PlayBack mode
        if (mode1RadioButton.isChecked()) {
            mSoundObject.setPlayBackMode(ChordScale.PLAYBACK_MODE_CHORDSCALE_BLOCK_CLUSTER);
            mSoundObject.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG * 2);
        }
        else if (mode2RadioButton.isChecked()) {
            mSoundObject.setPlayBackMode(ChordScale.PLAYBACK_MODE_CHORDSCALE_UP);
            mSoundObject.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_SHORT);
        }
        else if (mode3RadioButton.isChecked()) {
            mSoundObject.setPlayBackMode(ChordScale.PLAYBACK_MODE_CHORDSCALE_DOWN);
            mSoundObject.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_SHORT);
        }
    }
}
