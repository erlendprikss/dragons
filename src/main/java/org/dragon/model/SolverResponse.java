package org.dragon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolverResponse extends Game {

    private boolean success;
    private String message;

    public SolverResponse() {
        super();
    }

    public SolverResponse(boolean success, String message, int lives, int gold, int level, int score, int highScore, int turn) {
        super();
        this.success = success;
        this.message = message;
        setLives(lives);
        setGold(gold);
        setLevel(level);
        setScore(score);
        setHighScore(highScore);
        setTurn(turn);
    }

    public SolverResponse(boolean success) {
        super();
        this.success = success;
    }
}
