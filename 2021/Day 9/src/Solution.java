import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.PriorityQueue;
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
        char[][] heightMap = new char[100][100];
        for (int i = 0; i < 100; i++) {
            heightMap[i] = fs.next().toCharArray();
        }
        int ans = 0;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                if (i > 0 && heightMap[i - 1][j] <= heightMap[i][j]) {
                    continue;
                }
                if (i < 99 && heightMap[i + 1][j] <= heightMap[i][j]) {
                    continue;
                }
                if (j > 0 && heightMap[i][j - 1] <= heightMap[i][j]) {
                    continue;
                }
                if (j < 99 && heightMap[i][j + 1] <= heightMap[i][j]) {
                    continue;
                }
                ans += heightMap[i][j] - '/';
            }
        }
        return ans;
    }

    private PriorityQueue<Integer> sizes;
    private char[][] heightMap;

    private int part2() {
        sizes = new PriorityQueue<>();
        sizes.add(0);
        sizes.add(0);
        sizes.add(0);
        heightMap = new char[100][100];
        for (int i = 0; i < 100; i++) {
            heightMap[i] = fs.next().toCharArray();
        }
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                if (heightMap[i][j] == '.' || heightMap[i][j] == '9') {
                    continue;
                }
                if (i > 0 && heightMap[i - 1][j] <= heightMap[i][j]) {
                    continue;
                }
                if (i < 99 && heightMap[i + 1][j] <= heightMap[i][j]) {
                    continue;
                }
                if (j > 0 && heightMap[i][j - 1] <= heightMap[i][j]) {
                    continue;
                }
                if (j < 99 && heightMap[i][j + 1] <= heightMap[i][j]) {
                    continue;
                }
                sizes.add(findSize(i, j));
                sizes.poll();
            }
        }
        return sizes.poll() * sizes.poll() * sizes.poll();
    }

    private int findSize(int x, int y) {
        char temp = heightMap[x][y];
        if (temp == '9') {
            return 0;
        }
        heightMap[x][y] = '.';
        int count = 1;
        if (x > 0 && heightMap[x - 1][y] > temp) {
            count += findSize(x - 1, y);
        }
        if (x < 99 && heightMap[x + 1][y] > temp) {
            count += findSize(x + 1, y);
        }
        if (y > 0 && heightMap[x][y - 1] > temp) {
            count += findSize(x, y - 1);
        }
        if (y < 99 && heightMap[x][y + 1] > temp) {
            count += findSize(x, y + 1);
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

        private void close() {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
