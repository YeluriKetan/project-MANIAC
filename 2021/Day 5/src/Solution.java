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
        int[][] arr = new int[1000][1000];
        int x1, y1, x2, y2;
        int count = 0;
        for (int i = 0; i < 500; i++) {
            x1 = fs.nextInt();
            y1 = fs.nextInt();
            x2 = fs.nextInt();
            y2 = fs.nextInt();
            if (x1 == x2) {
                for (int j = Math.min(y1, y2); j <= Math.max(y1, y2); j++) {
                    arr[x1][j]++;
                }
            } else if (y1 == y2) {
                for (int j = Math.min(x1, x2); j <= Math.max(x1, x2); j++) {
                    arr[j][y1]++;
                }
            }
        }
        for (int[] ints : arr) {
            for (int j = 0; j < arr[0].length; j++) {
                if (ints[j] > 1) {
                    count++;
                }
            }
        }
        return count;
    }

    private int part2() {
        int[][] arr = new int[1000][1000];
        int x1, y1, x2, y2;
        int count = 0;
        int len;
        boolean direction;
        for (int i = 0; i < 500; i++) {
            x1 = fs.nextInt();
            y1 = fs.nextInt();
            x2 = fs.nextInt();
            y2 = fs.nextInt();
            if (x1 == x2) {
                for (int j = Math.min(y1, y2); j <= Math.max(y1, y2); j++) {
                    arr[x1][j]++;
                }
            } else if (y1 == y2) {
                for (int j = Math.min(x1, x2); j <= Math.max(x1, x2); j++) {
                    arr[j][y1]++;
                }
            } else {
                len = Math.abs(x2 - x1);
                if (y1 < y2) {
                    direction = x2 > x1;
                } else {
                    direction = x1 > x2;
                    x1 = x2;
                    y1 = y2;
                }
                if (direction) {
                    for (int j = 0; j <= len; j++) {
                        arr[x1 + j][y1 + j]++;
                    }
                } else {
                    for (int j = 0; j <= len; j++) {
                        arr[x1 - j][y1 + j]++;
                    }
                }
            }
        }
        for (int[] ints : arr) {
            for (int j = 0; j < arr[0].length; j++) {
                if (ints[j] > 1) {
                    count++;
                }
            }
        }
        return count;
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
