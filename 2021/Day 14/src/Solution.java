import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

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
        HashMap<String, Integer> pairCounterMap = new HashMap<>();
        String start = fs.next();
        for (int i = 0; i < start.length() - 1; i++) {
            pairCounterMap.merge(start.substring(i, i + 2), 1, Integer::sum);
        }
        fs.next();
        HashMap<String, Character> pairInsertionMap = new HashMap<>();
        String curr;
        for (int i = 0; i < 100; i++) {
            curr = fs.next();
            pairInsertionMap.put(curr.substring(0,2), curr.charAt(6));
        }
        HashMap<String, Integer> newCounterMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            for (String currKey: pairCounterMap.keySet()) {
                char currInsert = pairInsertionMap.get(currKey);
                String one = "" + currKey.charAt(0) + currInsert;
                newCounterMap.merge(one, pairCounterMap.get(currKey), Integer::sum);
                String two = "" + currInsert + currKey.charAt(1);
                newCounterMap.merge(two, pairCounterMap.get(currKey), Integer::sum);
            }
            HashMap<String, Integer> holder = pairCounterMap;
            pairCounterMap = newCounterMap;
            newCounterMap = holder;
            newCounterMap.clear();
        }
        int[] counter = new int[26];
        for (String currKey: pairCounterMap.keySet()) {
            counter[currKey.charAt(0) - 'A'] += pairCounterMap.get(currKey);
            counter[currKey.charAt(1) - 'A'] += pairCounterMap.get(currKey);
        }
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int currInt: counter) {
            if (currInt == 0) {
                continue;
            }
            max = Math.max(max, currInt);
            min = Math.min(min, currInt);
        }
        max = (max + 1) / 2;
        min = (min + 1) / 2;
        return max - min;
    }

    private long part2() {
        HashMap<String, Long> pairCounterMap = new HashMap<>();
        String start = fs.next();
        for (int i = 0; i < start.length() - 1; i++) {
            pairCounterMap.merge(start.substring(i, i + 2), (long) 1, Long::sum);
        }
        fs.next();
        HashMap<String, Character> pairInsertionMap = new HashMap<>();
        String curr;
        for (int i = 0; i < 100; i++) {
            curr = fs.next();
            pairInsertionMap.put(curr.substring(0,2), curr.charAt(6));
        }
        HashMap<String, Long> newCounterMap = new HashMap<>();
        for (int i = 0; i < 40; i++) {
            for (String currKey: pairCounterMap.keySet()) {
                char currInsert = pairInsertionMap.get(currKey);
                String one = "" + currKey.charAt(0) + currInsert;
                newCounterMap.merge(one, pairCounterMap.get(currKey), Long::sum);
                String two = "" + currInsert + currKey.charAt(1);
                newCounterMap.merge(two, pairCounterMap.get(currKey), Long::sum);
            }
            HashMap<String, Long> holder = pairCounterMap;
            pairCounterMap = newCounterMap;
            newCounterMap = holder;
            newCounterMap.clear();
        }
        long[] counter = new long[26];
        for (String currKey: pairCounterMap.keySet()) {
            counter[currKey.charAt(0) - 'A'] += pairCounterMap.get(currKey);
            counter[currKey.charAt(1) - 'A'] += pairCounterMap.get(currKey);
        }
        long max = Long.MIN_VALUE;
        long min = Long.MAX_VALUE;
        for (long currLong: counter) {
            if (currLong == 0) {
                continue;
            }
            max = Math.max(max, currLong);
            min = Math.min(min, currLong);
        }
        max = (max + 1) / 2;
        min = (min + 1) / 2;
        return max - min;
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
