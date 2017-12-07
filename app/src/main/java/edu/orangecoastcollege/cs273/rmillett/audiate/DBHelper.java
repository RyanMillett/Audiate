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
 * @author Ryan Millett
 * @version 1.2
 */
public class DBHelper extends SQLiteOpenHelper {

    private Context mContext;

    private static final String TAG = "DBHelper";

    static final String DATABASE_NAME = "ChordScaleLibrary";
    private static final int DATABASE_VERSION = 1;

    // Table of intervals (a <code>ChordScale</code> with only two chord members)
    private static final String INTERVALS_TABLE = "Intervals";
    private static final String INTERVALS_KEY_FIELD_ID = "_id";
    private static final String FIELD_INTERVAL_NAME = "interval_name";
    private static final String FIELD_INTERVAL_RATIO = "interval_ratio";
    private static final String FIELD_INTERVAL_CENTS = "interval_cents";
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

    // Table of users
    private static final String USERS_TABLE = "Users";
    private static final String USERS_KEY_FIELD_ID = "id";
    private static final String FIELD_USER_NAME = "user_name";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_LOW_PITCH = "low_pitch";
    private static final String FIELD_HIGH_PITCH = "high_pitch";
    private static final String FIELD_VOCAL_RANGE = "vocal_range";

    // TODO: Relational tables
    // Intervals:
        // ALL intervals (full 580 list)
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
        // ALL scales (full 4,000+ list)
        // Heptatonic (diatonic) scales and modes
            // Modes -> Ionian, Dorian, Phrygian, Lydian, Mixolydian, Aeolian, Locrian)
        // Olivier Messiaen's "Modes of Limited Transposition"


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
                + FIELD_INTERVAL_DESCRIPTION + ")";
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

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);

        onCreate(sqLiteDatabase);
    }

    public void addInterval(ChordScale interval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_INTERVAL_NAME, interval.getName());
        values.put(FIELD_INTERVAL_RATIO, interval.getIntervalRatio(0, 1));
        values.put(FIELD_INTERVAL_CENTS, interval.getIntervalDistanceInCents(0,1));
        values.put(FIELD_INTERVAL_DESCRIPTION, interval.getDescription());

        db.insert(INTERVALS_TABLE, null, values);
        db.close();
    }

    public void addChord(ChordScale chord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_CHORD_NAME, chord.getName());
        values.put(FIELD_CHORD_SIZE, chord.getSize());
        // TODO: figure out how to add chord members
        values.put(FIELD_CHORD_DESCRIPTION, chord.getDescription());

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

    /**
     * This deletes all the users from the database.
     */
    public void deleteAllUsers()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(USERS_TABLE, null, null);

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

    /**
     * This gets one user from the database.
     * @param id
     * @return
     */
    public User getUser(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                USERS_TABLE,
                new String[]{USERS_KEY_FIELD_ID,
                FIELD_USER_NAME, FIELD_EMAIL,
                FIELD_LOW_PITCH, FIELD_HIGH_PITCH,
                FIELD_VOCAL_RANGE},
                USERS_KEY_FIELD_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();

        User user = new User(cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5));

        db.close();
        return user;
    }

    public boolean importAllIntervalsFromCSV(String csvFileName) {
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
        String line; int lineNum = 1;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length !=4) {
                    Log.d(TAG, "Skipping Bad CSV Row" + Arrays.toString(fields));
                    continue;
                }
                //Log.e(TAG, "Line Num->" + lineNum++ + ", " + line + "\n");

                //int id = Integer.parseInt(fields[0].trim()); // TODO: fix this
                String name = fields[1].trim();
                String ratio = fields[2].trim();
                double cents = Double.parseDouble(fields[3].trim());

                ChordScale interval = new ChordScale(name, 2);
                interval.addChordMemberAt(0, new Note("Fundamental"));
                interval.addChordMemberAt(1, new Note(name,
                        interval.getChordMemberAtPos(0).getPitchFrequency()
                                * Music.convertRatioToDecimal(ratio), ratio));
                interval.setDescription("Size in cents: " + cents);

                // add to DB
                addInterval(interval);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

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
                        FIELD_INTERVAL_DESCRIPTION
                },
                null,
                null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                // build interval
                ChordScale interval =
                        new ChordScale(cursor.getInt(0), cursor.getString(1), 2);
                interval.addChordMember(new Note("Fundamental"));
                interval.addChordMember(new Note(cursor.getString(1),
                        interval.getChordMemberAtPos(0).getPitchFrequency()
                                * Music.convertRatioToDecimal(cursor.getString(2)),
                        cursor.getString(2)));
                interval.setDescription(cursor.getString(4));

                // add to list
                allIntervalsList.add(interval);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return allIntervalsList;
    }

    // TODO: import/get all chords method(s)

    // TODO: consolidate this with a "Scala handler" method in Music helper method
    public List<ChordScale> importAllScalesFromSCL() {
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
                            scale.addChordMember(new Note(
                                    scale.getChordMemberAtPos(0).getPitchFrequency()
                                            * interval));

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
