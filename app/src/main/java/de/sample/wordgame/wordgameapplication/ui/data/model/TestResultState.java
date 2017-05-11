package de.sample.wordgame.wordgameapplication.ui.data.model;

import static de.sample.wordgame.wordgameapplication.ui.data.model.TestResultState.COMPLETE;
import static de.sample.wordgame.wordgameapplication.ui.data.model.TestResultState.FAILED;
import static de.sample.wordgame.wordgameapplication.ui.data.model.TestResultState.PASSED;

import android.support.annotation.IntDef;

@IntDef({ PASSED, FAILED, COMPLETE })
public @interface TestResultState {
    int PASSED = 0;
    int FAILED = 1;
    int COMPLETE = 2;
}
