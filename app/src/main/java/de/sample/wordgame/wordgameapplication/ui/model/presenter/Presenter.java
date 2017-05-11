package de.sample.wordgame.wordgameapplication.ui.model.presenter;

interface Presenter<V> {
    void init(V view);

    void destroy();
}
