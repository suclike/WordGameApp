package de.sample.wordgame.wordgameapplication.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Words {

    @SerializedName("text_eng")
    @Expose
    public String textEng;

    @SerializedName("text_spa")
    @Expose
    public String textSpa;

    /**
     * No args constructor for use in serialization.
     */
    public Words() { }

    /**
     * @param  textSpa
     * @param  textEng
     */
    public Words(final String textEng, final String textSpa) {
        super();
        this.textEng = textEng;
        this.textSpa = textSpa;
    }
}
