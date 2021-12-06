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
        File file = new File(Paths.get("src", "input.txt").toString());

        fs = new FastScan(file);
        System.out.println(part1());
        fs.close();

        fs.changeFile(file);
        System.out.println(part2());
        fs.close();
    }

    private static int part1() {
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

    private static long part2() {
        long[] tracker = new long[9];
        for (int i = 0; i < 300; i++) {
            tracker[fs.nextInt()]++;
        }
        for (int i = 0; i < 256; i++) {
            long temp = tracker[0];
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

        public FastScan(File file) {
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        public void changeFile(File file) {
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        public String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return st.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public void close() {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
