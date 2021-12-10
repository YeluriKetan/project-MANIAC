import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;
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
        String curr;
        int sum = 0;
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < 94; i++) {
            curr = fs.next();
            stack.clear();
            for (int j = 0; j < curr.length(); j++) {
                char currChar = curr.charAt(j);
                switch (currChar) {
                case '(':
                case '{':
                case '[':
                case '<': {
                    stack.push(currChar);
                    break;
                }
                case ')': {
                    if (stack.peek() == '(') {
                        stack.pop();
                    } else {
                        sum += 3;
                        j = curr.length();
                    }
                    break;
                }
                case ']': {
                    if (stack.peek() == '[') {
                        stack.pop();
                    } else {
                        sum += 57;
                        j = curr.length();
                    }
                    break;
                }
                case '}': {
                    if (stack.peek() == '{') {
                        stack.pop();
                    } else {
                        sum += 1197;
                        j = curr.length();
                    }
                    break;
                }
                case '>': {
                    if (stack.peek() == '<') {
                        stack.pop();
                    } else {
                        sum += 25137;
                        j = curr.length();
                    }
                    break;
                }
                default:
                    break;
                }
            }
        }
        return sum;
    }

    private long part2() {
        String curr;
        ArrayList<Long> sums = new ArrayList<>();
        Stack<Character> stack = new Stack<>();
        long currSum;
        char currChar;
        boolean incomplete;
        for (int i = 0; i < 94; i++) {
            curr = fs.next();
            stack.clear();
            incomplete = true;
            for (int j = 0; j < curr.length(); j++) {
                currChar = curr.charAt(j);
                switch (currChar) {
                case '(':
                case '{':
                case '[':
                case '<': {
                    stack.push(currChar);
                    break;
                }
                case ')': {
                    if (stack.peek() == '(') {
                        stack.pop();
                    } else {
                        incomplete = false;
                        j = curr.length();
                    }
                    break;
                }
                case ']': {
                    if (stack.peek() == '[') {
                        stack.pop();
                    } else {
                        incomplete = false;
                        j = curr.length();
                    }
                    break;
                }
                case '}': {
                    if (stack.peek() == '{') {
                        stack.pop();
                    } else {
                        incomplete = false;
                        j = curr.length();
                    }
                    break;
                }
                case '>': {
                    if (stack.peek() == '<') {
                        stack.pop();
                    } else {
                        incomplete = false;
                        j = curr.length();
                    }
                    break;
                }
                default:
                    break;
                }
            }
            if (!incomplete) {
                continue;
            }
            currSum = 0;
            while (!stack.isEmpty()) {
                currChar = stack.pop();
                currSum *= 5;
                switch (currChar) {
                case '(':
                    currSum += 1;
                    break;
                case '[':
                    currSum += 2;
                    break;
                case '{':
                    currSum += 3;
                    break;
                case '<':
                    currSum += 4;
                    break;
                default:
                    break;
                }
            }
            sums.add(currSum);
        }
        sums.sort(Long::compareTo);
        return sums.get(sums.size() / 2);
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

        private void close() {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
