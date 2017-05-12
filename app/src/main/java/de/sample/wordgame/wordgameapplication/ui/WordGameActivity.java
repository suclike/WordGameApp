package de.sample.wordgame.wordgameapplication.ui;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

import static de.sample.wordgame.wordgameapplication.ui.data.model.TestResultState.COMPLETE;
import static de.sample.wordgame.wordgameapplication.ui.data.model.TestResultState.FAILED;
import static de.sample.wordgame.wordgameapplication.ui.data.model.TestResultState.PASSED;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import android.os.Bundle;

import android.support.annotation.StringRes;
import android.support.annotation.UiThread;

import android.support.design.widget.Snackbar;

import android.view.View;
import android.view.ViewGroup;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;

import de.sample.wordgame.wordgameapplication.R;
import de.sample.wordgame.wordgameapplication.di.ActivityComponent;
import de.sample.wordgame.wordgameapplication.domain.model.WordsWithTranslations;
import de.sample.wordgame.wordgameapplication.ui.data.model.TestResultState;
import de.sample.wordgame.wordgameapplication.ui.data.model.result.RawTestResult;
import de.sample.wordgame.wordgameapplication.ui.model.presenter.WordGamePresenter;
import de.sample.wordgame.wordgameapplication.ui.model.view.WordGame;

import rx.subscriptions.CompositeSubscription;

public class WordGameActivity extends BaseActivity implements WordGame {

    @Bind(R.id.falling_word_textview)
    TextView fallingWord;

    @Bind(R.id.game_timer_textview)
    TextView timer;

    @Bind(R.id.end_game_reason_textview)
    TextView endGameText;

    @Bind(R.id.start_button_layout)
    ViewGroup wordsLayout;

    @Bind(R.id.stop_or_cancel_layout)
    ViewGroup stopCancelLayout;

    @Bind(R.id.correct_answer_button)
    Button correctAnswerButton;

    @Bind(R.id.incorrect_answer_button)
    Button incorrectAnswerButton;

    @Inject
    CompositeSubscription compositeSubscription;

    @Inject
    WordGamePresenter presenter;

    private List<WordsWithTranslations> listOfWords = new ArrayList<>();
    private int currentGameRound = 0;
    private Animation fallingAnimation;
    private boolean answerSelected = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter.init(this);

        compositeSubscription.add(presenter.getWords());

        compositeSubscription.add(presenter.checkResults());
    }

    @Override
    protected void handleInjection(final ActivityComponent component) {
        component.inject(this);
    }

    @Override
    protected int layoutToInflate() {
        return R.layout.game_activity_layout;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.destroy();

        compositeSubscription.unsubscribe();
    }

    @Override
    public void initGameRound(final List<WordsWithTranslations> words, final boolean shouldStartNextRound) {
        answerSelected = false;
        listOfWords = words;

        if (shouldStartNextRound && currentGameRound < words.size()) {
            fallingWord.setText(words.get(currentGameRound).textToTranslateSpa);
            correctAnswerButton.setText(words.get(currentGameRound).textCorrectEng);
            incorrectAnswerButton.setText(words.get(currentGameRound).textWrongEng);

            float bottomOfScreen = getResources().getDisplayMetrics().heightPixels
                    - (fallingWord.getHeight() / 2 + wordsLayout.getHeight() * 3);

            fallingAnimation = new TranslateAnimation(0, 0, 0, bottomOfScreen);
            fallingAnimation.setInterpolator(new AccelerateInterpolator());
            fallingAnimation.setInterpolator(new LinearInterpolator());
            fallingAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(final Animation animation) {
                        presenter.startTimer();
                    }

                    @Override
                    public void onAnimationEnd(final Animation animation) {
                        stopTimer();
                        if (!answerSelected) {
                            presenter.postEventOf(new RawTestResult(FAILED));
                        }
                    }

                    @Override
                    public void onAnimationRepeat(final Animation animation) { }
                });
            fallingAnimation.setDuration(10000);
            fallingWord.startAnimation(fallingAnimation);
        } else {
            presenter.postEventOf(new RawTestResult(COMPLETE));
        }
    }

    @Override
    public void setupTimer(final int minutes, final int seconds) {
        timer.setText(String.format(Locale.US, "%d:%02d", minutes, seconds));
    }

    @Override
    public void stopTimer() {
        presenter.stopTimer();
    }

    @Override
    public void stopGameWithReason(@StringRes final int reason) {
        stopTimer();
        stopCancelLayout.setVisibility(View.VISIBLE);
        endGameText.setText(reason);
    }

    @Override
    @UiThread
    public void stopFallingAnimation() {
        fallingAnimation.cancel();
        fallingWord.clearAnimation();
    }

    @Override
    public void showSnackbar(final String message) {
        Snackbar snackbar = Snackbar.make(wordsLayout, message, LENGTH_SHORT);
        snackbar.setText(message);

        if (!snackbar.isShown()) {
            snackbar.show();
        }
    }

    public void checkResult(final View view) {
        answerSelected = true;

        stopTimer();
        stopFallingAnimation();

        final String originalString = listOfWords.get(currentGameRound).textToTranslateSpa;
        final String fallingString = fallingWord.getText().toString();

        final String selectedAnswer = ((Button) view).getText().toString();
        final String correctAnswer = listOfWords.get(currentGameRound).textCorrectEng;

        @TestResultState
        int resultState;

        if (fallingString.equalsIgnoreCase(originalString) && selectedAnswer.equalsIgnoreCase(correctAnswer)) {
            resultState = PASSED;
        } else {
            resultState = FAILED;
        }

        presenter.postEventOf(new RawTestResult(resultState));

        // increment counter to start next round
        currentGameRound++;
    }

    @Override
    public List<WordsWithTranslations> getCurrentListOfWords() {
        return listOfWords;
    }

    public void quitGame(final View view) {
        finish();
    }
}
