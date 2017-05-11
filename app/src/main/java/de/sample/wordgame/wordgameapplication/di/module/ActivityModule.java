package de.sample.wordgame.wordgameapplication.di.module;

import javax.inject.Singleton;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.app.Application;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;

import de.sample.wordgame.wordgameapplication.BuildConfig;
import de.sample.wordgame.wordgameapplication.di.qualifier.ActivityInstanceScope;
import de.sample.wordgame.wordgameapplication.di.qualifier.AppScope;
import de.sample.wordgame.wordgameapplication.util.Constants;

import rx.subscriptions.CompositeSubscription;

@Module
public class ActivityModule {

    private static final String SHARED_PREFERENCE_FILE_NAME = BuildConfig.APPLICATION_ID + "shared_preference";

    private final Activity activity;

    public ActivityModule(final Activity activity) {
        this.activity = activity;
    }

    @Provides
    @Singleton
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityInstanceScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @ActivityInstanceScope
    SharedPreferences provideSharedPreferences(@AppScope final Application app) {
        return app.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }
}
