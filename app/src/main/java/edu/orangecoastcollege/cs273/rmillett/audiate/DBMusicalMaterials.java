package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ryan Millett on 12/13/17.
 */
public class DBMusicalMaterials extends SQLiteOpenHelper {

    private Context mContext;

    private static final String TAG = "DBMusicalMaterials";

    private String mMusicalMaterialsDatabaseName;
    private static final int DATABASE_VERSION = 2;

    // Table of intervals
    private static final String INTERVALS_TABLE = "Intervals";
    private static final String INTERVALS_KEY_FIELD_ID = "_id";
    private static final String FIELD_INTERVAL_NAME = "interval_name";
    private static final String FIELD_INTERVAL_RATIO = "interval_ratio";
    private static final String FIELD_INTERVAL_CENTS = "interval_cents";
    private static final String FIELD_INTERVAL_TET = "interval_tet";
    private static final String FIELD_INTERVAL_LIMIT = "interval_limit";
    private static final String FIELD_INTERVAL_MEANTONE = "interval_meantone";
    private static final String FIELD_INTERVAL_SUPERPARTICULAR = "interval_superparticular";
    private static final String FIELD_INTERVAL_DESCRIPTION = "interval_description";

    // Table of chords
    private static final String CHORDS_TABLE = "Chords";
    private static final String CHORDS_KEY_FIELD_ID = "_id";
    private static final String FIELD_CHORD_NAME = "chord_name";
    private static final String FIELD_CHORD_SIZE = "chord_size";
    private static final String FIELD_CHORD_DESCRIPTION = "chord_description";
    private static final String FIELD_CHORD_SCL_FILE_NAME = "scl_file_name";

    // Table of scales
    private static final String SCALES_TABLE = "Scales";
    private static final String SCALES_KEY_FIELD_ID = "_id";
    private static final String FIELD_SCALE_NAME = "scale_name";
    private static final String FIELD_SCALE_SIZE = "scale_size";
    private static final String FIELD_SCALE_DESCRIPTION = "scale_description";
    private static final String FIELD_SCALE_SCL_FILE_NAME = "scl_file_name";


    public DBMusicalMaterials(String musicalMaterialsDatabaseName, Context context) {
        super(context, musicalMaterialsDatabaseName, null, DATABASE_VERSION);
        mContext = context;
    }

    public String getDataBaseName() {
        return mMusicalMaterialsDatabaseName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Intervals table
        String createQuery = "CREATE TABLE " + INTERVALS_TABLE + "("
                + INTERVALS_KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_INTERVAL_NAME + " TEXT, "
                + FIELD_INTERVAL_RATIO + " TEXT, "
                + FIELD_INTERVAL_CENTS + " REAL, "
                + FIELD_INTERVAL_TET + " TEXT, "
                + FIELD_INTERVAL_LIMIT + " INTEGER, "
                + FIELD_INTERVAL_MEANTONE + " TEXT, "
                + FIELD_INTERVAL_SUPERPARTICULAR + " TEXT, "
                + FIELD_INTERVAL_DESCRIPTION + " TEXT" + ")";
        db.execSQL(createQuery);

        // Create Chords table
        createQuery = "CREATE TABLE " + CHORDS_TABLE + "("
                + CHORDS_KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_CHORD_NAME + " TEXT, "
                + FIELD_CHORD_SIZE + " INTEGER, "
                + FIELD_CHORD_DESCRIPTION + " TEXT, "
                +FIELD_CHORD_SCL_FILE_NAME + " TEXT" + ")";
        db.execSQL(createQuery);

        // Create Scales table
        createQuery = "CREATE TABLE " + SCALES_TABLE + "("
                + SCALES_KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_SCALE_NAME + " TEXT, "
                + FIELD_SCALE_SIZE + " INTEGER, "
                + FIELD_SCALE_DESCRIPTION + " TEXT, "
                + FIELD_SCALE_SCL_FILE_NAME + " TEXT" + ")";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + INTERVALS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CHORDS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SCALES_TABLE);

