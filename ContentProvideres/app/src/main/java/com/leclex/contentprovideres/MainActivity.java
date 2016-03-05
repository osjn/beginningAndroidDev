package com.leclex.contentprovideres;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void onClickAddTitle(View view) {
        // add a book
        ContentValues values = new ContentValues();
        values.put(BooksProvider.TITLE,
                ((EditText) findViewById(R.id.txtTitle)).getText().toString());
        values.put(BooksProvider.ISBN,
                ((EditText) findViewById(R.id.txtISBN)).getText().toString());
        Uri uri = getContentResolver().insert(
                BooksProvider.CONTENT_URI, values);
        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
    }

    public void onClickRetrieveTitles(View view) {
        // retrieve the titles
        Uri allTitles = Uri.parse(
                "content://com.leclex.provider.Books/books");
        Cursor c;
        if (Build.VERSION.SDK_INT < 11) {
            // before Honeycomb
            //noinspection deprecation
            c = managedQuery(allTitles, null, null, null, "title desc");
        } else {
            // Honeycomb and later
            CursorLoader cursorLoader = new CursorLoader(
                    this, allTitles, null, null, null, "title desc");
            c = cursorLoader.loadInBackground();
        }

        if (c.moveToFirst()) {
            do {
                Toast.makeText(this,
                c.getString(c.getColumnIndex(BooksProvider._ID)) + ", " +
                c.getString(c.getColumnIndex(BooksProvider.TITLE)) + ", " +
                c.getString(c.getColumnIndex(BooksProvider.ISBN)), Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
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
        }

        return super.onOptionsItemSelected(item);
    }
}
