package de.sample.wordgame.wordgameapplication.di;

import javax.inject.Singleton;

import dagger.Subcomponent;

import de.sample.wordgame.wordgameapplication.di.module.ActivityModule;
import de.sample.wordgame.wordgameapplication.di.qualifier.ActivityInstanceScope;
import de.sample.wordgame.wordgameapplication.ui.ResultsActivity;
import de.sample.wordgame.wordgameapplication.ui.WordGameActivity;

@Singleton
@ActivityInstanceScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {

    void inject(ResultsActivity activity);

    void inject(WordGameActivity activity);
}
