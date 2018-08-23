package com.example.android.randomrhyme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.randomrhyme.data.PoemsContract;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritePoemsActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FAVORITE_POEMS_LOADER = 1;
    @BindView(R.id.poems_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private PoemsCursorAdapter poemsCursorAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_poems);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        poemsCursorAdapter = new PoemsCursorAdapter(this);
        recyclerView.setAdapter(poemsCursorAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                int id = (int) viewHolder.itemView.getTag();
                String stringId = Integer.toString(id);
                Uri uri = PoemsContract.PoemEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                getContentResolver().delete(uri, null, null);
                getSupportLoaderManager().restartLoader(FAVORITE_POEMS_LOADER, null, FavoritePoemsActivity.this);
            }
        }).attachToRecyclerView(recyclerView);

        getSupportLoaderManager().initLoader(FAVORITE_POEMS_LOADER, null, this);

        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544/6300978111");

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        boolean firstTime = prefs.getBoolean("firstTime", true);

        if (firstTime) {
            Toast.makeText(FavoritePoemsActivity.this, R.string.user_information, Toast.LENGTH_LONG).show();
            editor.putBoolean("firstTime", false);
            editor.commit();
        }
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
                Toast.makeText(FavoritePoemsActivity.this, R.string.toast_favorites_screen, Toast.LENGTH_LONG).show();
                return true;

            case R.id.go_to_home_screen:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                PoemsContract.PoemEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        poemsCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        poemsCursorAdapter.swapCursor(null);
    }

}
