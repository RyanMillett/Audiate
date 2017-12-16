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


    // Databases
    private DBMusicalMaterials mDBMusicalMaterials;
    private DBMusicalMaterials mDBScalaArchive;

    // Materials Lists
    private List<ChordScale> mAllIntervalsList;
    private List<ChordScale> mAllChordsList;
    private List<ChordScale> mAllScalesList;
        // Scala Archive
        private List<ChordScale> mScalaArchiveList;

    // Filtered Materials List
    private List<ChordScale> filteredMaterialsList;

    // ListView
    private ListView libraryListView;

    // spinners and adapters
    private Spinner selectMaterialSpinner;
    private Spinner sortBySpinner;
    private Spinner filterBySpinner;
    ArrayAdapter<String> filterMaterialSpinnerAdapter;

    // Info Views
    private EditText setFundamentalEditText;
    private TextView displayNameTextView;
    private TextView displayDescriptionTextView;

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

        // ---------- DELETE IF EXISTS, OTHERWISE COMMENT OUT ---------- //

//            deleteDatabase("MusicalMaterials");
//            deleteDatabase("ScalaArchive");

        // ---------- ---------- ---------- ---------- ---------- //


        // Databases
        mDBMusicalMaterials = new DBMusicalMaterials("MusicalMaterials", this);
        mDBScalaArchive = new DBMusicalMaterials("ScalaArchive", this);


        // ---------- DO IMPORTS ONCE, THEN COMMENT OUT ---------- //

