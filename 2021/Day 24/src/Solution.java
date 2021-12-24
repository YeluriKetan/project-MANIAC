import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class Solution {

    private static FastScan fs;
    private static int[][] input;

    public static void main(String[] args) {
        Solution sol = new Solution();
        File file = new File(Paths.get("src", "input.txt").toString());

        fs = new FastScan(file);
        getInput();
        fs.close();

        System.out.println(sol.part1());
        System.out.println(sol.part2());
    }

    private static void getInput() {
        input = new int[14][3];
        String curr;
        for (int i = 0; i < 14; i++) { // for each of the 14 blocks (each made of 18 lines)
            for (int j = 0; j < 4; j++) { // skip 4 lines
                fs.next();
            }
            curr = fs.next(); // Line 5: div z (1 or 26)
            int[] currInput = new int[3];
            currInput[0] = curr.charAt(6) == '1' ? 1 : 26;
            curr = fs.next(); // Line 6: add x (n)
            currInput[1] = Integer.parseInt(curr.substring(6));
            for (int j = 0; j < 9; j++) { // skip 9 lines
                fs.next();
            }
            curr = fs.next(); // Line 16: add y (m)
            currInput[2] = Integer.parseInt(curr.substring(6));
            for (int j = 0; j < 2; j++) { // skip 2 lines
                fs.next();
            }
            input[i] = currInput;
        }
    }

    private long part1() {
        long ans = dfsPart1(0, 0); // starting value of Z variable is 0
        long rev = 0;
        while (ans > 0) { // invert ans, since we get a reverse model number
            rev *= 10;
            rev += ans % 10;
            ans /= 10;
        }
        return rev;
    }

    private long dfsPart1(long currZ, int currIndex) {
        if (currIndex > 13) {
            return currZ == 0 ? 0 : -1; // currZ to be 0 upon reaching the end
        }
        if (input[currIndex][0] == 1) { // if div z 1
            long newZ = 26 * currZ + input[currIndex][2]; // 26z + w + m
            for (int i = 9; i > 0; i--) { // for each of the 9 possible values that this digit of the model number can take
                long ans = dfsPart1(newZ + i, currIndex + 1);
                if (ans == -1) {
                    continue;
                }
                return ans * 10 + i;
            }
            return -1;
        } else { // if div z 26
            int mod = (int) ((currZ % 26) + input[currIndex][1]); // z % 26 + n == w
            if (mod > 9 || mod < 1) {
                return -1;
            }
            long next = dfsPart1(currZ / 26, currIndex + 1); // floor(z / 26)
            return next == -1 ? -1 : next * 10 + mod;
        }
    }

    private long part2() { // same as above, only difference in dfsPart2
        long ans = dfsPart2(0, 0);
        long rev = 0;
        while (ans > 0) {
            rev *= 10;
            rev += ans % 10;
            ans /= 10;
        }
        return rev;
    }

    private long dfsPart2(long currZ, int currIndex) {
        if (currIndex > 13) {
            return currZ == 0 ? 0 : -1;
        }
        if (input[currIndex][0] == 1) {
            long newZ = 26 * currZ + input[currIndex][2];
            for (int i = 1; i < 10; i++) { // iterate from lower to higher
                long ans = dfsPart2(newZ + i, currIndex + 1);
                if (ans == -1) {
                    continue;
                }
                return ans * 10 + i;
            }
            return -1;
        } else {
            int mod = (int) ((currZ % 26) + input[currIndex][1]);
            if (mod > 9 || mod < 1) {
                return -1;
            }
            long next = dfsPart2(currZ / 26, currIndex + 1);
            return next == -1 ? -1 : next * 10 + mod;
        }
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
