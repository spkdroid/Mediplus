package mediplus.spkdroid.com.mediplus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class Overview extends AppCompatActivity implements View.OnClickListener {


    Button b1,b2,b3,b4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);


        b1=(Button)findViewById(R.id.records);
        b1.setOnClickListener(this);


        b2=(Button)findViewById(R.id.hospitals);
        b2.setOnClickListener(this);


        b4=(Button)findViewById(R.id.logout);
        b4.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v==b1)
        {
            Intent i=new Intent(getApplicationContext(),Research.class);
            startActivity(i);
        }

        if(v==b2)
        {
            Intent i=new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(i);
        }


        if(v==b4)
        {
           File f=new File(Environment.getExternalStorageDirectory()+"/dayatdal.txt");
           f.delete();
            finish();

            Intent i=new Intent(getApplicationContext(),LoginAuth.class);
            startActivity(i);

        }

    }
}
