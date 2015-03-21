package me.adegokeobasa.gitwatch.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.adegokeobasa.gitwatch.LandingFragment;
import me.adegokeobasa.gitwatch.R;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */

public class RepoAdapter extends CursorAdapter {
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;
    public RepoAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = R.layout.list_repo_item;
        View view = LayoutInflater.from(context).inflate(layoutId    , parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int repoId = cursor.getInt(LandingFragment.COL_REPO_ID);
        int repoType = cursor.getInt(LandingFragment.COL_TYPE);
        viewHolder.iconView.setImageResource(repoType == 1 ? R.drawable.ic_bitbucket : R.drawable.ic_github);

        viewHolder.repoNameView.setText(cursor.getString(LandingFragment.COL_NAME));
        viewHolder.lastCommitMessageView.setText(cursor.getString(LandingFragment.COL_LAST_COMMIT_MSG));
        viewHolder.ownerNameView.setText(cursor.getString(LandingFragment.COL_OWNER_NAME));
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView repoNameView;
        public final TextView lastCommitMessageView;
        public final TextView ownerNameView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.image);
            repoNameView = (TextView) view.findViewById(R.id.repo_name);
            lastCommitMessageView = (TextView) view.findViewById(R.id.last_commit_msg);
            ownerNameView = (TextView) view.findViewById(R.id.owner_name);
        }
    }
}
