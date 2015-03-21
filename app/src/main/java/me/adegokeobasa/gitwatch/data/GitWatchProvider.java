package me.adegokeobasa.gitwatch.data;

import android.content.ContentProvider;
import android.content.ContentUris;
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
        gitWatchDbHelper = new GitWatchDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor returnCursor;

        switch (sUriMatcher.match(uri)) {
            case REPO_ID: {
                returnCursor = getRepoByRepoId(uri, projection, sortOrder);
                break;
            }
            case REPO: {
                returnCursor = gitWatchDbHelper.getReadableDatabase().query(
                        GitWatchContract.RepoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REPO_WITH_IDENTIFIER: {
                returnCursor = getRepoByRepoIdentifier(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    private static final String repoIdentifierSelection =
            GitWatchContract.RepoEntry.TABLE_NAME+
                    "." + GitWatchContract.RepoEntry.COLUMN_REPO_IDENTIFIER + " = ? ";

    private Cursor getRepoByRepoIdentifier(Uri uri, String[] projection, String sortOrder) {
        String repoIdentifier = GitWatchContract.RepoEntry.getRepoIdentifierFromUri(uri);

        String selection = repoIdentifierSelection;
        String[] selectionArgs = new String[]{repoIdentifier};

        return gitWatchDbHelper.getReadableDatabase().query(
                GitWatchContract.RepoEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    public static final String repoIdSelection = GitWatchContract.RepoEntry.TABLE_NAME +
            "." + GitWatchContract.RepoEntry._ID + " = ? ";

    private Cursor getRepoByRepoId(Uri uri, String[] projection, String sortOrder) {
        long repoId = ContentUris.parseId(uri);
        String[] selectionArgs;
        String selection;

        selectionArgs = new String[]{String.valueOf(repoId)};
        selection = repoIdSelection;

        return gitWatchDbHelper.getReadableDatabase().query(
                GitWatchContract.RepoEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REPO: {
                return GitWatchContract.RepoEntry.CONTENT_TYPE;
            }
            case REPO_ID: {
                return GitWatchContract.RepoEntry.CONTENT_ITEM_TYPE;
            }
            case REPO_WITH_IDENTIFIER: {
                return GitWatchContract.RepoEntry.CONTENT_ITEM_TYPE;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = gitWatchDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;


        switch (match) {
            case REPO: {
                long _id = db.insert(GitWatchContract.RepoEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
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
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = gitWatchDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int result;
        switch (match) {
            case REPO: {
                result = db.update(GitWatchContract.RepoEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if (result != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }
}
