/*
* Copyright (C) 2013 The OmniROM Project
* Copyright (C) 2020 The Android Ice Cold Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package com.aicp.device;

import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Utils {

    private static final boolean DEBUG = false;
    private static final String TAG = "Utils";

    /**
     * Write a string value to the specified file.
     * @param filename      The filename
     * @param value         The value
     */
    public static void writeValue(String filename, String value) {
        if (filename == null) {
            return;
        }
        if (DEBUG) Log.d(TAG, "writeValue filename / value:"+filename+" / "+value);
        try {
            FileOutputStream fos = new FileOutputStream(new File(filename));
            fos.write(value.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write a string value to the specified sysfs file.
     * @param filename      The filename
     * @param value         The value
     */
    public static void writeValueSimple(String filename, String value) {
        if (filename == null) {
            return;
        }
        if (DEBUG) Log.d(TAG, "writeValueSimple filename / value:"+filename+" / "+value);
        try {
            FileOutputStream fos = new FileOutputStream(new File(filename));
            String valueSimple = value + "\n";
            fos.write(valueSimple.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the specified file exists.
     * @param filename      The filename
     * @return              Whether the file exists or not
     */
    public static boolean fileExists(String filename) {
        if (filename == null) {
            return false;
        }
        return new File(filename).exists();
    }

    public static boolean fileWritable(String filename) {
        return fileExists(filename) && new File(filename).canWrite();
    }

    public static String readLine(String filename) {
        if (filename == null) {
            return null;
        }
        BufferedReader br = null;
        String line = null;
        try {
            br = new BufferedReader(new FileReader(filename), 1024);
            line = br.readLine();
        } catch (IOException e) {
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return line;
    }

    /*
     * we need this little helper method, because HTC decided in their infinite wisdom
     * to chose this format in corresponding sysfs file:
     * [VIB] voltage input:2500mV
    */
    public static String declutterVibratorValue(String HtcOutput) {
        String declutteredValue = null;
        String[] seperateVolts = HtcOutput.split(":", 2);
        int declutterLength = seperateVolts[1].length();
        declutteredValue = seperateVolts[1].substring(0,declutterLength-2);
        Log.i(TAG,"decluttered vibrator value: "+declutteredValue);
        return declutteredValue;
    }

    public static boolean getFileValueAsBoolean(String filename, boolean defValue) {
        String fileValue = readLine(filename);
        if(fileValue!=null){
            return (fileValue.equals("0")?false:true);
        }
        return defValue;
    }

    public static String getFileValue(String filename, String defValue) {
        String fileValue = readLine(filename);
        if(fileValue!=null){
	    return declutterVibratorValue(fileValue);
        }
        if (DEBUG) Log.d(TAG,"getFileValue file / value:"+filename+" / "+defValue);
        return defValue;
    }
}