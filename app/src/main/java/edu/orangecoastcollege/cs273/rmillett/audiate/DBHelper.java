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

import static android.R.attr.id;

/**
 * @author Ryan Millett
 * @version 2.0
 *
 * USERS_TABLE @author Brian Wegener
 * @version 1.0
 */
public class DBHelper extends SQLiteOpenHelper {

    private Context mContext;

    private static final String TAG = "DBHelper";

    static final String DATABASE_NAME = "AudiateDatabase";
    private static final int DATABASE_VERSION = 1;

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

    // Table of Exercises
    private static final String EXERCISE_TABLE = "Exercises";
    private static final String EXERCISE_KEY_FIELD_ID = "_id";
    private static final String FIELD_EXERCISE_NAME = "exercise_name";
    private static final String FIELD_EXERCISE_MODE = "exercise_mode";
    private static final String FIELD_EXERCISE_MATERIAL = "exercise_material";
    private static final String FIELD_EXERCISE_DIFFICULTY = "exercise_difficulty";
    private static final String FIELD_EXERCISE_DESCRIPTION = "exercise_description";

    // Table of users
    private static final String USERS_TABLE = "Users";
    private static final String USERS_KEY_FIELD_ID = "_id";
    private static final String FIELD_USER_NAME = "user_name";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_LOW_PITCH = "lowest_pitch";
    private static final String FIELD_HIGH_PITCH = "highest_pitch";
    private static final String FIELD_VOCAL_RANGE = "vocal_range";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

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
        sqLiteDatabase.execSQL(createQuery);

        // Create Chords table
        createQuery = "CREATE TABLE " + CHORDS_TABLE + "("
                + CHORDS_KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_CHORD_NAME + " TEXT, "
                + FIELD_CHORD_SIZE + " INTEGER, "
                + FIELD_CHORD_DESCRIPTION + " TEXT, "
                +FIELD_CHORD_SCL_FILE_NAME + " TEXT" + ")";
        sqLiteDatabase.execSQL(createQuery);

        // Create Scales table
        createQuery = "CREATE TABLE " + SCALES_TABLE + "("
                + SCALES_KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_SCALE_NAME + " TEXT, "
                + FIELD_SCALE_SIZE + " INTEGER, "
                + FIELD_SCALE_DESCRIPTION + " TEXT, "
                + FIELD_SCALE_SCL_FILE_NAME + " TEXT" + ")";
        sqLiteDatabase.execSQL(createQuery);

        // Create Exercises table
        createQuery = "CREATE TABLE " + EXERCISE_TABLE + "("
                + EXERCISE_KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_EXERCISE_NAME + " TEXT, "
                + FIELD_EXERCISE_MODE + " TEXT, "
                + FIELD_EXERCISE_MATERIAL + " TEXT, "
                + FIELD_EXERCISE_DIFFICULTY + " INTEGER, "
                + FIELD_EXERCISE_DESCRIPTION + " TEXT" + ")";
        sqLiteDatabase.execSQL(createQuery);

