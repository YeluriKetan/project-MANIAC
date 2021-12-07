import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
        int[] arr = new int[200];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = fs.nextInt();
        }
        Arrays.sort(arr);
        int low = 0;
        int high = arr.length - 1;
        while (low < high) {
            switch (Integer.compare(arr[low] + arr[high], 2020)) {
            case -1:
                low++;
                break;
            case 0:
                return arr[low] * arr[high];
            case 1:
                high--;
                break;
            default:
                break;
            }
        }
        return -1;
    }

    private int part2() {
        int[] arr = new int[200];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = fs.nextInt();
        }
        Arrays.sort(arr);
        int fix = 0;
        int low, high;
        while (fix < arr.length - 2) {
            low = fix + 1;
            high = arr.length - 1;
            while (low < high) {
                switch (Integer.compare(arr[fix] + arr[low] + arr[high], 2020)) {
                case -1:
                    low++;
                    break;
                case 0:
                    return arr[fix] * arr[low] * arr[high];
                case 1:
                    high--;
                    break;
                default:
                    break;
                }
            }
            fix++;
        }
        return -1;
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
