package de.sample.wordgame.wordgameapplication.ui.data.model.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameStats extends Results {

    @SerializedName("passed")
    @Expose
    public String passed;

    @SerializedName("failed")
    @Expose
    public String failed;

    @SerializedName("is_complete")
    @Expose
    public boolean isComplete;

    public GameStats(final String passed, final String failed, final boolean isComplete) {
        this.passed = passed;
        this.failed = failed;
        this.isComplete = isComplete;
    }
}