        // Create Users table
        createQuery = "CREATE TABLE " + USERS_TABLE + "("
                + USERS_KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_USER_NAME + " TEXT, "
                + FIELD_EMAIL + " TEXT, "
                + FIELD_LOW_PITCH + " TEXT, "
                + FIELD_HIGH_PITCH + " TEXT, "
                + FIELD_VOCAL_RANGE + " TEXT" + ")";
        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + INTERVALS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CHORDS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SCALES_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EXERCISE_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);

        onCreate(sqLiteDatabase);
    }

    // ---------- ADD ---------- //

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

    public void addExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_EXERCISE_NAME, exercise.getExerciseName());
        values.put(FIELD_EXERCISE_MODE, exercise.getExerciseMode());
        values.put(FIELD_EXERCISE_MATERIAL, exercise.getExerciseMaterial());
        values.put(FIELD_EXERCISE_DIFFICULTY, exercise.getExerciseDifficulty());
        values.put(FIELD_EXERCISE_DESCRIPTION, exercise.getExerciseDescriptionTextFileName());

        db.insert(EXERCISE_TABLE, null, values);
        db.close();
    }

    /**
     * This adds a user to the database.
     * Each user contains a user name, email, low pitch, high pitch,
     * and vocal range.
     * @param user
     */
    public void addUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_USER_NAME, user.getUserName());
        values.put(FIELD_EMAIL, user.getEmail());
        values.put(FIELD_LOW_PITCH, user.getLowPitch());
        values.put(FIELD_HIGH_PITCH, user.getHighPitch());
        values.put(FIELD_VOCAL_RANGE, user.getVocalRange());

        long id = db.insert(USERS_TABLE, null, values);

        user.setId(id);

        db.close();
    }


    // ---------- UPDATE ---------- //

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

    public void updateExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_EXERCISE_NAME, exercise.getExerciseName());
        values.put(FIELD_EXERCISE_MODE, exercise.getExerciseMode());
        values.put(FIELD_EXERCISE_MATERIAL, exercise.getExerciseMaterial());
        values.put(FIELD_EXERCISE_DIFFICULTY, exercise.getExerciseDifficulty());
        values.put(FIELD_EXERCISE_DESCRIPTION, exercise.getExerciseDescriptionTextFileName());

        db.update(EXERCISE_TABLE, values, EXERCISE_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
        db.close();
    }

    /**
     * This updates the information from one user in the database.
     * @param user
     */
    public void updateUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_USER_NAME, user.getUserName());
        values.put(FIELD_EMAIL, user.getEmail());
        values.put(FIELD_LOW_PITCH, user.getLowPitch());
        values.put(FIELD_HIGH_PITCH, user.getHighPitch());
        values.put(FIELD_VOCAL_RANGE, user.getVocalRange());

        db.update(USERS_TABLE, values, USERS_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }


    // ---------- GET ---------- //

//    public Note getInterval(int id) {
//        // TODO: this method
//    }
//
//    public ChordScale getChord(int id) {
//        // TODO: this method
//    }
//
    public double[] createScaleFromSCL(ChordScale chordScale, String sclFileName) {
        double[] decimalIntervals = new double[chordScale.getSize()];
        AssetManager manager = mContext.getAssets();
        String line = "";
        try {
            InputStream inputStream = manager.open("scl/" + sclFileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));


            while (!Music.isInteger(line)) {
                line = br.readLine();
            }

//            Log.i(TAG, "chordScale object size->" + chordScale.getSize()
//                    + ", archive size->" + line);
//            Log.i(TAG, chordScale.getName());

            //double fundamentalFrequency = chordScale.getChordMemberAtPos(0).getPitchFrequency();

            line = br.readLine();
            //double interval; // decimal used for multiplication
            int i = 0;
            while (i < chordScale.getSize() && line != null) {
                // skip any scl comments
                if (line.startsWith("!")) {
                    line = br.readLine();
                }
                else {
                    try {
//                        chordScale.getChordMemberAtPos(i).setPitchFrequency(fundamentalFrequency
//                                * Music.parseDecimalFromScalaLine(line));
                        decimalIntervals[i++] = Music.parseDecimalFromScalaLine(line);
                        // get next line
                        line = br.readLine();
                    }
                    catch (NumberFormatException e) {
                        Log.e(TAG,"NumberFormatException: " + sclFileName + ", line->" + line + "\n");
                    }
                    catch (IOException e) {
                        Log.e(TAG,"IOException: " + sclFileName + ", line->" + line + "\n");
                    }
                }
            }
        }
        catch (IOException e) {
            Log.e(TAG, "Unable to locate " + sclFileName);
        }

