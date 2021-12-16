import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

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

    private int version;
    private boolean[] arr;

    private int part1() {
        String main = fs.next();
        arr = new boolean[main.length() * 4];
        for (int i = 0; i < main.length(); i++) {
            int curr = Integer.parseInt(String.valueOf(main.charAt(i)), 16);
            for (int j = 4 * i + 3; j >= 4 * i; j--) {
                arr[j] = (curr & 1) == 1;
                curr >>= 1;
            }
        }
        version = 0;
        reader(0);
        return version;
    }

    private int reader(int index) {
        int currVersion = 0;
        for (int i = index; i < index + 3; i++) {
            currVersion <<= 1;
            if (arr[i]) {
                currVersion += 1;
            }
        }
        version += currVersion;
        int currId = 0;
        for (int i = index + 3; i < index + 6; i++) {
            currId <<= 1;
            if (arr[i]) {
                currId += 1;
            }
        }
        if (currId == 4) {
            int currIndex = index + 6;
            while (arr[currIndex]) {
                currIndex += 5;
            }
            return currIndex + 5;
        }
        int currIndex;
        if (arr[index + 6]) {
            int numOfSub = 0;
            for (int i = index + 7; i < index + 18; i++) {
                numOfSub <<= 1;
                if (arr[i]) {
                    numOfSub += 1;
                }
            }
            currIndex = index + 18;
            for (int i = 0; i < numOfSub; i++) {
                currIndex = reader(currIndex);
            }
        } else {
            int numOfBits = 0;
            for (int i = index + 7; i < index + 22; i++) {
                numOfBits <<= 1;
                if (arr[i]) {
                    numOfBits += 1;
                }
            }
            currIndex = index + 22;
            while (currIndex < index + 22 + numOfBits) {
                currIndex = reader(currIndex);
            }
        }
        return currIndex;
    }

    private long part2() {
        String main = fs.next();
        arr = new boolean[main.length() * 4];
        for (int i = 0; i < main.length(); i++) {
            int curr = Integer.parseInt(String.valueOf(main.charAt(i)), 16);
            for (int j = 4 * i + 3; j >= 4 * i; j--) {
                arr[j] = (curr & 1) == 1;
                curr >>= 1;
            }
        }
        return reader2(0).value;
    }

    private Pair reader2(int index) {
        int currId = 0;
        for (int i = index + 3; i < index + 6; i++) {
            currId <<= 1;
            if (arr[i]) {
                currId += 1;
            }
        }
        if (currId == 4) {
            int currIndex = index + 6;
            while (arr[currIndex]) {
                currIndex += 5;
            }
            long calculatedVal = calculateValue(index + 6, currIndex + 5);
            return new Pair(currIndex + 5, calculatedVal);
        }
        int currIndex;
        ArrayList<Long> list = new ArrayList<>();
        if (arr[index + 6]) {
            int numOfSub = 0;
            for (int i = index + 7; i < index + 18; i++) {
                numOfSub <<= 1;
                if (arr[i]) {
                    numOfSub += 1;
                }
            }
            currIndex = index + 18;
            for (int i = 0; i < numOfSub; i++) {
                Pair currPair = reader2(currIndex);
                currIndex = currPair.index;
                list.add(currPair.value);
            }
        } else {
            int numOfBits = 0;
            for (int i = index + 7; i < index + 22; i++) {
                numOfBits <<= 1;
                if (arr[i]) {
                    numOfBits += 1;
                }
            }
            currIndex = index + 22;
            while (currIndex < index + 22 + numOfBits) {
                Pair currPair = reader2(currIndex);
                currIndex = currPair.index;
                list.add(currPair.value);
            }
        }
        long ans = 0;
        switch (currId) {
        case 0:
            ans = sumAll(list);
            break;
        case 1:
            ans = productAll(list);
            break;
        case 2:
            ans = minimumAll(list);
            break;
        case 3:
            ans = maximumAll(list);
            break;
        case 5:
            ans = greaterThan(list);
            break;
        case 6:
            ans = lessThan(list);
            break;
        case 7:
            ans = equalTo(list);
            break;
        default:
            break;
        }
        return new Pair(currIndex, ans);
    }

    private long sumAll(ArrayList<Long> list) {
        long ans = 0;
        for (Long curr: list) {
            ans += curr;
        }
        return ans;
    }

    private long productAll(ArrayList<Long> list) {
        long ans = 1;
        for (Long curr: list) {
            ans *= curr;
        }
        return ans;
    }

    private long minimumAll(ArrayList<Long> list) {
        long ans = Long.MAX_VALUE;
        for (Long curr: list) {
            ans = Math.min(ans, curr);
        }
        return ans;
    }

    private long maximumAll(ArrayList<Long> list) {
        long ans = Long.MIN_VALUE;
        for (Long curr: list) {
            ans = Math.max(ans, curr);
        }
        return ans;
    }

    private long greaterThan(ArrayList<Long> list) {
        return (list.get(0) > list.get(1)) ? 1 : 0;
    }

    private long lessThan(ArrayList<Long> list) {
        return (list.get(0) < list.get(1)) ? 1 : 0;
    }

    private long equalTo(ArrayList<Long> list) {
        return (list.get(0) == list.get(1)) ? 1 : 0;
    }

    private long calculateValue(int start, int end) {
        long value = 0;
        for (int i = start; i < end; i += 5) {
            for (int j = i + 1; j < i + 5; j++) {
                value <<= 1;
                if (arr[j]) {
                    value += 1;
                }
            }
        }
        return value;
    }

    private static class Pair {
        private final int index;
        private final long value;

        private Pair(int index, long value) {
            this.index = index;
            this.value = value;
        }
    }

    private static class FastScan {
        private BufferedReader br;

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
