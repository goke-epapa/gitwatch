package me.adegokeobasa.gitwatch.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import me.adegokeobasa.gitwatch.R;
import me.adegokeobasa.gitwatch.data.GitWatchContract;
import me.adegokeobasa.gitwatch.fragments.LandingFragment;
import me.adegokeobasa.gitwatch.fragments.RepoDetailFragment;
import me.adegokeobasa.gitwatch.utils.UIUtils;

public class RepoDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RepoDetailFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_repo_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_remove_repo) {
            removeRepo();
        }

        return super.onOptionsItemSelected(item);
    }

    private void removeRepo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?")
                .setMessage("All data about this repository will be lost.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (getIntent().hasExtra(LandingFragment.EXTRA_REPO_ID)) {
                            int repoId = getIntent().getIntExtra(LandingFragment.EXTRA_REPO_ID, 0);
                            Uri repoUri = GitWatchContract.RepoEntry.buildRepoUri(repoId);
                            int rows = getContentResolver().delete(repoUri, null, null);
                            if (rows > 0) {
                                UIUtils.makeToast(RepoDetailActivity.this, "Repository has been deleted");
                                finish();
                            }
                        }
                    }
                });
        builder.show();
    }
}
