import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
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
        String[] currArr;
        ArrayList<Pair> initialList = new ArrayList<>(); // Add all dots into a list
        for (int i = 0; i < 877; i++) {
            currArr = fs.next().split(",");
            Pair newPair = new Pair();
            newPair.x = Integer.parseInt(currArr[0]);
            newPair.y = Integer.parseInt(currArr[1]);
            initialList.add(newPair);
        }
        fs.next(); // skip line
        currArr = fs.next().split("=");
        int foldAbout = Integer.parseInt(currArr[1]);
        char foldAxis = currArr[0].charAt(currArr[0].length() - 1);
        HashSet<Pair> uniqSet = new HashSet<>();
        if (foldAxis == 'x') { // if axis to fold about is X
            for (Pair currPair: initialList) { // for each dot in the initial list
                if (currPair.x > foldAbout) { // if the dot is to right of the axis, modify the x coordinate
                    currPair.x = 2 * foldAbout - currPair.x;
                }
                uniqSet.add(currPair); // add the dot to the unique set, hashing takes care of duplicates
            }
        } else { // if axis to fold about is Y
            for (Pair currPair: initialList) { // for each dot in the initial list
                if (currPair.y > foldAbout) { // if the dot is below the axis, modify the y coordinate
                    currPair.y = 2 * foldAbout - currPair.y;
                }
                uniqSet.add(currPair); // add the dot to the unique set, hashing takes care of duplicates
            }
        }
        return uniqSet.size(); // size of the unique set would be the number of dots after one fold
    }

    private int part2() {
        String[] currArr;
        HashSet<Pair> uniqSet = new HashSet<>();
        for (int i = 0; i < 877; i++) { // Add all dots to a unique set
            currArr = fs.next().split(",");
            Pair newPair = new Pair();
            newPair.x = Integer.parseInt(currArr[0]);
            newPair.y = Integer.parseInt(currArr[1]);
            uniqSet.add(newPair);
        }
        fs.next(); // skip line
        int foldAbout;
        char foldAxis;
        HashSet<Pair> tempSet = new HashSet<>();
        String curr;
        while ((curr = fs.next()) != null) { // same logic as part 1, but repeated this time
            currArr = curr.split("=");
            foldAbout = Integer.parseInt(currArr[1]);
            foldAxis = currArr[0].charAt(currArr[0].length() - 1);
            if (foldAxis == 'x') {
                for (Pair currPair: uniqSet) {
                    if (currPair.x > foldAbout) {
                        currPair.x = 2 * foldAbout - currPair.x;
                    }
                    tempSet.add(currPair);
                }
            } else {
                for (Pair currPair: uniqSet) {
                    if (currPair.y > foldAbout) {
                        currPair.y = 2 * foldAbout - currPair.y;
                    }
                    tempSet.add(currPair);
                }
            }
            HashSet<Pair> holder = uniqSet;
            uniqSet = tempSet;
            tempSet = holder;
            tempSet.clear();
        }
        int xMax = Integer.MIN_VALUE;
        int yMax = Integer.MIN_VALUE;
        for (Pair currPair: uniqSet) {
            xMax = Math.max(xMax, currPair.x);
            yMax = Math.max(yMax, currPair.y);
        }
        char[][] arr = new char[yMax + 1][xMax + 1];
        for (char[] currCharArr: arr) {
            Arrays.fill(currCharArr, '.');
        }
        for (Pair currPair: uniqSet) {
            arr[currPair.y][currPair.x] = '#';
        }
        for (char[] currCharArr: arr) {
            System.out.println(Arrays.toString(currCharArr));
        }
        return 0;
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

    private static class Pair {
        private int x;
        private int y;

        private Pair() {
        }

        private Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Pair)) {
                return false;
            }
            Pair pair = (Pair) o;
            return x == pair.x && y == pair.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
