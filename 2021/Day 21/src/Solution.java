import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class Solution {

    private static FastScan fs;

    public static void main(String[] args) {
        Solution sol = new Solution();
        File file = new File(Paths.get("src", "input.txt").toString());

        fs = new FastScan(file);
        System.out.println(sol.part1());

        fs.changeFile(file);
        System.out.println(sol.part2());
        fs.close();
    }

    private int part1() {
        int[] playerPosition = new int[2];
        String[] currArr = fs.next().split(": ");
        playerPosition[0] = Integer.parseInt(currArr[1]);
        currArr = fs.next().split(": ");
        playerPosition[1] = Integer.parseInt(currArr[1]);
        int[] playerScore = {0, 0};
        int dice = 0;
        int turn = 0;
        int count = 0;
        while (playerScore[0] < 1000 && playerScore[1] < 1000) {
            int currPlay = 0;
            for (int i = 0; i < 3; i++) {
                dice++;
                if (dice > 100) {
                    dice = 1;
                }
                currPlay += dice;
            }
            count += 3;
            currPlay += playerPosition[turn];
            if (currPlay > 10) {
                currPlay %= 10;
                if (currPlay == 0) {
                    currPlay = 10;
                }
            }
            playerPosition[turn] = currPlay;
            playerScore[turn] += currPlay;
            turn = (turn + 1) % 2;
        }
        return Math.min(playerScore[0], playerScore[1]) * count;
    }

    private static final int[] arr = {0, 0, 0, 1, 3, 6, 7, 6, 3, 1}; // arr[i] is the number of universes formed when i is rolled in a turn by a player (considering turn includes all three die throws)

    private long part2() {
        // each game can be converted to a state
        // player 1 has a position from 1 - 10, player 2 has a position from 1 - 10
        // player 1 has a score from 0 - 20, player 2 has a score from 0 - 20 (scores of 21 or greater are considered wins)
        long[][][][] multiverse = new long[11][11][21][21];
        String[] currArr = fs.next().split(": ");
        int a = Integer.parseInt(currArr[1]);
        currArr = fs.next().split(": ");
        int b = Integer.parseInt(currArr[1]);
        multiverse[a][b][0][0] = 1; // initialize the first universe
        boolean turn = true;
        long player1Win = 0;
        long player2Win = 0;
        long[][][][] newMultiverse = new long[11][11][21][21]; // create a new copy of the multiverse
        while (true) {
            long count = 0;
            for (int i = 1; i < 11; i++) { // check if any universes are still running
                for (int j = 1; j < 11; j++) {
                    for (int k = 0; k < 21; k++) {
                        for (int l = 0; l < 21; l++) {
                            count += multiverse[i][j][k][l];
                            newMultiverse[i][j][k][l] = 0;
                        }
                    }
                }
            }
            if (count == 0) { // if done, the simulation is done
                break;
            }
            for (int i = 1; i < 11; i++) {
                for (int dice = 3; dice < 10; dice++) {
                    int dest = (i + dice) % 10; // calculate destination position
                    if (dest == 0) {
                        dest = 10;
                    }
                    for (int j = 1; j < 11; j++) {
                        for (int k = 0; k < 21; k++) {
                            int destScore = k + dest; // calculate new score
                            for (int l = 0; l < 21; l++) {
                                if (destScore > 20) { // if score > 20, add the number of wins
                                    if (turn) {
                                        // multiply by the number of universes split in this turn
                                        player1Win += arr[dice] * multiverse[i][j][k][l];
                                    } else {
                                        player2Win += arr[dice] * multiverse[j][i][l][k];
                                    }
                                } else {
                                    // else update the new multiverse to account for the new universes caused as a result
                                    // of this turn with splitting
                                    if (turn) {
                                        newMultiverse[dest][j][destScore][l] += arr[dice] * multiverse[i][j][k][l];
                                    } else {
                                        newMultiverse[j][dest][l][destScore] += arr[dice] * multiverse[j][i][l][k];
                                    }
                                }
                            }
                        }
                    }
                }
            }
            long[][][][] holder = multiverse; // reduce space usage by alternating between the two multiverses
            multiverse = newMultiverse; // replace the multiverse
            newMultiverse = holder;
            turn = !turn;
        }
        return Math.max(player1Win, player2Win);
    }

    private static class FastScan {
        private BufferedReader br;

        private FastScan(File file) {
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        private void changeFile(File file) {
            close();
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        private String next() {
            try {
                return br.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        private void close() {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
