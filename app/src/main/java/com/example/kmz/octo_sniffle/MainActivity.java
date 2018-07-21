package com.example.kmz.octo_sniffle;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Timer;
import java.util.Random;
import java.util.TimerTask;
import org.apache.commons.lang3.time.StopWatch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Timer time = new Timer();

    private StopWatch sw = new StopWatch();

    Random rand = new Random();

    boolean isStarted = false;
    boolean timerHasStopped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onStartClick(v, button);
            }
        });
    }

    public void onStartClick(final View v,final Button btn) {

        if(!isStarted) {
            //Toast.makeText(MainActivity.this, "Spiel startet!",Toast.LENGTH_SHORT).show();
            btn.setText("Drücke bei Rot");
            btn.setBackgroundColor(Color.YELLOW);

            isStarted = true;
            int waitTime = rand.nextInt(10 - 5 + 1) + 5;
            time = new Timer();
            sw = new StopWatch();



            timerHasStopped = false;

            time.schedule(new TimerTask() {
                @Override
                public void run() {
                    v.post(new Runnable() {
                        @Override
                        public void run() {
                            timerHasStopped = true;
                            btn.setBackgroundColor(Color.RED);
                            sw.start();
                        }
                    });
                }
            }, waitTime * 1000);

            return;
        }
        if(timerHasStopped) {
            sw.stop();
            //Toast.makeText(MainActivity.this, "Deine Reaktionszeit beträgt " + sw.getTime() + " ms", Toast.LENGTH_LONG).show();
            btn.setBackgroundColor(Color.WHITE);
            TextView textView = findViewById(R.id.textView);
            textView.setText("Letzte Zeit: " + sw.getTime() + " ms");

            isStarted = false;

            //openDialog((int)sw.getTime()).show();



        }
        else
        {
            time.cancel();
            //Toast.makeText(MainActivity.this, "Der Button war leider noch nicht rot", Toast.LENGTH_LONG).show();
            isStarted = false;
        }
        btn.setText(R.string.mainButtonText);
        btn.setBackgroundColor(Color.WHITE);
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

    public Dialog openDialog(int time) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        final View inf = inflater.inflate(R.layout.dialog_set_name, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_set_name, null))
                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Leaderboard.addRecord("Charles", String.valueOf(sw.getTime()), getApplicationContext());

                        EditText edit=(EditText)inf.findViewById(R.id.username);
                        String text=edit.getText().toString();


                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });



        return builder.create();
    }

    public String getBestTime(){
        Record[] recs = Leaderboard.readRecord(getApplicationContext());

        Arrays.sort(recs,new RecordComparator());

        return recs[0].time;
    }
}