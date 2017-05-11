package de.sample.wordgame.wordgameapplication.ui.data.model.result;

import android.support.annotation.ColorRes;

public class ResultStats extends Results {

    public String passed;
    public String failed;
    @ColorRes
    public int color;

    public ResultStats(final String passed, final String failed, @ColorRes final int color) {
        this.passed = passed;
        this.failed = failed;
        this.color = color;
    }
}
