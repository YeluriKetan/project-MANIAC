import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
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
        int[] arr = new int[1000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = fs.nextInt();
        }
        Arrays.sort(arr);
        int median = (arr[499] + arr[500]) / 2;
        int total = 0;
        for (int i = 0; i < arr.length; i++) {
            total += Math.abs(arr[i] - median);
        }
        return total;
    }

    private long part2() {
        int[] arr = new int[1000];
        int total = 0;
        for (int i = 0; i < arr.length; i++) {
            arr[i] = fs.nextInt();
            total += arr[i];
        }
        int mean = total / arr.length;
        total = 0;
        int curr;
        for (int i = 0; i < arr.length; i++) {
            curr = Math.abs(arr[i] - mean);
            total += (curr * (curr + 1)) / 2;
        }
        return total;
    }

    private static class FastScan {
        private BufferedReader br;
        private StringTokenizer st;

        private FastScan(File file) {
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void changeFile(File file) {
            close();
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return st.nextToken(",");
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public void close() {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
