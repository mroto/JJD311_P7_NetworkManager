package com.mroto.jjd311_p7_networkmanager;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by A5Alumno on 28/11/2016.
 */

public class MyParsingThread extends AsyncTask<String, Void, String> {

    private final static String TAG = MyParsingThread.class.getSimpleName();
    private Context threadContext;

    public MyParsingThread(Context context){
        this.threadContext=context;
    }

    @Override
    protected @Nullable String doInBackground(String... args) {
        try {
            Log.e(MyParsingThread.TAG,"Trying to parse url:"+args[0]);
            URL url = new URL(args[0]);

            HttpURLConnection myConnection = null;
            Log.e(MyParsingThread.TAG,"Trying to connect...");
            myConnection = (HttpURLConnection) url.openConnection();
            myConnection.setRequestMethod("GET");
            myConnection.setDoInput(true);
            myConnection.connect();

            int respCode= myConnection.getResponseCode();
            Log.e(MyParsingThread.TAG,"respCode="+respCode);

            if(respCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = myConnection.getInputStream();
                XmlPullParser xmlPullParser = Xml.newPullParser();
                xmlPullParser.setInput(inputStream, null);

                StringBuilder stringBuilder = new StringBuilder("");
                int event = xmlPullParser.nextTag();
                while(xmlPullParser.getEventType() != xmlPullParser.END_DOCUMENT){
                    switch(event){
                        case XmlPullParser.START_TAG:
                            if(xmlPullParser.getName().equals("item")){
                                xmlPullParser.nextTag();
                                xmlPullParser.next();
                                stringBuilder.append(xmlPullParser.getText());
                                stringBuilder.append("\n");
                            }
                            break;
                    }
                }
                inputStream.close();
                Log.e(MyParsingThread.TAG,"StringBuilder============================================================");
                Log.e(MyParsingThread.TAG,stringBuilder.toString());
                Log.e(MyParsingThread.TAG,"=========================================================================");
                return stringBuilder.toString();
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(MyParsingThread.TAG,"MalformedURLException: "+e.getMessage());
        }catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.e(MyParsingThread.TAG,"XmlPullParserException: "+e.getMessage());
        }catch (IOException e) {
            e.printStackTrace();
            Log.e(MyParsingThread.TAG,"IOException: "+e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String resultString) {
        super.onPostExecute(resultString);
        if(resultString == null){
            Toast.makeText(this.threadContext, "Can't read the feed!!!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this.threadContext, resultString, Toast.LENGTH_LONG).show();
        }
    }
}
