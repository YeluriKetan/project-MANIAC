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
        File file = new File(Paths.get("src","input.txt").toString());
        fs = new FastScan(file);
        System.out.println(part1());
        fs.close();
        fs = new FastScan(file);
        System.out.println(part2());
        fs.close();
    }

    private static int part1() {
        int prev = fs.nextInt();
        int curr;
        int count = 0;
        for (int i = 2; i <= 2000; i++) {
            curr = fs.nextInt();
            if (curr > prev) {
                count++;
            }
            prev = curr;
        }
        return count;
    }

    private static int part2() {
        int a, b, c, d, count;
        a = fs.nextInt();
        b = fs.nextInt();
        c = fs.nextInt();
        count = 0;
        for (int i = 4; i <= 2000; i++) {
            d = fs.nextInt();
            if (d > a) {
                count++;
            }
            a = b;
            b = c;
            c = d;
        }
        return count;
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
