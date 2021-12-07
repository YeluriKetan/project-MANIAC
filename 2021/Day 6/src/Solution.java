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
        File file = new File(Paths.get("src", "inputModified.txt").toString());

        fs = new FastScan(file);
        System.out.println(sol.part1());

        fs.changeFile(file);
        System.out.println(sol.part2());
        fs.close();
    }

    private int part1() {
        int[] tracker = new int[9];
        for (int i = 0; i < 300; i++) {
            tracker[fs.nextInt()]++;
        }
        for (int i = 0; i < 80; i++) {
            int temp = tracker[0];
            for (int j = 0; j < 8; j++) {
                tracker[j] = tracker[j + 1];
            }
            tracker[8] = temp;
            tracker[6] += temp;
        }
        int total = 0;
        for (int i = 0; i < 9; i++) {
            total += tracker[i];
        }
        return total;
    }

    private long part2() {
        long[] tracker = new long[9];
        for (int i = 0; i < 300; i++) {
            tracker[fs.nextInt()]++;
        }
        long temp;
        for (int i = 0; i < 256; i++) {
            temp = tracker[0];
            for (int j = 0; j < 8; j++) {
                tracker[j] = tracker[j + 1];
            }
            tracker[8] = temp;
            tracker[6] += temp;
        }
        long total = 0;
        for (int i = 0; i < 9; i++) {
            total += tracker[i];
        }
        return total;
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

        private int nextInt() {
            return Integer.parseInt(next());
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
