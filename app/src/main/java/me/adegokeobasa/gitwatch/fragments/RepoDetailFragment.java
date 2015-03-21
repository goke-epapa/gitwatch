package me.adegokeobasa.gitwatch.fragments;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.adegokeobasa.gitwatch.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RepoDetailFragment extends Fragment {

    public RepoDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_repo_detail, container, false);
        return rootView;
    }
}