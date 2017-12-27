package edu.orangecoastcollege.cs273.rmillett.audiate.Models;

import android.os.Parcelable;

/**
 * <code>SoundObject</code> is the abstract class for <code>Note</code>, <code>ChordScale</code>,
 * <code>Melody</code>, and <code>ChordSequence</code> objects.
 * objects.
 *
 * A <code>SoundObject</code> is used to encapsulate acoustic/musical information for categorization,
 * manipulation, and audio production.
 *
 * Instances of this class can be passed into <code>SoundObjectPlayer</code> objects, parameters
 * are read by the <code>SoundObjectPlayer</code> as instructions for audio playback.
 *
 * @author Ryan Millett
 * @version 2.0
 */
public abstract class SoundObject { // TODO: implement PLAYABLE interface (?)

    /**
     * int constant used as a default long duration in milliseconds for all <code>SoundObjects</code>.
     */
    public static final int DEFAULT_DURATION_MILLISECONDS_LONG = 3 * 1000;

    /**
     * int constant used as a default short duration in milliseconds for all <code>SoundObjects</code>.
     */
    public static final int DEFAULT_DURATION_MILLISECONDS_SHORT = 500;

    protected long mId;
    protected String mName;
    protected String mDescription;
    protected int mDurationMilliseconds;

    /**
     * Default constructor
     */
    public SoundObject() {
        mId = -1;
        mName = "";
        mDescription = "No information";
        mDurationMilliseconds = DEFAULT_DURATION_MILLISECONDS_LONG;
    }

    /**
     * Overloaded constructor
     *
     * @param name <code>SoundObject</code> name
     */
    public SoundObject(String name) {
        mId = -1;
        mName = name;
        mDescription = "No information";
        mDurationMilliseconds = DEFAULT_DURATION_MILLISECONDS_LONG;
    }

    /**
     * Overloaded constructor
     *
     * @param name <code>SoundObject</code> name
     */
    public SoundObject(long id, String name) {
        mId = id;
        mName = name;
        mDescription = "No information";
        mDurationMilliseconds = DEFAULT_DURATION_MILLISECONDS_LONG;
    }

    /**
     * Overloaded constructor
     *
     * @param name <code>SoundObject</code> Name
     * @param durationMilliseconds Duration of <code>SoundObject</code> in milliseconds
     */
    public SoundObject(String name, int durationMilliseconds) {
        mId = -1;
        mName = name;
        mDescription = "No information";
        mDurationMilliseconds = durationMilliseconds;
    }

    /**
     * Gets the name of the <code>SoundObject</code>
     *
     * @return String representing the name of the <code>SoundObject</code>
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the name of the <code>SoundObject</code>
     *
     * @param name String representing the name of the <code>SoundObject</code>
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Gets the duration of the <code>SoundObject</code> in milliseconds
     *
     * @return int representing the duration of the <code>SoundObject</code> in milliseconds
     */
    protected int getDurationMilliseconds() {
        return mDurationMilliseconds;
    }

    /**
     * Sets the duration of the <code>SoundObject</code> in milliseconds
     *
     * @param durationMilliseconds Duration of the <code>SoundObject</code> in milliseconds
     */
    protected void setDurationMilliseconds(int durationMilliseconds) {
        mDurationMilliseconds = durationMilliseconds;
    }

    /**
     * Gets the description of the <code>SoundObject</code>
     *
     * @return String containing a description of the <code>SoundObject</code>
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Sets a brief description for a <code>SoundObject</code>
     *
     * @param description String containing a description of a <code>SoundObject</code>
     */
    public void setDescription(String description) {
        mDescription = description;
    }

    /**
     * Gets the unique ID for a <code>SoundObject</code>
     *
     * @return long value representing the unique ID for a <code>SoundObject</code>
     */
    public long getId() {
        return mId;
    }
}
