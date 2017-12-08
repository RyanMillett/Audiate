package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Subclass of <code>SoundObject</code> used to instantiate a <code>Note</code> object.
 *
 * <code>Note</code> objects contain information about a single pitch including all parameters
 * inherited from the <code>SoundObject</code> superclass as well as pitch frequency in Hertz.
 *
 * <code>Note</code> objects can be instantiated alone or added to <code>ChordScale</code> and
 * <code>Melody</code> objects.
 *
 * @author Ryan Millett
 * @version 2.0
 */
public class Note extends SoundObject {

    /**
     * int constant used as a default frequency in Hertz for <code>Note</code> objects.
     */
    public static final int DEFAULT_FREQUENCY = 440;

    /**
     * String constant used as a default ratio for <code>Note</code> objects. A ratio of
     * 1/1——a "perfect" unison——sets the note as the root of a <code>ChordScale</code> object
     * or the virtual fundamental of a yet-to-be-defined <code>ChordScale</code> object.
     */
    public static final String DEFAULT_RATIO = "1/1";

    private double mPitchFrequency;
    private String mRatio;

    /**
     * Default constructor
     */
    public Note() {
        super();
        mPitchFrequency = DEFAULT_FREQUENCY;
        mRatio = DEFAULT_RATIO;
    }

    /**
     * Overloaded constructor
     *
     * @param name String representing the <code>Note</code> name
     */
    public Note(String name) {
        super(name);
        mPitchFrequency = DEFAULT_FREQUENCY;
        mRatio = DEFAULT_RATIO;
    }

    /**
     * Overloaded constructor
     *
     * @param pitchFrequency double value representing the <code>Note</code> pitch frequency in
     *                       Hertz
     */
    public Note(double pitchFrequency) {
        super();
        mPitchFrequency = pitchFrequency;
        mRatio = DEFAULT_RATIO;
    }

    /**
     * Overloaded constructor
     *
     * @param name String representing the <code>Note</code> name
     * @param pitchFrequency double value representing the <code>Note</code> pitch frequency in
     *                       Hertz
     */
    public Note(String name, double pitchFrequency) {
        super(name);
        mPitchFrequency = pitchFrequency;
        mRatio = DEFAULT_RATIO;
    }

    /**
     * Overloaded constructor
     *
     * @param name String representing the <code>Note</code> name
     * @param pitchFrequency ble value representing the <code>Note</code> pitch frequency in
     *                       Hertz
     * @param ratio a String value representing a <code>Note</code> object's relation to another
     *              <code>Note</code> object expressed as a ratio
     */
    public Note(String name, double pitchFrequency, String ratio) {
        super(name);
        mPitchFrequency = pitchFrequency;
        mRatio = ratio;
    }

    /**
     * Overloaded constructor
     *
     * @param name String representing the <code>Note</code> name
     * @param durationInMilliseconds int value representing the <code>Note</code> duration in milli-
     *                               seconds
     */
    public Note(String name, int durationInMilliseconds) {
        super(name, durationInMilliseconds);
        mPitchFrequency = DEFAULT_FREQUENCY;
    }

    /**
     * Gets the <code>Note</code> pitch frequency in Hertz
     *
     * @return double value representing the <code>Note</code> pitch frequency in Hertz
     */
    public double getPitchFrequency() {
        return mPitchFrequency;
    }

    /**
     * Sets the <code>Note</code> pitch frequency in Hertz
     *
     * @param pitchFrequency double value representing the <code>Note</code> pitch frequency in
     *                       Hertz
     */
    public void setPitchFrequency(double pitchFrequency) {
        mPitchFrequency = pitchFrequency;
    }

    /**
     * Gets a String value representing a <code>Note</code> object's relation to another
     * <code>Note</code> object expressed as a ratio.
     *
     * @return a String value representing a <code>Note</code> object's relation to another
     *          <code>Note</code> object expressed as a ratio.
     */
    public String getRatio() {
        return mRatio;
    }

    /**
     * Sets the ratio of a <code>Note</code> object represented as a String value
     *
     * @param ratio a String value representing a <code>Note</code> object's relation to another
     *              <code>Note</code> object expressed as a ratio.
     */
    public void setRatio(String ratio) {
        mRatio = ratio;
    }

    // -------------- Parcelable Implementation -------------- //

    private Note(Parcel parcel) {
        mId = parcel.readLong();
        mName = parcel.readString();
        mDescription = parcel.readString();
        mDurationMilliseconds = parcel.readInt();
        mPitchFrequency = parcel.readDouble();
        mRatio = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mName);
        parcel.writeString(mDescription);
        parcel.writeInt(mDurationMilliseconds);
        parcel.writeDouble(mPitchFrequency);
        parcel.writeString(mRatio);
    }

    public static final Parcelable.Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel parcel) {
            return new Note(parcel);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
