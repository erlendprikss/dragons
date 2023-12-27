package org.dragon;

import org.dragon.model.*;
import org.dragon.DragonGameAPIService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DragonGamePlayer {
    private  static final int WINNING_SCORE = 1000;
    private static final int MAX_PURCHASE_RETRY_COUNT = 5;
    private static final int MAX_SECONDS_TO_WAIT_BETWEEN_GAMES = 1;
    private static final List<String> SAFE_PROBABILITIES = Arrays.asList("Sure thing", "Quite likely", "Piece of cake", "Walk in the park");
    private final DragonGameAPIService gameAPIService;

    public DragonGamePlayer(DragonGameAPIService gameAPIService) {
        this.gameAPIService = gameAPIService;
    }

    public void playGame(Game game) {
        try {
            if (game == null) {
                game = startNewGame();
            }

            PurchaseItemResponse potion = purchaseHPotPotion(game);

            if (potion != null) {
                game.setLives(potion.getLives());
                game.setGold(potion.getGold());
            }
            Message safeMessage = getSafeMessage(game);

            SolverResponse solvedMessage = solveMessage(game, safeMessage);

            if (solvedMessage.isSuccess()) {
                game = handleSuccessfulSolvedMessage(game, solvedMessage, safeMessage);
            } else {
                game = handleFailedSolvedMessage(game, solvedMessage);
            }

            checkGameStatusAndContinue(game);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Game startNewGame() throws Exception {
        Game newGame = gameAPIService.startGame();

        if (newGame == null) {
            throw new Exception("Game not started");
        }

        System.out.println("Game started with id " + newGame.getGameId());
        return newGame;
    }

    private PurchaseItemResponse purchaseHPotPotion(Game game) throws Exception {
        int costThreshold = 100;

        if (game.getGold() > costThreshold) {
            PurchaseItemResponse potion = buyItemWithRetry(game, ShopItem.Id.hpot);
            if (potion.isShoppingSuccess()) {
                System.out.println("Potion transaction was successful");
                return potion;
            }
        }
        return null;
    }

    private Message getSafeMessage(Game game) throws Exception {
        Message[] messages = gameAPIService.getMessages(game.getGameId());

        return Arrays.stream(messages)
                .filter(message -> SAFE_PROBABILITIES.contains(message.getProbability()) && !message.getAdId().equals(game.getPreviousAdId()))
                .findFirst()
                .orElse(messages[0]);
    }

    private SolverResponse solveMessage(Game game, Message safeMessage) throws Exception {
        return gameAPIService.solveMessage(game.getGameId(), safeMessage.getAdId());
    }

    private Game handleSuccessfulSolvedMessage(Game game, SolverResponse solvedMessage, Message safeMessage) {
        game.setScore(solvedMessage.getScore());
        game.setGold(solvedMessage.getGold());
        game.setTurn(solvedMessage.getTurn());
        System.out.println("handleSuccessfulSolvedMessage: Score Is " + game.getScore() + " Lives " + game.getLives() + " Gold " + game.getGold() + " Successful turns to api  " + game.getTurn());
        return game;
    }

    private Game handleFailedSolvedMessage(Game game, SolverResponse solvedMessage) {
        if (solvedMessage.getTurn() == 0) {
            System.out.println("Dragon Api Doing Weird Things, Eventually It Will Work Maybe, Just Wait");
            return game;
        }

        game.setLives(game.getLives() - 1);
        game.setTurn(solvedMessage.getTurn());
        System.out.println("handleFailedSolvedMessage: Score Is " + game.getScore() + " Lives " + game.getLives() + " Gold " + game.getGold() + " Successful turns to api " + game.getTurn());
        return game;
    }

    private void checkGameStatusAndContinue(Game game) throws Exception {
        if (game.getScore() >= WINNING_SCORE) {
            System.out.println("You Win!");
            return;
        }

        if (game.getLives() == 0) {
            System.out.println("Game Over");
            return;
        }

        TimeUnit.SECONDS.sleep(MAX_SECONDS_TO_WAIT_BETWEEN_GAMES);
        game.setPreviousAdId(getSafeMessage(game).getAdId());
        playGame(game);
    }
    private PurchaseItemResponse buyItemWithRetry(Game game, ShopItem.Id itemId) throws Exception {
        PurchaseItemResponse potion = null;

        for (int tryCount = 1; tryCount <= MAX_PURCHASE_RETRY_COUNT; tryCount++) {
            potion = gameAPIService.buyItem(game.getGameId(), itemId);

            if (potion.isShoppingSuccess()) {
                break;
            } else {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return potion;
    }
}