import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

public class Solution {

    private static FastScan fs;
    private static final int[] prime = {2, 3, 5, 7, 11, 13, 17};

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
        String curr;
        String[] arr;
        int ans = 0;
        for (int i = 0; i < 200; i++) {
            curr = fs.next();
            arr = curr.split(" \\| ")[1].split(" ");
            for (int j = 0; j < arr.length; j++) {
                if (arr[j].length() == 2 || arr[j].length() == 3 || arr[j].length() == 4 || arr[j].length() == 7) {
                    ans++;
                }
            }
        }
        return ans;
    }

    private int part2() {
        String curr;
        String[] arr;
        int ans = 0; // add all output values here
        for (int i = 0; i < 200; i++) {
            curr = fs.next();
            arr = curr.split(" \\| "); // split into pattern and output
            ans += decodeSum(arr); // get 4 digit decimal value for this entry
        }
        return ans;
    }

    private int decodeSum(String[] arr) {
        String[] pattern = arr[0].split(" "); // all 10 unique signal patterns
        int[] patternHash = new int[10]; // custom hashed values for the above
        for (int i = 0; i < 10; i++) {
            patternHash[i] = customHash(pattern[i]);
        }
        HashMap<Integer, Integer> mixedMap = new HashMap<>(); // map custom hashed value with decimal value
        int one = 0;
        int seven = 0;
        int four = 0;
        for (int i = 0; i < 10; i++) {
            switch (pattern[i].length()) {
            case 2:
                one = patternHash[i]; // length of one is 2
                mixedMap.put(one, 1);
                break;
            case 3:
                seven = patternHash[i]; // length of seven is 3
                mixedMap.put(seven, 7);
                break;
            case 4:
                four = patternHash[i]; // length of 4 is 4
                mixedMap.put(four, 4);
                break;
            }
        }
        mixedMap.put(510510, 8); // 8 always custom hashes to 510510
        int top = seven / one; // find top segment
        int bottom = top * four; // temp value used to find bottom segment
        int nine = 0;
        for (int i = 0; i < patternHash.length; i++) {
            if (patternHash[i] != 510510 && patternHash[i] % bottom == 0) { // contains top + 4 and not 8 => 9
                mixedMap.put(patternHash[i], 9);
                bottom = patternHash[i] / bottom; // find exact bottom value
                nine = patternHash[i]; // place nine
                break;
            }
        }
        int mid = one * top * bottom; // temp value to find mid segment
        int topLeft = 0;
        for (int i = 0; i < patternHash.length; i++) {
            if (patternHash[i] % mid == 0 && isSingleSegment(patternHash[i] / mid)) {
                // contains top + 1 + bottom and has one extra segment => 3
                mixedMap.put(patternHash[i], 3);
                mid = patternHash[i] / mid; // find exact mid value
            } else if (nine % patternHash[i] == 0 && isSingleSegment(nine / patternHash[i])) {
                // contains nine and is short of one segment from nine, but not 3 => 5
                mixedMap.put(patternHash[i], 5);
                topLeft = nine / patternHash[i]; // find exact topLeft
            }
        }
        mixedMap.put(510510 / mid, 0); // 0 is mid removed from 8
        mixedMap.put(510510 / topLeft, 6); // 6 is topLeft removed from 8
        // mixedMap contains custom hashed value mappings for numbers 0,1,3,4,5,6,7,8,9 (only 2 missing)
        int outputValue = 0;
        String[] output = arr[1].split(" "); // for the 4 signals in output
        for (String s : output) {
            outputValue *= 10;
            // get mapped decimal value or 2
            outputValue += mixedMap.getOrDefault(customHash(s), 2);
        }
        return outputValue; // return 4 digit decimal value for this entry
    }

    private int customHash(String curr) {
        int hash = 1;
        for (int i = 0; i < curr.length(); i++) {
            hash *= prime[curr.charAt(i) - 'a'];
        }
        return hash;
    }

    private boolean isSingleSegment(int curr) {
        for (int j : prime) {
            if (curr == j) {
                return true;
            }
        }
        return false;
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
