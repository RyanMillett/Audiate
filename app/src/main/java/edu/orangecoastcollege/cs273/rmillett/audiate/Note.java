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

    /**
     * double constant used as a default size in cents for <code>Note</code> objects. A size of 0——
     * a "perfect" unison——sets the note as the root of a <code>ChordScale</code> object or the
     * virtual fundamental of a yet-to-be-defined <code>ChordScale</code> object.
     */
    public static final double DEFAULT_SIZE_IN_CENTS = 0.0;

    private double mPitchFrequency;
    private String mRatio;
    private double mSizeInCents;

    /**
     * Default constructor
     */
    public Note() {
        super();
        mPitchFrequency = DEFAULT_FREQUENCY;
        mRatio = DEFAULT_RATIO;
        mSizeInCents = DEFAULT_SIZE_IN_CENTS;
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
        mSizeInCents = DEFAULT_SIZE_IN_CENTS;
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
        mSizeInCents = DEFAULT_SIZE_IN_CENTS;
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
        mSizeInCents = DEFAULT_SIZE_IN_CENTS;
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
        mSizeInCents = Music.convertRatioToCents(ratio);
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
        mSizeInCents = DEFAULT_SIZE_IN_CENTS;
    }

    /**
     * Overloaded constructor
     *
     * @param name String representing the <code>Note</code> name
     * @param ratio a String value representing a <code>Note</code> object's relation to another
     *              <code>Note</code> object expressed as a ratio
     * @param cents a double value representing the size/distance in cents of an interval
     * @param description String containing a description of a <code>SoundObject</code>
     */
    public Note(String name, String ratio, double cents, String description) {
        super(name);
        mPitchFrequency = DEFAULT_FREQUENCY;
        mRatio = ratio;
        mSizeInCents = cents;
        mDescription = description;
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

    /**
     * Gets the size in cents of an interval
     *
     * @return a double value representing the size/distance in cents of an interval
     */
    public double getSizeInCents() {
        return mSizeInCents;
    }

    /**
     * Takes a double value representing the size/distance in cents of an interval
     *
     * @param sizeInCents a String representing the size in cents of an interval
     */
    public void setSizeInCents(double sizeInCents) {
        mSizeInCents = sizeInCents;
    }

    // -------------- Parcelable Implementation -------------- //

//    private Note(Parcel parcel) {
//        mId = parcel.readLong();
//        mName = parcel.readString();
//        mDescription = parcel.readString();
//        mDurationMilliseconds = parcel.readInt();
//        mPitchFrequency = parcel.readDouble();
//        mRatio = parcel.readString();
//        mSizeInCents = parcel.readDouble();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeLong(mId);
//        parcel.writeString(mName);
//        parcel.writeString(mDescription);
//        parcel.writeInt(mDurationMilliseconds);
//        parcel.writeDouble(mPitchFrequency);
//        parcel.writeString(mRatio);
//        parcel.writeDouble(mSizeInCents);
//    }
//
//    public static final Parcelable.Creator<Note> CREATOR = new Creator<Note>() {
//        @Override
//        public Note createFromParcel(Parcel parcel) {
//            return new Note(parcel);
//        }
//
//        @Override
//        public Note[] newArray(int size) {
//            return new Note[size];
//        }
//    };
}
