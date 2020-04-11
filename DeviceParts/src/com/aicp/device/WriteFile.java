/*
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

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceManager;
import android.util.Log;
import java.io.File;
import android.os.Environment;

import com.aicp.device.DeviceSettings;

public class WriteFile implements OnPreferenceChangeListener {

    private static final String TAG = "Utils";
    private static final String BASEDIR = "/sdcard/DeviceParts";
    private static final String FILE = BASEDIR + "/test.txt";
    public static final String SETTINGS_KEY = DeviceSettings.KEY_SETTINGS_PREFIX + DeviceSettings.KEY_WRITEFILE;

    private Context mContext;

    public WriteFile(Context context) {
        mContext = context;
    }

    public static void createBaseDir() {
        File f = new File(BASEDIR);
        if(!f.exists()) {
            f.mkdir();
            Log.i(TAG,"dir created: " + f);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Boolean enabled = (Boolean) newValue;
        Settings.System.putInt(mContext.getContentResolver(), SETTINGS_KEY, enabled ? 1 : 0);
        if (enabled) {
            createBaseDir();
            Utils.writeValueSimple(FILE, "hallo David");
	}
        return true;
    }
}
