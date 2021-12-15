import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

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

    private HashMap<Integer, ArrayList<ArrayList<Integer>>> map;
    private int a;
    private int b;
    private int count;
    private String currMsg;

    private int part1() {
        String curr;
        map = new HashMap<>();
        a = b = -1;
        for (int i = 0; i < 131; i++) {
            curr = fs.next();
            ArrayList<ArrayList<Integer>> currList = new ArrayList<>();
            String[] currArr = curr.split(": ");
            int ruleNum = Integer.parseInt(currArr[0]);
            if (curr.charAt(curr.length() - 1) == '"') {
                if (curr.charAt(curr.length() - 2) == 'a') {
                    a = ruleNum;
                } else {
                    b = ruleNum;
                }
                continue;
            }
            currArr = currArr[1].split(" \\| ");
            for (int j = 0; j < currArr.length; j++) {
                String[] newArr = currArr[j].split(" ");
                ArrayList<Integer> currPartList = new ArrayList<>();
                for (int k = 0; k < newArr.length; k++) {
                    currPartList.add(Integer.parseInt(newArr[k]));
                }
                currList.add(currPartList);
            }
            map.put(ruleNum, currList);
        }
        fs.next();
        count = 0;
        while ((currMsg = fs.next()) != null) {
            if (dfs(0, 0) == currMsg.length()) {
                count++;
            }
        }
        return count;
    }

    private int dfs(int ruleNum, int index) {
        if (index == -1 || index >= currMsg.length()) {
            return -1;
        }
        if (!map.containsKey(ruleNum)) {
            if (ruleNum == a && currMsg.charAt(index) == 'a') {
                return index + 1;
            } else if (ruleNum == b && currMsg.charAt(index) == 'b') {
                return index + 1;
            } else {
                return -1;
            }
        }
        for (ArrayList<Integer> currPartList : map.get(ruleNum)) {
            int currCheck = index;
            for (Integer currInt : currPartList) {
                currCheck = dfs(currInt, currCheck);
            }
            if (currCheck != -1) {
                return currCheck;
            }
        }
        return -1;
    }

    private int part2() {
        String curr;
        map = new HashMap<>();
        a = b = -1;
        for (int i = 0; i < 131; i++) { // as in part 1
            curr = fs.next();
            ArrayList<ArrayList<Integer>> currList = new ArrayList<>();
            String[] currArr = curr.split(": ");
            int ruleNum = Integer.parseInt(currArr[0]);
            if (curr.charAt(curr.length() - 1) == '"') {
                if (curr.charAt(curr.length() - 2) == 'a') {
                    a = ruleNum;
                } else {
                    b = ruleNum;
                }
                continue;
            }
            currArr = currArr[1].split(" \\| ");
            for (int j = 0; j < currArr.length; j++) {
                String[] newArr = currArr[j].split(" ");
                ArrayList<Integer> currPartList = new ArrayList<>();
                for (int k = 0; k < newArr.length; k++) {
                    currPartList.add(Integer.parseInt(newArr[k]));
                }
                currList.add(currPartList);
            }
            map.put(ruleNum, currList);
        }
        fs.next();
        ArrayList<ArrayList<Integer>> newList = new ArrayList<>();
        ArrayList<Integer> newSubList = new ArrayList<>();
        newSubList.add(42);
        newSubList.add(31);
        newList.add(newSubList);
        newSubList = new ArrayList<>();
        newSubList.add(42);
        newSubList.add(11);
        newSubList.add(31);
        newList.add(newSubList);
        map.put(11, newList); // replace rule 11 with the new looped rule 11
        // instead of modifying rule 8, we repeat rule 8 multiple times in part2Helper
        count = 0;
        while ((currMsg = fs.next()) != null) {
            if (part2Helper() == currMsg.length()) {
                count++;
            }
        }
        return count;
    }

    private int part2Helper() {
        ArrayList<Integer> indexes = new ArrayList<>();
        int currIndex = 0;
        while (true) {
            currIndex = dfs(8, currIndex); // apply rule 8 as many times as possible
            if (currIndex == -1) {
                break;
            } else {
                indexes.add(currIndex); // keep track of all the results from applying rule 8
            }
        }
        if (indexes.isEmpty()) { // if rule 8 can't be applied, rule 0 fails
            return -1;
        }
        for (Integer curr8Index: indexes) { // for each result of rule 8 application
            int curr11Ans = dfs(11, curr8Index); // try rule 11
            if (curr11Ans == currMsg.length()) {
                // only if the application of rule 11 ends in length of currMsg, the message is valid
                return curr11Ans;
            }
        }
        return -1;
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
