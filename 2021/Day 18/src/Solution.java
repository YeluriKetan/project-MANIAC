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

    private int part1() {
        String curr = fs.next();
        ArrayList<Character> prev = parseSnailNumber(curr);
        while ((curr = fs.next()) != null) {
            prev = addSnailNumbers(prev, parseSnailNumber(curr));
            reduce(prev);
        }
        return magnitude(prev, 0).val;
    }

    private ArrayList<Character> parseSnailNumber(String curr) {
        ArrayList<Character> list = new ArrayList<>();
        for (int i = 0; i < curr.length(); i++) {
            switch (curr.charAt(i)) {
            case '[':
            case ']':
                list.add(curr.charAt(i));
                break;
            case ',':
                list.add('\\');
                break;
            default:
                list.add((char) (curr.charAt(i) - '0'));
                break;
            }
        }
        return list;
    }

    private void reduce(ArrayList<Character> list) {
        boolean repeat = true;
        while (repeat) {
            repeat = explode(list);
            if (repeat) {
                continue;
            }
            repeat = split(list);
        }
    }

    private boolean explode(ArrayList<Character> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (count == 5) {
                count = i;
                break;
            }
            if (list.get(i) == '[') {
                count++;
            }
            if (list.get(i) == ']') {
                count--;
            }
        }
        if (count == 0) {
            return false;
        }
        for (int i = count - 1; i > -1; i--) {
            if (list.get(i) < '[') {
                list.set(i, (char) (list.get(i) + list.get(count)));
                break;
            }
        }
        for (int i = count + 3; i < list.size(); i++) {
            if (list.get(i) < '[') {
                list.set(i, (char) (list.get(i) + list.get(count + 2)));
                break;
            }
        }
        for (int i = 0; i < 5; i++) {
            list.remove(count - 1);
        }
        list.add(count - 1, (char) 0);
        return true;
    }

    private boolean split(ArrayList<Character> list) {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < '[' && list.get(i) > 9) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return false;
        }
        int temp = list.get(index);
        list.remove(index);
        list.add(index, ']');
        if (temp % 2 == 0) {
            list.add(index, (char) (temp / 2));
        } else {
            list.add(index, (char) ((temp / 2) + 1));
        }
        list.add(index, '\\');
        list.add(index, (char) (temp / 2));
        list.add(index, '[');
        return true;
    }

    private Pair magnitude(ArrayList<Character> list, int low) {
        int val;
        int index = low + 1;
        if (list.get(index) == '[') {
            Pair currPair = magnitude(list, index);
            val = 3 * currPair.val;
            index = currPair.index + 2;
        } else {
            val = 3 * list.get(index);
            index += 2;
        }
        if (list.get(index) == '[') {
            Pair currPair = magnitude(list, index);
            val += 2 * currPair.val;
            index = currPair.index + 1;
        } else {
            val += 2 * list.get(index);
            index += 1;
        }
        return new Pair(val, index);
    }

    private int part2() {
        ArrayList<ArrayList<Character>> snailNumbers = new ArrayList<>();
        String curr;
        while ((curr = fs.next()) != null) {
            snailNumbers.add(parseSnailNumber(curr));
        }
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < snailNumbers.size() - 1; i++) {
            for (int j = i + 1; j < snailNumbers.size(); j++) {
                ArrayList<Character> sum = addSnailNumbers(snailNumbers.get(i), snailNumbers.get(j));
                reduce(sum);
                max = Math.max(max, magnitude(sum, 0).val);
                sum = addSnailNumbers(snailNumbers.get(j), snailNumbers.get(i));
                reduce(sum);
                max = Math.max(max, magnitude(sum, 0).val);
            }
        }
        return max;
    }

    private ArrayList<Character> addSnailNumbers(ArrayList<Character> number1, ArrayList<Character> number2) {
        ArrayList<Character> ans = new ArrayList<>();
        ans.add('[');
        ans.addAll(number1);
        ans.add('\\');
        ans.addAll(number2);
        ans.add(']');
        return ans;
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
        private int val;
        private int index;

        private Pair(int val, int index) {
            this.val = val;
            this.index = index;
        }
    }
}
