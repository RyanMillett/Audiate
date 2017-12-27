package edu.orangecoastcollege.cs273.rmillett.audiate.Models;

import java.util.ArrayList;

/**
 * Subclass of <code>SoundObject</code> used to instantiate a <code>ChordScale</code> object.
 *
 * <code>ChordScale</code> objects are essentially an array of <code>Note</code> objects referred to
 * as "chord members" in this context.
 *
 * <code>ChordScale</code> objects can be used to represent CHORDS (two or more mutually-exclusive
 * <code>Note</code>s played simultaneously) and SCALES (a linear sequence of <code>Note</code>s
 * played in ascending or descending order.
 *
 * A <code>ChordScale</code> can be instantiated alone or as a part of a <code>ChordSequence</code>
 *
 * @author Ryan Millett
 * @version 2.0
 */
public class ChordScale extends SoundObject {

    /**
     * int constant used as a default frequency in Hertz for <code>ChordScale</code> objects.
     */
    public static final int DEFAULT_FUNDAMENTAL_FREQUENCY = 440;

    /**
     * String constant used to indicate that all chord members contained in a <code>ChordScale</code>
     * are to be played simultaneous as a "block chord".
     */
    public static final String PLAYBACK_MODE_CHORDSCALE_BLOCK_CLUSTER = "BlockCluster";

    /**
     * String constant used to indicate that chord members contained in a <code>ChordScale</code> are
     * to be played as an upward arpeggio--i.e. in sequence from lowest pitch to highest.
     */
    public static final String PLAYBACK_MODE_CHORDSCALE_UP = "Up";

    /**
     * String constant used to indicate that chord members contained in a <code>ChordScale</code> are
     * to be played as a downward arpeggio--i.e. in sequence from highest pitch to lowest.
     */
    public static final String PLAYBACK_MODE_CHORDSCALE_DOWN = "Down";

    /**
     * String constant used to indicate that chord members contained in a <code>ChordScale</code> are
     * to be played in "Alberti Bass" fashion--i.e. in the order lowest, highest, middle, highest.
     *
     * Important Note: can only be applied to a <code>ChordScale</code> containing ONLY three (3)
     * chord members.
     */
    public static final String PLAYBACK_MODE_ALBERTI = "Alberti";

    public static final int CHORDSCALE_DEFAULT_INITIAL_SIZE = 2;

    //private int mInitialSize;
    private ArrayList<Note> mChordMembers;
    private String mPlayBackMode;
    private String mSCLfileName;

    /**
     * Default constructor
     */
    public ChordScale() {
        super();
        mChordMembers = new ArrayList<>(CHORDSCALE_DEFAULT_INITIAL_SIZE);
        mChordMembers.add(0,new Note("Fundamental"));
        mPlayBackMode = PLAYBACK_MODE_CHORDSCALE_BLOCK_CLUSTER;
        mDurationMilliseconds = SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG;
        mDescription = "No information";
        mSCLfileName = "No associated .scl file";
    }

    /**
     * Overloaded constructor
     *
     * @param name
     */
    public ChordScale(String name) {
        super(name);
        mChordMembers = new ArrayList<>(CHORDSCALE_DEFAULT_INITIAL_SIZE);
        mPlayBackMode = PLAYBACK_MODE_CHORDSCALE_BLOCK_CLUSTER;
        mDurationMilliseconds = SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG;
        mDescription = "No information";
        mSCLfileName = "No associated .scl file";

        initializeChordScale(CHORDSCALE_DEFAULT_INITIAL_SIZE);
    }

    public ChordScale(String name, int initialSize) {
        super(name);
        mChordMembers = new ArrayList<>(initialSize);
        mPlayBackMode = PLAYBACK_MODE_CHORDSCALE_BLOCK_CLUSTER;
        mDurationMilliseconds = SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG;
        mDescription = "No information";
        mSCLfileName = "No associated .scl file";

        initializeChordScale(initialSize);
    }

    public ChordScale(String name, String description) {
        super(name);
        mChordMembers = new ArrayList<>(CHORDSCALE_DEFAULT_INITIAL_SIZE);
        mPlayBackMode = PLAYBACK_MODE_CHORDSCALE_BLOCK_CLUSTER;
        mDurationMilliseconds = SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG;
        mDescription = description;
        mSCLfileName = "No associated .scl file";

        initializeChordScale(CHORDSCALE_DEFAULT_INITIAL_SIZE);
    }


