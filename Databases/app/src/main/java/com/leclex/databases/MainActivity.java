package com.leclex.databases;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

        DBAdapter db = new DBAdapter(this);
        try {
            String destPath = "/data/data/" + getPackageName() + "/databases";
            File f = new File(destPath);
            if (!f.exists()) {
                f.mkdirs();
                f.createNewFile();

                // copy the db from the assets folder into the databases folder
                CopyDB(getBaseContext().getAssets().open("mydb"),
                        new FileOutputStream(destPath + "/MyDB"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add a contact
//        db.open();
//        long id = db.insertContact("Wei-Meng Lee", "weimenglee@leclex.com");
//        id = db.insertContact("Mary Jackson", "mary@leclex.com");
//        db.close();

        // get all contacts
        db.open();
        Cursor c = db.getAllContacts();
        if (c.moveToFirst()) {
            do {
                DisplayContact(c);
            } while (c.moveToNext());
        }
        db.close();

        // get a contact
//        db.open();
//        Cursor c = db.getContact(2);
//        if (c.moveToFirst()) {
//            DisplayContact(c);
//        } else {
//            Toast.makeText(this, "No contact found", Toast.LENGTH_SHORT).show();
//        }
//        db.close();

        // update a contact
//        db.open();
//        if (db.updateContact(1, "Wei-Meng Lee", "weimenglee@gamil.com")) {
//            Toast.makeText(this, "Update successfully.", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
//        }
//        db.close();

        // delete a contact
//        db.open();
//        if (db.deleteContact(1)) {
//            Toast.makeText(this, "Delete successfully.", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Delete failed.", Toast.LENGTH_SHORT).show();
//        }
//        db.close();
    }

    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        // copy 1K bytes at a time
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    public void DisplayContact(Cursor c) {
        Toast.makeText(this, "id: " + c.getString(0) + "\n" +
        "Name: " + c.getString(1) + "\n" +
        "Email: " + c.getString(2), Toast.LENGTH_SHORT).show();
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
