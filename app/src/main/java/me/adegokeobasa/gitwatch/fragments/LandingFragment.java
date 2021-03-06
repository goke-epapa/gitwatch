package me.adegokeobasa.gitwatch.fragments;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import me.adegokeobasa.gitwatch.R;
import me.adegokeobasa.gitwatch.activities.MainActivity;
import me.adegokeobasa.gitwatch.activities.RepoDetailActivity;
import me.adegokeobasa.gitwatch.adapters.RepoAdapter;
import me.adegokeobasa.gitwatch.data.GitWatchContract.RepoEntry;
import me.adegokeobasa.gitwatch.interfaces.RepoSelectedListener;

/**
 * A landing fragment containing a simple view.
 */
public class LandingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int REPO_LOADER = 0;

    public static final String[] REPO_COLUMNS = {
            RepoEntry.TABLE_NAME + "." + RepoEntry._ID,
            RepoEntry.COLUMN_IDENTIFIER,
            RepoEntry.COLUMN_TYPE,
            RepoEntry.COLUMN_NAME,
            RepoEntry.COLUMN_OWNER_NAME,
            RepoEntry.COLUMN_LAST_COMMIT_MSG
    };

    public static final int COL_REPO_ID = 0;
    public static final int COL_IDENIFIER = 1;
    public static final int COL_TYPE = 2;
    public static final int COL_NAME = 3;
    public static final int COL_OWNER_NAME = 4;
    public static final int COL_LAST_COMMIT_MSG = 5;

    public static final String EXTRA_REPO_ID = "repo_id";
    public static int selectedRepoId;
    private RepoSelectedListener listener;

    private RepoAdapter repoAdapter;

    public LandingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        repoAdapter = new RepoAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_landing, container, false);
        ListView reposListView = (ListView) rootView.findViewById(R.id.repos_list);
        reposListView.setEmptyView(rootView.findViewById(R.id.repos_list_empty));

        reposListView.setAdapter(repoAdapter);

        reposListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RepoAdapter cursorAdapter = (RepoAdapter) adapterView.getAdapter();
                Cursor cursor = (Cursor) cursorAdapter.getItem(i);
                if (cursor != null) {
                    selectedRepoId = cursor.getInt(COL_REPO_ID);
                    if(MainActivity.mTwoPane) {
                        listener.onRepoClicked();
                    } else {
                        Intent detailIntent = new Intent(getActivity(), RepoDetailActivity.class)
                                .putExtra(EXTRA_REPO_ID, selectedRepoId);
                        startActivity(detailIntent);
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = RepoEntry.CONTENT_URI;
        String sortOrder = RepoEntry.COLUMN_NAME + " ASC";
        return new CursorLoader(getActivity(), uri, REPO_COLUMNS, null, null, sortOrder);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(REPO_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        repoAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        repoAdapter.swapCursor(null);
    }

    public void setListener(RepoSelectedListener listener) {
        this.listener = listener;
    }
}