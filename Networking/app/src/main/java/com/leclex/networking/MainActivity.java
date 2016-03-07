package com.leclex.networking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    ImageView img;

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection)) {
            throw new IOException("Not an HTTP connection");
        }

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception e) {
            Log.d("Networking", e.getLocalizedMessage());
            throw new IOException("Error connecting");
        }

        return in;
    }

    private String WordDefinition(String word) {
        InputStream in;
        String strDefinition = "";
        try {
            in = OpenHttpConnection("http://services.aonaware.com/DictService/DictService.asmx/Define?word=" + word);
            Document doc = null;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            try {
                db = dbf.newDocumentBuilder();
                doc = db.parse(in);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            doc.getDocumentElement().normalize();

            // retrieve all the <Definition> elements
            NodeList definitionElements = doc.getElementsByTagName("Definition");
            // iterate through each <Definition> elements
            for (int i = 0; i < definitionElements.getLength(); i++) {
                Node itemNode = definitionElements.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    // convert the Definition node into an Element
                    Element definitionElement = (Element) itemNode;
                    // get all the <WordDefinition> elements under the <Definition> element
                    NodeList wordDefinitionElements = (definitionElement).getElementsByTagName("WordDefinition");
                    strDefinition = "";
                    // iterate through each <WordDefinition> elements
                    for (int j = 0; j < wordDefinitionElements.getLength(); j++) {
                        // convert a <WordDefinition> node into an Element
                        Element wordDefinitionElement = (Element) wordDefinitionElements.item(j);

                        // get all the child nodes under the <WordDefinition> element
                        NodeList textNodes = ((Node) wordDefinitionElement).getChildNodes();
                        strDefinition += ((Node) textNodes.item(0)).getNodeValue() + ". \n";
                    }
                }
            }
        } catch (IOException e) {
            Log.d("NetworkingActivity", e.getLocalizedMessage());
        }

        // return the definition of the word
        return strDefinition;
    }

    private class AccessWebServiceTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return WordDefinition(urls[0]);
        }

        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap DownloadImage(String URL) {
        Bitmap bitmap = null;
        InputStream in;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e) {
            Log.d("MainActivity", e.getLocalizedMessage());
        }

        return bitmap;
    }

//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        protected Bitmap doInBackground(String... urls) {
//            return DownloadImage(urls[0]);
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            ImageView img = (ImageView) findViewById(R.id.img);
//            img.setImageBitmap(result);
//        }
//    }

    private class DownloadImageTask extends AsyncTask<String, Bitmap, Long> {
        // takes in a list of image URLs in String type
        protected Long doInBackground(String... urls) {
            long imagesCount = 0;
            for (int i = 0; i < urls.length; i++) {
                // download the image
                Bitmap imageDownloaded = DownloadImage(urls[i]);
                if (imageDownloaded != null) {
                    // increment the image count
                    imagesCount++;
                    try {
                        // insert a delay of 3 seconds
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // return the image downloaded
                    publishProgress(imageDownloaded);
                }
            }
            return imagesCount;
        }

        // display the image downloaded
        protected void onProgressUpdate(Bitmap... bitmap) {
            img.setImageBitmap(bitmap[0]);
        }

        // when all the images have been downloaded
        protected void onPostExecute(Long imagesDownloaded) {
            Toast.makeText(getBaseContext(),
                    "Total " + imagesDownloaded + " images downloaded",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private String DownloadText(String URL) {
        int BUFFER_SIZE = 2000;
        InputStream in;
        try {
            in = OpenHttpConnection(URL);
        } catch (IOException e) {
            Log.d("Networking", e.getLocalizedMessage());
            return "";
        }

        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer)) > 0) {
                // convert the chars to a String
                String readString =
                        String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            Log.d("Networking", e.getLocalizedMessage());
            return "";
        }

        return str;
    }

    private class DownloadTextTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return DownloadText(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

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

//        new DownloadImageTask().execute("http://www.mayoff.com/5-01cablecarDCP01934.jpg");
        // download an image
        // code will not run in Android 3.0 and beyond
//        Bitmap bitmap = DownloadImage("http://www.mayoff.com/5-01cablecarDCP01934.jpg");
//        img = (ImageView) findViewById(R.id.img);
//        img.setImageBitmap(bitmap);
//        new DownloadImageTask().execute(
//        "http://www.mayoff.com/5-01cablecarDCP01934.jpg",
//        "http://www.hartiesinfo.net/greybox/Cable_Car_Hartbeespoort.jpg",
//        "http://travel.roro44.com/europe/france/Paris_France.jpg"
//        );

        // download text
//        new DownloadTextTask().execute("http://www.bing.com");

        // access a Web Service using GET
        new AccessWebServiceTask().execute("apple");
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
