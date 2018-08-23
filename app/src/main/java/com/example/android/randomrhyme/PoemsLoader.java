package com.example.android.randomrhyme;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.randomrhyme.model.Poem;
import com.example.android.randomrhyme.networking.NetworkUtils;
import com.example.android.randomrhyme.networking.XmlToJsonConverter;

import java.io.IOException;
import java.net.URL;

public class PoemsLoader extends AsyncTaskLoader<Poem> {

    private URL requestedUrl = NetworkUtils.buildUrl();
    private Poem receivedPoem;

    public PoemsLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (receivedPoem != null) {
            deliverResult(receivedPoem);
        } else {
            forceLoad();
        }

    }

    @Override
    public Poem loadInBackground() {

        try {
            String poemXML = NetworkUtils.getResponseFromHttpUrl(requestedUrl);
            String poemJSON = XmlToJsonConverter.convertXmlToJson(poemXML);
            return XmlToJsonConverter.extractFeatureFromJson(poemJSON);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deliverResult(Poem data) {
        receivedPoem = data;
        super.deliverResult(data);
    }
}




