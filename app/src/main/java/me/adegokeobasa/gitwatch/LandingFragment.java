package me.adegokeobasa.gitwatch;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A landing fragment containing a simple view.
 */
public class LandingFragment extends Fragment {

    public LandingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_landing, container, false);
        ListView reposList = (ListView) rootView.findViewById(R.id.repos_list);
        reposList.setEmptyView(rootView.findViewById(R.id.repos_list_empty));
        return rootView;
    }
}