import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Solution {

    private static FastScan fs;
    private static final HashSet<Integer> emptyHexD1Grid = new HashSet<>();

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
        String curr;
        HashSet<Pair> set = new HashSet<>();
        while ((curr = fs.next()) != null) {
            Pair currPair = getPairFromDirections(curr);
            if (set.contains(currPair)) {
                set.remove(currPair);
            } else {
                set.add(currPair);
            }
        }
        return set.size();
    }

    private Pair getPairFromDirections(String curr) {
        int index = 0;
        int xCor = 0;
        int yCor = 0;
        while (index < curr.length()) {
            switch (curr.charAt(index)) {
            case 'e':
                xCor++;
                break;
            case 'w':
                xCor--;
                break;
            case 'n': {
                if (yCor % 2 == 0) {
                    if (curr.charAt(++index) == 'w') {
                        xCor--;
                    }
                } else {
                    if (curr.charAt(++index) == 'e') {
                        xCor++;
                    }
                }
                yCor++;
                break;
            }
            case 's': {
                if (yCor % 2 == 0) {
                    if (curr.charAt(++index) == 'w') {
                        xCor--;
                    }
                } else {
                    if (curr.charAt(++index) == 'e') {
                        xCor++;
                    }
                }
                yCor--;
                break;
            }
            default:
                break;
            }
            index++;
        }
        return new Pair(xCor, yCor);
    }

    private int part2() {
        String curr;
        HashMap<Integer, HashSet<Integer>> hexD2Grid = new HashMap<>();
        while ((curr = fs.next()) != null) {
            Pair currPair = getPairFromDirections(curr);
            HashSet<Integer> currHexD1Grid = hexD2Grid.getOrDefault(currPair.y, new HashSet<>());
            if (currHexD1Grid.contains(currPair.x)) {
                currHexD1Grid.remove(currPair.x);
            } else {
                currHexD1Grid.add(currPair.x);
            }
            hexD2Grid.put(currPair.y, currHexD1Grid);
        }
        HashMap<Integer, HashSet<Integer>> newHexD2Grid = new HashMap<>();
        for (int k = 0; k < 100; k++) { // for 100 times
            for (int y = -16 - k; y <= 16 + k; y++) {
                HashSet<Integer> newHexD1Grid = new HashSet<>();
                for (int x = -16 - k; x <= 16 + k; x++) {
                    int count = 0;
                    count += hexD2Grid.getOrDefault(y, emptyHexD1Grid).contains(x - 1) ? 1 : 0;
                    count += hexD2Grid.getOrDefault(y, emptyHexD1Grid).contains(x + 1) ? 1 : 0;
                    if (y % 2 == 0) {
                        count += hexD2Grid.getOrDefault(y + 1, emptyHexD1Grid).contains(x) ? 1 : 0;
                        count += hexD2Grid.getOrDefault(y + 1, emptyHexD1Grid).contains(x - 1) ? 1 : 0;
                        count += hexD2Grid.getOrDefault(y - 1, emptyHexD1Grid).contains(x) ? 1 : 0;
                        count += hexD2Grid.getOrDefault(y - 1, emptyHexD1Grid).contains(x - 1) ? 1 : 0;
                    } else {
                        count += hexD2Grid.getOrDefault(y + 1, emptyHexD1Grid).contains(x + 1) ? 1 : 0;
                        count += hexD2Grid.getOrDefault(y + 1, emptyHexD1Grid).contains(x) ? 1 : 0;
                        count += hexD2Grid.getOrDefault(y - 1, emptyHexD1Grid).contains(x + 1) ? 1 : 0;
                        count += hexD2Grid.getOrDefault(y - 1, emptyHexD1Grid).contains(x) ? 1 : 0;
                    }
                    if (hexD2Grid.getOrDefault(y, emptyHexD1Grid).contains(x)) {
                        if (count == 1 || count == 2) {
                            newHexD1Grid.add(x);
                        }
                    } else {
                        if (count == 2) {
                            newHexD1Grid.add(x);
                        }
                    }
                }
                newHexD2Grid.put(y, newHexD1Grid);
            }
            HashMap<Integer, HashSet<Integer>> temp = hexD2Grid;
            hexD2Grid = newHexD2Grid;
            newHexD2Grid = temp;
            newHexD2Grid.clear();
        }
        int total = 0;
        for (HashSet<Integer> currHexD1Grid: hexD2Grid.values()) {
            total += currHexD1Grid.size();
        }
        return total;
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
