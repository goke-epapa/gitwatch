package me.adegokeobasa.gitwatch.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.adegokeobasa.gitwatch.fragments.RepoDetailFragment;
import me.adegokeobasa.gitwatch.models.Commit;
import me.adegokeobasa.gitwatch.utils.UIUtils;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */
public class FetchRepoDetailTask extends AsyncTask<String, Void, Void> {
    ProgressDialog progressDialog;
    Context context;
    private static RequestQueue requestQueue;
    public FetchRepoDetailTask(Context context) {
        this.context = context;
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Getting commits, please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... strings) {
        String url = strings[0];
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                RepoDetailFragment.commits.clear();

                if(!response.has("responseData")) {
                    UIUtils.makeToast(context, "Invalid response from server");
                } else {
                    try {
                        JSONArray entries = response.getJSONObject("responseData")
                                .getJSONObject("feed")
                                .getJSONArray("entries");
                        for(int i = 0; i < entries.length(); i++){
                            Commit commit = Commit.fromJson(entries.getJSONObject(i));
                            RepoDetailFragment.commits.add(commit);
                        }

                        RepoDetailFragment.commitAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                UIUtils.makeToast(context, "An error occurred while fetching commits");
            }
        });
        requestQueue.add(jsonObjectRequest);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
