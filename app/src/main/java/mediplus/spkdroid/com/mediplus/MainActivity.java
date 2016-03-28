package mediplus.spkdroid.com.mediplus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Filename : MainActivity.java
 * <p/>
 * This is the first screen in the client application
 * <p/>
 * <p/>
 * the user can login into the system or just can sign up
 * <p/>
 * Once the Authentication is done this page will not be fired again
 */
public class MainActivity extends Activity implements OnClickListener {

    Button LoginButton;
    TextView NewUser;
    EditText netid, password;
    ProgressDialog pd;
    TextView tv;
    private static String url;
    private static String jsonStr = "ram";
    String tokenresult;

    ArrayList<String> r = new ArrayList<String>();
    ProgressDialog pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Linking the XML and UI

        LoginButton = (Button) findViewById(R.id.bLogin);
        NewUser = (TextView) findViewById(R.id.tvRegisterLink);
        LoginButton.setOnClickListener(this);
        NewUser.setOnClickListener(this);
        netid = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
     //   tv = (TextView) findViewById(R.id.errormessage);
    }




    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

		/*
         *
		 * When the User clicks the AddUser button
		 *
		 */

        if (v == NewUser) {
            finish();
            Intent i = new Intent(this, AddUser.class);
            startActivity(i);
        }



		/*
		 * When the User clicks the login button
		 *
		 * The application will fetch the username and the password and will correlate with the server
		 *
		 * The Server will be returing TRUE tag or FALSE tag.
		 *
		 *
		 */


        if (v == LoginButton) {
            final String userName;
            String passWord;
            userName = netid.getText().toString();
            passWord = password.getText().toString();

            // Request Construction for checking the User name and the password

            if (userName.length() > 0 && passWord.length() > 0) {
                Toast.makeText(getApplicationContext(), userName + ":" + passWord, Toast.LENGTH_LONG).show();
                url = "http://www.spkdroid.com/merlin/passwordcheck.php?login_id=" + userName.trim().replace(" ", "%20") + "&login_password=" + passWord.trim().replace(" ", "%20");
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        pr = new ProgressDialog(MainActivity.this);
                        pr.setMessage("Please Wait");
                        pr.show();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        ServiceHandler sh = new ServiceHandler();
                        jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

                        try {
                            JSONArray jr = new JSONArray(jsonStr);
                            JSONObject jsonObj = new JSONObject();
                            jsonObj = jr.getJSONObject(0);
                            tokenresult = jsonObj.getString("result");
                            Log.i("Ram:", tokenresult);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        if (tokenresult.equals("TRUE")) {
                     //       Toast.makeText(MainActivity.this, tokenresult, Toast.LENGTH_LONG).show();
                            finish();
                            File file = new File(Environment.getExternalStorageDirectory() + "dayatdal.txt");
                            if (!file.exists()) {
                                File fp = new File(Environment.getExternalStorageDirectory() + "/dayatdal.txt");
                                try {
                                    FileUtils.writeStringToFile(fp, netid.getText().toString());
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            Intent i = new Intent(MainActivity.this, Overview.class);
                            i.putExtra("idName", userName);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(),"Password Incorrect",Toast.LENGTH_LONG).show();
                            //tv.setText("Password Incorrect");
                            pr.dismiss();
                        }
                    }
                }.execute();
            } else {
                // Error Message when any credential is missing
                Toast.makeText(getApplicationContext(), "Input Missing!!!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