//            mDBMusicalMaterials.deleteAllIntervals();
//            mDBMusicalMaterials.deleteAllScales();
//            mDBScalaArchive.deleteAllScales();
//
//            mDBMusicalMaterials.importIntervalsFromCSV("pitch_intervals_redux.csv");
//            // TODO: import chords
//            // TODO: import scales
//
//            mDBScalaArchive.importScalesFromCSV("ScalaArchiveRedux.csv");

        // ---------- ---------- ---------- ---------- ---------- //


        // Lists
        mAllIntervalsList = mDBMusicalMaterials.getAllIntervalsAsChordScale();
        // TODO: chords list
        mAllScalesList = mDBMusicalMaterials.getAllScales();

        // Scala Archive
        mScalaArchiveList = mDBScalaArchive.getAllScales();

        filteredMaterialsList = new ArrayList<>();


        displayNameTextView = findViewById(R.id.selectionNameDisplayTextView);
        displayDescriptionTextView = findViewById(R.id.selectionDescriptionTextView);

        selectMaterialSpinner = findViewById(R.id.materialSelectionSpinner);
        sortBySpinner = findViewById(R.id.sortMaterialSpinner);
        filterBySpinner = findViewById(R.id.filterMaterialSpinner);

        libraryListView = (ListView) findViewById(R.id.libraryListView);

        mLibraryListAdapter = new LibraryListAdapter(this,
                R.layout.library_list_item, filteredMaterialsList);
        libraryListView.setAdapter(mLibraryListAdapter);
        libraryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectionDetailsHandler(view);
            }
        });

        // spinner adapters
        ArrayAdapter<String> selectMaterialSpinnerAdapter =
                new ArrayAdapter<String>(this, R.layout.library_spinner_item, getAllMusicalMaterialTypes());
        selectMaterialSpinner.setAdapter(selectMaterialSpinnerAdapter);
        selectMaterialSpinner.setOnItemSelectedListener(selectMaterialSpinnerListener);
        selectMaterialSpinner.setSelection(0);

        ArrayAdapter<String> sortMaterialBySpinnerAdapter =
                new ArrayAdapter<String>(this, R.layout.library_spinner_item, getAllSortCriteria());
        sortBySpinner.setAdapter(sortMaterialBySpinnerAdapter);
        // TODO: spinner listener

        filterMaterialSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.library_spinner_item);
        filterBySpinner.setAdapter(filterMaterialSpinnerAdapter);
        filterBySpinner.setOnItemSelectedListener(filterMaterialSpinnerListener);


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
        //setFundamentalEditText = findViewById(R.id.setFundamentalFreqEditText);

        // Playback Buttons
        //testFundamentalButton = findViewById(R.id.testFundamentalFreqButton);
        playSelectionButton = findViewById(R.id.playSelectionButton);
        playSelectionButton.setEnabled(false);

        // Sound Object
        mChordScale = new ChordScale("Selected Material");

        // Sound Object Player
        mSoundObjectPlayer = new SoundObjectPlayer();

    }

    // SPINNER LISTENERS //

    public AdapterView.OnItemSelectedListener selectMaterialSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> spinner, View view, int i, long l) {

            // Update playback group text
            setPlaybackSettingsText(i);

            // Update Library ListView
            mLibraryListAdapter.clear();
            filteredMaterialsList.clear();

            // Determine selected material
            switch (i) {
                case 1:
                    // add intervals
                    filteredMaterialsList = new ArrayList<>(mAllIntervalsList);
                    // Update playback options
                    enablePlaybackSettings();
                    break;
                case 2:
                    // TODO: add chords

                    // Update playback options
                    enablePlaybackSettings();
                    break;
                case 3:
                    // TODO: add scales
                    filteredMaterialsList = new ArrayList<>(mScalaArchiveList);
                    // Update playback options
                    enablePlaybackSettings();
                    break;
                default:
                    // Do nothing
                    disablePlaybackSettings();
                    break;
            }

            // Update list adapter
            mLibraryListAdapter.addAll(filteredMaterialsList);
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

            // Determine selected material
            String selectedMaterial = selectMaterialSpinner.getSelectedItem().toString();
            //Log.i(TAG, selectedMaterial);

            // Update Library ListView
            mLibraryListAdapter.clear();
            filteredMaterialsList.clear();

            if (selectedMaterial.equalsIgnoreCase(getString(R.string.select_intervals))) {
                // filter interval materials
                filteredMaterialsList = new ArrayList<>(filterIntervals());
            }
            else if (selectedMaterial.equalsIgnoreCase(getString(R.string.select_chords))) {
                // filter chord materials
                filteredMaterialsList = new ArrayList<>(filterChords());
            }
            else if (selectedMaterial.equalsIgnoreCase(getString(R.string.select_scales))) {
                // filter scale materials
                filteredMaterialsList = new ArrayList<>(filterScales());
            }
            else {
                // clear (nothing)
            }

            // Update list adapter
            mLibraryListAdapter.addAll(filteredMaterialsList);
            mLibraryListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // UNUSED
        }
    };


    // FILTER METHODS //

    private ArrayList<ChordScale> filterIntervals() {

        String filterMaterial = filterBySpinner.getSelectedItem().toString();

        if (filterMaterial.equalsIgnoreCase(getString(R.string.all_harmonics))) {
            return new ArrayList<>(mDBMusicalMaterials.getAllHarmonics());
        }
        else if (filterMaterial.equalsIgnoreCase(getString(R.string.limit_intervals))) {
            // TODO: all limit intervals
            return new ArrayList<>(mAllIntervalsList);
        }
        else if (filterMaterial.equalsIgnoreCase(getString(R.string.meantone_intervals))) {
            return new ArrayList<>(mDBMusicalMaterials.getAllMeantoneIntervals());
        }
        else if (filterMaterial.equalsIgnoreCase(getString(R.string.superparticular_intervals))) {
            // TODO: all superparticular
            return new ArrayList<>(mAllIntervalsList);
        }
        else if (filterMaterial.equalsIgnoreCase(getString(R.string.et_intervals))) {
            return new ArrayList<>(mDBMusicalMaterials.getAllEqualTemperedIntervals());
        }
        else if (filterMaterial.equalsIgnoreCase(getString(R.string.pythag_intervals))) {
            // TODO: all pythag
            return new ArrayList<>(mAllIntervalsList);
        }
        else if (filterMaterial.equalsIgnoreCase(getString(R.string.comma_intervals))) {
            // TODO: all commas
            return new ArrayList<>(mAllIntervalsList);
        }
        else {
            return new ArrayList<>(mAllIntervalsList);
        }
    }

    private ArrayList<ChordScale> filterChords() {
        return new ArrayList<>(mAllChordsList);
    }

    private ArrayList<ChordScale> filterScales() {
        return new ArrayList<>(mAllScalesList);
    }


    // SPINNER STRINGS //

    private String[] getAllMusicalMaterialTypes() {
        return getResources().getStringArray(R.array.SelectMaterialsArray);
    }

    private String[] getAllSortCriteria() {
        return getResources().getStringArray(R.array.SortMaterialsArray);
    }

    private String[] getFilterCriteria() {

        switch (selectMaterialSpinner.getSelectedItemPosition()) {
            case 1: // "Intervals" selected
                return getResources().getStringArray(R.array.IntervalsArray);
            case 2: // "Chords" selected
                return getResources().getStringArray(R.array.ChordsArray);
            case 3: // "Scales" selected
                return getResources().getStringArray(R.array.ScalesArray);
            default: // nothing selected
                disablePlaybackSettings();
                playSelectionButton.setEnabled(false);
                return new String[]{""};
        }
    }


    // SELECTION HANDLERS //

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
//            case R.id.testFundamentalFreqButton:
//                mChordScale.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG);
//                mSoundObjectPlayer.playSoundObject(mChordScale.getChordMemberAtPos(0));
//                break;
            case R.id.playSelectionButton:
                detectPlaybackMode();
                mSoundObjectPlayer.playSoundObject(mChordScale);
                break;
        }

        // TODO: disable/change color of button on play, re-enable on stop

    }

    public void selectionDetailsHandler(View view) {

        if (view instanceof LinearLayout) {
            LinearLayout selectedLayout = (LinearLayout) view;
            ChordScale selectedChordScale = (ChordScale) selectedLayout.getTag();
            Log.i(TAG, selectedChordScale.getName() + ", size: " + selectedChordScale.getSize());

            // Build SoundObject
            mChordScale = selectedChordScale;

            if (mChordScale.getSize() > ChordScale.CHORDSCALE_DEFAULT_INITIAL_SIZE) {
                mDBMusicalMaterials.buildChordScaleFromSCL(mChordScale, "scl/", mChordScale.getSCLfileName());
            }

            Log.i(TAG,"mChordScale->" + mChordScale.getName() + " | size:" + mChordScale.getSize());
            for (int i = 0; i < mChordScale.getSize(); ++i) {
                Log.i(TAG, i + " " + mChordScale.getChordMemberAtPos(i).getRatio());
            }

            displayNameTextView.setText(selectedChordScale.getName());
            displayDescriptionTextView.setText(selectedChordScale.getDescription());

            // Enable playback // TODO: add to listener
            playSelectionButton.setEnabled(true);
        }
    }


    // PLAYBACK OPTIONS //

    // TODO: consider adding this to an OnChangeListener
    private void detectPlaybackMode() {
        // Set PlayBack mode
        if (mode1RadioButton.isChecked()) {
            mChordScale.setPlayBackMode(ChordScale.PLAYBACK_MODE_CHORDSCALE_BLOCK_CLUSTER);
            mChordScale.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG * 3);
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

    private void setPlaybackSettingsText(int materials) {

        switch (materials) {
            case 1:
                mode1RadioButton.setText(getString(R.string.block_chord));
                mode1RadioButton.setChecked(true);
                mode2RadioButton.setText(getString(R.string.arp_up));
                mode3RadioButton.setText(getString(R.string.arp_down));
                aux1CheckBox.setText(getString(R.string.aux_interval_invert));
                break;
            case 2:
                mode1RadioButton.setText(getString(R.string.block_chord));
                mode1RadioButton.setChecked(true);
                mode2RadioButton.setText(getString(R.string.arp_up));
                mode3RadioButton.setText(getString(R.string.arp_down));
                aux1CheckBox.setText(getString(R.string.aux_chord_invert));
                break;
            case 3:
                mode1RadioButton.setText(getString(R.string.cluster_chord));
                mode2RadioButton.setText(getString(R.string.scale_up));
                mode2RadioButton.setChecked(true);
                mode3RadioButton.setText(getString(R.string.scale_down));
                aux1CheckBox.setText(getString(R.string.aux_scale_invert));
                break;
            default:
                disablePlaybackSettings();
                break;
        }
    }
}
