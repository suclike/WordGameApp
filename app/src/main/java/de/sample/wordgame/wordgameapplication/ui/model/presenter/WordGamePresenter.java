package de.sample.wordgame.wordgameapplication.ui.model.presenter;

import static de.sample.wordgame.wordgameapplication.ui.data.model.TestResultState.COMPLETE;
import static de.sample.wordgame.wordgameapplication.ui.data.model.TestResultState.FAILED;
import static de.sample.wordgame.wordgameapplication.ui.data.model.TestResultState.PASSED;

import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.SharedPreferences;

import android.os.CountDownTimer;

import android.support.annotation.StringRes;

import de.sample.wordgame.wordgameapplication.R;
import de.sample.wordgame.wordgameapplication.domain.model.Words;
import de.sample.wordgame.wordgameapplication.domain.model.WordsWithTranslations;
import de.sample.wordgame.wordgameapplication.domain.parser.LocalGSonParser;
import de.sample.wordgame.wordgameapplication.ui.data.model.result.GameStats;
import de.sample.wordgame.wordgameapplication.ui.data.model.result.RawTestResult;
import de.sample.wordgame.wordgameapplication.ui.model.view.WordGame;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

import rx.android.schedulers.AndroidSchedulers;

import rx.functions.Action1;
import rx.functions.Func1;

import rx.observers.SafeSubscriber;

import rx.schedulers.Schedulers;

import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class WordGamePresenter extends BasePresenter<WordGame> {

    private static final String GAME_RESULT_SAVED_LIST = "GAME_RESULT";

    private LocalGSonParser localGSonParser;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private static final Type type = new TypeToken<List<GameStats>>() { }.getType();

    private CountDownTimer countDownTimer;

    private final Subject serializedSubject = new SerializedSubject<>(PublishSubject.create());
    private final List<RawTestResult> listOfFailedResults = new ArrayList<>();
    private final List<RawTestResult> listOfPassedResults = new ArrayList<>();
    private final List<RawTestResult> listOfCompleteResults = new ArrayList<>();
    private List<GameStats> finalResults = new ArrayList<>();

    @Inject
    WordGamePresenter(final LocalGSonParser localGSonParser, final Gson gson,
            final SharedPreferences sharedPreferences) {
        this.localGSonParser = localGSonParser;
        this.gson = gson;
        this.sharedPreferences = sharedPreferences;
        this.finalResults = getSavedResults();
    }

    public Subscription getWords() {
        return
            localGSonParser.getFromJson()  //
                           .toObservable() //
                           .flatMap(new Func1<List<Words>, Observable<Words>>() {
                                   @Override
                                   public Observable<Words> call(final List<Words> words) {
                                       return Observable.from(words);
                                   }
                               })          //
                           .filter(new Func1<Words, Boolean>() {
                                   @Override
                                   public Boolean call(final Words words) {
                                       return (words.textEng != null || !words.textEng.isEmpty())
                                               && (words.textSpa != null || !words.textSpa.isEmpty());
                                   }
                               })          //
                           .toList()       //
                           .map(new Func1<List<Words>, List<WordsWithTranslations>>() {
                                   @Override
                                   public List<WordsWithTranslations> call(final List<Words> words) {
                                       final List<WordsWithTranslations> wordsWithTranslations = new ArrayList<>(
                                               words.size());

                                       for (Words word : words) {
                                           final int random = (int) (Math.random() * words.size());
                                           wordsWithTranslations.add(
                                               new WordsWithTranslations(word.textSpa, word.textEng,
                                                   words.get(random).textEng));
                                       }

                                       return wordsWithTranslations;
                                   }
                               })                                     //
                           .observeOn(AndroidSchedulers.mainThread()) //
                           .subscribeOn(Schedulers.io())              //
                           .subscribe(new SafeSubscriber<>(new Subscriber<List<WordsWithTranslations>>() {
                                       @Override
                                       public void onCompleted() { }

                                       @Override
                                       public void onError(final Throwable e) {
                                           if (getAttachedView() != null) {
                                               getAttachedView().stopGameWithReason(
                                                   R.string.end_game_cannot_fetch_words);
                                           }
                                       }

                                       @Override
                                       public void onNext(final List<WordsWithTranslations> words) {
                                           Collections.shuffle(words);

                                           if (getAttachedView() != null) {
                                               getAttachedView().initGameRound(words, true);
                                           }
                                       }
                                   }));
    }

    public Subscription checkResults() {
        return observeEventOf(RawTestResult.class, new Action1<RawTestResult>() {
                    @Override
                    public void call(final RawTestResult testResult) {
                        switch (testResult.resultState) {

                            case PASSED :
                                listOfPassedResults.add(testResult);
                                continueGame("Well done! Good luck with next word...");

                                break;

                            case COMPLETE :
                                listOfCompleteResults.add(testResult);
                                stopGame(R.string.end_game_not_next_round);
                                saveResults();
                                break;

                            default :
                            case FAILED :
                                if (listOfFailedResults.size() < 2) {
                                    listOfFailedResults.add(testResult);
                                    continueGame("Uupps... Good luck with next word...");
                                } else {
                                    listOfFailedResults.add(testResult);
                                    if (getAttachedView() != null) {
                                        stopGame(R.string.end_game_attempts_limit_reached);
                                        saveResults();
                                    }
                                }

                                break;
                        }
                    }
                });
    }

    private void continueGame(final String message) {
        if (getAttachedView() != null) {
            getAttachedView().initGameRound(getAttachedView().getCurrentListOfWords(), true);
            getAttachedView().showSnackbar(message);
        }
    }

    private void stopGame(@StringRes final int reason) {
        if (getAttachedView() != null) {
            getAttachedView().stopGameWithReason(reason);
        }
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(10000, 100) {

            public void onTick(final long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                if (getAttachedView() != null) {
                    getAttachedView().setupTimer(minutes, seconds);
                }
            }

            public void onFinish() {
                if (getAttachedView() != null) {
                    postEventOf(new RawTestResult(FAILED));
                    getAttachedView().stopFallingAnimation();
                    getAttachedView().showSnackbar("Time is up!!!");
                }
            }
        };

        countDownTimer.start();
    }

    public void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public <E> void postEventOf(final E event) {
        serializedSubject.onNext(event);
    }

    private <T> Subscription observeEventOf(final Class<T> eventClass, final Action1<T> onNext) {
        return
            serializedSubject.ofType(eventClass)                        //
                             .observeOn(AndroidSchedulers.mainThread()) //
                             .subscribeOn(Schedulers.io())              //
                             .subscribe(onNext);
    }

    private void saveResults() {

        final String passed = String.valueOf(listOfPassedResults.size());
        final String failed = String.valueOf(listOfFailedResults.size());
        final boolean isComplete = !listOfCompleteResults.isEmpty();

        final GameStats gameStats = new GameStats(passed, failed, isComplete);
        finalResults.add(0, gameStats);

        final String jsonResultsList = gson.toJson(finalResults, type);

        sharedPreferences.edit().putString(GAME_RESULT_SAVED_LIST, jsonResultsList).apply();
    }

    private List<GameStats> getSavedResults() {
        final String jsonList = sharedPreferences.getString(GAME_RESULT_SAVED_LIST, "");

        if (jsonList.isEmpty()) {
            return new ArrayList<>();
        }

        return gson.fromJson(jsonList, type);
    }
}
