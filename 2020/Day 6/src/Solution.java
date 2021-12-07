import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.BitSet;

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
        BitSet set = new BitSet(26);
        String curr;
        int currIndex;
        int count = 0;
        for (int i = 0; i < 2278; i++) {
            curr = fs.next();
            if (curr == null || curr.isBlank()) {
                set.clear();
                continue;
            }
            for (int j = 0; j < curr.length(); j++) {
                currIndex = curr.charAt(j) - 'a';
                if (!set.get(currIndex)) {
                    set.set(currIndex);
                    count++;
                }
            }
        }
        return count;
    }

    private int part2() {
        int[] tracker = new int[26];
        String curr;
        int groupCount = 0;
        int count = 0;
        for (int i = 0; i < 2278; i++) {
            curr = fs.next();
            if (curr == null || curr.isBlank()) {
                for (int j = 0; j < tracker.length; j++) {
                    if (tracker[j] == groupCount) {
                        count++;
                    }
                    tracker[j] = 0;
                }
                groupCount = 0;
                continue;
            }
            groupCount++;
            for (int j = 0; j < curr.length(); j++) {
                tracker[curr.charAt(j) - 'a']++;
            }
        }
        return count;
    }

    private static class FastScan {
        private BufferedReader br;

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

        private String next() {
            try {
                return br.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
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
