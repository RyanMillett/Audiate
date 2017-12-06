package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The <code>User</code> creates a user with a specific ID, user name, first name,
 * last name, password, and gets their latitude, and longitude.
 *
 * @author bwegener
 * @version 1.0
 *
 * Created by Brian Wegener on 11/17/17.
 */

public class User implements Parcelable {

    private long mId;
    private String mUserName;
    private String mLowPitch;
    private String mHighPitch;
    private String mVocalRange;


    public User(long id, String userName, String lowPitch, String highPitch, String vocalRange)
    {
        mId = id;
        mUserName = userName;
        mLowPitch = lowPitch;
        mHighPitch = highPitch;
        mVocalRange = vocalRange;
    }

    public User(String userName, String lowPitch, String highPitch, String vocalRange)
    {
        this(-1, userName, lowPitch, highPitch, vocalRange);
    }

    protected User(Parcel in)
    {
        mId = in.readLong();
        mUserName = in.readString();
        mLowPitch = in.readString();
        mHighPitch = in.readString();
        mVocalRange = in.readString();
    }

    public long getId() { return mId; }

    public void setId(long id) { mId = id; }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getLowPitch() {
        return mLowPitch;
    }

    public void setLowPitch(String lowPitch) {
        mLowPitch = lowPitch;
    }

    public String getHighPitch() {
        return mHighPitch;
    }

    public void setHighPitch(String highPitch) {
        mHighPitch = highPitch;
    }

    public String getVocalRange() {
        return mVocalRange;
    }

    public void setVocalRange(String vocalRange) {
        mVocalRange = vocalRange;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeLong(mId);
        parcel.writeString(mUserName);
        parcel.writeString(mLowPitch);
        parcel.writeString(mHighPitch);
        parcel.writeString(mVocalRange);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    @Override
    public String toString() {
        return "User{" +
                "mUserName='" + mUserName + '\'' +
                ", mLowPitch='" + mLowPitch + '\'' +
                ", mHighPitch='" + mHighPitch + '\'' +
                ", mVocalRange='" + mVocalRange + '\'' +
                '}';
    }
}
