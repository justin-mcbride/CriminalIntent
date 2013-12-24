package com.justin.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Justin on 12/22/13.
 */
public class Photo {
    private static final String JSON_FILENAME = "filename";

    private String mFilename;

    // Create a photo representing an exisiting file on disk
    public Photo(String fn) {
        mFilename = fn;
    }

    public Photo(JSONObject json) throws JSONException {
        mFilename = json.getString(JSON_FILENAME);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        return json;
    }

    public String getFilename() {
        return mFilename;
    }
}
