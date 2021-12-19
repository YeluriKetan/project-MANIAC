import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Solution {

    private static FastScan fs;
    private static final long mod = 20201227;

    public static void main(String[] args) {
        Solution sol = new Solution();
        File file = new File(Paths.get("src", "input.txt").toString());

        fs = new FastScan(file);
        System.out.println(sol.part1());
        fs.close();
    }

    private long part1() {
        long cardPublicKey = fs.nextInt();
        int cardLoopSize = getLoopSize(cardPublicKey);
        long doorPublicKey = fs.nextInt();
        return getEncryptionKey(cardLoopSize, doorPublicKey);
    }

    private int getLoopSize(long publicKey) {
        long curr = 1;
        int loopSize = 0;
        while (curr != publicKey) {
            loopSize += 1;
            curr *= 7;
            curr %= mod;
        }
        return loopSize;
    }

    private long getEncryptionKey(int loopSize, long publicKey) {
        long curr = 1;
        for (int i = 0; i < loopSize; i++) {
            curr *= publicKey;
            curr %= mod;
        }
        return curr;
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
