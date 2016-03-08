package com.leclex.threading;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static TextView txtView1;

    DoCountingTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtView1 = (TextView) findViewById(R.id.textView1);
    }

    // used for updating the UI on the main activity
    static Handler UIUpdater = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] buffer = (byte[]) msg.obj;

            // convert the entire byte array to string
            String strReceived = new String(buffer);

            // display the text received on the TextView
            txtView1.setText(strReceived);
            Log.d("Threading", "running");
        }
    };

    private class DoCountingTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < 1000; i++) {
                // report its progress
                publishProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.d("Threading", e.getLocalizedMessage());
                }

                if (isCancelled()) break;
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            txtView1.setText(progress[0].toString());
            Log.d("Threading", "updating...");
        }
    }

    public void startCounter(View view) {
//        new DoCountingTask().execute();
        task = (DoCountingTask) new DoCountingTask().execute();
//        for (int i = 0; i <= 1000; i++) {
//            txtView1.setText(String.valueOf(i));
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                Log.d("Threading", e.getLocalizedMessage());
//            }
//        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i <= 1000; i++) {
////                    final int valueOfi = i;
//
//                    // update the main activity UI
//                    MainActivity.UIUpdater.obtainMessage(0, String.valueOf(i).getBytes()).sendToTarget();
//
//                    // update UI
////                    txtView1.post(new Runnable() {
////                        @Override
////                        public void run() {
////                            // UI thread for updating
////                            txtView1.setText(String.valueOf(valueOfi));
////                        }
////                    });
//
////                    txtView1.setText(String.valueOf(i));
//                    // insert a delay
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        Log.d("Threading", e.getLocalizedMessage());
//                    }
//                }
//            }
//        }).start();
    }

    public void stopCounter(View view) {
        task.cancel(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCounter(txtView1);
    }

    private void checkVersion() {
        PackageManager pm = getPackageManager();
        try {
            // get the package info
            PackageInfo pi = pm.getPackageInfo("com.leclex.threading", 0);
            // display the versioncode
            Toast.makeText(getBaseContext(),
                    "VersionCode: " + Integer.toString(pi.versionCode),
                    Toast.LENGTH_SHORT).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
