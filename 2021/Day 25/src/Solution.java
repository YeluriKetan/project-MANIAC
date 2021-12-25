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
        fs.close();
    }

    private int part1() {
        int r = 137;
        int c = 139;
        char[][] grid = new char[r][];
        for (int i = 0; i < r; i++) {
            grid[i] = fs.next().toCharArray();
        }
        char[][] newGrid = new char[r][c];
        char[][] holder;
        int count = 1;
        int index = 0;
        int next;
        while (count > 0) {
            index++;
            count = 0;
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    if (grid[i][j] != '>') {
                        newGrid[i][j] = grid[i][j];
                    } else {
                        next = j + 1;
                        if (next >= c) {
                            next = 0;
                        }
                        if (grid[i][next] == '.') {
                            newGrid[i][next] = '>';
                            newGrid[i][j] = '.';
                            count++;
                            j++;
                        } else {
                            newGrid[i][j] = '>';
                        }
                    }
                }
            }
            holder = grid;
            grid = newGrid;
            newGrid = holder;
            for (int j = 0; j < c; j++) {
                for (int i = 0; i < r; i++) {
                    if (grid[i][j] != 'v') {
                        newGrid[i][j] = grid[i][j];
                    } else {
                        next = i + 1;
                        if (next >= r) {
                            next = 0;
                        }
                        if (grid[next][j] == '.') {
                            newGrid[i][j] = '.';
                            newGrid[next][j] = 'v';
                            count++;
                            i++;
                        } else {
                            newGrid[i][j] = 'v';
                        }
                    }
                }
            }
            holder = grid;
            grid = newGrid;
            newGrid = holder;
        }
        return index;
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
