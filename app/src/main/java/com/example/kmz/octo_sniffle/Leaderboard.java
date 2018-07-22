package com.example.kmz.octo_sniffle;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Leaderboard {

    static String filename = "statistics";

    static void addRecord(String name, String time, Context context) {
        File data = new File(context.getFilesDir(), filename);
        try {
            data.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(time )

        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write((name.replace(" ", "_") + " " + time + "\n").getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static Integer[] readRecord(Context context) {
        File data = new File(context.getFilesDir(), filename);


        if(!data.exists()){
            return null;
        }
        List<Integer> lines = new ArrayList<>();

        try {
            FileInputStream fis = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while((line = reader.readLine()) != null){
                String[] splitted = line.split(" ");
                if(splitted.length == 2){
                    try{
                        lines.add(Integer.parseInt(splitted[1]));
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Integer[] recordArr = new Integer[lines.size()];
        recordArr = lines.toArray(recordArr);

        return recordArr;


    }


    static int getBestTime(Context context){
        Integer[] recs = Leaderboard.readRecord(context);

        if(recs == null){
            return null;
        }

        Arrays.sort(recs);


        return recs[0];
    }
}
