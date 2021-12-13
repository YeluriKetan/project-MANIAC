import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

    private static FastScan fs;
    private static final Pattern num = Pattern.compile("\\d+");
    private static final Pattern departure = Pattern.compile("^departure.*$");

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
        boolean[] validValues = new boolean[1001];
        String curr;
        Matcher currMatcher;
        int left, right;
        for (int i = 0; i < 20; i++) {
            curr = fs.next();
            currMatcher = num.matcher(curr);
            for (int j = 0; j < 2; j++) {
                currMatcher.find();
                left = Integer.parseInt(currMatcher.group());
                currMatcher.find();
                right = Integer.parseInt(currMatcher.group());
                Arrays.fill(validValues, left, right + 1, true);
            }
        }
        for (int i = 0; i < 5; i++) {
            fs.next();
        }
        String[] currArr;
        int total = 0;
        int currInt;
        while ((curr = fs.next()) != null) {
            currArr = curr.split(",");
            for (int i = 0; i < currArr.length; i++) {
                currInt = Integer.parseInt(currArr[i]);
                if (!validValues[currInt]) {
                    total += currInt;
                }
            }
        }
        return total;
    }

    private ArrayList<String> listOfFields;
    private HashMap<String, ArrayList<Integer>> mapFieldToPossible;
    private HashMap<String, Integer> finalMapping;

    private long part2() {
        String[] currArr;
        Matcher currMatcher;
        HashMap<String, Field> map = new HashMap<>();
        for (int i = 0; i < 20; i++) {
            currArr = fs.next().split(":");
            currMatcher = num.matcher(currArr[1]);
            Field currField = new Field();
            currMatcher.find();
            currField.firstLow = Integer.parseInt(currMatcher.group());
            currMatcher.find();
            currField.firstHigh = Integer.parseInt(currMatcher.group());
            currMatcher.find();
            currField.secondLow = Integer.parseInt(currMatcher.group());
            currMatcher.find();
            currField.secondHigh = Integer.parseInt(currMatcher.group());
            map.put(currArr[0], currField);
        }
        fs.next();
        fs.next();
        int[][] tickets = new int[20][236];
        currArr = fs.next().split(",");
        for (int i = 0; i < 20; i++) {
            tickets[i][0] = Integer.parseInt(currArr[i]);
        }
        fs.next();
        fs.next();
        for (int i = 1; i < 236; i++) {
            currArr = fs.next().split(",");
            for (int j = 0; j < 20; j++) {
                boolean isValid = false;
                int currVal = Integer.parseInt(currArr[j]);
                for (String currKey: map.keySet()) {
                    if (map.get(currKey).isValid(currVal)) {
                        isValid = true;
                        break;
                    }
                }
                if (isValid) {
                    tickets[j][i] = Integer.parseInt(currArr[j]);
                } else {
                    for (int k = 0; k < 20; k++) {
                        tickets[k][i] = -1;
                    }
                    break;
                }
            }
        }
        mapFieldToPossible = new HashMap<>();
        for (String currKey: map.keySet()) {
            ArrayList<Integer> currList = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                if (map.get(currKey).isValid(tickets[i])) {
                    currList.add(i);
                }
            }
            mapFieldToPossible.put(currKey, currList);
        }
        listOfFields = new ArrayList<>(map.keySet());
        finalMapping = new HashMap<>();
        boolean[] used = new boolean[20];
        dp(0, used);
        long ans = 1;
        for (String currKey: finalMapping.keySet()) {
            if (departure.matcher(currKey).matches()) {
                ans *= tickets[finalMapping.get(currKey)][0];
            }
        }
        return ans;
    }

    private boolean dp(int currField, boolean[] used) {
        if (currField == listOfFields.size() - 1) {
            for (Integer currInt: mapFieldToPossible.get(listOfFields.get(currField))) {
                if (!used[currInt]) {
                    finalMapping.put(listOfFields.get(currField), currInt);
                    return true;
                }
            }
            return false;
        }
        for (Integer currInt: mapFieldToPossible.get(listOfFields.get(currField))) {
            if (!used[currInt]) {
                used[currInt] = true;
                boolean check = dp(currField + 1, used);
                if (check) {
                    finalMapping.put(listOfFields.get(currField), currInt);
                    return true;
                }
                used[currInt] = false;
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

    private static class Field {
        private int firstLow;
        private int firstHigh;
        private int secondLow;
        private int secondHigh;

        private Field() {
        }

        private boolean isValid(int val) {
            return (firstLow <= val && val <= firstHigh) || (secondLow <= val && val <= secondHigh);
        }

        private boolean isValid(int[] arr) {
            for (int curr: arr) {
                if (curr == -1) {
                    continue;
                }
                if (!isValid(curr)) {
                    return false;
                }
            }
            return true;
        }
    }
}
