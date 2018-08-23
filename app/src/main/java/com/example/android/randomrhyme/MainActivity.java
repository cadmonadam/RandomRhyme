package com.example.android.randomrhyme;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.randomrhyme.data.PoemsContract;
import com.example.android.randomrhyme.model.Poem;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Poem> {

    private static final int QUOTES_LOADER_ID = 1;
    @BindView(R.id.title_textView)
    TextView titleTextView;
    @BindView(R.id.poemBody_textView)
    TextView poemTextView;
    @BindView(R.id.author_textView)
    TextView authorTextView;
    @BindView(R.id.error_message)
    TextView errorTextView;
    @BindView(R.id.network_error_message)
    TextView networkErrorTextView;
    @BindView(R.id.favorite_icon)
    ImageView favoriteImageView;
    @BindView(R.id.new_poem_icon)
    ImageView newPoemImageView;
    @BindView(R.id.share_icon)
    ImageView shareImageView;
    @BindView(R.id.try_again_button)
    Button retryButton;
    @BindView(R.id.icon_bar_gridview)
    GridLayout buttonsRelativeLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Poem currentPoem = null;
    private boolean isAddedToFavorites;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("isAddedToFavorites")) {
                favoriteImageView.setImageResource(R.drawable.ic_action_heart);
                favoriteImageView.setEnabled(false);
                favoriteImageView.setClickable(false);
            }
        }

        newPoemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.restartLoader(QUOTES_LOADER_ID, null, MainActivity.this);
                favoriteImageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                favoriteImageView.setEnabled(true);
                favoriteImageView.setClickable(true);
                isAddedToFavorites = false;
                Snackbar snackbar = Snackbar.make(view, getString(R.string.get_new_poem), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        favoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPoem != null) {
                    favoriteImageView.setImageResource(R.drawable.ic_action_heart);
                    favoriteImageView.setEnabled(false);
                    favoriteImageView.setClickable(false);
                    isAddedToFavorites = true;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PoemsContract.PoemEntry.COLUMN_POEM_BODY_TEXT, currentPoem.getPoemBodyText());
                    contentValues.put(PoemsContract.PoemEntry.COLUMN_AUTHOR, currentPoem.getAuthor());
                    contentValues.put(PoemsContract.PoemEntry.COLUMN_POEM_TITLE, currentPoem.getPoemTitle());
                    getContentResolver().insert(PoemsContract.PoemEntry.CONTENT_URI, contentValues);
                    Snackbar snackbar1 = Snackbar.make(view, getString(R.string.added_to_favorites), Snackbar.LENGTH_LONG);
                    snackbar1.show();

                }
            }
        });

        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPoem != null) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, currentPoem.getAuthor() + "\n\n" + currentPoem.getPoemTitle() + "\n\n" + currentPoem.getPoemBodyText());
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));

                }
            }
        });

        setSupportActionBar(toolbar);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(QUOTES_LOADER_ID, null, this);

        } else {
            showNetworkErrorMessage();
        }

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedItem = item.getItemId();

        switch (selectedItem) {
            case R.id.go_to_favorites_screen:

                Intent intent = new Intent(this, FavoritePoemsActivity.class);
                startActivity(intent);
                return true;

            case R.id.go_to_home_screen:
                Toast.makeText(MainActivity.this, R.string.toast_home_screen, Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public Loader<Poem> onCreateLoader(int id, Bundle args) {
        return new PoemsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Poem> loader, Poem data) {

        currentPoem = data;
        if (currentPoem != null) {
            authorTextView.setText(currentPoem.getAuthor());
            titleTextView.setText(currentPoem.getPoemTitle());
            poemTextView.setText(currentPoem.getPoemBodyText());
            showMainElements();
        } else {
            showErrorMessage();

            retryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoaderManager loaderManager = getSupportLoaderManager();
                    loaderManager.restartLoader(QUOTES_LOADER_ID, null, MainActivity.this);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Poem> loader) {
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isAddedToFavorites", isAddedToFavorites);
    }


    public void showMainElements() {
        authorTextView.setVisibility(View.VISIBLE);
        titleTextView.setVisibility(View.VISIBLE);
        poemTextView.setVisibility(View.VISIBLE);
        buttonsRelativeLayout.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        networkErrorTextView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
    }

    public void showErrorMessage() {
        authorTextView.setVisibility(View.INVISIBLE);
        titleTextView.setVisibility(View.INVISIBLE);
        poemTextView.setVisibility(View.INVISIBLE);
        buttonsRelativeLayout.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.VISIBLE);
        networkErrorTextView.setVisibility(View.GONE);
    }

    public void showNetworkErrorMessage() {
        authorTextView.setVisibility(View.INVISIBLE);
        titleTextView.setVisibility(View.INVISIBLE);
        poemTextView.setVisibility(View.INVISIBLE);
        buttonsRelativeLayout.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        networkErrorTextView.setVisibility(View.VISIBLE);
    }
}