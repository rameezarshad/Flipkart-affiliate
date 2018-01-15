package com.flipkart.android.register;

/*
import com.opencsv.CSVReader;
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VectoR on 13-09-2017.
 */

public class DeviceModels {
    InputStream inputStream;

    public DeviceModels(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public List<String[]> read(){
        List<String[]> resultList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            reader.readLine();
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.trim().replace(", ","").split(",");
                resultList.add(row);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return resultList;
    }

/*    public List<String[]> readFromCsv(){

        String next[] = {};
        List<String[]> list = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));//Specify asset file name
            //in open();
            reader.readNext();
            for(;;) {
                next = reader.readNext();

                if(next != null) {
                    if(next.length >1)
                         list.add(next);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }*/

}
