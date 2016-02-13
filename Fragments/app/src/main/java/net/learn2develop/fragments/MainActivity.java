package net.learn2develop.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        // get the current display info
//        WindowManager wm = getWindowManager();
//        Display d = wm.getDefaultDisplay();
//        Point size = new Point();
//        d.getSize(size);
//        if (size.x > size.y) {
//            // landscape mode
//            Fragment1 fragment1 = new Fragment1();
//            // android.R.id.content refers to the content view of the activity
//            fragmentTransaction.replace(android.R.id.content, fragment1);
//        } else {
//            // portrait mode
//            Fragment2 fragment2 = new Fragment2();
//            fragmentTransaction.replace(android.R.id.content, fragment2);
//        }
//        // add to the back stack
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
