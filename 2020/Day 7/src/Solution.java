import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

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

    private static HashSet<String> uniqBagSet;
    private static HashMap<String, LinkedList<String>> parentMap;

    private int part1() {
        parentMap = new HashMap<>();
        uniqBagSet = new HashSet<>();
        String curr;
        while ((curr = fs.next()) != null) {
            String[] arr1 = curr.split(" bags contain (\\d+ |no other bags\\.)");
            uniqBagSet.add(arr1[0]);
            if (arr1.length == 1) {
                continue;
            }
            String[] arr2 = arr1[1].split(" ba(gs|g)(, \\d+ |\\.)");
            for (int j = 0; j < arr2.length; j++) {
                uniqBagSet.add(arr2[j]);
                LinkedList<String> currList;
                if (parentMap.containsKey(arr2[j])) {
                    currList = parentMap.get(arr2[j]);
                    if (!currList.contains(arr1[0])) {
                        currList.add(arr1[0]);
                    }
                } else {
                    currList = new LinkedList<>();
                    currList.add(arr1[0]);
                    parentMap.put(arr2[j], currList);
                }
            }
        }
        return countParentBags("shiny gold");
    }

    private int countParentBags(String curr) {
        if (!parentMap.containsKey(curr)) {
            return 0;
        }
        int total = 0;
        LinkedList<String> currSet = parentMap.get(curr);
        for (String parent: currSet) {
            if (uniqBagSet.contains(parent)) {
                uniqBagSet.remove(parent);
                total += 1 + countParentBags(parent);
            }
        }
        return total;
    }

    private static HashMap<String, HashMap<String, Integer>> childMap;

    private int part2() {
        childMap = new HashMap<>();
        String curr;
        while ((curr = fs.next()) != null) {
            String[] arr1 = curr.split(" bags contain( no other bags\\.| )");
            if (arr1.length == 1) {
                continue;
            }
            String[] arr2 = arr1[1].split(" ba(gs|g)(, |\\.)");
            HashMap<String, Integer> currMap = new HashMap<>();
            for (int j = 0; j < arr2.length; j++) {
                String[] arr3 = arr2[j].split(" ", 2);
                currMap.put(arr3[1], Integer.parseInt(arr3[0]));
            }
            childMap.put(arr1[0], currMap);
        }
        return countChildBags("shiny gold");
    }

    private int countChildBags(String curr) {
        if (!childMap.containsKey(curr)) {
            return 0;
        }
        int total = 0;
        HashMap<String, Integer> currMap = childMap.get(curr);
        for (String child: currMap.keySet()) {
            total += currMap.get(child) * (1 + countChildBags(child));
        }
        return total;
    }

    private static class FastScan {
        private BufferedReader br;

        private FastScan(File file) {
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void changeFile(File file) {
            close();
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (IOException ex) {
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
