package org.dragon.model;

import lombok.Getter;

@Getter
public class ShopItem {

    public enum Id {
        hpot, cs, gas, wax, tricks, wingpot, ch, rf, iron, mtrix, wingpotmax
    }

    private Id id;
    private String name;
    private int cost;
}
