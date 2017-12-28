package edu.orangecoastcollege.cs273.rmillett.audiate.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.orangecoastcollege.cs273.rmillett.audiate.Models.*;
import edu.orangecoastcollege.cs273.rmillett.audiate.Databases.MusicalMaterialsDB;
import edu.orangecoastcollege.cs273.rmillett.audiate.ListAdapters.LibraryListAdapter;
import edu.orangecoastcollege.cs273.rmillett.audiate.R;

public class LibraryActivity extends AppCompatActivity {

    // Debug Tags
    private static final String TAG = "LibraryActivity";

    // Databases
    private MusicalMaterialsDB mMusicalMaterialsDB;
    private MusicalMaterialsDB mScalaArchiveDB;

    // Materials Lists
    private List<ChordScale> mAllIntervalsList;
    private List<ChordScale> mAllChordsList;
    private List<ChordScale> mAllScalesList;

    // Scala Archive
    private List<ChordScale> mScalaArchiveList;

    // Filtered Materials List
    private List<ChordScale> mFilteredMaterialsList;

    // ListView
    private ListView mLibraryListView;

    // spinners and adapters
    private Spinner mSelectMaterialSpinner;
    private Spinner mSortBySpinner;
    private Spinner mFilterBySpinner;
    private Spinner mPlaybackModesSpinner;
    ArrayAdapter<String> mFilterMaterialSpinnerAdapter;
    ArrayAdapter<String> mPlaybackModesSpinnerAdapter;

    // Info Views
    private TextView mDisplayNameTextView;
    private TextView mDisplayDescriptionTextView;

    // playback buttons
    private Button mPlaySelectionButton;

    // List adapter
    private LibraryListAdapter mLibraryListAdapter;

    // ChordScale
    private ChordScale mChordScale;

    // SoundObjectPlayer
    private SoundObjectPlayer mSoundObjectPlayer;

    private Handler mHandler;

