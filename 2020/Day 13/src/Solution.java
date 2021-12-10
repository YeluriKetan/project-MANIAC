import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Solution {

    private static FastScan fs;
    private static final String skip = "x";

    public static void main(String[] args) {
        Solution sol = new Solution();
        File file = new File(Paths.get("src", "input.txt").toString());

        fs = new FastScan(file);
        System.out.println(sol.part1());

        fs.changeFile(file);
        System.out.println(sol.part2());
        fs.close();
    }

    private int part1() { // find the element that is divisible using the least offset from the given timestamp
        int min = Integer.MAX_VALUE;
        int minId = -1;
        int start = fs.nextInt();
        String[] arr = fs.next().split(",");
        int curr;
        for (String s : arr) {
            if (skip.equals(s)) {
                continue;
            }
            curr = Integer.parseInt(s);
            if (curr - start % curr < min) {
                min = curr - start % curr;
                minId = curr;
            }
        }
        return minId * min;
    }

    private long part2() {
        fs.next(); // skip line 1
        String[] strArr = fs.next().split(",");
        HashMap<Integer, Integer> map = new HashMap<>(); // add all mappings of id to index
        ArrayList<Integer> list = new ArrayList<>(); // add all ids
        int currInt;
        for (int i = 0; i < strArr.length; i++) {
            if (skip.equals(strArr[i])) {
                continue;
            }
            currInt = Integer.parseInt(strArr[i]);
            list.add(currInt);
            map.put(currInt, i);
        }
        list.sort(Integer::compareTo); // sort ids
        int max = list.get(list.size() - 1);
        long interval = max;
        // intervals are chosen in such a way that after every interval from curr, the same pattern
        // we have accumulated occurs again. Start with the biggest element as interval
        long curr = 0;
        // curr is the first timestamp from 0 that the accumulated pattern (w.r.t to the max element) occurs.
        // Start from 0 since all buses start together here
        int temp, diff; // temp variables
        for (int i = list.size() - 2; i > -1; i--) {
            temp = list.get(i); // get current element, go in descending order
            diff = map.get(max) - map.get(temp); // find the difference in indices
            while ((curr - diff) % temp != 0) { // check if curr - difference is a multiple of temp
                curr += interval; // if not move to the next interval
            }
            interval *= temp; // update interval to take into account of temp as well
        }
        return curr - (map.get(max) - map.get(list.get(0)));
        // return first timestamp where the first element in the pattern occurs;
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
