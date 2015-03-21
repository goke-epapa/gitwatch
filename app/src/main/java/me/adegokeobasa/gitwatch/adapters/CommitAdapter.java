package me.adegokeobasa.gitwatch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import me.adegokeobasa.gitwatch.R;
import me.adegokeobasa.gitwatch.models.Commit;

/**
 * Created by Adegoke Obasa <adegokeobasa@gmail.com> on 3/21/15.
 */
public class CommitAdapter extends ArrayAdapter<Commit> {

    private int resourceId;
    private List<Commit> list;
    private Context context;

    public CommitAdapter(Context context, int resource, List<Commit> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
        this.list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(this.resourceId, parent, false);
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Commit commit = list.get(position);

        if(viewHolder == null) {
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        viewHolder.commitTitle.setText(commit.getTitle());
        viewHolder.commitAuthor.setText(commit.getAuthor());
        viewHolder.commitPublishDate.setText(commit.getDate());

        return view;
    }

    public static class ViewHolder {
        public final TextView commitTitle;
        public final TextView commitAuthor;
        public final TextView commitPublishDate;

        public ViewHolder(View view) {
            commitTitle = (TextView) view.findViewById(R.id.commit_title);
            commitAuthor = (TextView) view.findViewById(R.id.commit_author);
            commitPublishDate = (TextView) view.findViewById(R.id.commit_publish_date);
        }
    }
}