    /**
     * Creates an instance of <code>LibraryActivity</code> in the view
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // Sound Object
        mChordScale = new ChordScale("Selected Material");

        // Sound Object Player
        mSoundObjectPlayer = new SoundObjectPlayer();

        // ---------- DELETE IF EXISTS, OTHERWISE COMMENT OUT ---------- //

//            deleteDatabase("MusicalMaterials");
//            deleteDatabase("ScalaArchive");

        // ---------- ---------- ---------- ---------- ---------- //


        // Databases
        mMusicalMaterialsDB = new MusicalMaterialsDB("MusicalMaterials", this);
        mScalaArchiveDB = new MusicalMaterialsDB("ScalaArchive", this);


        // ---------- DO IMPORTS ONCE, THEN COMMENT OUT ---------- //

//        mMusicalMaterialsDB.deleteAllIntervals();
//        mMusicalMaterialsDB.deleteAllScales();
//        mScalaArchiveDB.deleteAllScales();
//
//        mMusicalMaterialsDB.importIntervalsFromCSV("pitch_intervals_redux.csv");
//        // TODO: import chords
//        // TODO: import scales
//
//        mScalaArchiveDB.importScalesFromCSV("ScalaArchiveRedux.csv");

        // ---------- ---------- ---------- ---------- ---------- //


        // Lists
        mAllIntervalsList = mMusicalMaterialsDB.getAllIntervalsAsChordScale();
        // TODO: chords list
        mAllScalesList = mMusicalMaterialsDB.getAllScales();

        // Scala Archive
        mScalaArchiveList = mScalaArchiveDB.getAllScales();

        mFilteredMaterialsList = new ArrayList<>();


        mDisplayNameTextView = findViewById(R.id.selectionNameDisplayTextView);
        mDisplayDescriptionTextView = findViewById(R.id.selectionDescriptionTextView);

        mSelectMaterialSpinner = findViewById(R.id.materialSelectionSpinner);
        mSortBySpinner = findViewById(R.id.sortMaterialSpinner);
        mFilterBySpinner = findViewById(R.id.filterMaterialSpinner);
        mPlaybackModesSpinner = findViewById(R.id.playbackModesSpinner);

        mLibraryListView = (ListView) findViewById(R.id.libraryListView);

        mLibraryListAdapter = new LibraryListAdapter(this,
                R.layout.library_list_item, mFilteredMaterialsList);
        mLibraryListView.setAdapter(mLibraryListAdapter);
        mLibraryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectionDetailsHandler(view);
            }
        });

        // spinner adapters
        ArrayAdapter<String> selectMaterialSpinnerAdapter =
                new ArrayAdapter<>(this, R.layout.library_spinner_item, getAllMusicalMaterialTypes());
        mSelectMaterialSpinner.setAdapter(selectMaterialSpinnerAdapter);
        mSelectMaterialSpinner.setOnItemSelectedListener(mSelectMaterialSpinnerListener);
        mSelectMaterialSpinner.setSelection(0);

        ArrayAdapter<String> sortMaterialBySpinnerAdapter =
                new ArrayAdapter<>(this, R.layout.library_spinner_item, getAllSortCriteria());
        mSortBySpinner.setAdapter(sortMaterialBySpinnerAdapter);
        // TODO: spinner listener

        mFilterMaterialSpinnerAdapter = new ArrayAdapter<>(this, R.layout.library_spinner_item);
        mFilterBySpinner.setAdapter(mFilterMaterialSpinnerAdapter);
        mFilterBySpinner.setOnItemSelectedListener(mFilterMaterialSpinnerListener);

        mPlaybackModesSpinnerAdapter = new ArrayAdapter<>(this, R.layout.library_spinner_item);
        mPlaybackModesSpinner.setAdapter(mPlaybackModesSpinnerAdapter);
        mPlaybackModesSpinner.setOnItemSelectedListener(mPlaybackModeSpinnerListener);

        mPlaySelectionButton = findViewById(R.id.playSelectionButton);
        setPlaybackGroupVisibility(false);

        mHandler = new Handler();

    }


    // LISTENERS //

    public AdapterView.OnItemSelectedListener mSelectMaterialSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> spinner, View view, int i, long l) {

            // Update "Filter" spinner adapter
            mFilterMaterialSpinnerAdapter.clear();
            mFilterMaterialSpinnerAdapter.addAll(getFilterCriteria());
            mFilterMaterialSpinnerAdapter.notifyDataSetChanged();

            // Update Playback Modes spinner adapter
            mPlaybackModesSpinnerAdapter.clear();
            mPlaybackModesSpinnerAdapter.addAll(getPlaybackModes());
            mPlaybackModesSpinnerAdapter.notifyDataSetChanged();

            // Update Library ListView
            mLibraryListAdapter.clear();
            mFilteredMaterialsList.clear();

            // Determine selected material
            switch (i) {
                case 1:
                    // add intervals
                    mFilteredMaterialsList = new ArrayList<>(mAllIntervalsList);
                    // Update playback options
                    setPlaybackGroupVisibility(true);
                    mPlaybackModesSpinner.setSelection(0);
                    break;
                case 2:
                    // TODO: add chords
                    // Update playback options
                    setPlaybackGroupVisibility(true);
                    mPlaybackModesSpinner.setSelection(0);
                    break;
                case 3:
                    // TODO: add scales
                    mFilteredMaterialsList = new ArrayList<>(mScalaArchiveList);
                    // Update playback options
                    setPlaybackGroupVisibility(true);
                    mPlaybackModesSpinner.setSelection(1);
                    break;
                default:
                    // Do nothing
                    setPlaybackGroupVisibility(false);
                    break;
            }

            // Update list adapter
            mLibraryListAdapter.addAll(mFilteredMaterialsList);
            mLibraryListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // UNUSED
        }
    };

    public AdapterView.OnItemSelectedListener mFilterMaterialSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> spinner, View view, int i, long l) {

            // Determine selected material
            String selectedMaterial = mSelectMaterialSpinner.getSelectedItem().toString();
            //Log.i(TAG, selectedMaterial);

            // Update Library ListView
            mLibraryListAdapter.clear();
            mFilteredMaterialsList.clear();

            if (selectedMaterial.equalsIgnoreCase(getString(R.string.select_intervals))) {
                // filter interval materials
                mFilteredMaterialsList = new ArrayList<>(filterIntervals());
            }
            else if (selectedMaterial.equalsIgnoreCase(getString(R.string.select_chords))) {
                // filter chord materials
                mFilteredMaterialsList = new ArrayList<>(filterChords());
            }
            else if (selectedMaterial.equalsIgnoreCase(getString(R.string.select_scales))) {
                // filter scale materials
                mFilteredMaterialsList = new ArrayList<>(filterScales());
            }
            else {
                // clear (nothing)
            }

            // Update list adapter
            mLibraryListAdapter.addAll(mFilteredMaterialsList);
            mLibraryListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // UNUSED
        }
    };

    public AdapterView.OnItemSelectedListener mPlaybackModeSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            detectPlaybackMode(i);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // UNUSED
        }
    };


    // FILTER METHODS //

    private ArrayList<ChordScale> filterIntervals() {

        String filterMaterial = mFilterBySpinner.getSelectedItem().toString();

        if (filterMaterial.equalsIgnoreCase(getString(R.string.all_harmonics))) {
            return new ArrayList<>(mMusicalMaterialsDB.getAllHarmonics());
        }
        else if (filterMaterial.equalsIgnoreCase(getString(R.string.limit_intervals))) {
            // TODO: all limit intervals
            return new ArrayList<>(mAllIntervalsList);
        }
        else if (filterMaterial.equalsIgnoreCase(getString(R.string.meantone_intervals))) {
            return new ArrayList<>(mMusicalMaterialsDB.getAllMeantoneIntervals());
        }
        else if (filterMaterial.equalsIgnoreCase(getString(R.string.superparticular_intervals))) {
            // TODO: all superparticular
            return new ArrayList<>(mAllIntervalsList);
        }
        else if (filterMaterial.equalsIgnoreCase(getString(R.string.et_intervals))) {
            return new ArrayList<>(mMusicalMaterialsDB.getAllEqualTemperedIntervals());
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

    // TODO: this
    private ArrayList<ChordScale> filterChords() {
        return new ArrayList<>(mAllChordsList);
    }

    // TODO: this
    private ArrayList<ChordScale> filterScales() {
        return new ArrayList<>(mAllScalesList);
    }


    // SPINNER STRING-ARRAYS //

    private String[] getAllMusicalMaterialTypes() {
        return getResources().getStringArray(R.array.SelectMaterialsArray);
    }

    private String[] getAllSortCriteria() {
        return getResources().getStringArray(R.array.SortMaterialsArray);
    }

    private String[] getFilterCriteria() {

        switch (mSelectMaterialSpinner.getSelectedItemPosition()) {
            case 1: // "Intervals" selected
                return getResources().getStringArray(R.array.IntervalsArray);
            case 2: // "Chords" selected
                return getResources().getStringArray(R.array.ChordsArray);
            case 3: // "Scales" selected
                return getResources().getStringArray(R.array.ScalesArray);
            default: // nothing selected
                setPlaybackGroupVisibility(false);
                return new String[]{""};
        }
    }

    private String[] getPlaybackModes() {

        switch (mSelectMaterialSpinner.getSelectedItemPosition()) {
            case 1: // "Intervals" selected
                return getResources().getStringArray(R.array.ChordsPlaybackModes);
            case 2: // "Chords" selected
                return getResources().getStringArray(R.array.ChordsPlaybackModes);
            case 3: // "Scales" selected
                return getResources().getStringArray(R.array.ScalesPlaybackModes);
            default: // nothing selected
                return new String[]{""};
        }
    }


    // SELECTION HANDLERS //

    public void playbackHandler(View view) {

        // Determine button ID
        switch (view.getId()) {
            case R.id.playSelectionButton:
                detectPlaybackMode(mPlaybackModesSpinner.getSelectedItemPosition());
                mSoundObjectPlayer.playSoundObject(mChordScale);
                break;
        }

        setPlaybackGroupEnable(false);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // disable/change color of button on play, re-enable on stop
                setPlaybackGroupEnable(true);
            }
        }, mChordScale.getDurationMilliseconds() + 500);
    }

    public void selectionDetailsHandler(View view) {

        if (view instanceof LinearLayout) {
            // ListView item
            LinearLayout selectedLayout = (LinearLayout) view;
            ChordScale selectedChordScale = (ChordScale) selectedLayout.getTag();

            // Build SoundObject
            mChordScale = selectedChordScale;

            // Determine file path
            if (mChordScale.getSize() > ChordScale.CHORDSCALE_DEFAULT_INITIAL_SIZE) {
                String pathName;

                if (mFilterBySpinner.getSelectedItem().toString().equalsIgnoreCase("Full Scala Archive")) {
                    pathName = "scl/"; // dir for HUGE Scala libaray
                }
                else {
                    pathName = "basic_scales"; // separate directory for basic scales .scl files
                }

                // Build ChordScale
                mMusicalMaterialsDB.buildChordScaleFromSCL(mChordScale, pathName, mChordScale.getSCLfileName());
            }

            // Info text
            mDisplayNameTextView.setText(selectedChordScale.getName());
            mDisplayDescriptionTextView.setText(selectedChordScale.getDescription());

            // Enable playback
            setPlaybackGroupVisibility(true);
        }
        else {
            // Info text
            mDisplayNameTextView.setText("");
            mDisplayDescriptionTextView.setText("");

            // disable playback
            setPlaybackGroupVisibility(false);
        }
    }


    // PLAYBACK OPTIONS //

    private void detectPlaybackMode(int pos) {
        // Set PlayBack mode
        switch (pos) {
            case 0: // Block/Cluster
                mChordScale.setPlayBackMode(ChordScale.PLAYBACK_MODE_CHORDSCALE_BLOCK_CLUSTER);
                mChordScale.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG * 3);
                break;
            case 1: // Up
                mChordScale.setPlayBackMode(ChordScale.PLAYBACK_MODE_CHORDSCALE_UP);
                mChordScale.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_SHORT);
                break;
            case 2: // Down
                mChordScale.setPlayBackMode(ChordScale.PLAYBACK_MODE_CHORDSCALE_DOWN);
                mChordScale.setDurationMilliseconds(SoundObject.DEFAULT_DURATION_MILLISECONDS_SHORT);
                break;
            case 3: // Unused (for now)
                break;
            default: // Unused (for now)
        }
    }

    private void setPlaybackGroupVisibility(boolean visible) {
        if (visible) {
            mPlaySelectionButton.setVisibility(View.VISIBLE);
            mPlaybackModesSpinner.setVisibility(View.VISIBLE);
        }
        else {
            mPlaySelectionButton.setVisibility(View.INVISIBLE);
            mPlaybackModesSpinner.setVisibility(View.INVISIBLE);
        }
    }

    private void setPlaybackGroupEnable(boolean enabled) {
        if (enabled) {
            mPlaySelectionButton.setBackgroundColor(getResources().getColor(R.color.colorPlayActive));
            mPlaySelectionButton.setEnabled(true);
            mPlaybackModesSpinner.setEnabled(true);
        }
        else {
            mPlaySelectionButton.setBackgroundColor(getResources().getColor(R.color.colorPlayInactive));
            mPlaySelectionButton.setEnabled(false);
            mPlaybackModesSpinner.setEnabled(false);
        }
    }
}
