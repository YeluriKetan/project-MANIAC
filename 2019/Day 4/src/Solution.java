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
        File file = new File(Paths.get("src", "input.txt").toString());

        fs = new FastScan(file);
        System.out.println(sol.part1());

        fs.changeFile(file);
        System.out.println(sol.part2());
        fs.close();
    }

    private int part1() {
        int low = fs.nextInt() + 1;
        int high = fs.nextInt();
        int count = 0;
        while (low < high) {
            if (isValidPart1(low++)) {
                count++;
            }
        }
        return count;
    }

    private boolean isValidPart1(int curr) {
        int prevInt;
        int currInt = curr % 10;
        curr /= 10;
        boolean adjSame = false;
        while (curr > 0) {
            prevInt = currInt;
            currInt = curr % 10;
            curr /= 10;
            if (prevInt == currInt) {
                adjSame = true;
            }
            if (prevInt < currInt) {
                return false;
            }
        }
        return adjSame;
    }

    private int part2() {
        int low = fs.nextInt() + 1;
        int high = fs.nextInt();
        int count = 0;
        while (low < high) {
            if (isValidPart2(low++)) {
                count++;
            }
        }
        return count;
    }

    private boolean isValidPart2(int curr) {
        int prevInt;
        int prevCount = 1;
        int currInt = curr % 10;
        curr /= 10;
        boolean adjSame = false;
        while (curr > 0) {
            prevInt = currInt;
            currInt = curr % 10;
            curr /= 10;
            if (currInt == prevInt) {
                prevCount++;
            } else {
                if (prevCount == 2) {
                    adjSame = true;
                }
                prevCount = 1;
            }
            if (currInt > prevInt) {
                return false;
            }
        }
        if (prevCount == 2) {
            adjSame = true;
        }
        return adjSame;
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
            return st.nextToken("-");
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
