package com.leclex.services;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class ServicesActivity extends AppCompatActivity {
    IntentFilter intentFilter;

    MyService serviceBinder;
    Intent i;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // called when the connection is made
            serviceBinder = ((MyService.MyBinder)service).getService();
            try {
                URL[] urls = new URL[] {
                    new URL("http://www.amazon.com/somefile.pdf"),
                    new URL("http://www.wrox.com/somefile.pdf"),
                    new URL("http://www.google.com/somefiles.pdf"),
                    new URL("http://www.leclex.com/somefiles.pdf")
                };
                // assign the URLs to the service through the serviceBinder object
                serviceBinder.urls = urls;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            startService(i);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // called when the service disconnects
            serviceBinder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
    }

    @Override
    public void onResume() {
        super.onResume();

        // intent to filter for file downloaded intent
        intentFilter = new IntentFilter();
        intentFilter.addAction("FILE_DOWNLOADED_ACTION");

        // register the receiver
        registerReceiver(intentReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        // unregister the receiver
        unregisterReceiver(intentReceiver);
    }

    public void startService(View view) {
//        startService(new Intent(getBaseContext(), MyService.class));
        // OR
//        startService(new Intent("com.leclex.MyService"));

//        Intent intent = new Intent(getBaseContext(), MyService.class);
//        try {
//            URL[] urls = new URL[] {
//                    new URL("http://www.amazon.com/somefile.pdf"),
//                    new URL("http://www.wrox.com/somefile.pdf"),
//                    new URL("http://www.google.com/somefiles.pdf"),
//                    new URL("http://www.leclex.com/somefiles.pdf")
//            };
//            intent.putExtra("URLs", urls);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        startService(intent);

//        startService(new Intent(getBaseContext(), MyIntentService.class));

        i = new Intent(ServicesActivity.this, MyService.class);
        bindService(i, connection, BIND_AUTO_CREATE);
    }

    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getBaseContext(), "File downloaded!", Toast.LENGTH_SHORT).show();
        }
    };
}
