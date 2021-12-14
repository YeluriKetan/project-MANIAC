import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

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

    private HashMap<Integer, HashMap<Integer, HashSet<Integer>>> d3Map;
    private static final HashMap<Integer, HashSet<Integer>> emptyD2Map = new HashMap<>();
    private static final HashSet<Integer> emptyD1Map = new HashSet<>();

    private HashMap<Integer, HashMap<Integer, HashMap<Integer, HashSet<Integer>>>> d4Map;
    private static final HashMap<Integer, HashMap<Integer, HashSet<Integer>>> emptyD3Map = new HashMap<>();

    private int part1() {
        d3Map = new HashMap<>();
        HashMap<Integer, HashSet<Integer>> tempD2Map = new HashMap<>();
        String curr;
        for (int i = 0; i < 8; i++) {
            curr = fs.next();
            HashSet<Integer> currD1Map = new HashSet<>();
            for (int j = 0; j < curr.length(); j++) {
                if (curr.charAt(j) == '#') {
                    currD1Map.add(j - 4);
                }
            }
            tempD2Map.put(i - 4, currD1Map);
        }
        d3Map.put(0, tempD2Map);
        int x = 5;
        int y = 5;
        int z = 1;
        for (int i = 0; i < 6; i++) {
            part1Helper(x++, y++, z++);
        }
        int total = 0;
        for (HashMap<Integer, HashSet<Integer>> currD2Map: d3Map.values()) {
            for (HashSet<Integer> currD1map: currD2Map.values()) {
                total += currD1map.size();
            }
        }
        return total;
    }

    private void part1Helper(int xLimit, int yLimit, int zLimit) {
        HashMap<Integer, HashMap<Integer, HashSet<Integer>>> newD3Map = new HashMap<>();
        for (int z = -zLimit; z <= zLimit; z++) {
            HashMap<Integer, HashSet<Integer>> currD2Map = d3Map.getOrDefault(z, emptyD2Map);
            HashMap<Integer, HashSet<Integer>> newD2Map = new HashMap<>();
            for (int y = -yLimit; y <= yLimit; y++) {
                HashSet<Integer> currD1Map = currD2Map.getOrDefault(y, emptyD1Map);
                HashSet<Integer> newD1Map = new HashSet<>();
                for (int x = -xLimit; x <= xLimit; x++) {
                    int count = 0;
                    for (int a = Math.max(z - 1, -zLimit); a <= Math.min(z + 1, zLimit) ; a++) {
                        for (int b = Math.max(y - 1, -yLimit); b <= Math.min(y + 1, yLimit); b++) {
                            for (int c = Math.max(x - 1, -xLimit); c <= Math.min(x + 1, xLimit); c++) {
                                if (d3Map.getOrDefault(a, emptyD2Map).getOrDefault(b, emptyD1Map).contains(c)) {
                                    count++;
                                }
                            }
                        }
                    }
                    if (currD1Map.contains(x)) {
                        if (count == 3 || count == 4) { // count includes self
                            newD1Map.add(x);
                        }
                    } else {
                        if (count == 3) { // count doesn't include self
                            newD1Map.add(x);
                        }
                    }
                }
                if (!newD1Map.isEmpty()) {
                    newD2Map.put(y, newD1Map);
                }
            }
            if (!newD2Map.isEmpty()) {
                newD3Map.put(z, newD2Map);
            }
        }
        d3Map = newD3Map;
    }

    private int part2() {
        d4Map = new HashMap<>();
        HashMap<Integer, HashMap<Integer, HashSet<Integer>>> tempD3Map = new HashMap<>();
        HashMap<Integer, HashSet<Integer>> tempD2Map = new HashMap<>();
        String curr;
        for (int i = 0; i < 8; i++) {
            curr = fs.next();
            HashSet<Integer> currD1Map = new HashSet<>();
            for (int j = 0; j < curr.length(); j++) {
                if (curr.charAt(j) == '#') {
                    currD1Map.add(j - 4);
                }
            }
            tempD2Map.put(i - 4, currD1Map);
        }
        tempD3Map.put(0, tempD2Map);
        d4Map.put(0, tempD3Map);
        int w = 1;
        int z = 1;
        int y = 5;
        int x = 5;
        for (int i = 0; i < 6; i++) {
            part2Helper(w++, z++, y++, x++);
        }
        int total = 0;
        for (HashMap<Integer, HashMap<Integer, HashSet<Integer>>> currD3Map : d4Map.values()){
            for (HashMap<Integer, HashSet<Integer>> currD2Map : currD3Map.values()) {
                for (HashSet<Integer> currD1map : currD2Map.values()) {
                    total += currD1map.size();
                }
            }
        }
        return total;
    }

    private void part2Helper(int wLimit, int zLimit, int yLimit, int xLimit) {
        HashMap<Integer, HashMap<Integer, HashMap<Integer, HashSet<Integer>>>> newD4Map = new HashMap<>();
        for (int w = -wLimit; w <= wLimit; w++) {
            HashMap<Integer, HashMap<Integer, HashSet<Integer>>> currD3Map = d4Map.getOrDefault(w, emptyD3Map);
            HashMap<Integer, HashMap<Integer, HashSet<Integer>>> newD3Map = new HashMap<>();
            for (int z = -zLimit; z <= zLimit; z++) {
                HashMap<Integer, HashSet<Integer>> currD2Map = currD3Map.getOrDefault(z, emptyD2Map);
                HashMap<Integer, HashSet<Integer>> newD2Map = new HashMap<>();
                for (int y = -yLimit; y <= yLimit; y++) {
                    HashSet<Integer> currD1Map = currD2Map.getOrDefault(y, emptyD1Map);
                    HashSet<Integer> newD1Map = new HashSet<>();
                    for (int x = -xLimit; x <= xLimit; x++) {
                        int count = 0;
                        for (int a = Math.max(w - 1, -wLimit); a <= Math.min(w + 1, wLimit); a++) {
                            for (int b = Math.max(z - 1, -zLimit); b <= Math.min(z + 1, zLimit); b++) {
                                for (int c = Math.max(y - 1, -yLimit); c <= Math.min(y + 1, yLimit); c++) {
                                    for (int d = Math.max(x - 1, -xLimit); d <= Math.min(x + 1, xLimit); d++) {
                                        if (d4Map.getOrDefault(a, emptyD3Map).getOrDefault(b, emptyD2Map).getOrDefault(c, emptyD1Map).contains(d)) {
                                            count++;
                                        }
                                    }
                                }
                            }
                        }
                        if (currD1Map.contains(x)) {
                            if (count == 3 || count == 4) { // count includes self
                                newD1Map.add(x);
                            }
                        } else {
                            if (count == 3) { // count doesn't include self
                                newD1Map.add(x);
                            }
                        }
                    }
                    if (!newD1Map.isEmpty()) {
                        newD2Map.put(y, newD1Map);
                    }
                }
                if (!newD2Map.isEmpty()) {
                    newD3Map.put(z, newD2Map);
                }
            }
            if (!newD3Map.isEmpty()) {
                newD4Map.put(w, newD3Map);
            }
        }
        d4Map = newD4Map;
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
}
