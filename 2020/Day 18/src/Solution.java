import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Stack;

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

    private int index;
    private String curr;

    private long part1() {
        long total = 0;
        while ((curr = fs.next()) != null) {
            index = 0;
            total += part1Helper();
        }
        return total;
    }

    private long part1Helper() {
        long total = 0;
        char operation = '+';
        char currChar;
        while (index < curr.length()) {
            currChar = curr.charAt(index);
            if (currChar == ' ') {
                index++;
                continue;
            }
            if (currChar == '+' || currChar == '*') {
                operation = currChar;
                index++;
                continue;
            }
            if (currChar == ')') {
                break;
            }
            long currVal;
            if (currChar == '(') {
                index++;
                currVal = part1Helper();
            } else {
                currVal = currChar - '0';
            }
            if (operation == '+') {
                total += currVal;
            } else {
                total *= currVal;
            }
            index++;
        }
        return total;
    }

    private long part2() {
        long total = 0;
        while ((curr = fs.next()) != null) {
            index = 0;
            total += part2Helper();
        }
        return total;
    }

    private long part2Helper() {
        Stack<Long> stack = new Stack<>();
        boolean addition = false;
        char currChar;
        while (index < curr.length()) {
            currChar = curr.charAt(index);
            if (currChar == ' ' || currChar == '*') {
                index++;
                continue;
            }
            if (currChar == '+') {
                addition = true;
                index++;
                continue;
            }
            if (currChar == ')') {
                break;
            }
            long currVal;
            if (currChar == '(') {
                index++;
                currVal = part2Helper();
            } else {
                currVal = currChar - '0';
            }
            if (addition) {
                stack.push(stack.pop() + currVal);
                addition = false;
            } else {
                stack.push(currVal);
            }
            index++;
        }
        long total = 1;
        while (!stack.isEmpty()) {
            total *= stack.pop();
        }
        return total;
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
