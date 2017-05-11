package de.sample.wordgame.wordgameapplication.domain.parser;

import java.lang.reflect.Type;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.sample.wordgame.wordgameapplication.domain.model.Words;
import de.sample.wordgame.wordgameapplication.domain.source.LocalFileDataSource;

import rx.Single;

import rx.functions.Func1;

public class WordFileParser implements LocalGSonParser {

    private Gson gson;
    private LocalFileDataSource wordDataSource;

    public WordFileParser(final Gson gson, final LocalFileDataSource wordDataSource) {
        this.gson = gson;
        this.wordDataSource = wordDataSource;
    }

    @Override
    public Single<List<Words>> getFromJson() {
        return wordDataSource.getLocalJSON().map(new Func1<String, List<Words>>() {
                    @Override
                    public List<Words> call(final String s) {
                        final Type typeOfListOfIdea = new TypeToken<List<Words>>() { }.getType();

                        return gson.fromJson(s, typeOfListOfIdea);
                    }
                });
    }
}
