package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The <code>User</code> creates a user with a specific ID, user name, first name,
 * last name, password, and gets their latitude, and longitude.
 *
 * @author bwegener
 * @version 1.0
 *          <p>
 *          Created by Brian Wegener on 11/17/17.
 */

public class User implements Parcelable {

    private long mId;
    private String mUserName;
    private String mEmail;
    private String mLowPitch;
    private String mHighPitch;
    private String mVocalRange;


    /**
     * This creates a user with an id, userName, email, low pitch, high pitch, and vocal range.
     *
     * @param id
     * @param userName
     * @param email
     * @param lowPitch
     * @param highPitch
     * @param vocalRange
     */
    public User(long id, String userName, String email, String lowPitch, String highPitch, String vocalRange) {
        mId = id;
        mUserName = userName;
        mEmail = email;
        mLowPitch = lowPitch;
        mHighPitch = highPitch;
        mVocalRange = vocalRange;
    }

    /**
     * This creates a user but sets the id to -1.
     *
     * @param userName
     * @param email
     * @param lowPitch
     * @param highPitch
     * @param vocalRange
     */
    public User(String userName, String email, String lowPitch, String highPitch, String vocalRange) {
        this(-1, userName, email, lowPitch, highPitch, vocalRange);
    }

    /**
     * This reads a parcelled user.
     *
     * @param in
     */
    protected User(Parcel in) {
        mId = in.readLong();
        mUserName = in.readString();
        mEmail = in.readString();
        mLowPitch = in.readString();
        mHighPitch = in.readString();
        mVocalRange = in.readString();
    }

    /**
     * This gets an id for a user.
     *
     * @return
     */
    public long getId() {
        return mId;
    }

    /**
     * This sets an id for the user.
     *
     * @param id
     */
    public void setId(long id) {
        mId = id;
    }

    /**
     * This gets the user name.
     *
     * @return
     */
    public String getUserName() {
        return mUserName;
    }

    /**
     * This sets the user name.
     *
     * @param userName
     */
    public void setUserName(String userName) {
        mUserName = userName;
    }

    /**
     * This gets an email address for the user.
     *
     * @return
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * This sets an email address for the user.
     *
     * @param email
     */
    public void setEmail(String email) {
        mEmail = email;
    }

    /**
     * This gets the lowest pitch that the user can sing/hum.
     *
     * @return
     */
    public String getLowPitch() {
        return mLowPitch;
    }

    /**
     * This sets the lowest pitch that the user can sing/hum.
     *
     * @param lowPitch
     */
    public void setLowPitch(String lowPitch) {
        mLowPitch = lowPitch;
    }

    /**
     * This gets the highest pitch that the user can sing/hum.
     *
     * @return
     */
    public String getHighPitch() {
        return mHighPitch;
    }

    /**
     * This sets the highest pitch that the user can sing/hum.
     *
     * @param highPitch
     */
    public void setHighPitch(String highPitch) {
        mHighPitch = highPitch;
    }

    /**
     * This gets the vocal range from the user.
     *
     * @return
     */
    public String getVocalRange() {
        return mVocalRange;
    }

    /**
     * This sets the vocal range of the user.
     *
     * @param vocalRange
     */
    public void setVocalRange(String vocalRange) {
        mVocalRange = vocalRange;
    }

    /**
     * This describes the contents.
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * This writes the user to the parcel.
     *
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mUserName);
        parcel.writeString(mEmail);
        parcel.writeString(mLowPitch);
        parcel.writeString(mHighPitch);
        parcel.writeString(mVocalRange);
    }

    /**
     * This creates teh parcel.
     */
    public static final Creator<User> CREATOR = new Creator<User>() {
        /**
         * This creates a new user from the parcel.
         * @param in
         * @return
         */
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        /**
         * This gets a new user array.
         * @param size
         * @return
         */
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     * This is the toString of the user.
     *
     * @return
     */
    @Override
    public String toString() {
        return "User{" +
                "mUserName='" + mUserName + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mLowPitch='" + mLowPitch + '\'' +
                ", mHighPitch='" + mHighPitch + '\'' +
                ", mVocalRange='" + mVocalRange + '\'' +
                '}';
    }
}
