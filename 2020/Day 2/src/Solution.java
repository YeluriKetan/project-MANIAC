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
        Solution sol = new Solution();
        File file = new File(Paths.get("src", "inputModified.txt").toString());

        fs = new FastScan(file);
        System.out.println(sol.part1());

        fs.changeFile(file);
        System.out.println(sol.part2());
        fs.close();
    }

    private int part1() {
        int total = 0;
        int low, high, count;
        char req;
        String password;
        for (int i = 0; i < 1000; i++) {
            low = fs.nextInt();
            high = fs.nextInt();
            req = fs.next().charAt(0);
            password = fs.next();
            count = 0;
            for (int j = 0; j < password.length(); j++) {
                if (password.charAt(j) == req) {
                    count++;
                }
            }
            if (low <= count && count <= high) {
                total++;
            }
        }
        return total;
    }

    private int part2() {
        int total = 0;
        int low, high, count;
        char req;
        String password;
        for (int i = 0; i < 1000; i++) {
            low = fs.nextInt();
            high = fs.nextInt();
            req = fs.next().charAt(0);
            password = fs.next();
            count = 0;
            if (password.charAt(low - 1) == req) {
                count++;
            }
            if (password.charAt(high - 1) == req) {
                count++;
            }
            if (count == 1) {
                total++;
            }
        }
        return total;
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
