package mcts;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import mcts.TicTacToe.TicTacToeState;

public class TestTicTacToe {
    private final int timePerActionSec1 = 10000;
    private final int timePerActionSec2 = 10000;
    private int maxIterations1 = 100;
    private int maxIterations2 = 100;
    private int threads1 = 1;
    private final int threads2 = 1;

    private int dim = 3;
    private int needed = 3;

    @Test
    public void testSingleThreaded3x3() {
        threads1 = 1;
        dim = 3;
        needed = 3;
        maxIterations1 = 1000;
        maxIterations2 = 1000;
        int[] scores = testScores(100);
        System.out.println(Arrays.toString(scores));
        assertTrue(scores[0] > 95);
    }

    @Test
    public void testSingleThreadedMoreIterationWins6x3() {
        threads1 = 1;
        dim = 6;
        needed = 3;
        maxIterations1 = 300;
        maxIterations2 = 1000;
        int[] scores = testScores(100);
        System.out.println(Arrays.toString(scores));
        assertEquals(0, scores[0]);
        assertTrue(scores[1] * 1.5 < scores[2]);
    }

    @Test
    @Ignore
    public void testSingleVsMultiThreaded10x8() {
        dim = 10;
        needed = 8;
        threads1 = Runtime.getRuntime().availableProcessors();
        maxIterations1 = 1200 / threads1;
        maxIterations2 = 1200;
        System.out.println(threads1 + " x " + maxIterations1 + " vs 1x " + maxIterations2);
        int[] scores = testScores(10);
        System.out.println(Arrays.toString(scores));
        assertTrue(scores[0] >= 8);
    }

    @Test
    @Ignore
    public void testMultiVsSingleThreaded10x8() {
        dim = 10;
        needed = 8;
        threads1 = Runtime.getRuntime().availableProcessors();
        maxIterations1 = 500;
        maxIterations2 = 500;
        System.out.println(threads1 + " x " + maxIterations1 + " vs 1x " + maxIterations2);
        int[] scores = testScores(10);
        System.out.println(Arrays.toString(scores));
        assertTrue(scores[1] > 3);
        assertEquals(0, scores[2]);
    }

    private int[] testScores(int times) {
        int[] scores = new int[3];
        for (int i = 0; i < times; i++) {
            TicTacToeState startState = TicTacToe.start(dim, needed);
            SelfPlay<TicTacToe.Action, TicTacToeState> play = new SelfPlay<TicTacToe.Action, TicTacToeState>(
                startState,
                threads1,
                threads2,
                timePerActionSec1,
                timePerActionSec2,
                maxIterations1,
                maxIterations2);

            int winner = play.play();

            scores[winner]++;
            play.stop();
            // System.out.println(Arrays.toString(scores));
        }
        return scores;
    }

}
