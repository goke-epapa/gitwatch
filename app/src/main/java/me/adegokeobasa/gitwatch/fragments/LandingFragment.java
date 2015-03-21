package me.adegokeobasa.gitwatch.fragments;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */

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
import android.widget.ListView;

import me.adegokeobasa.gitwatch.R;
import me.adegokeobasa.gitwatch.adapters.RepoAdapter;
import me.adegokeobasa.gitwatch.data.GitWatchContract.RepoEntry;

/**
 * A landing fragment containing a simple view.
 */
public class LandingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int REPO_LOADER = 0;

    private static final String[] REPO_COLUMNS = {
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

    private RepoAdapter repoAdapter;

    public LandingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        repoAdapter = new RepoAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_landing, container, false);
        ListView reposList = (ListView) rootView.findViewById(R.id.repos_list);
        reposList.setEmptyView(rootView.findViewById(R.id.repos_list_empty));

        reposList.setAdapter(repoAdapter);

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
}