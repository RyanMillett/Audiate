package edu.orangecoastcollege.cs273.rmillett.audiate.Databases;

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

import edu.orangecoastcollege.cs273.rmillett.audiate.Models.Exercise;

/**
 * Created by RyanMillett on 12/13/17.
 */
public class ExercisesDB extends SQLiteOpenHelper {

    private Context mContext;

    private static final String TAG = "ExercisesDB";

    static final String EXERCISES_DATABASE = "ExercisesDatabase";
    private static final int DATABASE_VERSION = 2;

    // Table of Exercises
    private static final String EXERCISE_TABLE = "Exercises";
    private static final String EXERCISE_KEY_FIELD_ID = "_id";
    private static final String FIELD_EXERCISE_NAME = "exercise_name";
    private static final String FIELD_EXERCISE_MODE = "exercise_mode";
    private static final String FIELD_EXERCISE_MATERIAL = "exercise_material";
    private static final String FIELD_EXERCISE_DIFFICULTY = "exercise_difficulty";
    private static final String FIELD_EXERCISE_DESCRIPTION = "exercise_description";

    public ExercisesDB(Context context) {
        super(context, EXERCISES_DATABASE, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Exercises table
        String createQuery = "CREATE TABLE " + EXERCISE_TABLE + "("
                + EXERCISE_KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_EXERCISE_NAME + " TEXT, "
                + FIELD_EXERCISE_MODE + " TEXT, "
                + FIELD_EXERCISE_MATERIAL + " TEXT, "
                + FIELD_EXERCISE_DIFFICULTY + " INTEGER, "
                + FIELD_EXERCISE_DESCRIPTION + " TEXT" + ")";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EXERCISE_TABLE);

        onCreate(db);
    }

    // ---------- SINGLE ENTRIES:

    public void addExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_EXERCISE_NAME, exercise.getExerciseName());
        values.put(FIELD_EXERCISE_MODE, exercise.getExerciseMode());
        values.put(FIELD_EXERCISE_MATERIAL, exercise.getExerciseMaterial());
        values.put(FIELD_EXERCISE_DIFFICULTY, exercise.getExerciseDifficultyLevel());
        values.put(FIELD_EXERCISE_DESCRIPTION, exercise.getExerciseDescriptionTextFileName());

        db.insert(EXERCISE_TABLE, null, values);
        db.close();
    }

    public void updateExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_EXERCISE_NAME, exercise.getExerciseName());
        values.put(FIELD_EXERCISE_MODE, exercise.getExerciseMode());
        values.put(FIELD_EXERCISE_MATERIAL, exercise.getExerciseMaterial());
        values.put(FIELD_EXERCISE_DIFFICULTY, exercise.getExerciseDifficultyLevel());
        values.put(FIELD_EXERCISE_DESCRIPTION, exercise.getExerciseDescriptionTextFileName());

        db.update(EXERCISE_TABLE, values, EXERCISE_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
        db.close();
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

    public void deleteExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(EXERCISE_TABLE, EXERCISE_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
        db.close();
    }


    // ---------- FULL LISTS:

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

    public void deleteAllExercies() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(EXERCISE_TABLE, null, null);
        database.close();
    }


    // ---------- FILTERED LISTS:

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


    // ---------- IMPORTS:

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
}