    public ChordScale(String name, int initialSize, String description, String sclFileName) {
        super(name);
        mChordMembers = new ArrayList<>(initialSize);
        mPlayBackMode = PLAYBACK_MODE_CHORDSCALE_BLOCK_CLUSTER;
        mDurationMilliseconds = SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG;
        mDescription = description;
        mSCLfileName = sclFileName;

        initializeChordScale(initialSize);
    }

    private void initializeChordScale(int initialSize) {
        mChordMembers.add(0, new Note("Tonic/Fundamental"));
        for (int i = 1; i < initialSize; ++i) {
            mChordMembers.add(i, new Note());
        }
        resetFundamentalFrequency(ChordScale.DEFAULT_FUNDAMENTAL_FREQUENCY);
    }

    /**
     * Overloaded constructor
     *
     * @param name
     * @param id
     */
    public ChordScale(long id, String name, int initialSize) {
        super(id, name);
        mChordMembers = new ArrayList<>(initialSize);
        mPlayBackMode = PLAYBACK_MODE_CHORDSCALE_BLOCK_CLUSTER;
        mDurationMilliseconds = SoundObject.DEFAULT_DURATION_MILLISECONDS_LONG;
        mDescription = "No information";
        mSCLfileName = "No associated .scl file";
    }

    /**
     * Gets the size of the <code>ChordScale</code> object--i.e. how many individual <code>Note</code>
     * objects are contained in the <code>ChordScale</code>.
     *
     * @return int value representing the number of individual <code>Note</code> objects contained in
     *          the <code>ChordSequence</code>.
     */
    public int getSize() {
        return mChordMembers.size();
    }

    /**
     * Returns a list of all the chord members contained in a <code>ChordSequence</code>.
     *
     * @return ArrayList of all the chord members contained in a <code>ChordSequence</code>.
     */
    public ArrayList<Note> getAllChordMembers() {
        return mChordMembers;
    }

    /**
     * Clears all chord members contained in a <code>ChordScale</code> and resets its size to 0.
     */
    public void clearAllChordMembers() {
        mChordMembers.clear();
    }

    /**
     * Adds a new chord member to the end of a <code>ChordScale</code>.
     *
     * @param note <code>Note</code> object to be added to the end of a <code>ChordScale</code>.
     */
    public void addChordMember(Note note) {
        note.setDurationMilliseconds(this.mDurationMilliseconds);
        mChordMembers.add(note);
        note.setPitchFrequency(mChordMembers.get(0).getPitchFrequency()
                * Music.convertRatioToDecimal(note.getRatio()));
    }

    public void addChordMemberAt(int pos, Note note) {
        note.setDurationMilliseconds(this.mDurationMilliseconds);
        mChordMembers.add(pos, note);
        note.setPitchFrequency(mChordMembers.get(0).getPitchFrequency()
                * Music.convertRatioToDecimal(note.getRatio()));
    }

    /**
     * Inserts a new chord member at the specified position in <code>ChordScale</code>. The chord
     * member currently at that position (if any) and any subsequent chord members are shifted to the
     * right. <code>ChordScale</code> size is increased by 1.
     *
     * @param note <code>Note</code> object to be inserted into <code>ChordScale</code>.
     * @param pos position where <code>Note</code> object is to be inserted.
     */
    public void insertChordMemberAtPos(int pos, Note note) {
        note.setDurationMilliseconds(this.mDurationMilliseconds);
        mChordMembers.add(pos, note);
    }

    /**
     * Gets a single chord member found at the specified position.
     *
     * @param pos specified position of the chord member.
     * @return a <code>Note</code> object found at specified position.
     */
    public Note getChordMemberAtPos(int pos) {
        return mChordMembers.get(pos);
    }

    public void resetFundamentalFrequency(double newFundamentalFrequency) {
        // reset fundamental
        this.getChordMemberAtPos(0).setPitchFrequency(newFundamentalFrequency);

        // adjust all chord members
        for (int i = 1; i < this.mChordMembers.size(); ++i) {
            this.getChordMemberAtPos(i).setPitchFrequency(
                    Music.convertRatioToDecimal(getChordMemberAtPos(i).getRatio())
                            * newFundamentalFrequency);
        }
    }

    /**
     * Gets a list of all the pitch frequencies of each chord member contained in the <code>ChordScale</code>.
     *
     * @return array of double values representing the frequencies in Hertz of all chord members.
     */
    public double[] getAllChordMemberFrequencies() {
        double[] frequencies = new double[this.mChordMembers.size()];
        for (int i = 0; i < this.mChordMembers.size(); ++i) {
            frequencies[i] = this.mChordMembers.get(i).getPitchFrequency();
        }
        return frequencies;
    }

