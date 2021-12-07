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

    private int part1() {
        int max = Integer.MIN_VALUE;
        int row, col;
        String curr;
        for (int i = 0; i < 881; i++) {
            curr = fs.next();
            row = 0;
            col = 0;
            for (int j = 0; j < 7; j++) {
                row <<= 1;
                if (curr.charAt(j) == 'B') {
                    row += 1;
                }
            }
            for (int j = 7; j < 10; j++) {
                col <<= 1;
                if (curr.charAt(j) == 'R') {
                    col += 1;
                }
            }
            max = Math.max(max, row * 8 + col);
        }
        return max;
    }

    private int part2() {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int row, col, currId;
        int total = 0;
        String curr;
        for (int i = 0; i < 881; i++) {
            curr = fs.next();
            row = 0;
            col = 0;
            for (int j = 0; j < 7; j++) {
                row <<= 1;
                if (curr.charAt(j) == 'B') {
                    row += 1;
                }
            }
            for (int j = 7; j < 10; j++) {
                col <<= 1;
                if (curr.charAt(j) == 'R') {
                    col += 1;
                }
            }
            currId = row * 8 + col;
            min = Math.min(min, currId);
            max = Math.max(max, currId);
            total += currId;
        }
        int requiredSum = (max * (max + 1)) / 2;
        requiredSum -= ((min - 1) * min) / 2;
        return requiredSum - total;
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
