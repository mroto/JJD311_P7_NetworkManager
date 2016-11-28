package com.mroto.jjd311_p7_networkmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String URL_FEED = "https://www.theguardian.com/international/rss";
    private static final String URL_IMAGE = "https://i.guim.co.uk/img/media/e3f32053cf2f7cae3d3f2537da9f2386d03ac629/54_0_633_380/master/633.jpg";

    private ImageView imgView;
    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_img_download).setOnClickListener(this);
        findViewById(R.id.btn_read_feed).setOnClickListener(this);
        this.imgView = (ImageView)findViewById(R.id.image_view_main);
        this.txtView = (TextView)findViewById(R.id.text_view_main);
    }

    @Override
    public void onClick(View view) {
        ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        if(netInfo!=null && netInfo.isConnected()) {
            //WE HAVE CONNECTION:
            int id = view.getId();
            if (id == R.id.btn_img_download) {
                this.downloadImage();
            } else if (id == R.id.btn_read_feed) {
                this.readFeed(MainActivity.URL_FEED);
            }
        }else{
            Toast.makeText(this,"Connection error!",Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap downloadImage(){
        //TO USE PICASSO, ADD THE LINE: compile 'com.squareup.picasso:picasso:2.5.2' INTO build.gradle Module:app
        Picasso.with(this).load(MainActivity.URL_IMAGE).into(this.imgView);
        return null;
    }

    private void readFeed(String feed){
        new MyParsingThread(this).execute(feed);
    }
}
