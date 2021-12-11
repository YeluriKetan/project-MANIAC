import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
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
        String[] inputArr = fs.next().split(",");
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inputArr.length - 1; i++) {
            map.put(Integer.parseInt(inputArr[i]), i);
        }
        int prev = Integer.parseInt(inputArr[inputArr.length - 1]);
        int curr;
        for (int i = inputArr.length; i < 2020; i++) {
            curr = map.getOrDefault(prev, i - 1);
            map.put(prev, i - 1);
            prev = i - 1 - curr;
        }
        return prev;
    }

    private int part2() {
        String[] inputArr = fs.next().split(",");
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inputArr.length - 1; i++) {
            map.put(Integer.parseInt(inputArr[i]), i);
        }
        int prev = Integer.parseInt(inputArr[inputArr.length - 1]);
        int curr;
        for (int i = inputArr.length; i < 30000000; i++) {
            curr = map.getOrDefault(prev, i - 1);
            map.put(prev, i - 1);
            prev = i - 1 - curr;
        }
        return prev;
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

        private void close() {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
