package de.sample.wordgame.wordgameapplication.ui.model.view;

import java.util.List;

import de.sample.wordgame.wordgameapplication.ui.data.model.result.ResultStats;

public interface ResultsView {
    void showResults(List<ResultStats> resultStats);

    void showNoResultsMessage();
}
