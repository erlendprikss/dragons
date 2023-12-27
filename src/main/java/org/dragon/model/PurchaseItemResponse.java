package org.dragon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseItemResponse extends Game {

    private boolean shoppingSuccess;

    public PurchaseItemResponse() {
        super();
    }

    public PurchaseItemResponse(boolean shoppingSuccess, int lives, int gold, int level,  int turn) {
        super();
        this.shoppingSuccess = shoppingSuccess;
        setLives(lives);
        setGold(gold);
        setLevel(level);
        setTurn(turn);
    }

    public PurchaseItemResponse(boolean success) {

        super();
        this.shoppingSuccess = success;
    }
}
