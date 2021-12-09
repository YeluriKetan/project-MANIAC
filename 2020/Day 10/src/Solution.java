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
        int one = 0;
        int three = 0;
        int[] arr = new int[94];
        for (int i = 0; i < 94; i++) {
            arr[i] = fs.nextInt();
        }
        Arrays.sort(arr);
        for (int i = 1; i < 94; i++) {
            switch (arr[i] - arr[i - 1]) {
            case 1:
                one++;
                break;
            case 3:
                three++;
                break;
            default:
                break;
            }
        }
        if (arr[0] == 1) {
            one++;
        } else if (arr[0] == 3) {
            three++;
        }
        three++;
        return one * three;
    }

    private long part2() {
        int[] arr = new int[95];
        for (int i = 0; i < arr.length - 1; i++) {
            arr[i] = fs.nextInt();
        }
        arr[arr.length - 1] = 0; // add charging outlet
        Arrays.sort(arr);
        long[] memo = new long[arr.length]; // memoize the number of possibilities
        memo[memo.length - 1] = 1; // number of possibilities from the highest rated adapter is only 1 => to your device
        for (int i = memo.length - 2; i > -1; i--) {
            for (int j = i + 1; j < memo.length; j++) {
                if (arr[j] > arr[i] + 3) {
                    break;
                }
                memo[i] += memo[j];
            }
        }
        return memo[0]; // return number of possibilities from charging outlet
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
