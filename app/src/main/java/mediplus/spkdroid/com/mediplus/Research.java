package mediplus.spkdroid.com.mediplus;

/**
 * Created by Ramkumar Velmurugan on 3/26/2016.
 *
 *
 *
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 *
 * This is an Newsfeed part of the code.
 *
 * This class file will fetch all the news from the web server and will be posting back to the
 *
 * user
 *
 * Research Related News Feed
 *
 * @author Ramkumar Velmurugan
 *
 */

public class Research  extends  Activity implements OnItemClickListener
{

    private static final String TAG = "FEED";
    // the webservice link to fetch the news information
    private String url = "http://www.spkdroid.com/mediplus/newresearch.php";
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_feed);

        listView = (ListView)findViewById(R.id.list);
        adapter = new CustomListAdapter(this, movieList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        url=url+"?axar=%27"+read()+"%27";

        // the movieReq and movie are the news feed information that added into the movieList which can be loaded into listview
Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Movie movie = new Movie();
                                movie.setTitle(obj.getString("title"));
                                movie.setThumbnailUrl(obj.getString("img_url"));
                                movie.setRating((obj.getString("type")));
                                movie.setYear(obj.getString("date"));
                                movie.setDescription(obj.getString("description"));
                                movie.setUrl(obj.getString("url"));
                                movie.setGenre(obj.getString("type"));
                                movieList.add(movie);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }


        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    // shows the alertbox that shows a dialog that will shows the information

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        final String alert_title,alert_url,alert_description;

        alert_title=((TextView)view.findViewById(R.id.title)).getText().toString();
        alert_url=((TextView)view.findViewById(R.id.url)).getText().toString();
        alert_description=((TextView)view.findViewById(R.id.description)).getText().toString();
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle(alert_title)
                .setMessage("\n"+alert_description)
                .setPositiveButton("visit Dal Site",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(alert_url));
                                startActivity(i);
                            }
                        }
                )
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                ).show();
    }

    public String read() {
        BufferedReader br = null;
        String response = null;
        try {
            StringBuffer output = new StringBuffer();
            String fpath = Environment.getExternalStorageDirectory() + "/dayatdal.txt";
            br = new BufferedReader(new FileReader(fpath));
            String line = "";
            while ((line = br.readLine()) != null) {
                output.append(line);
            }
            response = output.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }


    public void onBackPressed()
    {
        finish();
    }

}