    /**
     * Gets a list of ratios representing each chord member's relation to the root pitch in a
     * <code>ChordScale</code>.
     *
     * @return array of Strings representing each chord member's relation to the root pitch.
     */
    public String[] getAllChordMemberRatios() {
        String[] allChordMemberRatios = new String[this.mChordMembers.size()];
        for (int i = 0; i < this.mChordMembers.size(); ++i) {
            allChordMemberRatios[i] = this.mChordMembers.get(i).getRatio();
        }
        return allChordMemberRatios;
    }

    /**
     * Gets the relationship between two chord members expressed as a simple whole-number ratio
     *
     * @param pos1 position of first chord member
     * @param pos2 position of second chord member
     * @return String containing a simple whole-number ratio representing the relationship between
     *          two chord members.
     */
    public String getIntervalRatio(int pos1, int pos2) {
        return Music.convertDecimalToRatio(mChordMembers.get(pos1).getPitchFrequency()
                / mChordMembers.get(pos2).getPitchFrequency());
    }

    /**
     * Gets the distance measured in cents between any two chord members.
     *
     * Formula used (where m/n is the ratio): 1200*log(m/n)/log(2)
     *
     * @param pos1 position of first chord member
     * @param pos2 position of second chord member
     * @return double value representing the distance in cents between two chord members
     */
    public double getIntervalDistanceInCents(int pos1, int pos2) {
        // Get whole-number ratio
        String ratio = Music.convertDecimalToRatio(
                        this.getChordMemberAtPos(pos2).getPitchFrequency()
                                / this.getChordMemberAtPos(pos1).getPitchFrequency());
        // Convert to cents
        return Music.convertRatioToCents(ratio);
    }

    /**
     * Gets the playback mode of a <code>ChordScale</code>.
     *
     * @return String representing the playback mode of a <code>ChordScale</code>.
     */
    public String getPlayBackMode() {
        return mPlayBackMode;
    }

    /**
     * Sets the playback mode of a <code>ChordScale</code>.
     *
     * @param playBackMode String indicating the playback mode of a <code>ChordScale</code>.
     */
    public void setPlayBackMode(String playBackMode) {
        mPlayBackMode = playBackMode;
    }

    /**
     * Sets the duration of each chord member contained in the <code>ChordScale</code> to the same
     * specified duration in milliseconds.
     *
     * @param durationMilliseconds Duration in milliseconds to be assigned to each chord member.
     */
    @Override
    public void setDurationMilliseconds(int durationMilliseconds) {
        mDurationMilliseconds = durationMilliseconds;
        for (Note note : this.mChordMembers) {
            note.setDurationMilliseconds(durationMilliseconds);
        }
    }

    @Override
    public int getDurationMilliseconds() {
        if (this.getPlayBackMode().equalsIgnoreCase(PLAYBACK_MODE_CHORDSCALE_BLOCK_CLUSTER)) {
            return mDurationMilliseconds;
        }
        else {
            return mDurationMilliseconds * this.getSize();
        }
    }

    public String getSCLfileName() {
        return mSCLfileName;
    }

    public void setSCLfileName(String SCLfileName) {
        this.mSCLfileName = SCLfileName;
    }

    // -------------- Parcelable Implementation -------------- //
    // TODO: still broken
//    private ChordScale(Parcel parcel) {
//        mId = parcel.readLong();
//        mName = parcel.readString();
//        mDescription = parcel.readString();
//        mDurationMilliseconds = parcel.readInt();
//        // NEW WAY: Read as an array of Notes, then add them to the ArrayList member variable
//        Note[] tempArray = (Note[]) parcel.readArray(Note.class.getClassLoader());
//        mChordMembers = new ArrayList<>(tempArray.length);
//        for (Note note : tempArray)
//            mChordMembers.add(note);
//        // OLD WAY: (BELOW)
////        mChordMembers = parcel.createTypedArrayList(Note.CREATOR);
//        mPlayBackMode = parcel.readString();
//        mSCLfileName = parcel.readString();
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
//        parcel.writeArray(mChordMembers.toArray());
//        parcel.writeString(mPlayBackMode);
//        parcel.writeString(mSCLfileName);
//    }
//
//    public static final Parcelable.Creator<ChordScale> CREATOR = new Creator<ChordScale>() {
//        @Override
//        public ChordScale createFromParcel(Parcel parcel) {
//            return new ChordScale(parcel);
//        }
//
//        @Override
//        public ChordScale[] newArray(int size) {
//            return new ChordScale[size];
//        }
//    };
}
