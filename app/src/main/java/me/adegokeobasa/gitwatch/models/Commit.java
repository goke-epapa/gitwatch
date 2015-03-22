package me.adegokeobasa.gitwatch.models;

import com.ocpsoft.pretty.time.PrettyTime;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */
public class Commit {

    private String title;
    private String content;
    private String id;
    private String url;
    private String author;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private static String getFormattedTime(String rawDate) {
        DateFormat dateFormatterRssPubDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        try {
            Date date = dateFormatterRssPubDate.parse(rawDate);
            PrettyTime prettyTime = new PrettyTime();
            return prettyTime.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return rawDate;
        }
    }

    public static Commit fromJson(JSONObject json)
            throws JSONException
    {
        Commit commit = new Commit();
        commit.setAuthor(json.getString("author"));
        commit.setTitle(json.getString("title"));
        commit.setContent(json.getString("contentSnippet"));
        commit.setUrl(json.getString("link"));
        commit.setDate(getFormattedTime(json.getString("publishedDate")));
        return commit;
    }
}
