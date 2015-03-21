package me.adegokeobasa.gitwatch.activities;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

import me.adegokeobasa.gitwatch.AddRepoDialogFragment;
import me.adegokeobasa.gitwatch.R;
import me.adegokeobasa.gitwatch.data.GitWatchContract;
import me.adegokeobasa.gitwatch.fragments.LandingFragment;
import me.adegokeobasa.gitwatch.utils.StringUtils;
import me.adegokeobasa.gitwatch.utils.UIUtils;


public class MainActivity extends ActionBarActivity implements AddRepoDialogFragment.AddRepoDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LandingFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_add_repo) {
            addRepo();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void addRepo() {
        DialogFragment addRepoDialog = new AddRepoDialogFragment();
        addRepoDialog.show(getSupportFragmentManager(), "addRepoDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Boolean wantToCloseDialog = false;

        TextView repoIdentifierTv = (TextView) dialog.getDialog().findViewById(R.id.dialog_repo_identifier);
        repoIdentifierTv.setError(null);
        Spinner repoTypeSpinner = (Spinner) dialog.getDialog().findViewById(R.id.dialog_repo_type);
        String repoIdentifier = repoIdentifierTv.getText().toString();
        if (repoIdentifier == null || repoIdentifier.length() == 0) {
            repoIdentifierTv.setError("Please enter the repository identifier");
            return;
        }

        if (repoIdentifier.indexOf('/') == -1 || repoIdentifier.indexOf('/') == 0 || repoIdentifier.indexOf('/') == repoIdentifier.length() - 1) {
            repoIdentifierTv.setError("Invalid Repository Identifier format.");
            return;
        }

        int repoType;
        switch (repoTypeSpinner.getSelectedItem().toString().toLowerCase()) {
            case "github":
                repoType = GitWatchContract.RepoEntry.TYPE_GITHUB;
                break;
            default:
                repoType = GitWatchContract.RepoEntry.TYPE_BITBUCKET;
                break;
        }

        ContentValues repoValues = new ContentValues();
        repoValues.put(GitWatchContract.RepoEntry.COLUMN_IDENTIFIER, repoIdentifier);
        repoValues.put(GitWatchContract.RepoEntry.COLUMN_TYPE, repoType);
        String[] arr = repoIdentifier.split("/");
        String repoName = StringUtils.toTitleCase(arr[1].toLowerCase());
        String username = arr[0].toLowerCase();
        repoValues.put(GitWatchContract.RepoEntry.COLUMN_NAME, repoName);
        repoValues.put(GitWatchContract.RepoEntry.COLUMN_OWNER_NAME, username);
        repoValues.put(GitWatchContract.RepoEntry.COLUMN_LAST_COMMIT_MSG, "N/A");
        getContentResolver().insert(GitWatchContract.RepoEntry.CONTENT_URI, repoValues);
        UIUtils.makeToast(this, "Repository " + repoName + " has been added. :)");
        wantToCloseDialog = true;

        if(wantToCloseDialog)
            dialog.dismiss();
    }
}