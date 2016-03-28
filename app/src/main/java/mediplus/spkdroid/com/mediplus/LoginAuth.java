package mediplus.spkdroid.com.mediplus;

/**
 * Created by mainu on 26/03/2016.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

/**
 * Filename : LoginAuth.java
 * <p/>
 * <p/>
 * LoginAuth is the Login Authentication service. When the user successfully login for the first time a dayatdal.txt
 * <p/>
 * file is created and stored in the sdcard. This text file will be holding the user name.
 * <p/>
 * if the text file is missing the Day at Dal client app will force the user to run for an authentication test
 * <p/>
 * this logic will allow the client application to enable one time login method
 */

public class LoginAuth extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_bridge);
        File file = new File(Environment.getExternalStorageDirectory() + "/dayatdal.txt");

        // One time Login Logic has been Implemented

        // The application will check for the dayatdal.txt file in the memory card

        // if the authentication is done the dayatdal.txt file would have been created

        // if the file is missing then it will redirect to the MainActivity.java which is the login module

        // which will ask for the authentication process

        if (file.exists()) {
            finish();
            Intent i = new Intent(this, Overview.class);
            i.putExtra("idName", read());
            startActivity(i);
        } else {
            finish();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

	/*
     *
	 * Read Function is used to fetch the Username from the text file.
	 *
	 */

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
}