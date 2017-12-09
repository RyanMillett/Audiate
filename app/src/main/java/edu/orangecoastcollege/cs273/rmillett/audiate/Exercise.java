package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Ryan Millett
 * @version 1.0
 */
public class Exercise implements Parcelable {

    public static final String EAR_TRAINING_EXERCISE_MODE = "Ear Training Exercise";
    public static final String SIGHT_SINGING_EXERCISE_MODE = "Sight Singing Exercise";

    public static final String EXERCISE_MATERIAL_INTERVALS = "Intervals";
    public static final String EXERCISE_MATERIAL_CHORDS = "Chords";
    public static final String EXERCISE_MATERIAL_CHORDS_SEQUENCES = "Chord Sequences";
    public static final String EXERCISE_MATERIAL_SCALES = "Scales";
    public static final String EXERCISE_MATERIAL_MELODIC_PATTERNS = "Melodic Patterns";

    public static final int EXERCISE_DIFFICULTY_1_BEGINNER = 1;
    public static final int EXERCISE_DIFFICULTY_2_INTERMEDIATE = 2;
    public static final int EXERCISE_DIFFICULTY_3_PROFICIENT = 3;
    public static final int EXERCISE_DIFFICULTY_4_ADVANCED = 4;
    public static final int EXERCISE_DIFFICULTY_5_EXPERT = 5;

    public static final String DEFAULT_DESCRIPTION = "No description.";

    private long mId;
    private String mExerciseName;
    private String mExerciseMode;
    private String mExerciseMaterial;
    private int mExerciseDifficulty;
    private String mExerciseDescription;


    public Exercise() {
        mId = -1;
        mExerciseName = "";
        mExerciseMode = "";
        mExerciseMaterial = "";
        mExerciseDifficulty = EXERCISE_DIFFICULTY_1_BEGINNER;
        mExerciseDescription = DEFAULT_DESCRIPTION;
    }

    public Exercise(String exerciseName, String exerciseMode, String exerciseMaterial, int difficulty, String exerciseDescription) {
        mId = -1;
        mExerciseName = exerciseName;
        mExerciseMode = exerciseMode;
        mExerciseMaterial = exerciseMaterial;
        mExerciseDifficulty = difficulty;
        mExerciseDescription = exerciseDescription;
    }

    public Exercise(long Id, String exerciseName, String exerciseMode, String exerciseMaterial, int exerciseDifficulty, String exerciseDescription) {
        mId = Id;
        mExerciseName = exerciseName;
        mExerciseMode = exerciseMode;
        mExerciseMaterial = exerciseMaterial;
        mExerciseDifficulty = exerciseDifficulty;
        mExerciseDescription = exerciseDescription;
    }

    public long getId() {
        return mId;
    }

    public String getExerciseName() {
        return mExerciseName;
    }

    public void setExerciseName(String exerciseName) {
        mExerciseName = exerciseName;
    }

    public String getExerciseMode() {
        return mExerciseMode;
    }

    public void setExerciseMode(String exerciseMode) {
        mExerciseMode = exerciseMode;
    }

    public String getExerciseMaterial() {
        return mExerciseMaterial;
    }

    public void setExerciseMaterial(String exerciseMaterial) {
        mExerciseMaterial = exerciseMaterial;
    }

    public int getExerciseDifficulty() {
        return mExerciseDifficulty;
    }

    public void setExerciseDifficulty(int exerciseDifficulty) {
        mExerciseDifficulty = exerciseDifficulty;
    }

    public String getExerciseDescription() {
        return mExerciseDescription;
    }

    public void setExerciseDescription(String exerciseDescription) {
        mExerciseDescription = exerciseDescription;
    }

    // PARCELABLE IMPLEMENTATION //

    private Exercise(Parcel parcel) {
        mId = parcel.readLong();
        mExerciseName = parcel.readString();
        mExerciseMode = parcel.readString();
        mExerciseMaterial = parcel.readString();
        mExerciseDifficulty = parcel.readInt();
        mExerciseDescription = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mExerciseName);
        parcel.writeString(mExerciseMode);
        parcel.writeString(mExerciseMaterial);
        parcel.writeInt(mExerciseDifficulty);
        parcel.writeString(mExerciseDescription);
    }

    public static final Parcelable.Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel parcel) {
            return new Exercise(parcel);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };


}
