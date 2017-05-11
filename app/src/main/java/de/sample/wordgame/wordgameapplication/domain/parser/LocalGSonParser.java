package de.sample.wordgame.wordgameapplication.domain.parser;

import java.util.List;

import de.sample.wordgame.wordgameapplication.domain.model.Words;

import rx.Single;

public interface LocalGSonParser {
    Single<List<Words>> getFromJson();
}
