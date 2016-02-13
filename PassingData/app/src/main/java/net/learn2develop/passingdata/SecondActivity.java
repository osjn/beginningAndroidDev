package net.learn2develop.passingdata;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
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

        // get the data passed in using getStringExtra()
        Toast.makeText(SecondActivity.this, getIntent().getStringExtra("str1"),
                Toast.LENGTH_SHORT).show();

        // get the data passed in using getIntExtra()
        Toast.makeText(SecondActivity.this, Integer.toString(
                getIntent().getIntExtra("age1", 0)
        ), Toast.LENGTH_SHORT).show();

        // get the Bundle object passed in
        Bundle bundle = getIntent().getExtras();

        // get the data using the getString()
        Toast.makeText(SecondActivity.this, bundle.getString("str2"), Toast.LENGTH_SHORT).show();

        // get the data using the getInt() method
        Toast.makeText(SecondActivity.this, Integer.toString(bundle.getInt("age2")), Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v) {
        // use an Intent object to return data
        Intent i = new Intent();

        // use the putExtra() method to return some value
        i.putExtra("age3", 45);

        // use the setData() method to return some value
        i.setData(Uri.parse("Something passed back to main activity"));

        // set the result with OK and the Intent object
        setResult(RESULT_OK, i);

        // destroy the current activity
        finish();
    }
}
