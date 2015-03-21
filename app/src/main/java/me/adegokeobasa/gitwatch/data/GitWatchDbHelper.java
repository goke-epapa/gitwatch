package me.adegokeobasa.gitwatch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */
public class GitWatchDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "gitwatch.db";

    public GitWatchDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_REPO_TABLE = "CREATE TABLE " + GitWatchContract.RepoEntry.TABLE_NAME + " (" +
                GitWatchContract.RepoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                GitWatchContract.RepoEntry.COLUMN_REPO_IDENTIFIER + " TEXT NOT NULL" +
                GitWatchContract.RepoEntry.COLUMN_REPO_TYPE + " INTEGER NOT NULL";

        final String SQL_CREATE_COMMITS_TABLE = "";

        sqLiteDatabase.execSQL(SQL_CREATE_REPO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GitWatchContract.RepoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
