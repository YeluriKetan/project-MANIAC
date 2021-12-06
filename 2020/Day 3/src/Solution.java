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

        fs.changeFile(file);
        System.out.println(part2());
        fs.close();
    }

    private static int part1() {
        int total = 0;
        int pointer = 0;
        String curr;
        for (int i = 0; i < 323; i++) {
            curr = fs.next();
            if (curr.charAt(pointer) == '#') {
                total++;
            }
            pointer += 3;
            pointer %= 31;
        }
        return total;
    }

    private static long part2() {
        int[] right = {1,3,5,7,1};
        int[] total = new int[5];
        int[] pointer = new int[5];
        String curr;
        for (int i = 0; i < 323; i++) {
            curr = fs.next();
            for (int j = 0; j < 4; j++) {
                if (curr.charAt(pointer[j]) == '#') {
                    total[j]++;
                }
                pointer[j] = (pointer[j] + right[j]) % 31;
            }
            if (i % 2 == 0) {
                if (curr.charAt(pointer[4]) == '#') {
                    total[4]++;
                }
                pointer[4] = (pointer[4] + right[4]) % 31;
            }
        }
        long ans = 1;
        for (int i = 0; i < 5; i++) {
            ans *= total[i];
        }
        return ans;
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

        public void changeFile(File file) {
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

        public void close() {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
