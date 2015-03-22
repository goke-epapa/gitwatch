package me.adegokeobasa.gitwatch.fragments;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import me.adegokeobasa.gitwatch.R;
import me.adegokeobasa.gitwatch.adapters.CommitAdapter;
import me.adegokeobasa.gitwatch.data.GitWatchContract;
import me.adegokeobasa.gitwatch.interfaces.CommitsLoadListener;
import me.adegokeobasa.gitwatch.models.Commit;
import me.adegokeobasa.gitwatch.tasks.FetchRepoDetailTask;
import me.adegokeobasa.gitwatch.utils.ApiHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class RepoDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, CommitsLoadListener {

    private static final int REPO_LOADER = 0;
    ShareActionProvider mShareActionProvider;
    public static final String SHARE_HASH_TAG = "#GitWatch";
    public static final String TAG = RepoDetailFragment.class.getSimpleName();
    public static ArrayList<Commit> commits = new ArrayList<Commit>();
    public static CommitAdapter commitAdapter;
    private String repoName;
    private String repoUrl;
    private long repoId = 0;
    private boolean dataLoaded;

    public RepoDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            repoId = arguments.getInt(LandingFragment.EXTRA_REPO_ID);
            setHasOptionsMenu(true);
        }

        View rootView = inflater.inflate(R.layout.fragment_repo_detail, container, false);
        ListView commitsListView = (ListView) rootView.findViewById(R.id.list_commits);
        commitsListView.setEmptyView(rootView.findViewById(R.id.commits_empty));

        commitAdapter = new CommitAdapter(getActivity(), R.layout.list_commit_item, commits);
        commitsListView.setAdapter(commitAdapter);

        commitsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Commit commit = commits.get(i);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(commit.getUrl()));
                startActivity(browserIntent);
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (repoId == 0) {
            getView().findViewById(R.id.detail_empty).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.content_container).setVisibility(View.INVISIBLE);
            return null;
        }

        Uri repoUri = GitWatchContract.RepoEntry.buildRepoUri(repoId);

        return new CursorLoader(
                getActivity(),
                repoUri,
                LandingFragment.REPO_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(!data.moveToFirst()){
            return;
        }

        getView().findViewById(R.id.content_container).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.detail_empty).setVisibility(View.GONE);

        String repoIdentifier = data.getString(LandingFragment.COL_IDENIFIER);
        int repoType = data.getInt(LandingFragment.COL_TYPE);

        String url = repoType == GitWatchContract.RepoEntry.TYPE_BITBUCKET ? ApiHelper.getJsonApiUrl(ApiHelper.getBitbucketUrl(repoIdentifier)) : ApiHelper.getJsonApiUrl(ApiHelper.getGithubUrl(repoIdentifier));

        TextView repoUsernameTv = (TextView) getView().findViewById(R.id.detail_repo_username);
        TextView repoNameTv = (TextView) getView().findViewById(R.id.detail_repo_name);
        TextView repoLastUpdateTimeTv = (TextView) getView().findViewById(R.id.detail_repo_last_updated);

        repoName = data.getString(LandingFragment.COL_NAME);
        repoUsernameTv.setText(data.getString(LandingFragment.COL_OWNER_NAME));
        repoNameTv.setText(repoName);
        repoLastUpdateTimeTv.setText("Just Now");
        ImageView repoImageView = (ImageView) getView().findViewById(R.id.detail_repo_image);
        repoUrl = ApiHelper.getBitbucketShareUrl(repoIdentifier);
        if(repoType == GitWatchContract.RepoEntry.TYPE_GITHUB) {
            repoUrl = ApiHelper.getGithubShareUrl(repoIdentifier);
            repoImageView.setImageResource(R.drawable.ic_github);
        }

        setSharedIntent();

        if(!dataLoaded) {
            fetchCommits(url);
            dataLoaded = true;
        }
    }

    private void setSharedIntent() {
        if(repoName != null && repoUrl != null && mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent(repoName, repoUrl));
        }
    }

    private void fetchCommits(String url) {
        FetchRepoDetailTask repoDetailTask = new FetchRepoDetailTask(getActivity());
        repoDetailTask.setCommitsListener(this);
        repoDetailTask.execute(url);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(REPO_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private Intent createShareForecastIntent(String repoName, String repoUrl) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my repository " + repoName + " @ " + repoUrl + " " + SHARE_HASH_TAG);
        return shareIntent;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_fragment_detail, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setSharedIntent();
    }

    @Override
    public void load() {
        commitAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateRepo() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GitWatchContract.RepoEntry.COLUMN_LAST_COMMIT_MSG, commits.get(commits.size() -1).getTitle());
        if(getActivity() != null) {
            getActivity().getContentResolver().update(
                    GitWatchContract.RepoEntry.buildRepoUri(repoId),
                    contentValues, null, null);
        }
    }
}