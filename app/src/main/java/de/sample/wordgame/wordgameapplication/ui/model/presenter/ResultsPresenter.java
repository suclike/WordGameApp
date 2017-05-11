package de.sample.wordgame.wordgameapplication.ui.model.presenter;

import java.lang.reflect.Type;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.SharedPreferences;

import android.support.annotation.ColorRes;
import android.support.annotation.VisibleForTesting;

import de.sample.wordgame.wordgameapplication.R;
import de.sample.wordgame.wordgameapplication.ui.data.model.result.GameStats;
import de.sample.wordgame.wordgameapplication.ui.data.model.result.ResultStats;
import de.sample.wordgame.wordgameapplication.ui.model.view.ResultsView;

import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.Subscription;

import rx.android.schedulers.AndroidSchedulers;

import rx.functions.Func1;

import rx.schedulers.Schedulers;

public class ResultsPresenter extends BasePresenter<ResultsView> {

    private static final String GAME_RESULT_SAVED_LIST = "GAME_RESULT";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    private static final Type type = new TypeToken<LinkedList<GameStats>>() { }.getType();

    @Inject
    ResultsPresenter(final Gson gson, final SharedPreferences sharedPreferences) {
        this.gson = gson;
        this.sharedPreferences = sharedPreferences;
    }

    @VisibleForTesting
    protected Single<List<GameStats>> getSavedResults() {
        final String jsonList = sharedPreferences.getString(GAME_RESULT_SAVED_LIST, "");

        if (jsonList.isEmpty()) {
            return Single.just(Collections.<GameStats>emptyList());
        }

        return Single.just(((List<GameStats>) gson.fromJson(jsonList, type)));
    }

    public Subscription getTransformedGameResults() {
        return getSavedResults().toObservable() //
                                .flatMap(new Func1<List<GameStats>, Observable<GameStats>>() {
                                        @Override
                                        public Observable<GameStats> call(final List<GameStats> gameStats) {
                                            return Observable.from(gameStats);
                                        }
                                    })          //
                                .map(new Func1<GameStats, ResultStats>() {
                                        @Override
                                        public ResultStats call(final GameStats gameStats) {
                                            final String passed = String.format("Passed %s ", gameStats.passed);
                                            final String failed = String.format("Failed %s ", gameStats.failed);

                                            @ColorRes
                                            final int color = gameStats.isComplete ? R.color.cell_green_color
                                                                                   : android.R.color.darker_gray;

                                            return new ResultStats(passed, failed, color);
                                        }
                                    })                                     //
                                .toList()                                  //
                                .observeOn(AndroidSchedulers.mainThread()) //
                                .subscribeOn(Schedulers.io())              //
                                .subscribe(new Subscriber<List<ResultStats>>() {
                                        @Override
                                        public void onCompleted() { }

                                        @Override
                                        public void onError(final Throwable e) {
                                            if (getAttachedView() != null) {
                                                getAttachedView().showNoResultsMessage();
                                            }
                                        }

                                        @Override
                                        public void onNext(final List<ResultStats> resultStats) {
                                            if (getAttachedView() != null) {
                                                if (resultStats.isEmpty()) {
                                                    getAttachedView().showNoResultsMessage();
                                                } else {
                                                    getAttachedView().showResults(resultStats);
                                                }
                                            }
                                        }
                                    });
    }
}
