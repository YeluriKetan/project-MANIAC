import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.StringTokenizer;

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

    private int[][] arr;

    private int part1() {
        arr = new int[10][10];
        String curr;
        // read, parse and build the octopus grid
        for (int i = 0; i < 10; i++) {
            curr = fs.next();
            for (int j = 0; j < 10; j++) {
                arr[i][j] = curr.charAt(j) - '0';
            }
        }
        int total = 0;
        // for 100 times
        for (int i = 0; i < 100; i++) {
            // simulate a step for all 100 octopus
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    helper(j, k);
                }
            }
            // count and add the flashes to the total
            // reset flashed octopuses with new energy level 0
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    if (arr[j][k] == -1) {
                        total++;
                        arr[j][k] = 0;
                    }
                }
            }
        }
        return total;
    }

    private void helper(int x, int y) {
        if (arr[x][y] == -1) { // ignore if already flashed in this step
            return;
        }
        arr[x][y]++; // increment energy level by one (default)
        if (arr[x][y] < 10) { // if energy is less than 10, move on
            return;
        }
        arr[x][y] = -1; // if greater than 9, mark as flashed (use -1)
        // for all neighbours, simulate the step
        // the following loop will call helper for the current octopus also,
        // but since its already marked as flashed, it will just return with no effect
        for (int i = Math.max(0, x - 1); i <= Math.min(9, x + 1); i++) {
            for (int j = Math.max(0, y - 1); j <= Math.min(9, y + 1) ; j++) {
                helper(i, j);
            }
        }
    }

    private int part2() {
        arr = new int[10][10];
        String curr;
        for (int i = 0; i < 10; i++) {
            curr = fs.next();
            for (int j = 0; j < 10; j++) {
                arr[i][j] = curr.charAt(j) - '0';
            }
        }
        int total;
        int count = 0;
        while (true) { // until simultaneous flashing is found
            count++; // track of the step count
            // same as in part 1, simulate the step for all 100 octopus
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    helper(j, k);
                }
            }
            total = 0;
            // count the number of flashes in this step and reset them to 0
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    if (arr[j][k] == -1) {
                        total++;
                        arr[j][k] = 0;
                    }
                }
            }
            // if the number of flashes in this round is 100 => all octopus have flashed simultaneously
            if (total == 100) {
                return count; // return the step count
            }
        }
    }

    private static class FastScan {
        private BufferedReader br;
        private StringTokenizer st;

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
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return st.nextToken();
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
