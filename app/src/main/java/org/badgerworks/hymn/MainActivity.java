package org.badgerworks.hymn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import org.json.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private EditText query;
    private Button search;
    private ArrayList<ListItem> results;
    private ListView videoList;
    private WebView displayYoutubeVideo;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//      final SearchView search = (SearchView) findViewById(R.id.search_query)s
        query = (EditText) findViewById(R.id.query);
        search = (Button) findViewById(R.id.search);
        results = new ArrayList<ListItem>();
        videoList = (ListView) findViewById(R.id.results);

        displayYoutubeVideo = (WebView) findViewById(R.id.mWebView);

        String[] bar = results.toArray(new String[results.size()]);
        ListAdapter videoAdaptor = new ListAdapter(this, results);

        displayYoutubeVideo.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = displayYoutubeVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        videoList.setAdapter(videoAdaptor);
        videoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String frameVideo = "<html><body>Video From YouTube<br><iframe width=\"400\" height=\"200\" src=\""+results.get(i).getUrl()+"\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
                displayYoutubeVideo.loadData(frameVideo, "text/html", "utf-8");
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmp = query.getText().toString();
                String res = null;
                String url = Constants.YOUTUBE_SEARCH+tmp;
                new youtubeTask().execute(url);
                //Log.d("HTTP", res);
            }
        });
        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void refresh() {
        ListAdapter tmp = new ListAdapter(this, results);
        videoList.setAdapter(tmp);
        videoList.invalidate();
        Log.d("UI", "refreshing video Result list with " + results.size());
    }

    class youtubeTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                URLConnection urlConnection = url.openConnection();
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String message = org.apache.commons.io.IOUtils.toString(br);
                JSONObject obj = new JSONObject(message);
                JSONArray arr = obj.getJSONArray("items");
                results.clear();
                for(int i = 0; i < arr.length(); i++){
                    try{
                        JSONObject obj2 = arr.getJSONObject(i);
                        Log.d("JSONObj",obj2.toString());
                        String gen_url= "https://www.youtube.com/embed/"+obj2.getJSONObject("id").getString("videoId");
                        String title = obj2.getJSONObject("snippet").getString("title");
                        Log.d("JSONObj",gen_url);
                        results.add(new ListItem(title,gen_url));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                Log.d("RESPONSE_BODY",message);
                return message;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            refresh();
        }
    }
}
