import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Solution {

    private static FastScan fs;
    private static int[] ref = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};

    public static void main(String[] args) {
        File file = new File(Paths.get("src", "input.txt").toString());

        fs = new FastScan(file);
        int ans = part1();
        fs.close();
        System.out.println("Part 1 answer: " + ans);

        int a;
        fs.changeFile(file);
        a = part2a();
        fs.close();

        int b;
        fs.changeFile(file);
        b = part2b();
        fs.close();

        System.out.println("Part 2 answer: " + a * b);
    }

    private static int part1() {
        int currInt;
        int[] count = new int[12];
        for (int i = 0; i < 1000; i++) {
            currInt = fs.nextInt();
            for (int j = 11; j > -1; j--) {
                if ((currInt & 1) == 1) {
                    count[j] += 1;
                }
                currInt >>= 1;
            }
        }
        int gamma = 0;
        int epsilon = 0;
        for (int i = 0; i < 12; i++) {
            gamma <<= 1;
            epsilon <<= 1;
            if (count[i] > 500) {
                gamma += 1;
            } else {
                epsilon += 1;
            }
        }
        return gamma * epsilon;
    }

    private static int part2a() {
        HashSet<Integer> mainSet = new HashSet<>();
        HashSet<Integer> sideSet = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            mainSet.add(fs.nextInt());
        }
        int count;
        for (int i = 11; i > -1; i--) {
            if (mainSet.size() == 1) {
                break;
            }
            count = 0;
            for (Integer currInt: mainSet) {
                if ((currInt & ref[i]) > 0) {
                    count++;
                }
            }
            if (count >= mainSet.size() - count) {
                for (Integer currInt: mainSet) {
                    if ((currInt & ref[i]) > 0) {
                        sideSet.add(currInt);
                    }
                }
            } else {
                for (Integer currInt: mainSet) {
                    if ((currInt & ref[i]) == 0) {
                        sideSet.add(currInt);
                    }
                }
            }
            HashSet<Integer> temp = mainSet;
            mainSet = sideSet;
            temp.clear();
            sideSet = temp;
        }
        for (Integer currInt: mainSet) {
            return currInt;
        }
        return 0;
    }

    private static int part2b() {
        HashSet<Integer> mainSet = new HashSet<>();
        HashSet<Integer> sideSet = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            mainSet.add(fs.nextInt());
        }
        int count;
        for (int i = 11; i > -1; i--) {
            if (mainSet.size() == 1) {
                break;
            }
            count = 0;
            for (Integer currInt: mainSet) {
                if ((currInt & ref[i]) > 0) {
                    count++;
                }
            }
            if (count < mainSet.size() - count) {
                for (Integer currInt: mainSet) {
                    if ((currInt & ref[i]) > 0) {
                        sideSet.add(currInt);
                    }
                }
            } else {
                for (Integer currInt: mainSet) {
                    if ((currInt & ref[i]) == 0) {
                        sideSet.add(currInt);
                    }
                }
            }
            HashSet<Integer> temp = mainSet;
            mainSet = sideSet;
            temp.clear();
            sideSet = temp;
        }
        for (Integer currInt: mainSet) {
            return currInt;
        }
        return 0;
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

        public int nextInt() {
            return Integer.parseInt(next(), 2);
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
