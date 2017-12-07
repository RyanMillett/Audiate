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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    private static final String TAG = "LibraryActivity";

    // DB and lists
    private DBHelper db;
    private List<ChordScale> allIntervalsList;
    private List<ChordScale> allChordsList; // TODO: construct from scratch
    private List<ChordScale> allScalesList;
    private List<ChordScale> filteredChordScaleList;

    // views
    private EditText setFundamentalEditText;
    private TextView intervalDisplayTextView;
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

        deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);

        setFundamentalEditText = findViewById(R.id.setFundamentalFreqEditText);
        intervalDisplayTextView = findViewById(R.id.libraryListNameTextView);
        selectionDisplayTextView = findViewById(R.id.selectionDescriptionTextView);

        selectMaterialSpinner = findViewById(R.id.materialSelectionSpinner);
        sortBySpinner = findViewById(R.id.sortMaterialSpinner);
        filterBySpinner = findViewById(R.id.filterMaterialSpinner);

        libraryListView = (ListView) findViewById(R.id.libraryListView);

        db.importAllIntervalsFromCSV("intervals.csv");
        filteredChordScaleList = new ArrayList<>(10);

        mLibraryListAdapter = new LibraryListAdapter(this,
                R.layout.library_list_item, filteredChordScaleList);
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

        testFundamentalButton = findViewById(R.id.testFundamentalFreqButton);
        playSelectionButton = findViewById(R.id.playSelectionButton);

        mChordScale = new ChordScale("Selected ChordScale");
        mChordScale.addChordMember(new Note("Fundamental"));
        mChordScale.addChordMember(new Note("Interval"));

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
                mLibraryListAdapter.addAll(allIntervalsList = db.getAllIntervals());
                Log.i(TAG + "AllItvls", "mLibraryListAdapter count->" + mLibraryListAdapter.getCount());
                // Update playback options
                mode1RadioButton.setEnabled(true);
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
                allScalesList = db.importAllScalesFromSCL();
                mLibraryListAdapter.addAll(allScalesList);
                Log.i(TAG + "Scl", "mLibraryListAdapter count->" + mLibraryListAdapter.getCount());
                // Update playback options
                mode1RadioButton.setEnabled(true);
                mode1RadioButton.setChecked(true);
                mode1RadioButton.setText(getString(R.string.cluster_chord));
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

    // TODO: make String[]s dynamic——currently hard-coded for testing purposes
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

        switch (selectMaterialSpinner.getSelectedItemPosition()) {
            case 1: // "Intervals" selected
                filters = new String[8];
                filters[0] = "All " + getString(R.string.select_intervals);
                filters[1] = "Harmonics (First 127)";
                filters[2] = "Historical";
                filters[3] = "Pythagorean";
                filters[4] = "Equal-Tempered (by approximation)";
                filters[5] = "Diatonic";
                filters[6] = "Dodecaphonic";
                filters[7] = "Misc./Unnamed";
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

        // TODO: consider adding this to an OnChangeListener if possible
        // Get fundamental frequency
        if (!TextUtils.isEmpty(setFundamentalEditText.getText())) {
            mChordScale.resetFundamentalFrequency(Double.parseDouble(setFundamentalEditText.getText().toString()));
        }

        // Determine button ID
        switch (view.getId()) {
            case R.id.testFundamentalFreqButton:
                mChordScale.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG);
                mSoundObjectPlayer.playSoundObject(mChordScale);
                break;
            case R.id.playSelectionButton:
                detectPlaybackMode();
                mSoundObjectPlayer.playSoundObject(mChordScale);
                break;
        }

        // TODO: disable/change color of button on play, re-enable on stop

    }

    public void selectionDetailsHandler(View view) {
        // TODO: this method
        // use this method to build ChordScale
    }

    // TODO: consider adding this to an OnChangeListener if possible
    private void detectPlaybackMode() {
        // Set PlayBack mode
        if (mode2RadioButton.isChecked()) {
            mChordScale.setPlayBackMode(ChordScale.PLAYBACK_MODE_ARP_UP);
            mChordScale.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_SHORT);
        }
        else if (mode3RadioButton.isChecked()) {
            mChordScale.setPlayBackMode(ChordScale.PLAYBACK_MODE_ARP_DOWN);
            mChordScale.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_SHORT);
        }
        else {
            mChordScale.setPlayBackMode(ChordScale.PLAYBACK_MODE_BLOCK_CHORD);
            mChordScale.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG);
        }
    }
}
