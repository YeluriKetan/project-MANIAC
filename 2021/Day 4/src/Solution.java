import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Solution {

    private static FastScan fs;
    private static int[] draw;

    public static void main(String[] args) {
        Solution sol = new Solution();
        File file = new File(Paths.get("src", "input.txt").toString());

        fs = new FastScan(file);
        draw = sol.drawNumbers();
        System.out.println(sol.part1());

        fs.changeFile(file);
        draw = sol.drawNumbers();
        System.out.println(sol.part2());
        fs.close();
    }

    private int[] drawNumbers() {
        String[] strArray = fs.next().split(",");
        int[] newDraw = new int[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            newDraw[i] = Integer.parseInt(strArray[i]);
        }
        return newDraw;
    }

    private static int count;
    private static int score;
    private static ArrayList<HashSet<Integer>> rows;
    private static ArrayList<HashSet<Integer>> cols;

    private int part1() {
        count = Integer.MAX_VALUE;
        score = 0;
        rows = new ArrayList<>();
        cols = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            rows.add(new HashSet<>());
            cols.add(new HashSet<>());
        }
        int curr;
        for (int n = 0; n < 100; n++) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    curr = fs.nextInt();
                    rows.get(i).add(curr);
                    cols.get(j).add(curr);
                }
            }
            part1Helper();
            for (int i = 0; i < 5; i++) {
                rows.get(i).clear();
                cols.get(i).clear();
            }
        }
        return score;
    }

    private void part1Helper() {
        for (int i = 0; i < draw.length; i++) {
            for (HashSet<Integer> currSet: rows) {
                if (currSet.contains(draw[i])) {
                    currSet.remove(draw[i]);
                    if (currSet.isEmpty()) {
                        if (i < count) {
                            count = i;
                            score = score(rows) * draw[i];
                        }
                        return;
                    }
                    break;
                }
            }
            for (HashSet<Integer> currSet: cols) {
                if (currSet.contains(draw[i])) {
                    currSet.remove(draw[i]);
                    if (currSet.isEmpty()) {
                        if (i < count) {
                            count = i;
                            score = score(cols) * draw[i];
                        }
                        return;
                    }
                    break;
                }
            }
        }
    }

    private int part2() {
        count = -1;
        score = 0;
        rows = new ArrayList<>();
        cols = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            rows.add(new HashSet<>());
            cols.add(new HashSet<>());
        }
        int curr;
        for (int n = 0; n < 100; n++) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    curr = fs.nextInt();
                    rows.get(i).add(curr);
                    cols.get(j).add(curr);
                }
            }
            part2Helper();
            for (int i = 0; i < 5; i++) {
                rows.get(i).clear();
                cols.get(i).clear();
            }
        }
        return score;
    }

    private void part2Helper() {
        for (int i = 0; i < draw.length; i++) {
            for (HashSet<Integer> currSet: rows) {
                if (currSet.contains(draw[i])) {
                    currSet.remove(draw[i]);
                    if (currSet.isEmpty()) {
                        if (i > count) {
                            count = i;
                            score = score(rows) * draw[i];
                        }
                        return;
                    }
                    break;
                }
            }
            for (HashSet<Integer> currSet: cols) {
                if (currSet.contains(draw[i])) {
                    currSet.remove(draw[i]);
                    if (currSet.isEmpty()) {
                        if (i > count) {
                            count = i;
                            score = score(rows) * draw[i];
                        }
                        return;
                    }
                    break;
                }
            }
        }
    }

    private int score(ArrayList<HashSet<Integer>> arr) {
        int total = 0;
        for (HashSet<Integer> currSet: arr) {
            for (Integer currInt: currSet) {
                total += currInt;
            }
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
