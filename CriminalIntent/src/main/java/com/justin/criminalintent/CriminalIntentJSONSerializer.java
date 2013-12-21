package com.justin.criminalintent;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Justin on 11/26/13.
 */
public class CriminalIntentJSONSerializer {
    private Context mContext;
    private String mFilename;
    private File mFile;

    private boolean mExternalAvailable;
    private boolean mExternalWriteable;


    private void testAccess() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can READ WRITE
            mExternalAvailable = mExternalWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can READ
            mExternalAvailable = true;
            mExternalWriteable = false;
        } else {
            // can't READ OR WRITE
            mExternalWriteable = mExternalAvailable = false;
        }
    }

    private String getFilePath(String f) {
        testAccess();
        if (mExternalWriteable && mExternalAvailable) {
            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/criminal");
            dir.mkdir();
            mFile = new File(dir, f);
            Log.d("Criminal", "external available: " + mFile.getAbsolutePath());
            return mFile.getAbsolutePath();
        }
        else {
            Log.d("Criminal", "external NOT available");
            return f;
        }
    }

    public CriminalIntentJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = getFilePath(f);

    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for (Crime c : crimes) {
            array.put(c.toJSON());
        }

        Writer writer = null;
        try {
            if (mExternalWriteable && mExternalAvailable) {
                writer = new FileWriter(mFile);
            } else {
                OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
                writer = new OutputStreamWriter(out);
            }
            writer.write(array.toString());
        } finally {
            if (writer != null) writer.close();
        }
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;

        try {
            if (mExternalAvailable && mExternalWriteable) {
                FileReader fis = new FileReader(mFile);
                reader = new BufferedReader(fis);
            } else {
                InputStream in = mContext.openFileInput(mFilename);
                reader = new BufferedReader(new InputStreamReader(in));
            }

            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            Log.d("Criminal", "external file not found");
        }
        finally {
            if (reader != null) {
                reader.close();
            }
        }

        return crimes;
    }
}