        onCreate(db);
    }


    // ---------- SINGLE ENTRIES:

        // ADD:

    public void addInterval(Note interval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_INTERVAL_NAME, interval.getName());
        values.put(FIELD_INTERVAL_RATIO, interval.getRatio());
        values.put(FIELD_INTERVAL_CENTS, interval.getSizeInCents());
        values.put(FIELD_INTERVAL_TET, Arrays.toString(interval.getTET()));
        values.put(FIELD_INTERVAL_LIMIT, interval.getLimit());
        values.put(FIELD_INTERVAL_MEANTONE,interval.isMeantone() ? "Meantone":"");
        values.put(FIELD_INTERVAL_SUPERPARTICULAR, interval.isSuperparticular() ? "Superparticular":"");
        values.put(FIELD_INTERVAL_DESCRIPTION, interval.getDescription());

        db.insert(INTERVALS_TABLE, null, values);
        db.close();
    }

    public void addChord(ChordScale chord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_CHORD_NAME, chord.getName());
        values.put(FIELD_CHORD_SIZE, chord.getSize());
        values.put(FIELD_CHORD_DESCRIPTION, chord.getDescription());
        values.put(FIELD_CHORD_SCL_FILE_NAME, chord.getSCLfileName());

        db.insert(CHORDS_TABLE, null, values);
        db.close();
    }

    public void addScale(ChordScale scale) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_SCALE_NAME, scale.getName());
        values.put(FIELD_SCALE_SIZE, scale.getSize());
        values.put(FIELD_SCALE_DESCRIPTION, scale.getDescription());
        values.put(FIELD_SCALE_SCL_FILE_NAME, scale.getSCLfileName());

        db.insert(SCALES_TABLE, null, values);
        db.close();
    }

        // UPDATE:

    public void updateInterval(Note interval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_INTERVAL_NAME, interval.getName());
        values.put(FIELD_INTERVAL_RATIO, interval.getRatio());
        values.put(FIELD_INTERVAL_CENTS, interval.getSizeInCents());
        values.put(FIELD_INTERVAL_TET, Arrays.toString(interval.getTET()));
        values.put(FIELD_INTERVAL_LIMIT, interval.getLimit());
        values.put(FIELD_INTERVAL_MEANTONE,interval.isMeantone() ? "Meantone":"");
        values.put(FIELD_INTERVAL_SUPERPARTICULAR, interval.isSuperparticular() ? "Superparticular":"");
        values.put(FIELD_INTERVAL_DESCRIPTION, interval.getDescription());


        db.update(INTERVALS_TABLE, values, INTERVALS_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(interval.getId())});
        db.close();
    }

    public void updateChord(ChordScale chord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_CHORD_NAME, chord.getName());
        values.put(FIELD_CHORD_SIZE, chord.getSize());
        values.put(FIELD_CHORD_DESCRIPTION, chord.getDescription());
        values.put(FIELD_CHORD_SCL_FILE_NAME, chord.getSCLfileName());

        db.update(CHORDS_TABLE, values, CHORDS_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(chord.getId())});
        db.close();
    }

    public void updateScale(ChordScale scale) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_SCALE_NAME, scale.getName());
        values.put(FIELD_SCALE_SIZE, scale.getSize());
        values.put(FIELD_SCALE_DESCRIPTION, scale.getDescription());
        values.put(FIELD_SCALE_SCL_FILE_NAME, scale.getSCLfileName());

        db.update(SCALES_TABLE, values, SCALES_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(scale.getId())});
        db.close();
    }

        // DELETE:

    public void deleteInterval(Note interval) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(INTERVALS_TABLE, INTERVALS_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(interval.getId())});
        db.close();
    }

    public void deleteChord(ChordScale chord) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CHORDS_TABLE, CHORDS_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(chord.getId())});
    }

    public void deleteScale(ChordScale scale) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(SCALES_TABLE, SCALES_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(scale.getId())});
        db.close();
    }


    // ---------- FULL LISTS:

        // GET ALL:

    public List<Note> getAllIntervals() {
        List<Note> allIntervalsList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                INTERVALS_TABLE,
                new String[]{
                        INTERVALS_KEY_FIELD_ID,
                        FIELD_INTERVAL_NAME,
                        FIELD_INTERVAL_RATIO,
                        FIELD_INTERVAL_CENTS,
                        FIELD_INTERVAL_TET,
                        FIELD_INTERVAL_LIMIT,
                        FIELD_INTERVAL_MEANTONE,
                        FIELD_INTERVAL_SUPERPARTICULAR,
                        FIELD_INTERVAL_DESCRIPTION
                },
                null,
                null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                boolean meantone = cursor.getString(5).equalsIgnoreCase("Meantone");
                boolean superparticular = cursor.getString(6).equalsIgnoreCase("Superparticular");

                // Create interval
                Note interval = new Note(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        meantone,
                        superparticular,
                        cursor.getString(7)
                );

                // add to list
                allIntervalsList.add(interval);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return allIntervalsList;
    }

    public List<ChordScale> getAllChords() {
        ArrayList<ChordScale> allChordsList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                SCALES_TABLE,
                new String[]{SCALES_KEY_FIELD_ID,
                        FIELD_SCALE_NAME,
                        FIELD_SCALE_SIZE,
                        FIELD_SCALE_DESCRIPTION,
                        FIELD_SCALE_SCL_FILE_NAME},
                null,
                null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                // Build chord
                ChordScale chord = new ChordScale(
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4));

                // add scale
                allChordsList.add(chord);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return allChordsList;
    }

    public List<ChordScale> getAllScales() {
        ArrayList<ChordScale> allScalesList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                SCALES_TABLE,
                new String[]{SCALES_KEY_FIELD_ID,
                        FIELD_SCALE_NAME,
                        FIELD_SCALE_SIZE,
                        FIELD_SCALE_DESCRIPTION,
                        FIELD_SCALE_SCL_FILE_NAME},
                null,
                null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                // Build scale
                ChordScale scale = new ChordScale(
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4));

                // add scale
                allScalesList.add(scale);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return allScalesList;
    }

        // DELETE ALL:

    public void deleteAllIntervals() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(INTERVALS_TABLE, null, null);
        database.close();
    }

    public void deleteAllChords() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(CHORDS_TABLE, null, null);
        database.close();
    }

    public void deleteAllScales() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(SCALES_TABLE, null, null);
        database.close();
    }


    // ---------- FILTERED LISTS:

        // Intervals:

    public List<ChordScale> getAllEqualTemperedIntervals() {
        ArrayList<ChordScale> allEqualTemperedIntervalsList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                INTERVALS_TABLE,
                new String[]{
                        INTERVALS_KEY_FIELD_ID,
                        FIELD_INTERVAL_NAME,
                        FIELD_INTERVAL_RATIO,
                        FIELD_INTERVAL_CENTS,
                        FIELD_INTERVAL_TET,
                        FIELD_INTERVAL_LIMIT,
                        FIELD_INTERVAL_MEANTONE,
                        FIELD_INTERVAL_SUPERPARTICULAR,
                        FIELD_INTERVAL_DESCRIPTION
                },
                null,
                null,
                null, null, null, null);

        if (cursor.moveToFirst()) {

            do {
                if (cursor.getString(1).toUpperCase().contains("equal")
                        || Music.parseTET(cursor.getString(4))[0] != 0
                        || Music.parseTET(cursor.getString(4)).length > 1) {

                    // Create ChordScale
                    ChordScale interval =
                            new ChordScale(cursor.getString(1), cursor.getString(7));

                    boolean meantone =
                            cursor.getString(5).equalsIgnoreCase("Meantone");

                    boolean superparticular =
                            cursor.getString(6).equalsIgnoreCase("Superparticular");

                    // Add interval
                    interval.addChordMember(new Note(
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getDouble(3),
                            cursor.getString(4),
                            cursor.getInt(5),
                            meantone,
                            superparticular,
                            cursor.getString(7)
                    ));

                    allEqualTemperedIntervalsList.add(interval);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return allEqualTemperedIntervalsList;
    }

    public List<ChordScale> getAllJustIntervals() {
        ArrayList<ChordScale> allAllJustIntervalsList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                INTERVALS_TABLE,
                new String[]{
                        INTERVALS_KEY_FIELD_ID,
                        FIELD_INTERVAL_NAME,
                        FIELD_INTERVAL_RATIO,
                        FIELD_INTERVAL_CENTS,
                        FIELD_INTERVAL_TET,
                        FIELD_INTERVAL_LIMIT,
                        FIELD_INTERVAL_MEANTONE,
                        FIELD_INTERVAL_SUPERPARTICULAR,
                        FIELD_INTERVAL_DESCRIPTION
                },
                null,
                null,
                null, null, null, null);

        if (cursor.moveToFirst()) {

            do {
                if (cursor.getString(1).toUpperCase().contains("just")) {

                    // Create ChordScale
                    ChordScale interval =
                            new ChordScale(cursor.getString(1), cursor.getString(7));

                    boolean meantone =
                            cursor.getString(5).equalsIgnoreCase("Meantone");

                    boolean superparticular =
                            cursor.getString(6).equalsIgnoreCase("Superparticular");

                    // Add interval
                    interval.addChordMember(new Note(
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getDouble(3),
                            cursor.getString(4),
                            cursor.getInt(5),
                            meantone,
                            superparticular,
                            cursor.getString(7)
                    ));

                    allAllJustIntervalsList.add(interval);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return allAllJustIntervalsList;
    }

    public List<ChordScale> getAllMeantoneIntervals() {
        ArrayList<ChordScale> allMeantoneIntervalsList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                INTERVALS_TABLE,
                new String[]{
                        INTERVALS_KEY_FIELD_ID,
                        FIELD_INTERVAL_NAME,
                        FIELD_INTERVAL_RATIO,
                        FIELD_INTERVAL_CENTS,
                        FIELD_INTERVAL_TET,
                        FIELD_INTERVAL_LIMIT,
                        FIELD_INTERVAL_MEANTONE,
                        FIELD_INTERVAL_SUPERPARTICULAR,
                        FIELD_INTERVAL_DESCRIPTION
                },
                FIELD_INTERVAL_MEANTONE + "=?",
                new String[]{String.valueOf("Meantone")},
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                // Create ChordScale
                ChordScale interval = new ChordScale(cursor.getString(1), cursor.getString(7));

                boolean meantone = cursor.getString(5).equalsIgnoreCase("Meantone");
                boolean superparticular = cursor.getString(6).equalsIgnoreCase("Superparticular");

                // Add interval
                interval.addChordMember(new Note(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        meantone,
                        superparticular,
                        cursor.getString(7)
                ));

                // add to list
                allMeantoneIntervalsList.add(interval);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return allMeantoneIntervalsList;
    }

    public List<ChordScale> getAllHarmonics() {
        ArrayList<ChordScale> allHarmonicssList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                INTERVALS_TABLE,
                new String[]{
                        INTERVALS_KEY_FIELD_ID,
                        FIELD_INTERVAL_NAME,
                        FIELD_INTERVAL_RATIO,
                        FIELD_INTERVAL_CENTS,
                        FIELD_INTERVAL_TET,
                        FIELD_INTERVAL_LIMIT,
                        FIELD_INTERVAL_MEANTONE,
                        FIELD_INTERVAL_SUPERPARTICULAR,
                        FIELD_INTERVAL_DESCRIPTION
                },
                null,
                null,
                null, null, null, null);

        if (cursor.moveToFirst()) {

            do {
                if (cursor.getString(1).toUpperCase().contains("harmonic")) {

                    // Create ChordScale
                    ChordScale interval =
                            new ChordScale(cursor.getString(1), cursor.getString(7));

                    boolean meantone =
                            cursor.getString(5).equalsIgnoreCase("Meantone");

                    boolean superparticular =
                            cursor.getString(6).equalsIgnoreCase("Superparticular");

                    // Add interval
                    interval.addChordMember(new Note(
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getDouble(3),
                            cursor.getString(4),
                            cursor.getInt(5),
                            meantone,
                            superparticular,
                            cursor.getString(7)
                    ));

                    allHarmonicssList.add(interval);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return allHarmonicssList;
    }


    // ---------- Scala HELPER METHODS:

    public void buildChordScaleFromSCL(ChordScale chordScale, String pathName, String sclFileName) {

        AssetManager manager = mContext.getAssets();
        String line = "";
        try {
            InputStream inputStream = manager.open(pathName + sclFileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            // skip all header text/info
            while (!Music.isInteger(line)) {
                line = br.readLine();
            }

            // ChordScale size
            line = br.readLine();

            Log.i(TAG, "chordScale size->" + chordScale.getSize() + " | archive size->" + line);

            double fundamentalFrequency = chordScale.getChordMemberAtPos(0).getPitchFrequency();

            // read each interval line
            for (int i = 1; i < chordScale.getSize() && line != null; ++i) {

                // skip any scl comments
                if (line.startsWith("!")) {
                    line = br.readLine();
                }
                else {
                    chordScale.getChordMemberAtPos(i)
                            .buildFromSCL(Music.parseDecimalFromScalaLine(line), fundamentalFrequency);

                    Log.i(TAG, "[Confirm freq: " + chordScale.getChordMemberAtPos(i).getPitchFrequency()
                            + " | scala line: " + line + "]");
                }
            }
        }
        catch (IOException e) {
            Log.e(TAG, "Unable to locate " + sclFileName + " in " + pathName);
        }
    }


    // ---------- IMPORTS:

    public boolean importIntervalsFromCSV(String csvFileName) {
        AssetManager manager = mContext.getAssets();
        InputStream inputStream;
        try {
            inputStream = manager.open(csvFileName);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        int i = 6; String line; int num = 1;
        String tempStr = "temp";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 8) {
                    Log.d(TAG, "Skipping Bad CSV Row" + Arrays.toString(fields));
                    continue;
                }


                //int id = Integer.parseInt(fields[0].trim());
                Log.i(TAG, "No.->" + num++);

                double cents = Double.parseDouble(fields[1].trim());
                Log.i(TAG, "cents->" + cents);

                String ratio = !(fields[2].trim().contains("power"))
                        ? fields[2].trim().replace(":", "/") : fields[2].trim();
                Log.i(TAG, "ratio->" + ratio);

                int limit = (Music.isInteger(fields[3])) ? Integer.parseInt(fields[3].trim()) : -1;
                Log.i(TAG, "limit->" + limit);

                boolean meantone = fields[4].trim().contains("Meantone");
                Log.i(TAG, "meantone->" + meantone);

                boolean superparticular = fields[5].trim().contains("Superparticular");
                Log.i(TAG, "superparticular->" + superparticular);

                String tet = "";
                ;
                if (fields[6].startsWith("\"")) {
                    tempStr = "";
                    while (!fields[i].endsWith("\"")) {
                        tempStr += (fields[i] + ",");
                        i++;
                    }
                    tet = tempStr + fields[i];
                    tet = tet.replaceAll("\"", "");
                }
                else if (fields[6].isEmpty()) {
                    tet = "0";
                }
                else {
                    tet = fields[6].trim();
                }

                Log.i(TAG, "tet->" + tet);

                i++;
                String name = "";
                tempStr = "";
                if (fields[i].startsWith("\"")) {
                    while (i < fields.length - 1) {
                        tempStr += (fields[i] + ", ");
                        i++;
                    }
                    name = tempStr + fields[i];
                }
                else {
                    name = fields[i].trim();
                }

                name = name.replaceAll("\"", "");

                name = name.replaceAll("\\[\\d+\\]", "");

                Log.i(TAG, "Interval name->" + name);

                String description = "Ratio: " + ratio + " | Size in cents: " + cents
                        + (limit > 0 ? " | Limit: " + limit : "")
                        + (!tet.equals("0") ? " | " + tet + "-TET" : "");
                Log.i(TAG, "description->" + description);


                if (tet.contains(".")) tet =
                        String.valueOf((int) Math.round(Double.parseDouble(tet)));

                Note interval = new Note(
                        name,
                        ratio,
                        cents,
                        tet,
                        limit,
                        meantone,
                        superparticular,
                        description);

                Log.i(TAG, "//--------------//");

                // add interval to DB
                addInterval(interval);
                i = 6;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean importChordsFromCSV(String csvFileName) {
        // TODO: this method
        AssetManager manager = mContext.getAssets();
        InputStream inputStream;
        try {
            inputStream = manager.open(csvFileName);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line; int chordNo = 1; int totalChords = 0;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 4) {
                    Log.d(TAG, "Skipping Bad CSV Row" + Arrays.toString(fields));
                    continue;
                }

                Log.i(TAG, "schordNo-> " + chordNo++);

                //int id = Integer.parseInt(fields[0].trim()); // TODO: fix this
                String sclFileName = fields[1].trim();
                Log.i(TAG, "fileName-> " + sclFileName);

                String name = fields[2].trim();
                Log.i(TAG, "name-> " + name);

                int size = Integer.parseInt(fields[3].trim());
                Log.i(TAG, "size-> " + size);

                String description = "";

                for (int j = 4; j < fields.length; ++j) {
                    description += fields[j];
                }

                Log.i(TAG, "description-> " + description);

                ChordScale chord = new ChordScale(name, size, description, sclFileName);

                Log.i(TAG, "[Confirm size-> " + chord.getSize() + "]");
                Log.i(TAG, "//----------//----------//");
                // add to DB
                addChord(chord);
                totalChords++;
            }
            Log.i(TAG, totalChords + " TOTAL CHORDS IMPORTED!");
            Log.i(TAG, "//----------//----------//");
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean importScalesFromCSV(String csvFileName) {
        AssetManager manager = mContext.getAssets();
        InputStream inputStream;
        try {
            inputStream = manager.open(csvFileName);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line; int scaleNo = 1; int totalScales = 0;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 4) {
                    Log.d(TAG, "Skipping Bad CSV Row" + Arrays.toString(fields));
                    continue;
                }

                Log.i(TAG, "scaleNo-> " + scaleNo++);

                //int id = Integer.parseInt(fields[0].trim()); // TODO: fix this
                String sclFileName = fields[1].trim();
                Log.i(TAG, "fileName-> " + sclFileName);

                String name = fields[2].replaceAll("\"", "").trim();
                Log.i(TAG, "name-> " + name);

                int size = Integer.parseInt(fields[3].trim());
                Log.i(TAG, "size-> " + size);

                String description = "";

                for (int j = 4; j < fields.length; ++j) {
                    description += fields[j];
                }

                Log.i(TAG, "description-> " + description);

                ChordScale scale = new ChordScale(name, size, description, sclFileName);

                Log.i(TAG, "[Confirm size-> " + scale.getSize() + "]");
                Log.i(TAG, "//----------//----------//");
                // add to DB
                addScale(scale);
                totalScales++;
            }
            Log.i(TAG, totalScales + " TOTAL SCALES IMPORTED! (Whew)");
            Log.i(TAG, "//----------//----------//");
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
