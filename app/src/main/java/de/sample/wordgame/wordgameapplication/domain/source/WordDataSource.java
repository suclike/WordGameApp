package de.sample.wordgame.wordgameapplication.domain.source;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import android.content.res.AssetManager;

import android.net.Uri;

import rx.Single;

import rx.functions.Func0;

public class WordDataSource implements LocalFileDataSource {

    private Context context;
    private String assertFileName;

    public WordDataSource(final Context context, final String assertFileName) {
        this.context = context;
        this.assertFileName = assertFileName;
    }

    @Override
    public Single<String> getLocalJSON() {
        return Single.fromCallable(new Func0<String>() {
                    @Override
                    public String call() {
                        String json = null;
                        try {
                            final Uri uri = Uri.parse(assertFileName);
                            InputStream is = context.getAssets().open(uri.getPath(), AssetManager.ACCESS_STREAMING);
                            final int size = is.available();
                            final byte[] buffer = new byte[size];
                            is.read(buffer);
                            is.close();
                            json = new String(buffer, "UTF-8");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        return json;
                    }
                });
    }
}
