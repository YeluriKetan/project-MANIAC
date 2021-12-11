import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

    private static FastScan fs;
    private static final Pattern address = Pattern.compile("\\d+");

    public static void main(String[] args) {
        Solution sol = new Solution();
        File file = new File(Paths.get("src", "input.txt").toString());

        fs = new FastScan(file);
        System.out.println(sol.part1());

        fs.changeFile(file);
        System.out.println(sol.part2());
        fs.close();
    }

    private long part1() {
        String curr;
        long maskZeroes = -1;
        long maskOnes = 0;
        HashMap<Long, Long> memMap = new HashMap<>();
        for (int i = 0; i < 579; i++) {
            curr = fs.next();
            if (curr.charAt(1) == 'a') {
                Pair newMask = parseMask(curr.substring(7));
                maskZeroes = newMask.a;
                maskOnes = newMask.b;
                continue;
            }

            Pair memVal = parseMemAndVal(curr);
            memMap.put(memVal.a, (memVal.b & maskZeroes) | maskOnes);
        }
        long sum = 0;
        for (Long currVal: memMap.values()) {
            sum += currVal;
        }
        return sum;
    }

    private Pair parseMask(String mask) {
        long zeroes = -1; // all ones
        long ones = 0; // all zeroes
        for (int i = 0; i < mask.length(); i++) {
            ones <<= 1;
            zeroes <<= 1;
            if (mask.charAt(i) != 'X') {
                ones += mask.charAt(i) - '0';
                zeroes += mask.charAt(i) - '0';
            } else {
                zeroes += 1;
            }
        }
        return new Pair(zeroes, ones);
    }

    private Pair parseMemAndVal(String memAndVal) {
        Pair parsed = new Pair();
        Matcher matcher = address.matcher(memAndVal);
        matcher.find();
        parsed.a = Long.parseLong(matcher.group());
        matcher.find();
        parsed.b = Long.parseLong(matcher.group());
        return parsed;
    }

    private char[] currMask; // mask for part 2
    private int currTotalFloat; // total number of possibilities that the memory can take with the above mask

    private long part2() {
        currMask = new char[0];
        currTotalFloat = 0;
        String curr;
        HashMap<Long, Long> memMap = new HashMap<>(); // memory map
        for (int i = 0; i < 579; i++) {
            curr = fs.next();
            if (curr.charAt(1) == 'a') { // if second letter of the instruction is 'a' => mask instruction
                currMask = curr.toCharArray(); // set mask
                currTotalFloat = 0;
                for (int j = 0; j < currMask.length; j++) {
                    if (currMask[j] == 'X') {
                        currTotalFloat++;
                    }
                }
                currTotalFloat = (int) Math.pow(2, currTotalFloat); // set number of float possibilities
                // n number of floats => 2^n possibilities
                continue; // move on to the next iteration
            }
            Pair memVal = parseMemAndVal(curr); // get memory address and value
            long currMemAddress; // current intended memory address
            long currFloatAddress; // current floated address combined with a float possibility
            int currFloatValue; // current combined value that the floats together can take
            for (int j = 0; j < currTotalFloat; j++) { // for each of the 2^n possibilities
                currFloatValue = j; // use iterations from 0 to 2^n - 1 as the combined values that float can take
                currFloatAddress = 0; // initialise the current floated address to 0, can start building now
                currMemAddress = memVal.a; // set to the intended address
                for (int k = currMask.length - 1; k > -1; k--) { // for each of the 36 elements of the mask in reverse order
                    currFloatAddress <<= 1;
                    switch (currMask[k]) {
                    case '0': { // if mask is 0, get unchanged value from intended memory address
                        currFloatAddress += (currMemAddress & 1);
                        break;
                    }
                    case '1': { // if mask is 1, set to 1
                        currFloatAddress += 1;
                        break;
                    }
                    case 'X': { // if mask is X, take the bit from the current float value
                        currFloatAddress += (currFloatValue & 1);
                        currFloatValue >>= 1;
                        break;
                    }
                    default:
                        break;
                    }
                    currMemAddress >>= 1;
                }
                memMap.put(currFloatAddress, memVal.b); // for the current floated address, put the value in the memory Map
            }
        }
        long sum = 0;
        for (Long currVal: memMap.values()) {
            sum += currVal;
        }
        return sum;
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

    private static class Pair {
        private long a;
        private long b;

        private Pair() {
        }

        private Pair(long a, long b) {
            this.a = a;
            this.b = b;
        }
    }
}
