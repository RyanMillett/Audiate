package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Ryan Millett
 * @version 1.0
 */
public class ExerciseActivityType implements Parcelable {

    public static final String EAR_TRAINING_EXERCISE = "Ear Training Exercise";
    public static final String SIGHT_SINGING_EXERCISE = "Sight Singing Exercise";

    public static final String EXERCISE_TYPE_INTERVALS = "Intervals";
    public static final String EXERCISE_TYPE_CHORDS = "Chords";
    public static final String EXERCISE_TYPE_SCALES = "Scales";

    public static final String DEFAULT_NONE_SELECTED = "None Selected";

    private String mExerciseName;
    private String mExerciseType;
    private String mDifficulty;
    private String mExerciseDescription;

    public ExerciseActivityType(String exerciseName, String exerciseType, String difficulty, String exerciseDescription) {
        mExerciseName = exerciseName;
        mExerciseType = exerciseType;
        mDifficulty = difficulty;
        mExerciseDescription = exerciseDescription;
    }

    public String getExerciseName() {
        return mExerciseName;
    }

    public void setExerciseName(String exerciseName) {
        mExerciseName = exerciseName;
    }

    public String getExerciseType() {
        return mExerciseType;
    }

    public void setExerciseType(String exerciseType) {
        mExerciseType = exerciseType;
    }

    public String getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(String difficulty) {
        mDifficulty = difficulty;
    }

    public String getExerciseDescription() {
        return mExerciseDescription;
    }

    public void setExerciseDescription(String exerciseDescription) {
        mExerciseDescription = exerciseDescription;
    }

    private ExerciseActivityType(Parcel parcel) {
        mExerciseName = parcel.readString();
        mExerciseType = parcel.readString();
        mDifficulty = parcel.readString();
        mExerciseDescription = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mExerciseName);
        parcel.writeString(mExerciseType);
        parcel.writeString(mDifficulty);
        parcel.writeString(mExerciseDescription);
    }

    public static final Parcelable.Creator<ExerciseActivityType> CREATOR = new Creator<ExerciseActivityType>() {
        @Override
        public ExerciseActivityType createFromParcel(Parcel parcel) {
            return new ExerciseActivityType(parcel);
        }

        @Override
        public ExerciseActivityType[] newArray(int size) {
            return new ExerciseActivityType[size];
        }
    };
}
