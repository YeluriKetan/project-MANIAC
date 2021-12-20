import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

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
        HashMap<String, Integer> ingCount = new HashMap<>();
        HashMap<String, HashSet<String>> algToIngMap = new HashMap<>();
        while ((curr = fs.next()) != null) {
            String[] ingArr;
            int indexOfAlg = curr.indexOf('(');
            if (indexOfAlg == -1) {
                ingArr = curr.split(" ");
            } else {
                ingArr = curr.substring(0, indexOfAlg - 1).split(" ");
            }
            for (String currIng: ingArr) {
                ingCount.merge(currIng, 1, Integer::sum);
            }
            if (indexOfAlg == -1) { // if no allergens mentioned in this food, continue
                continue;
            }
            String[] algArr = curr.substring(indexOfAlg + 10, curr.length() - 1).split(", ");
            HashSet<String> ingSet = new HashSet<>(Arrays.asList(ingArr)); // get the current food's ingredients as a Set
            for (String currAlg: algArr) {
                if (algToIngMap.containsKey(currAlg)) {
                    // if the allergen already has a possible ingredients set
                    // replace it with an intersection set using the current food's ingredient set
                    algToIngMap.put(currAlg, intersectionSet(algToIngMap.get(currAlg), ingSet));
                } else {
                    // just place the entire ingredients set
                    algToIngMap.put(currAlg, new HashSet<>(ingSet));
                }
            }
        }
        // get a combined ingredients set where each ingredient may contain an allergen
        HashSet<String> possibleIngWithAlg = new HashSet<>();
        for (HashSet<String> currIngSet: algToIngMap.values()) {
            possibleIngWithAlg.addAll(currIngSet);
        }
        int total = 0;
        for (String currIng: ingCount.keySet()) { // for each ingredient in the ingCount map
            if (!possibleIngWithAlg.contains(currIng)) { // if the ingredient may contain an allergen, don't count
                total += ingCount.get(currIng); // else count the number of times the ingredient appeared in foods list
            }
        }
        return total;
    }

    private HashSet<String> intersectionSet(HashSet<String> setA, HashSet<String> setB) {
        HashSet<String> newSet = new HashSet<>();
        for (String currString: setA) {
            if (setB.contains(currString)) {
                newSet.add(currString);
            }
        }
        return newSet;
    }

    private HashMap<String, HashSet<String>> algToIngMap;
    private TreeMap<String, String> finalAlgToIngMap;
    private ArrayList<String> algList;

    private String part2() {
        String curr;
        algToIngMap = new HashMap<>();
        while ((curr = fs.next()) != null) {
            int indexOfAlg = curr.indexOf('(');
            if (indexOfAlg == -1) { // if the there's no mention of allergens, ignore
                continue;
            }
            String[] ingArr = curr.substring(0, indexOfAlg - 1).split(" ");
            String[] algArr = curr.substring(indexOfAlg + 10, curr.length() - 1).split(", ");
            HashSet<String> ingSet = new HashSet<>(Arrays.asList(ingArr)); // get the current food's ingredients as a Set
            for (String currAlg: algArr) {
                if (algToIngMap.containsKey(currAlg)) {
                    // if the allergen already has a possible ingredients set
                    // replace it with an intersection set using the current food's ingredient set
                    algToIngMap.put(currAlg, intersectionSet(algToIngMap.get(currAlg), ingSet));
                } else {
                    // just place the entire ingredients set
                    algToIngMap.put(currAlg, new HashSet<>(ingSet));
                }
            }
        }
        algList = new ArrayList<>(algToIngMap.keySet()); // place the allergens in a serialized list
        finalAlgToIngMap = new TreeMap<>();
        dfs(0, new HashSet<>()); // try matching using dfs
        StringBuilder sb = new StringBuilder();
        finalAlgToIngMap.forEach((x, y) -> sb.append(y + ",")); // build the canonical dangerous ingredient list
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private boolean dfs(int index, HashSet<String> used) {
        if (index == algList.size() - 1) {
            for (String possibleIng: algToIngMap.get(algList.get(index))) {
                if (!used.contains(possibleIng)) {
                    finalAlgToIngMap.put(algList.get(index), possibleIng);
                    return true;
                }
            }
            return false;
        }
        HashSet<String> newUsed = new HashSet<>(used);
        for (String possibleIng: algToIngMap.get(algList.get(index))) {
            if (!used.contains(possibleIng)) {
                newUsed.add(possibleIng);
                finalAlgToIngMap.put(algList.get(index), possibleIng);
                boolean check = dfs(index + 1, newUsed);
                if (check) {
                    return true;
                }
                finalAlgToIngMap.remove(algList.get(index));
                newUsed.remove(possibleIng);
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
