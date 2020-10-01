package com.ericlam.mc.minigames.core.factory.scoboard;

import com.ericlam.mc.minigames.core.character.GamePlayer;

import java.util.function.BiFunction;

final class IndividualScore {
    private final String text;
    private final int score;
    private final BiFunction<GamePlayer, String, String> parser;

    IndividualScore(String text, int score, BiFunction<GamePlayer, String, String> parser) {
        this.text = text;
        this.score = score;
        this.parser = parser;
    }

    String getText() {
        return text;
    }

    int getScore() {
        return score;
    }


    public BiFunction<GamePlayer, String, String> getParser() {
        return parser;
    }
}
