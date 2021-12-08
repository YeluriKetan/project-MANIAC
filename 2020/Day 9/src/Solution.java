import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Solution {

    private static FastScan fs;

    public static void main(String[] args) {
        Solution sol = new Solution();
        File file = new File(Paths.get("src", "input.txt").toString());

        fs = new FastScan(file);
        long a = sol.part1();
        System.out.println(a);

        fs.changeFile(file);
        System.out.println(sol.part2(a));
        fs.close();
    }

    private long part1() {
        ArrayList<Long> queue = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            queue.add(fs.nextLong());
        }
        long curr;
        long temp;
        for (int i = 0; i < 975; i++) {
            curr = fs.nextLong();
            boolean found = false;
            for (int j = 0; j < 24; j++) {
                temp = queue.get(j);
                if (temp == curr - temp) {
                    continue;
                }
                if (queue.contains(curr - temp)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return curr;
            } else {
                queue.remove(0);
                queue.add(curr);
            }
        }
        return -1;
    }

    private long part2(long req) {
        ArrayList<Long> queue = new ArrayList<>();
        long sum = 0;
        long curr;
        for (int i = 0; i < 1000; i++) {
            curr = fs.nextLong();
            switch (Long.compare(sum, req)) {
            case -1: {
                sum += curr;
                queue.add(curr);
                break;
            }
            case 0: {
                if (queue.size() > 1) {
                    queue.sort(Long::compare);
                    return queue.get(0) + queue.get(queue.size() - 1);
                } else {
                    while (sum > req) {
                        sum -= queue.get(0);
                        queue.remove(0);
                    }
                    sum += curr;
                    queue.add(curr);
                    break;
                }
            }
            case 1: {
                while (sum > req) {
                    sum -= queue.get(0);
                    queue.remove(0);
                }
                if (sum == req && queue.size() > 1) {
                    queue.sort(Long::compare);
                    return queue.get(0) + queue.get(queue.size() - 1);
                }
                sum += curr;
                queue.add(curr);
                break;
            }
            default:
                break;
            }
        }
        return -1;
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

        private long nextLong() {
            return Long.parseLong(next());
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
