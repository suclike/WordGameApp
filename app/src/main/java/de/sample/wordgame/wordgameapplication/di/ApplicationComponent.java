package de.sample.wordgame.wordgameapplication.di;

import javax.inject.Singleton;

import dagger.Component;

import de.sample.wordgame.wordgameapplication.WordGameApplication;
import de.sample.wordgame.wordgameapplication.di.module.ActivityModule;
import de.sample.wordgame.wordgameapplication.di.module.ApplicationModule;
import de.sample.wordgame.wordgameapplication.di.module.DataSourceModule;
import de.sample.wordgame.wordgameapplication.di.qualifier.AppScope;

@AppScope
@Singleton
@Component(
    modules = {
        ApplicationModule.class, //
        DataSourceModule.class
    }
)
public interface ApplicationComponent {

    ActivityComponent plus(ActivityModule activityModule);

    void injectApplication(WordGameApplication application);
}
