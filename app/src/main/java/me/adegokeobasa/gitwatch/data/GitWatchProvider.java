package me.adegokeobasa.gitwatch.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */
public class GitWatchProvider extends ContentProvider {
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    public static final int REPO = 300;
    private static final int REPO_ID = 301;
    private static final int REPO_WITH_TYPE = 302;
    private static final int REPO_WITH_IDENTIFIER = 303;
    private static final int REPO_WITH_IDENTIFIER_AND_TYPE = 304;

    private GitWatchDbHelper gitWatchDbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = GitWatchContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, GitWatchContract.PATH_REPO, REPO);
        matcher.addURI(authority, GitWatchContract.PATH_REPO + "/*", REPO_WITH_IDENTIFIER);
        matcher.addURI(authority, GitWatchContract.PATH_REPO + "/*/*", REPO_WITH_IDENTIFIER_AND_TYPE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {


        final SQLiteDatabase db = gitWatchDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;


        switch (match) {
            case REPO: {
                long _id = db.insert(GitWatchContract.RepoEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = GitWatchContract.RepoEntry.buildRepoUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = gitWatchDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case REPO:
                rowsDeleted = db.delete(
                        GitWatchContract.RepoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
