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
        public static final String TABLE_NAME = "repos";

        public static final String COLUMN_REPO_IDENTIFIER = "repo_identifier";

        public static final String COLUMN_REPO_TYPE = "repo_type";

        public static Uri buildRepoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
