package de.sample.wordgame.wordgameapplication.ui;

import android.os.Bundle;

import android.support.annotation.LayoutRes;

import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

import de.sample.wordgame.wordgameapplication.WordGameApplication;
import de.sample.wordgame.wordgameapplication.di.ActivityComponent;
import de.sample.wordgame.wordgameapplication.di.ApplicationComponent;
import de.sample.wordgame.wordgameapplication.di.module.ActivityModule;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        // handling injection
        activityComponent = createActivityComponent();
        handleInjection(activityComponent);

        super.onCreate(savedInstanceState);

        setContentView(layoutToInflate());

        // init butterknife
        ButterKnife.bind(this);
    }

    private ActivityComponent createActivityComponent() {
        ApplicationComponent applicationComponent = ((WordGameApplication) getApplication()).applicationComponent;
        ActivityModule activityInstanceModule = new ActivityModule(this);
        return applicationComponent.plus(activityInstanceModule);
    }

    protected abstract void handleInjection(final ActivityComponent component);

    @LayoutRes
    protected abstract int layoutToInflate();
}
