import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Solution {

    private static FastScan fs;
    private static final Pattern big = Pattern.compile("^[A-Z]+$");
    private static final String start = "start";
    private static final String end = "end";

    public static void main(String[] args) {
        Solution sol = new Solution();
        File file = new File(Paths.get("src", "input.txt").toString());

        fs = new FastScan(file);
        System.out.println(sol.part1());

        fs.changeFile(file);
        System.out.println(sol.part2());
        fs.close();
    }

    private int total;
    private HashMap<String, HashSet<String>> map;

    private int part1() {
        String curr1, curr2;
        map = new HashMap<>();
        for (int i = 0; i < 23; i++) {
            curr1 = fs.next();
            curr2 = fs.next();
            HashSet<String> curr1Set = map.getOrDefault(curr1, new HashSet<>());
            curr1Set.add(curr2);
            map.put(curr1, curr1Set);
            HashSet<String> curr2Set = map.getOrDefault(curr2, new HashSet<>());
            curr2Set.add(curr1);
            map.put(curr2, curr2Set);
        }
        total = 0;
        HashSet<String> path = new HashSet<>();
        path.add(start);
        dfsPart1(start, path);
        return total;
    }

    private void dfsPart1(String curr, HashSet<String> path) {
        if (end.equals(curr)) {
            total++;
            return;
        }
        for (String child: map.get(curr)) {
            if (big.matcher(child).matches()) {
                HashSet<String> newPath = new HashSet<>(path);
                newPath.add(child);
                dfsPart1(child, newPath);
            } else if (!path.contains(child)) {
                HashSet<String> newPath = new HashSet<>(path);
                newPath.add(child);
                dfsPart1(child, newPath);
            }
        }
    }

    private int part2() {
        String curr1, curr2;
        map = new HashMap<>();
        for (int i = 0; i < 23; i++) {
            curr1 = fs.next();
            curr2 = fs.next();
            HashSet<String> curr1Set = map.getOrDefault(curr1, new HashSet<>());
            curr1Set.add(curr2);
            map.put(curr1, curr1Set);
            HashSet<String> curr2Set = map.getOrDefault(curr2, new HashSet<>());
            curr2Set.add(curr1);
            map.put(curr2, curr2Set);
        }
        total = 0;
        HashSet<String> path = new HashSet<>();
        path.add(start);
        dfsPart2(start, path, false);
        return total;
    }

    private void dfsPart2(String curr, HashSet<String> path, boolean repeated) {
        for (String child: map.get(curr)) {
            if (big.matcher(child).matches()) {
                HashSet<String> newPath = new HashSet<>(path);
                newPath.add(child);
                dfsPart2(child, newPath, repeated);
            } else {
                if (start.equals(child)) {
                    continue;
                }
                if (end.equals(child)) {
                    total++;
                    continue;
                }
                if (path.contains(child)) {
                    if (!repeated) {
                        HashSet<String> newPath = new HashSet<>(path);
                        newPath.add(child);
                        dfsPart2(child, newPath, true);
                    }
                } else {
                    HashSet<String> newPath = new HashSet<>(path);
                    newPath.add(child);
                    dfsPart2(child, newPath, repeated);
                }
            }
        }
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
            return st.nextToken("-");
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
