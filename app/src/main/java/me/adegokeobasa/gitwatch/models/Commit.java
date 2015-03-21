package me.adegokeobasa.gitwatch.models;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static Commit fromJson(JSONObject json)
            throws JSONException
    {
        Commit commit = new Commit();
        commit.setAuthor(json.getString("author"));
        commit.setTitle(json.getString("title"));
        commit.setContent(json.getString("contentSnippet"));
        commit.setUrl(json.getString("link"));
        commit.setDate(json.getString("publishedDate"));
        return commit;
    }
}
