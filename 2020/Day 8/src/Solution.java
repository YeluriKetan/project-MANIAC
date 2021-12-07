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

    public static void main(String[] args) {
        Solution sol = new Solution();
        File file = new File(Paths.get("src", "input.txt").toString());

        fs = new FastScan(file);
        System.out.println(sol.part1());

        fs.changeFile(file);
        System.out.println(sol.part2());
        fs.close();
    }

    private static String[] instr;
    private static int[] val;

    private int part1() {
        instr = new String[601];
        val = new int[601];
        for (int i = 0; i < 601; i++) {
            instr[i] = fs.next();
            val[i] = fs.nextInt();
        }
        int currIndex = 0;
        int accum = 0;
        while (instr[currIndex] != null) {
            switch (instr[currIndex]) {
            case "acc":
                instr[currIndex] = null;
                accum += val[currIndex];
                currIndex++;
                break;
            case "jmp":
                instr[currIndex] = null;
                currIndex += val[currIndex];
                break;
            case "nop":
                instr[currIndex] = null;
                currIndex++;
                break;
            default:
                break;
            }
        }
        return accum;
    }

    private int part2() {
        instr = new String[601];
        val = new int[601];
        for (int i = 0; i < 601; i++) {
            instr[i] = fs.next();
            val[i] = fs.nextInt();
        }
        int currIndex = 0;
        int accum = 0;
        HashSet<Integer> visited = new HashSet<>();
        while (!visited.contains(currIndex)) {
            visited.add(currIndex);
            switch (instr[currIndex]) {
            case "acc": {
                accum += val[currIndex];
                currIndex++;
                break;
            }
            case "jmp": {
                int alt = simulator(accum, currIndex, new HashSet<>(visited), true);
                if (alt != -1) {
                    return alt;
                }
                currIndex += val[currIndex];
                break;
            }
            case "nop": {
                int alt = simulator(accum, currIndex, new HashSet<>(visited), false);
                if (alt != -1) {
                    return alt;
                }
                currIndex++;
                break;
            }
            default:
                break;
            }
        }
        return accum;
    }

    private int simulator(int accum, int currIndex, HashSet<Integer> visited, boolean replacement) {
        if (replacement) {
            currIndex++;
        } else {
            currIndex += val[currIndex];
        }
        while (!visited.contains(currIndex) && currIndex < 601) {
            visited.add(currIndex);
            switch (instr[currIndex]) {
            case "acc": {
                accum += val[currIndex];
                currIndex++;
                break;
            }
            case "jmp": {
                currIndex += val[currIndex];
                break;
            }
            case "nop": {
                currIndex++;
                break;
            }
            default:
                break;
            }
        }
        if (currIndex > 600) {
            return accum;
        } else {
            return -1;
        }
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
