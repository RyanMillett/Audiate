package edu.orangecoastcollege.cs273.rmillett.audiate;

/**
 * Created by Brian Wegener on 12/5/2017.
 */

public class User {

    private String mUserName;
    private String mEmail;
    private String mLowPitch;
    private String mHighPitch;
    private String mVocalRange;


    public User(String userName, String email, String lowPitch, String highPitch, String vocalRange)
    {
        mUserName = userName;
        mEmail = email;
        mLowPitch = lowPitch;
        mHighPitch = highPitch;
        mVocalRange = vocalRange;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmLowPitch() {
        return mLowPitch;
    }

    public void setmLowPitch(String mLowPitch) {
        this.mLowPitch = mLowPitch;
    }

    public String getmHighPitch() {
        return mHighPitch;
    }

    public void setmHighPitch(String mHighPitch) {
        this.mHighPitch = mHighPitch;
    }

    public String getmVocalRange() {
        return mVocalRange;
    }

    public void setmVocalRange(String mVocalRange) {
        this.mVocalRange = mVocalRange;
    }

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
