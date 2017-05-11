package de.sample.wordgame.wordgameapplication;

import android.app.Application;

import de.sample.wordgame.wordgameapplication.di.ApplicationComponent;
import de.sample.wordgame.wordgameapplication.di.DaggerApplicationComponent;
import de.sample.wordgame.wordgameapplication.di.module.ApplicationModule;
import de.sample.wordgame.wordgameapplication.di.module.DataSourceModule;

public class WordGameApplication extends Application {

    // Dagger injection
    public ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Creating component
        applicationComponent = createComponent();

        // Injecting application itself
        applicationComponent.injectApplication(this);
    }

    // Building DaggerApplicationComponent generated within the Dagger2 lib.
    private ApplicationComponent createComponent() {
        return
            DaggerApplicationComponent.builder()                                      //
                                      .applicationModule(new ApplicationModule(this)) //
                                      .dataSourceModule(new DataSourceModule())       //
                                      .build();
    }
}
