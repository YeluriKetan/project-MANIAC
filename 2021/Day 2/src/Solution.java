import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Solution {

    private static FastScan fs;

    public static void main(String[] args) {
        File file = new File(Paths.get("src", "input.txt").toString());
        fs = new FastScan(file);
        System.out.println(part1());
        fs.close();
        fs = new FastScan(file);
        System.out.println(part2());
        fs.close();
    }

    private static int part1() {
        String currCommand;
        int currInt;
        int forward = 0;
        int depth = 0;
        for (int i = 0; i < 1000; i++) {
            currCommand = fs.next();
            currInt = fs.nextInt();
            switch (currCommand) {
            case "forward":
                forward += currInt;
                break;
            case "down":
                depth += currInt;
                break;
            case "up":
                depth -= currInt;
                break;
            default:
                break;
            }
        }
        System.out.println(forward + ", " + depth);
        return forward * depth;
    }

    private static int part2() {
        String currCommand;
        int currInt;
        int forward = 0;
        int depth = 0;
        int aim = 0;
        for (int i = 0; i < 1000; i++) {
            currCommand = fs.next();
            currInt = fs.nextInt();
            switch (currCommand) {
            case "forward":
                forward += currInt;
                depth += aim * currInt;
                break;
            case "down":
                aim += currInt;
                break;
            case "up":
                aim -= currInt;
                break;
            default:
                break;
            }
        }
        System.out.println(forward + ", " + depth);
        return forward * depth;
    }

    private static class FastScan {
        private BufferedReader br;
        private StringTokenizer st;

        public FastScan(File file) {
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException ex) {
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
            return st.nextToken();
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
