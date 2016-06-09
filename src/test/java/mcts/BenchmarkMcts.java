package mcts;

import java.util.Arrays;

public class BenchmarkMcts {
    private final int numCpu = Runtime.getRuntime().availableProcessors();
    private final int times = 200;
    private final int maxIterations2 = 200;
    private final int timePerActionSec = 999;
    private final int dim = 5;
    private final int needed = 4;

    private int[] scores;
    private SelfPlay<TicTacToe> play;

    private void run() {
        int t = 1;
        while (t <= numCpu) {
            testScores(1, t * maxIterations2);
            int winPercent = (int) (100. * scores[1] / times);
            System.out.printf(
                "1x%d vs 1x%d: %d%% %s\n",
                t * maxIterations2,
                maxIterations2,
                winPercent,
                Arrays.toString(scores));

            if (t == 1) {
                t = t * 2;
                continue;
            }
            testScores(t, maxIterations2);
            winPercent = (int) (100. * scores[1] / times);
            System.out.printf(
                "%dx%d vs 1x%d: %d%% %s\n",
                t,
                maxIterations2,
                maxIterations2,
                winPercent,
                Arrays.toString(scores));

            t = t * 2;
        }
    }

    private void testScores(int threads1, int maxIterations1) {
        scores = new int[3];
        int threads2 = 1;
        for (int i = 0; i < times; i++) {
            TicTacToe startState = TicTacToe.start(dim, needed);
            play = new SelfPlay<>(
                startState,
                threads1,
                threads2,
                timePerActionSec,
                timePerActionSec,
                maxIterations1,
                maxIterations2);

            int winner = play.play();

            scores[winner]++;
            play.stop();
        }
    }

    public static void main(String[] args) {
        new BenchmarkMcts().run();
    }

}