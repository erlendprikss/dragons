package org.dragon;

import org.dragon.model.Game;
import org.dragon.model.Message;
import org.dragon.model.PurchaseItemResponse;
import org.dragon.model.SolverResponse;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DragonGamePlayerTest {

    @Test
    public void testPlayGame_SuccessfulGame() throws Exception {
        DragonGameAPIService gameAPIService = mock(DragonGameAPIService.class);
        when(gameAPIService.startGame()).thenReturn(createMockGame());
        when(gameAPIService.buyItem(any(), any())).thenReturn(createMockPurchaseItemResponse(true));
        when(gameAPIService.getMessages(any())).thenReturn(createMockMessages());
        when(gameAPIService.solveMessage(any(), any()))
                .thenReturn(createMockSolverResponse(true, 1, 3))
                .thenReturn(createMockSolverResponse(true, 2, 3))
                .thenReturn(createMockSolverResponse(true, 3, 3))
                .thenReturn(createMockSolverResponse(true, 4, 3))
                .thenReturn(createMockSolverResponse(true, 5, 3))
                .thenReturn(createMockSolverResponse(true, 6, 3))
                .thenReturn(createMockSolverResponse(true, 7, 3))
                .thenReturn(createMockSolverResponse(true, 8, 3))
                .thenReturn(createMockSolverResponse(true, 9, 3))
                .thenReturn(createMockSolverResponse(true, 10, 3));


        DragonGamePlayer dragonGamePlayer = new DragonGamePlayer(gameAPIService);
        dragonGamePlayer.playGame(null);
    }
    private Game createMockGame() {
        Game game = new Game();
        game.setGameId("game123");
        game.setLives(3);
        game.setGold(0);
        game.setLevel(1);
        game.setScore(0);
        game.setHighScore(0);
        game.setTurn(1);
        return game;
    }

    private PurchaseItemResponse createMockPurchaseItemResponse(boolean success) {
        PurchaseItemResponse purchaseItemResponse = new PurchaseItemResponse(true);
        purchaseItemResponse.setLives(3);
        purchaseItemResponse.setGold(100);
        purchaseItemResponse.setScore(100);
        purchaseItemResponse.setTurn(1);

        return purchaseItemResponse;
    }

    private Message[] createMockMessages() {
        Message message1 = new Message();
        message1.setProbability("Sure thing");
        message1.setAdId("ad123");

        Message message2 = new Message();
        message2.setProbability("Quite likely");
        message2.setAdId("ad456");

        Message message3 = new Message();
        message3.setProbability("Risky");
        message3.setAdId("ad666");

        return new Message[]{message1, message2, message3};
    }

    private SolverResponse createMockSolverResponse(boolean success, int runCount, int remainingLives) {
        SolverResponse solverResponse = new SolverResponse(true);
        solverResponse.setGold(runCount * 100);
        solverResponse.setScore(runCount * 100);
        solverResponse.setTurn(runCount);
        solverResponse.setLives(remainingLives);
        return solverResponse;
    }

}