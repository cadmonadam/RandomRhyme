package com.example.android.randomrhyme.networking;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.randomrhyme.model.Poem;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class XmlToJsonConverter {

    private static final String KEY_ROOT = "versek";
    private static final String KEY_PARENT = "vers";
    private static final String KEY_TITLE = "cim";
    private static final String KEY_POEM = "versszoveg";
    private static final String KEY_AUTHOR = "szerzo";

    public static String convertXmlToJson(String string) {

        XmlToJson xmlToJson = new XmlToJson.Builder(string).build();

        return xmlToJson.toString();
    }

    public static Poem extractFeatureFromJson(String poemJSON) {

        String poemTitle = "";
        String poemBodyText = "";
        String author = "";
        Poem poem = null;

        if (TextUtils.isEmpty(poemJSON)) {
            return null;
        }
        try {

            JSONObject rawJsonObject = new JSONObject(poemJSON);

            JSONObject root = rawJsonObject.getJSONObject(KEY_ROOT);

            JSONObject parent = root.getJSONObject(KEY_PARENT);

            if (parent.has(KEY_TITLE)) {
                poemTitle = parent.optString(KEY_TITLE);
            }

            if (parent.has(KEY_POEM)) {
                poemBodyText = parent.optString(KEY_POEM);
            }

            if (parent.has(KEY_AUTHOR)) {
                author = parent.optString(KEY_AUTHOR);
            }


            poem = new Poem(poemTitle, poemBodyText, author);

        } catch (JSONException e) {

            Log.e("XmlToJsonConverter", "A problem occured when parsing the JSON results", e);
        }

        return poem;
    }
}

