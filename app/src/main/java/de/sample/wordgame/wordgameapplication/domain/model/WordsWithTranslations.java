package de.sample.wordgame.wordgameapplication.domain.model;

public class WordsWithTranslations {

    public String textToTranslateSpa;
    public String textCorrectEng;
    public String textWrongEng;

    public WordsWithTranslations(final String textToTranslateSpa, final String textCorrectEng,
            final String textWrongEng) {
        this.textToTranslateSpa = textToTranslateSpa;
        this.textCorrectEng = textCorrectEng;
        this.textWrongEng = textWrongEng;
    }
}
