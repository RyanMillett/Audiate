package edu.orangecoastcollege.cs273.rmillett.audiate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Ryan Millett on 12/13/17.
 */
public class DBUser extends SQLiteOpenHelper {

    private Context mContext;

    private static final String TAG = "UserDB";

    static final String USER_DATABASE = "UserDatabase";

    private static final int DATABASE_VERSION = 2;

    // Users
    private static final String USERS_TABLE = "Users";
    private static final String USERS_KEY_FIELD_ID = "_id";
    private static final String FIELD_USER_NAME = "user_name";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_LOW_PITCH = "lowest_pitch";
    private static final String FIELD_HIGH_PITCH = "highest_pitch";
    private static final String FIELD_VOCAL_RANGE = "vocal_range";


    public DBUser(Context context) {
        super(context, USER_DATABASE, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Users table
        String createQuery = "CREATE TABLE " + USERS_TABLE + "("
                + USERS_KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_USER_NAME + " TEXT, "
                + FIELD_EMAIL + " TEXT, "
                + FIELD_LOW_PITCH + " TEXT, "
                + FIELD_HIGH_PITCH + " TEXT, "
                + FIELD_VOCAL_RANGE + " TEXT" + ")";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);

        onCreate(db);

    }


    // ---------- SINGLE ENTRIES:

    /**
     * This adds a user to the database.
     * Each user contains a user name, email, low pitch, high pitch,
     * and vocal range.
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_USER_NAME, user.getUserName());
        values.put(FIELD_EMAIL, user.getEmail());
        values.put(FIELD_LOW_PITCH, user.getLowPitch());
        values.put(FIELD_HIGH_PITCH, user.getHighPitch());
        values.put(FIELD_VOCAL_RANGE, user.getVocalRange());

        long id = db.insert(USERS_TABLE, null, values);

        user.setId(id);

        db.close();
    }

    /**
     * This updates the information from one user in the database.
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_USER_NAME, user.getUserName());
        values.put(FIELD_EMAIL, user.getEmail());
        values.put(FIELD_LOW_PITCH, user.getLowPitch());
        values.put(FIELD_HIGH_PITCH, user.getHighPitch());
        values.put(FIELD_VOCAL_RANGE, user.getVocalRange());

        db.update(USERS_TABLE, values, USERS_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This gets one user from the database.
     * @param
     * @return
     */
    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                USERS_TABLE,
                new String[]{USERS_KEY_FIELD_ID,
                        FIELD_USER_NAME, FIELD_EMAIL,
                        FIELD_LOW_PITCH, FIELD_HIGH_PITCH,
                        FIELD_VOCAL_RANGE},
                FIELD_EMAIL + "=?",
                new String[]{email},
                null, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();

        User user = new User(cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5));

        cursor.close();
        db.close();
        return user;
    }

    /**
     * This deletes a user from the database.
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(USERS_TABLE, USERS_KEY_FIELD_ID + " = ?",
                new String[] {String.valueOf(user.getId())});
        db.close();
    }


    // ---------- LIST ENTRIES:

    /**
     * This gets a list of all the users from the database.
     * @return
     */
    public ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                USERS_TABLE,
                new String[]{USERS_KEY_FIELD_ID,
                        FIELD_USER_NAME, FIELD_EMAIL,
                        FIELD_LOW_PITCH, FIELD_HIGH_PITCH,
                        FIELD_VOCAL_RANGE},
                null,
                null,
                null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                User user = new User(cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        return userList;
    }

    /**
     * This deletes all the users from the database.
     */
    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(USERS_TABLE, null, null);

        db.close();
    }

}