//        chordScale.resetFundamentalFrequency();
        for (double dub : decimalIntervals) Log.i(TAG, dub + " ");
        return decimalIntervals;
    }

    public Exercise getExercise(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                EXERCISE_TABLE,
                new String[]{EXERCISE_KEY_FIELD_ID,
                        FIELD_EXERCISE_NAME,
                        FIELD_EXERCISE_MODE,
                        FIELD_EXERCISE_MATERIAL,
                        FIELD_EXERCISE_DIFFICULTY,
                        FIELD_EXERCISE_DESCRIPTION},
                EXERCISE_KEY_FIELD_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        Exercise exercise = new Exercise(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getString(5));

        cursor.close();
        db.close();
        return exercise;
    }



    /**
     * This gets one user from the database.
     * @param id
     * @return
     */
    public User getUser(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                USERS_TABLE,
                new String[]{USERS_KEY_FIELD_ID,
                        FIELD_USER_NAME, FIELD_EMAIL,
                        FIELD_LOW_PITCH, FIELD_HIGH_PITCH,
                        FIELD_VOCAL_RANGE},
                FIELD_EMAIL + "=?",
                new String[]{email},
                null, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        User user = new User(cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5));

        cursor.close();
        db.close();
        return user;
    }


    // ---------- DELETE ---------- //

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

    public void deleteExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(EXERCISE_TABLE, EXERCISE_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
        db.close();
    }

    /**
     * This deletes a user from the database.
     * @param user
     */
    public void deleteUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(USERS_TABLE, USERS_KEY_FIELD_ID + " = ?",
                new String[] {String.valueOf(user.getId())});
        db.close();
    }

    // ---------- GET ALL ---------- //

    // TODO: revise this to construct and return a Note
    public List<ChordScale> getAllIntervals() {
        List<ChordScale> allIntervalsList = new ArrayList<>();
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
                // Create ChordScale
                ChordScale interval = new ChordScale(cursor.getString(1), cursor.getString(7));
                interval.addChordMemberAt(0,new Note("Tonic/Fundamental"));

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
                allIntervalsList.add(interval);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return allIntervalsList;
    }

    // TODO: getAllChords()

    public List<ChordScale> getAllScales() {
        return null;
    }

    public List<ChordScale> getAllScalaArchiveScales() {
        ArrayList<ChordScale> fullScalaArchive = new ArrayList<>();
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
                ChordScale chordScale = new ChordScale(
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4));

                // add scale
                fullScalaArchive.add(chordScale);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return fullScalaArchive;
    }

    public List<Exercise> getAllExercises() {
        ArrayList<Exercise> allExercisesList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                EXERCISE_TABLE,
                new String[]{EXERCISE_KEY_FIELD_ID,
                        FIELD_EXERCISE_NAME,
                        FIELD_EXERCISE_MODE,
                        FIELD_EXERCISE_MATERIAL,
                        FIELD_EXERCISE_DIFFICULTY,
                        FIELD_EXERCISE_DESCRIPTION},
                null,
                null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise(cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5));

                allExercisesList.add(exercise);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return allExercisesList;
    }

    /**
     * This gets a list of all the users from the database.
     * @return
     */
    public ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                USERS_TABLE,
                new String[]{USERS_KEY_FIELD_ID,
                        FIELD_USER_NAME, FIELD_EMAIL,
                        FIELD_LOW_PITCH, FIELD_HIGH_PITCH,
                        FIELD_VOCAL_RANGE},
                null,
                null,
                null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                User user = new User(cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        return userList;
    }

    // ---------- DELETE ALL ---------- //

    public void deleteAllIntervals() {
        // TODO: this method
    }

    public void deleteALLChords() {
        // TODO: this method
    }

    public void deleteAllScales() {
        // TODO: this method
    }

    public void deleteAllExercies() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(EXERCISE_TABLE, null, null);
        database.close();
    }

    /**
     * This deletes all the users from the database.
     */
    public void deleteAllUsers()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(USERS_TABLE, null, null);

        db.close();
    }


    // ---------- FILTERED LISTS ---------- //

    // Intervals:
        // Harmonics (up to 127th)
        // Historical (all named intervals)
        // Diatonic Just-Intoned
        // Dodecaphonic Just-Intoned
        // Diatonic-Chromatic Just and E.T.
        // All Just-Intervals
    // Chords:
        // Triads -> Maj, min, Aug, dim
        // 7ths -> Maj-maj7th, Maj-min7th (Dom.7th), min-maj7th, min-min7th, half-dimished 7th, fully-dimished 7th
        // Just vs. E.T. triads/7ths
    // Scales:
        // Heptatonic (diatonic) scales and modes
        // Modes -> Ionian, Dorian, Phrygian, Lydian, Mixolydian, Aeolian, Locrian)
        // Olivier Messiaen's "Modes of Limited Transposition"


    public List<Exercise> getAllExercisesByMode(String exerciseMode) {
        ArrayList<Exercise> allExercisesList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                EXERCISE_TABLE,
                new String[]{EXERCISE_KEY_FIELD_ID,
                        FIELD_EXERCISE_NAME,
                        FIELD_EXERCISE_MODE,
                        FIELD_EXERCISE_MATERIAL,
                        FIELD_EXERCISE_DIFFICULTY,
                        FIELD_EXERCISE_DESCRIPTION},
                FIELD_EXERCISE_MODE + "=?",
                new String[]{String.valueOf(exerciseMode)},
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise(cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5));

                allExercisesList.add(exercise);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return allExercisesList;
    }

    public List<Exercise> getAllExercisesByMaterial(String exerciseMaterial) {
        ArrayList<Exercise> allExercisesList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                EXERCISE_TABLE,
                new String[]{EXERCISE_KEY_FIELD_ID,
                        FIELD_EXERCISE_NAME,
                        FIELD_EXERCISE_MODE,
                        FIELD_EXERCISE_MATERIAL,
                        FIELD_EXERCISE_DIFFICULTY,
                        FIELD_EXERCISE_DESCRIPTION},
                FIELD_EXERCISE_MATERIAL + "=?",
                new String[]{String.valueOf(exerciseMaterial)},
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise(cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5));

                allExercisesList.add(exercise);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return allExercisesList;
    }

    public List<Exercise> getAllExercisesByDifficulty(int exerciseDifficulty) {
        ArrayList<Exercise> allExercisesList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                EXERCISE_TABLE,
                new String[]{EXERCISE_KEY_FIELD_ID,
                        FIELD_EXERCISE_NAME,
                        FIELD_EXERCISE_MODE,
                        FIELD_EXERCISE_MATERIAL,
                        FIELD_EXERCISE_DIFFICULTY,
                        FIELD_EXERCISE_DESCRIPTION},
                FIELD_EXERCISE_DIFFICULTY + "=?",
                new String[]{String.valueOf(exerciseDifficulty)},
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise(cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5));

                allExercisesList.add(exercise);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return allExercisesList;
    }

    public List<Exercise> getAllIntervalExercisesByModeAndMaterial(String exerciseMode, String exerciseMaterial) {
        ArrayList<Exercise> allExercisesList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                EXERCISE_TABLE,
                new String[]{EXERCISE_KEY_FIELD_ID,
                        FIELD_EXERCISE_NAME,
                        FIELD_EXERCISE_MODE,
                        FIELD_EXERCISE_MATERIAL,
                        FIELD_EXERCISE_DIFFICULTY,
                        FIELD_EXERCISE_DESCRIPTION},
                FIELD_EXERCISE_MODE + "=?",
                new String[]{String.valueOf(exerciseMode)},
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(3).equalsIgnoreCase(exerciseMaterial)) {
                    Exercise exercise = new Exercise(cursor.getLong(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getInt(4),
                            cursor.getString(5));
                    allExercisesList.add(exercise);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        Log.i(TAG, "allIntervalsBy " + exerciseMode + " and " + exerciseMaterial + "-->" + allExercisesList.size());
        return allExercisesList;
    }


    // ---------- IMPORTS ---------- //

    public boolean importPitchIntervalsFromCSV(String csvFileName) {
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
                        + "\n" + (limit >0 ? "Limit: " + limit : "")
                        + (!tet.equals("0") ? "| " + tet + "-TET":"")
                        + "\n" + (meantone ? " | (Meantone)": (superparticular ? " | (Superparticular)":""));
                Log.i(TAG, "description->" + description);



                // reformat ratio
                ratio = ratio.replaceAll("\\s","");
                ratio = ratio.replaceAll("\\[\\d+\\]", "");

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

    public boolean importKyleGannOctaveAnatomyFromCSV(String csvFileName) {
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
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length !=4) {
                    Log.d(TAG, "Skipping Bad CSV Row" + Arrays.toString(fields));
                    continue;
                }

                //int id = Integer.parseInt(fields[0].trim()); // TODO: fix this
                String name =
                        fields[1].trim().equalsIgnoreCase("UNNAMED")
                                ? fields[2].trim() : fields[1].trim();
                String ratio = fields[2].trim();
                double cents = Double.parseDouble(fields[3].trim());
                String description =
                        name.equalsIgnoreCase(ratio)
                                ? "Size in cents: " + cents
                                : "Ratio: " + ratio + "\nSize in cents: " + cents;

                Note interval = new Note(name, ratio, cents, description);

                // add interval to DB
                addInterval(interval);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // TODO: importAllChordsFromSCL()

    public boolean importScalaArchiveFromCSV(String csvFileName) {
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

                String name = fields[2].trim();
                Log.i(TAG, "name-> " + name);

                int size = Integer.parseInt(fields[3].trim());
                Log.i(TAG, "size-> " + size);

                String description = "";

                for (int j = 4; j < fields.length; ++j) {
                    description += fields[j];
                }

                Log.i(TAG, "description-> " + description);

                ChordScale scale = new ChordScale(name, size, description, sclFileName);

                Log.i(TAG, "Confirm size-> " + scale.getSize());
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

    public boolean importAllExercisesFromCSV(String csvFileName) {
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
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length !=6) {
                    Log.d(TAG, "Skipping Bad CSV Row" + Arrays.toString(fields));
                    continue;
                }

                int id = Integer.parseInt(fields[0].trim()); // TODO: fix this
                String exerciseName = fields[1].trim();
                String exerciseMode = fields[2].trim();
                String exerciseMaterial = fields[3].trim();
                int exerciseDifficulty = Integer.parseInt(fields[4].trim());
                String exerciseDescriptionTextFileName = fields[5];

                Exercise exercise = new Exercise(
                                    ++id,
                                    exerciseName,
                                    exerciseMode,
                                    exerciseMaterial,
                                    exerciseDifficulty,
                                    exerciseDescriptionTextFileName
                );

                Log.i(TAG, "exercise-->" + exercise.getExerciseName());

                // add to DB
                addExercise(exercise);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    // ----------------- Obsolete Method Waste Land -------------------------------- //

    // obsolete (for now)
    public List<ChordScale> getAllScalesFromSCL() {
        // create list with at least 4k initial capacity
        List<ChordScale> allScalesList = new ArrayList<>(5000);

        // create local object
        ChordScale scale;

        // member variables
        String name;
        int size;
        String description = "No description.";
        String sclFileName;

        AssetManager manager = mContext.getAssets();

        String line;
        try {
            String fileList[] = manager.list("scl");

            int scaleNum = 1; // for logging purposes
            // loop through each .scl file in the scl directory
            for (String file : fileList) {
                // file is one .scl file
                InputStream inputStream = manager.open("scl/" + file);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                // Line 1, .scl file name
                line = br.readLine().replace("!", "");
                sclFileName = line.trim();

                // Format name
                name = sclFileName.replace(".scl", "");
                name = name.replace("_", " ");
                // TODO: format capitalization

                // Scale Description (if any)
                while (!Music.isInteger(line)) {
                    if (line.contains("!")) {
                        line = br.readLine();
                    }
                    else {
                        description = line;
                        line = br.readLine();
                    }
                }

                // Scale Size
                size = Integer.parseInt(line.trim());

                Log.i(TAG, "(Scale No.: " + scaleNum++ + ") sclFileName->" + sclFileName + "\n");
//                Log.i(TAG, "name->" + name + "\n");
//                Log.i(TAG, "description->" + description + "\n");
//                Log.i(TAG, "size->" + size + "\n");

                // Build scale
                scale = new ChordScale(name, size, description, sclFileName);
                scale.addChordMember(new Note("Tonic"));

                line = br.readLine();
                double interval; // decimal used for multiplication
                int i = size;
                while (i >= 0 && line != null) {
                    // skip any scl comments
                    if (line.startsWith("!")) {
                        line = br.readLine();
                    }
                    else {
                        try {
                            interval = Music.parseDecimalFromScalaLine(line);
                            scale.addChordMember(new Note(interval));

                            // get next line
                            line = br.readLine();
                        }
                        catch (NumberFormatException e) {
                            Log.e(TAG,"NumberFormatException: " + sclFileName + ", line->" + line + "\n");
                        }
                        catch (IOException e) {
                            Log.e(TAG,"IOException: " + sclFileName + ", line->" + line + "\n");
                        }
                    }
                    i--;
                }
                allScalesList.add(scale);
                addScale(scale);
                br.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return allScalesList;
    }
}
