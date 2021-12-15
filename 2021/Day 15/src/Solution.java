import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

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
        int[][] map = new int[100][100];
        Pair[][] dist = new Pair[100][100];
        TreeSet<Pair> treeSet = new TreeSet<>();
        String curr;
        for (int i = 0; i < 100; i++) {
            curr = fs.next();
            for (int j = 0; j < 100; j++) {
                map[i][j] = curr.charAt(j) - '0';
                dist[i][j] = new Pair(i, j);
                treeSet.add(dist[i][j]);
            }
        }
        treeSet.remove(dist[0][0]);
        dist[0][0].dist = 0;
        treeSet.add(dist[0][0]);
        Pair currPair;
        while (!treeSet.isEmpty()) {
            currPair = treeSet.pollFirst();
            currPair.visited = true;
            if (currPair.x == 99 && currPair.y == 99) {
                break;
            }
            Pair tempPair;
            if (currPair.x > 0 && !dist[currPair.x - 1][currPair.y].visited) {
                tempPair = dist[currPair.x - 1][currPair.y];
                if (currPair.dist + map[currPair.x - 1][currPair.y] < tempPair.dist) {
                    treeSet.remove(tempPair);
                    tempPair.dist = currPair.dist + map[currPair.x - 1][currPair.y];
                    treeSet.add(tempPair);
                }
            }
            if (currPair.x < 99 && !dist[currPair.x + 1][currPair.y].visited) {
                tempPair = dist[currPair.x + 1][currPair.y];
                if (currPair.dist + map[currPair.x + 1][currPair.y] < tempPair.dist) {
                    treeSet.remove(tempPair);
                    dist[currPair.x + 1][currPair.y].dist = currPair.dist + map[currPair.x + 1][currPair.y];
                    treeSet.add(tempPair);
                }
            }
            if (currPair.y > 0 && !dist[currPair.x][currPair.y - 1].visited) {
                tempPair = dist[currPair.x][currPair.y - 1];
                if (currPair.dist + map[currPair.x][currPair.y - 1] < tempPair.dist) {
                    treeSet.remove(tempPair);
                    dist[currPair.x][currPair.y - 1].dist = currPair.dist + map[currPair.x][currPair.y - 1];
                    treeSet.add(tempPair);
                }
            }
            if (currPair.y < 99 && !dist[currPair.x][currPair.y + 1].visited) {
                tempPair = dist[currPair.x][currPair.y + 1];
                if (currPair.dist + map[currPair.x][currPair.y + 1] < tempPair.dist) {
                    treeSet.remove(tempPair);
                    dist[currPair.x][currPair.y + 1].dist = currPair.dist + map[currPair.x][currPair.y + 1];
                    treeSet.add(tempPair);
                }
            }
        }
        return dist[99][99].dist;
    }

    private int part2() {
        int[][] map = new int[500][500];
        Pair[][] dist = new Pair[500][500];
        TreeSet<Pair> treeSet = new TreeSet<>();
        String curr;
        for (int i = 0; i < 100; i++) {
            curr = fs.next();
            for (int j = 0; j < 100; j++) {
                int currVal = curr.charAt(j) - '0';
                for (int k = i; k < 500; k += 100) {
                    for (int l = j; l < 500; l += 100) {
                        map[k][l] = currVal + k / 100 + l / 100;
                        if (map[k][l] > 9) {
                            map[k][l] -= 9;
                        }
                        dist[k][l] = new Pair(k, l);
                        treeSet.add(dist[k][l]);
                    }
                }
            }
        }
        treeSet.remove(dist[0][0]);
        dist[0][0].dist = 0;
        treeSet.add(dist[0][0]);
        Pair currPair;
        while (!treeSet.isEmpty()) {
            currPair = treeSet.pollFirst();
            currPair.visited = true;
            if (currPair.x == 499 && currPair.y == 499) {
                break;
            }
            Pair tempPair;
            if (currPair.x > 0 && !dist[currPair.x - 1][currPair.y].visited) {
                tempPair = dist[currPair.x - 1][currPair.y];
                if (currPair.dist + map[currPair.x - 1][currPair.y] < tempPair.dist) {
                    treeSet.remove(tempPair);
                    tempPair.dist = currPair.dist + map[currPair.x - 1][currPair.y];
                    treeSet.add(tempPair);
                }
            }
            if (currPair.x < 499 && !dist[currPair.x + 1][currPair.y].visited) {
                tempPair = dist[currPair.x + 1][currPair.y];
                if (currPair.dist + map[currPair.x + 1][currPair.y] < tempPair.dist) {
                    treeSet.remove(tempPair);
                    dist[currPair.x + 1][currPair.y].dist = currPair.dist + map[currPair.x + 1][currPair.y];
                    treeSet.add(tempPair);
                }
            }
            if (currPair.y > 0 && !dist[currPair.x][currPair.y - 1].visited) {
                tempPair = dist[currPair.x][currPair.y - 1];
                if (currPair.dist + map[currPair.x][currPair.y - 1] < tempPair.dist) {
                    treeSet.remove(tempPair);
                    dist[currPair.x][currPair.y - 1].dist = currPair.dist + map[currPair.x][currPair.y - 1];
                    treeSet.add(tempPair);
                }
            }
            if (currPair.y < 499 && !dist[currPair.x][currPair.y + 1].visited) {
                tempPair = dist[currPair.x][currPair.y + 1];
                if (currPair.dist + map[currPair.x][currPair.y + 1] < tempPair.dist) {
                    treeSet.remove(tempPair);
                    dist[currPair.x][currPair.y + 1].dist = currPair.dist + map[currPair.x][currPair.y + 1];
                    treeSet.add(tempPair);
                }
            }
        }
        return dist[499][499].dist;
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

    private static class Pair implements Comparable<Pair> {
        private int x;
        private int y;
        private int dist = Integer.MAX_VALUE;
        private boolean visited = false;

        private Pair() {
        }

        private Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Pair o) {
            if (this.dist != o.dist) {
                return Integer.compare(dist, o.dist);
            } else {
                if (x != o.x) {
                    return Integer.compare(x, o.x);
                } else {
                    return Integer.compare(y, o.y);
                }
            }
        }
    }
}
