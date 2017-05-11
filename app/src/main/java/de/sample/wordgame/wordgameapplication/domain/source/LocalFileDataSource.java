package de.sample.wordgame.wordgameapplication.domain.source;

import rx.Single;

public interface LocalFileDataSource {
    Single<String> getLocalJSON();
}
