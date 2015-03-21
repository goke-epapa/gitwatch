package me.adegokeobasa.gitwatch.utils;

import android.net.Uri;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */
public class ApiHelper {
    public static final String GITHUB_BASE = "github.com";
    public static final String BITBUCKET_BASE = "bitbucket.org";

    public static String getGithubUrl(String repoIdentifier) {
        Uri.Builder uriBuilder = new Uri.Builder();
        String[] arr = repoIdentifier.split("/");
        uriBuilder.scheme("https")
                .authority(GITHUB_BASE)
                .appendPath(arr[0])
                .appendPath(arr[1])
                .appendPath("commits")
                .appendPath("master.atom");
        return uriBuilder.build().toString();
    }

    public static String getBitbucketUrl(String repoIdentifier) {
        Uri.Builder uriBuilder = new Uri.Builder();
        String[] arr = repoIdentifier.split("/");
        uriBuilder.scheme("https")
                .authority(BITBUCKET_BASE)
                .appendPath(arr[0])
                .appendPath(arr[1])
                .appendPath("rss");
        return uriBuilder.build().toString();
    }

    public static String getJsonApiUrl(String feedUrl) {
        Uri.Builder uriBuilder = Uri.parse("https://ajax.googleapis.com/ajax/services/feed/load").buildUpon();
        uriBuilder.appendQueryParameter("q", feedUrl)
                .appendQueryParameter("v", "2.0");
        return uriBuilder.build().toString();
    }


}
