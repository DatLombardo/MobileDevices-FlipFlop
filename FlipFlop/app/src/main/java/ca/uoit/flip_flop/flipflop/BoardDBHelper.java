package ca.uoit.flip_flop.flipflop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BoardDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_FILENAME = "randdit.db";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_POST = "post";
    public static final String TABLE_DISCUSSION = "discussion";
    public static final String TABLE_LIKES = "likes";
    public static final String TABLE_DISLIKES = "dislikes";

    /**
     *  CREATE_USERS_STATEMENT
     *  CREATE_LIKES_STATEMENT
     *  CREATE_DISLIKES_STATEMENT
     *
     * SQL Statements to create each of the given tables into the database
     */
    public static final String CREATE_USERS_STATEMENT = "" +
            "CREATE TABLE " + TABLE_USERS + " (" +
            "  _id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  username TEXT NOT NULL," +
            "  password TEXT NOT NULL," +
            "  iconFileName TEXT NOT NULL," +
            "  dateCreated TEXT NOT NULL" +
            ")";

    public static final String CREATE_LIKES_STATEMENT = "" +
            "CREATE TABLE " + TABLE_LIKES + " (" +
            "  _id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  post_id INTEGER NOT NULL," +
            "  user_id INTEGER NOT NULL," +
            "FOREIGN KEY(user_id) REFERENCES users(_id)" +
            ")";

    public static final String CREATE_DISLIKES_STATEMENT = "" +
            "CREATE TABLE " + TABLE_DISLIKES + " (" +
            "  _id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  post_id INTEGER NOT NULL," +
            "  user_id INTEGER NOT NULL," +
            "FOREIGN KEY(user_id) REFERENCES users(_id)" +
            ")";

    /**
     * DROP_USERS_STATEMENT
     * DROP_LIKES_STATEMENT
     * DROP_DISLIKES_STATEMENT
     * SQL Statements for dropping (removing) the given table.
     *
     */
    public static final String DROP_USERS_STATEMENT = "" +
            "DROP TABLE " + TABLE_USERS;

    public static final String DROP_LIKES_STATEMENT = "" +
            "DROP TABLE " + TABLE_LIKES;

    public static final String DROP_DISLIKES_STATEMENT = "" +
            "DROP TABLE " + TABLE_DISLIKES;

    public BoardDBHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_STATEMENT);
        db.execSQL(CREATE_LIKES_STATEMENT);
        db.execSQL(CREATE_DISLIKES_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // upgrade

        } else {
            // downgrade

        }

        db.execSQL(DROP_USERS_STATEMENT);
        db.execSQL(CREATE_USERS_STATEMENT);
        db.execSQL(DROP_LIKES_STATEMENT);
        db.execSQL(CREATE_LIKES_STATEMENT);
        db.execSQL(CREATE_DISLIKES_STATEMENT);
        db.execSQL(DROP_DISLIKES_STATEMENT);
    }

    /**
     * createUser
     * Create an element based on passed parameters in User table.
     *
     * @param username
     * @param password
     * @param iconFileName
     * @param dateCreated
     * @return
     */
    public User createUser(String username,
                           String password,
                           String iconFileName,
                           String dateCreated) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("iconFileName", iconFileName);
        values.put("dateCreated", dateCreated);

        long id = db.insert(TABLE_USERS, null, values);

        //  wrap everything up into an entity object
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        //newUser.setIconFileName(iconFileName);
        newUser.setDateCreated(dateCreated);
        newUser.setUserId((int) id);

        return newUser;
    }

    /**
     * createLike
     * Creates an element based on passed parameters into Like table.
     *
     * @param post_id
     * @param user_id
     */
    public void createLike(int post_id,
                           int user_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("post_id", post_id);
        values.put("user_id", user_id);

        long id = db.insert(TABLE_LIKES, null, values);

    }

    /**
     * createDislike
     * Creates an element based on passed parameters into Dislike table.
     *
     * @param post_id
     * @param user_id
     */
    public void createDislike(int post_id,
                              int user_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("post_id", post_id);
        values.put("user_id", user_id);

        long id = db.insert(TABLE_DISLIKES, null, values);

    }

    /**
     * getUser
     * Queries to find one user based on primary key (id)
     *
     * @param id
     * @return
     */
    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        User user = null;

        String[] columns = new String[] {
                "_id",
                "username",
                "password",
                "iconFileName",
                "dateCreated"
        };

        String[] args = new String[] {
                "" + id
        };

        Cursor cursor = db.query(TABLE_USERS, columns,
                "_id = ?", args,
                "", "",
                "");

        if (cursor.getCount() >= 1) {
            cursor.moveToFirst();

            String username = cursor.getString(1);
            String password = cursor.getString(2);
            String iconFileName = cursor.getString(3);
            String dateCreated = cursor.getString(4);

            user = new User();
            user.setUsername(username);
            user.setPassword(password);
            //user.setIconFileName(iconFileName);
            user.setDateCreated(dateCreated);
            user.setUserId((int) id);
        }

        return user;
    }

    /**
     * getAllUsers
     * Reads whole Users table into list of elements
     *
     * @return
     */
    public List<User> getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[] {
                "_id",
                "username",
                "password",
                "iconFileName",
                "dateCreated"
        };

        String[] args = new String[] {
        };

        Cursor cursor = db.query(TABLE_USERS, columns,
                "", args,
                "", "",
                "username");

        List<User> users = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                long id = cursor.getLong(0);
                String username = cursor.getString(1);
                String password = cursor.getString(2);
                String iconFileName = cursor.getString(3);
                String dateCreated = cursor.getString(4);

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                //user.setIconFileName(iconFileName);
                user.setDateCreated(dateCreated);
                user.setUserId((int) id);

                users.add(user);

                cursor.moveToNext();
            }
        }
        return users;
    }


    /**
     * getUserLikes
     * Queries all of the likes for given userID passed.
     *
     * @param uID
     * @return
     */
    public List<Integer> getUserLikes(int uID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[] {
                "_id",
                "post_id",
                "user_id"
        };

        String[] args = new String[] {
                "" + uID
        };

        Cursor cursor = db.query(TABLE_LIKES, columns,
                "user_id = ?", args,
                "", "",
                "");

        List<Integer> likes = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int post = cursor.getInt(1);
                likes.add(post);
                cursor.moveToNext();
            }
        }

        return likes;
    }


    /**
     * getUserDislikes
     * Queries all of the dislikes for given userID passed.
     *
     * @param uID
     * @return
     */
    public List<Integer> getUserDislikes(int uID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[] {
                "_id",
                "post_id",
                "user_id"
        };

        String[] args = new String[] {
                "" + uID
        };

        Cursor cursor = db.query(TABLE_DISLIKES, columns,
                "user_id = ?", args,
                "", "",
                "");

        List<Integer> dislikes = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int post = cursor.getInt(1);
                dislikes.add(post);
                cursor.moveToNext();
            }
        }

        return dislikes;
    }


    /**
     * deleteLikes
     * Removes a like element from the DB
     *
     * @param id
     * @return
     */
    public boolean deleteLikes(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] args = new String[] {
                "" + id
        };

        int numRowsAffected = db.delete(TABLE_LIKES,
                "_id = ?", args);

        return (numRowsAffected == 1);
    }

    /**
     * deleteAllLikes
     * Used primarily for testing purposes, removes all like elements from the DB.
     *
     */
    public void deleteAllLikes() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_LIKES, "", new String[] {});
    }

    /**
     * deleteAllDislikes
     * Used primarily for testing purposes, removes all dislike elements from the DB.
     *
     */
    public void deleteAllDislikes() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_DISLIKES, "", new String[] {});
    }

    /**
     * deleteAllUsers
     */
    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_USERS, "", new String[] {});
    }

}