package com.example.himanshu.passwordgenerator;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    CharSequence[] PasswordArray={"","","","","","","","","",""};
    CharSequence[] EmptyArray={"","","","","","","","","",""};

    CharSequence[] AsyncPasswordArray={"","","","","","","","","",""};
    CharSequence[] AsyncEmptyArray={"","","","","","","","","",""};

    int j=0;
    AlertDialog alert;
    ExecutorService threadPool;
    SeekBar seekBar_count, seekBar_length;
    TextView textView_count, textView_length, password_result;
   public int indicator_count=1,indicator_length=8;
    Button btn_thread,btn_async;
    int minimum_count=1, minimum_length=8;
    Handler handler;
    ProgressDialog progressDialog;
    int flag =0;
    Bundle data;
    AlertDialog.Builder builder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        threadPool= Executors.newFixedThreadPool(2);



        seekBar_count=(SeekBar)findViewById(R.id.seekBar_count);
        seekBar_count.setProgress(1);

        seekBar_length=(SeekBar)findViewById(R.id.seekBar_length);
        seekBar_length.setProgress(8);

        textView_count=(TextView)findViewById(R.id.textView_countResult);
        textView_count.setText(String.valueOf(minimum_count));
       //indicator_count=Integer.parseInt(textView_count.getText().toString());

        textView_length=(TextView)findViewById(R.id.textView_lengthResult);
        textView_length.setText(String.valueOf(minimum_length));

        password_result=(TextView)findViewById(R.id.textView_PasswordResult);


        btn_thread=(Button)findViewById(R.id.button_thread);
        btn_async=(Button)findViewById(R.id.button_Async);

        builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Pick a password");

             //  builder.setMessage("Are you sure?");
        //Log.d("demo",""+items[1]);


       builder.setItems(PasswordArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(PasswordArray[which]=="")
                {
                    Toast.makeText(getApplicationContext(),"No Password was selected",Toast.LENGTH_LONG).show();

                }

                    password_result.setText(PasswordArray[which]);
                    for(int k=0;k<PasswordArray.length;k++)
                    {
                        PasswordArray[k]="";
                    }
                }
        });
Log.d("demo",builder.toString());
alert = builder.create();

Log.d("demo",alert.toString());


        btn_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("demo", "button clicked");
                handler = new Handler(new Handler.Callback() {
                    @Override


                    public boolean handleMessage(Message message) {

                        switch (message.what) {
                            case DoWork.STATUS_START:
                                Log.d("demo", "thread started");
                                progressDialog = new ProgressDialog(MainActivity.this);
                                progressDialog.setMessage("Computing progress");
                                progressDialog.setMax(indicator_count);
                                progressDialog.setCancelable(false);
                                progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
                                progressDialog.show();


                                break;
                            case DoWork.STATUS_STEP:
                                Log.d("demo", "thread resume");
                                Toast.makeText(getApplicationContext(),message.getData().getString("PASSWORD"),Toast.LENGTH_LONG).show();
                                break;

                            case DoWork.STATUS_DONE:

                                Log.d("demo",alert.toString());
                                alert.show();

                                progressDialog.dismiss();

                        }

                        return false;

                    }


                });
                threadPool.execute(new DoWork());
                //progressDialog.setProgress(i);

            }
        });


        btn_async.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DoWorkAsync().execute();

            }
        });








        seekBar_count.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(progress <= minimum_count)
                {

                    indicator_count=minimum_count;
                    textView_count.setText(String.valueOf(indicator_count));

                }

                else
                {

                    textView_count.setText(String.valueOf(progress));
                    indicator_count=Integer.parseInt(textView_count.getText().toString());

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_length.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress <= minimum_length)
                {

                    indicator_length=minimum_length;
                    textView_length.setText(String.valueOf(indicator_length));

                }

                else
                {
                    indicator_length=progress;
                    textView_length.setText(String.valueOf(indicator_length));

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }






    class DoWork implements Runnable{

        public static final int STATUS_START = 0;
        public static final int STATUS_STEP = 1;
        public static final int STATUS_DONE = 2;

        @Override
        public void run() {

            Log.d("demo","in run method");
            Message message = new Message();
            message.what = STATUS_START;
           handler.sendMessage(message);
            for(int i=0;i<indicator_count;i++) {
                message = new Message();
                message.what = STATUS_STEP;
                flag = flag + 1;
                String password = Util.getPassword(indicator_length);

                //message.obj = i + 1;


                data = new Bundle();
                data.putInt("PROGRESS", flag);
                data.putString("PASSWORD", password);
                progressDialog.setProgress(flag);
                message.setData(data);
                PasswordArray[i]= password;
                handler.sendMessage(message);
            }
            //}
            message = new Message();
            message.what = STATUS_DONE;
            handler.sendMessage(message);
        }
    }


    class DoWorkAsync extends AsyncTask<Void, Integer, String>

    {


        @Override
        protected String doInBackground(Void... params) {
            for(int i=0;i<indicator_count;i++) {
                String password = Util.getPassword(indicator_length);
                PasswordArray[i]=password;
                publishProgress(i+1);

            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(indicator_count);
            progressDialog.setMessage("Computing progress");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            builder.setItems(PasswordArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    password_result.setText(PasswordArray[which]);

                    if(PasswordArray[which]=="")
                    {
                        Toast.makeText(getApplicationContext(),"No Password was selected",Toast.LENGTH_LONG).show();

                    }
                    for(int k=0;k<PasswordArray.length;k++)
                    {
                        PasswordArray[k]="";
                    }
                }
            });
            final AlertDialog alertAsync = builder.create();
            alertAsync.show();


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);



        }
    }






}
