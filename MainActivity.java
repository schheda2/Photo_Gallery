package com.example.group25_inclass05;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//    Str
    CharSequence[] cs = {"UNCC", "Android", "Winter", "Aurora", "Wonders"};
    TextView keyword;
    static String urlData;
    static  String[] uncc;
    int count=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        keyword = (TextView) findViewById(R.id.keyword);


        findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count>0)
                {
                    count--;
                    new GetData().execute("http://dev.theappsdr.com/apis/photos/index.php?keyword=uncc");

                }
            }
        });

        findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    count++;
                    new GetData().execute("http://dev.theappsdr.com/apis/photos/index.php?keyword=uncc");


            }
        });

        findViewById(R.id.goButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectedOnline()) {


                    keyword.setEnabled(false);


                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Choose a keyword");

                    alert.setItems(cs, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            keyword.setText(cs[i]);


                            Toast.makeText(MainActivity.this, cs[i], Toast.LENGTH_SHORT).show();
                        }
                    });

                    alert.show();
                     new GetData().execute("http://dev.theappsdr.com/apis/photos/index.php?keyword=uncc");
                    //Toast.makeText(MainActivity.this, "Inmain"+uncc[1], Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Not connected", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean isConnectedOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public class GetImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {


            URL url = null;

            try {
                url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                Bitmap image = BitmapFactory.decodeStream(con.getInputStream());


                return image;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                ImageView iv = (ImageView) findViewById(R.id.imageView);
                iv.setImageBitmap(bitmap);
            } else {
                Log.d("demo", "Null data");
            }
        }

    }


    private class GetData extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {


            URL url = null;
            BufferedReader reader = null;
            InputStream in = null;
            try {
                url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                in = con.getInputStream();


                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");


                }
                urlData=sb.toString();
                return urlData;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }

            }
            //Toast.makeText(MainActivity.this,"check",Toast.LENGTH_LONG).show();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
               // ArrayList<String> uncc = new ArrayList<>();
                Toast.makeText(MainActivity.this, "InONPOST"+Integer.toString(count), Toast.LENGTH_SHORT).show();
                Image(s,count);

               // new GetImage().execute("http://cdn1.theodysseyonline.com/files/2016/01/09/635879466452203457-1500514842_UNC-Charlotte-043.jpg");
                Log.d("demo", s);

            } else {
                Log.d("demo", "Null data");
            }
        }
    }

    public void Image(String s,int index)
    {
        uncc = s.split(";");
        new GetImage().execute(uncc[index]);
        Toast.makeText(MainActivity.this,uncc[1], Toast.LENGTH_SHORT).show();

    }

}