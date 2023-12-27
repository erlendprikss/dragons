package org.dragon;
public class Main {
    public static void main(String[] args) {
        DragonGameAPIService gameAPIService = new DragonGameAPIService();
        new DragonGamePlayer(gameAPIService).playGame(null);
    }
}