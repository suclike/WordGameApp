package de.sample.wordgame.wordgameapplication.ui.model.view;

import java.util.List;

import android.support.annotation.StringRes;

import de.sample.wordgame.wordgameapplication.domain.model.WordsWithTranslations;

public interface WordGame {
    void initGameRound(List<WordsWithTranslations> words, boolean shouldStartNextRound);

    void stopGameWithReason(@StringRes final int reason);

    void setupTimer(final int minutes, final int seconds);

    void stopTimer();

    void stopFallingAnimation();

    void showSnackbar(final String message);

    List<WordsWithTranslations> getCurrentListOfWords();
}
