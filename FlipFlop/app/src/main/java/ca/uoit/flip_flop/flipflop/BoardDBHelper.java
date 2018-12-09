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
    public static final String TABLE_LIKES = "likes";
    public static final String TABLE_DISLIKES = "dislikes";

    /**
     * CREATE_LIKES_STATEMENT
     * CREATE_DISLIKES_STATEMENT
     * <p>
     * SQL Statements to create each of the given tables into the database
     */

    public static final String CREATE_LIKES_STATEMENT = "" +
            "CREATE TABLE " + TABLE_LIKES + " (" +
            "  _id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  post_id INTEGER NOT NULL," +
            "  user_id INTEGER NOT NULL" +
            ")";

    public static final String CREATE_DISLIKES_STATEMENT = "" +
            "CREATE TABLE " + TABLE_DISLIKES + " (" +
            "  _id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  post_id INTEGER NOT NULL," +
            "  user_id INTEGER NOT NULL" +
            ")";

    /**
     * DROP_LIKES_STATEMENT
     * DROP_DISLIKES_STATEMENT
     * SQL Statements for dropping (removing) the given table.
     */

    public static final String DROP_LIKES_STATEMENT = "" +
            "DROP TABLE " + TABLE_LIKES;

    public static final String DROP_DISLIKES_STATEMENT = "" +
            "DROP TABLE " + TABLE_DISLIKES;

    public BoardDBHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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
        db.execSQL(DROP_LIKES_STATEMENT);
        db.execSQL(CREATE_LIKES_STATEMENT);
        db.execSQL(CREATE_DISLIKES_STATEMENT);
        db.execSQL(DROP_DISLIKES_STATEMENT);
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

        // Remove any dislikes by the user on this post
        if (db.delete(TABLE_DISLIKES, "post_id=? AND user_id=?",
                new String[]{
                        Integer.toString(post_id),
                        Integer.toString(user_id)
                }) > 0)
        {
            // We're only un-disliking, no need to add a like
            return;
        }

        // Check to see if post has already been liked
        String query = "SELECT * FROM " + TABLE_LIKES + " WHERE post_id="
                + post_id + " AND user_id=" + user_id;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            System.out.println("User has already liked this post");
        } else {
            db.insert(TABLE_LIKES, null, values);
        }
        cursor.close();
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

        // Remove any likes by the user on this post
        if (db.delete(TABLE_LIKES, "post_id=? AND user_id=?",
                new String[]{
                        Integer.toString(post_id),
                        Integer.toString(user_id)
                }) > 0)
        {
            // We're only un-liking, no need to add a dislike
            return;
        }

        // Check to see if post has already been disliked
        String query = "SELECT * FROM " + TABLE_DISLIKES + " WHERE post_id="
                + post_id + " AND user_id=" + user_id;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            System.out.println("User has already disliked this post");
        } else {
            db.insert(TABLE_DISLIKES, null, values);
        }
        cursor.close();
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

        String[] columns = new String[]{
                "_id",
                "post_id",
                "user_id"
        };

        String[] args = new String[]{
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

        String[] columns = new String[]{
                "_id",
                "post_id",
                "user_id"
        };

        String[] args = new String[]{
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

        String[] args = new String[]{
                "" + id
        };

        int numRowsAffected = db.delete(TABLE_LIKES,
                "_id = ?", args);

        return (numRowsAffected == 1);
    }

    /**
     * deleteAllLikes
     * Used primarily for testing purposes, removes all like elements from the DB.
     */
    public void deleteAllLikes() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_LIKES, "", new String[]{});
    }

    /**
     * deleteAllDislikes
     * Used primarily for testing purposes, removes all dislike elements from the DB.
     */
    public void deleteAllDislikes() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_DISLIKES, "", new String[]{});
    }
}