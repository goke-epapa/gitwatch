package me.adegokeobasa.gitwatch.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */
public class GitWatchContract {
    public static final String CONTENT_AUTHORITY = "me.adegokeobasa.gitwatch";
    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_REPO = "repo";

    public static final class RepoEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REPO).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_REPO;

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_REPO;

        public static final String TABLE_NAME = "repos";

        public static final String COLUMN_IDENTIFIER = "identifier";

        public static final String COLUMN_TYPE = "type";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_OWNER_NAME = "owner_name";

        public static final String COLUMN_LAST_COMMIT_MSG = "last_commit_msg";

        public static Uri buildRepoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getRepoIdentifierFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static long getRepoIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static final int TYPE_BITBUCKET = 1;
        public static final int TYPE_GITHUB = 2;
    }

}
