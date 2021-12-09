import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
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

    private int part1() {
        int min = Integer.MAX_VALUE;
        int minId = -1;
        int start = fs.nextInt();
        String[] arr = fs.next().split(",");
        int curr;
        for (int i = 0; i < arr.length; i++) {
            if (skip.equals(arr[i])) {
                continue;
            }
            curr = Integer.parseInt(arr[i]);
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
        HashMap<Integer, Integer> map = new HashMap<>();
        PriorityQueue<Integer> list = new PriorityQueue<>();
        for (int i = 0; i < strArr.length; i++) {
            if (skip.equals(strArr[i])) {
                continue;
            }
            list.add(Integer.parseInt(strArr[i]));
            map.put(, i);
        }
        long interval = list.poll();
        long curr = interval;
        int temp, diff;
        for (int i = list.size(); i > 0; i--) {
            temp = list.poll();
            diff = map.get(937) - map.get(temp);
            while ((curr - diff) % temp != 0) {
                curr += interval;
            }
            interval *= temp;
        }
        return curr;
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
