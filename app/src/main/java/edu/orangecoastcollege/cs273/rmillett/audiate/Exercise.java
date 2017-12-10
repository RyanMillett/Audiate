package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Ryan Millett
 * @version 1.0
 */
public class Exercise implements Parcelable {

    public static final String EXERCISE_MODE_LISTENING = "Listening";
    public static final String EXERCISE_MODE_SINGING = "Singing";

    public static final String EXERCISE_MATERIAL_INTERVALS = "Intervals";
    public static final String EXERCISE_MATERIAL_CHORDS = "Chords";
    public static final String EXERCISE_MATERIAL_CHORDS_SEQUENCES = "Chord Sequences";
    public static final String EXERCISE_MATERIAL_SCALES = "Scales";
    public static final String EXERCISE_MATERIAL_MELODIC_PATTERNS = "Melodic Patterns";

    public static final int EXERCISE_DIFFICULTY_BEGINNER = 1;
    public static final String EXERCISE_DIFFICULTY_1 = "Beginner";
    public static final int EXERCISE_DIFFICULTY_INTERMEDIATE = 2;
    public static final String EXERCISE_DIFFICULTY_2 = "Intermediate";
    public static final int EXERCISE_DIFFICULTY_PROFICIENT = 3;
    public static final String EXERCISE_DIFFICULTY_3 = "Proficient";
    public static final int EXERCISE_DIFFICULTY_ADVANCED = 4;
    public static final String EXERCISE_DIFFICULTY_4 = "Advanced";
    public static final int EXERCISE_DIFFICULTY_EXPERT = 5;
    public static final String EXERCISE_DIFFICULTY_5 = "Expert";
    public static final int EXERCISE_DIFFICULTY_MASTER = 6;
    public static final String EXERCISE_DIFFICULTY_6 = "Master";
    public static final String DEFAULT_DESCRIPTION_TEXT_FILE_NAME = "No file found.";

    private long mId;
    private String mExerciseName;
    private String mExerciseMode;
    private String mExerciseMaterial;
    private int mExerciseDifficulty;
    private String mExerciseDescriptionTextFileName;


    public Exercise() {
        mId = -1;
        mExerciseName = "";
        mExerciseMode = "";
        mExerciseMaterial = "";
        mExerciseDifficulty = EXERCISE_DIFFICULTY_BEGINNER;
        mExerciseDescriptionTextFileName = DEFAULT_DESCRIPTION_TEXT_FILE_NAME;
    }

    public Exercise(String exerciseName, String exerciseMode, String exerciseMaterial, int difficulty, String exerciseDescriptionTextFileName) {
        mId = -1;
        mExerciseName = exerciseName;
        mExerciseMode = exerciseMode;
        mExerciseMaterial = exerciseMaterial;
        mExerciseDifficulty = difficulty;
        mExerciseDescriptionTextFileName = exerciseDescriptionTextFileName;
    }

    public Exercise(long Id, String exerciseName, String exerciseMode, String exerciseMaterial, int exerciseDifficulty, String exerciseDescriptionTextFileName) {
        mId = Id;
        mExerciseName = exerciseName;
        mExerciseMode = exerciseMode;
        mExerciseMaterial = exerciseMaterial;
        mExerciseDifficulty = exerciseDifficulty;
        mExerciseDescriptionTextFileName = exerciseDescriptionTextFileName;
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

    public String getExerciseDifficultyString() {
        switch (this.getExerciseDifficulty()) {
            case EXERCISE_DIFFICULTY_INTERMEDIATE:
            return EXERCISE_DIFFICULTY_2;
            case EXERCISE_DIFFICULTY_PROFICIENT:
            return EXERCISE_DIFFICULTY_3;
            case EXERCISE_DIFFICULTY_ADVANCED:
            return EXERCISE_DIFFICULTY_4;
            case EXERCISE_DIFFICULTY_EXPERT:
            return EXERCISE_DIFFICULTY_5;
            case EXERCISE_DIFFICULTY_MASTER:
            return EXERCISE_DIFFICULTY_6;
            default:
                return EXERCISE_DIFFICULTY_1;
        }
    }

    public String getExerciseDescriptionTextFileName() {
        return mExerciseDescriptionTextFileName;
    }

    public void setExerciseDescriptionTextFileName(String exerciseDescriptionTextFileName) {
        mExerciseDescriptionTextFileName = exerciseDescriptionTextFileName;
    }

    public String getDescriptionText() {
        return null;
    }

    public void reset() {
        mId = -1;
        mExerciseName = "";
        mExerciseMode = "";
        mExerciseMaterial = "";
        mExerciseDifficulty = EXERCISE_DIFFICULTY_BEGINNER;
        mExerciseDescriptionTextFileName = DEFAULT_DESCRIPTION_TEXT_FILE_NAME;
    }

    // PARCELABLE IMPLEMENTATION //

    private Exercise(Parcel parcel) {
        mId = parcel.readLong();
        mExerciseName = parcel.readString();
        mExerciseMode = parcel.readString();
        mExerciseMaterial = parcel.readString();
        mExerciseDifficulty = parcel.readInt();
        mExerciseDescriptionTextFileName = parcel.readString();
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
        parcel.writeString(mExerciseDescriptionTextFileName);
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
