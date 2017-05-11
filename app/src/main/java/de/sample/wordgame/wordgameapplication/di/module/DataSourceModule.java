package de.sample.wordgame.wordgameapplication.di.module;

import javax.inject.Singleton;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

import de.sample.wordgame.wordgameapplication.di.qualifier.AppScope;
import de.sample.wordgame.wordgameapplication.domain.parser.LocalGSonParser;
import de.sample.wordgame.wordgameapplication.domain.parser.WordFileParser;
import de.sample.wordgame.wordgameapplication.domain.source.LocalFileDataSource;
import de.sample.wordgame.wordgameapplication.domain.source.WordDataSource;
import de.sample.wordgame.wordgameapplication.util.Constants;

@Module
public class DataSourceModule {

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) //
                   .setDateFormat(Constants.DATE_TIME_FORMAT)                           //
                   .excludeFieldsWithoutExposeAnnotation();

        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    LocalFileDataSource provideTransitionsLocalFileDataSource(@AppScope final Application context) {
        return new WordDataSource(context, Constants.WORDS_JSON_FILE_PATH);
    }

    @Provides
    @Singleton
    LocalGSonParser provideLocalGSonParser(final Gson gson, final LocalFileDataSource wordDataSource) {
        return new WordFileParser(gson, wordDataSource);
    }
}
