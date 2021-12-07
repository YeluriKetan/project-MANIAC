import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class Solution {

    private static FastScan fs;
    private static HashMap<String, Integer> indexMap;
    private static int numOfPassports;
    private static final String blank = "#";
    private static final String end = "###";
    private static final String birthYear = "byr";
    private static final String issueYear = "iyr";
    private static final String expYear = "eyr";
    private static final String height = "hgt";
    private static final String hairColour = "hcl";
    private static final String eyeColour = "ecl";
    private static final String passportId = "pid";
    private static final String countryId = "cid";
    private static final String unitCm = "cm";
    private static final Pattern heightPattern = Pattern.compile("^[0-9]+(cm|in)$");
    private static final Pattern hairColourPattern = Pattern.compile("^#[0-9a-f]{6}$");
    private static final Pattern eyeColourPattern = Pattern.compile("^(amb|blu|brn|gry|grn|hzl|oth)$");
    private static final Pattern passportIdPattern = Pattern.compile("^[0-9]{9}$");

    public static void main(String[] args) {
        Solution sol = new Solution();
        File file = new File(Paths.get("src", "inputModified.txt").toString());
        setIndexMap();

        fs = new FastScan(file);
        numOfPassports = sol.countPassports();

        fs.changeFile(file);
        System.out.println(sol.part1());

        fs.changeFile(file);
        System.out.println(sol.part2());
        fs.close();
    }

    private static void setIndexMap() {
        indexMap = new HashMap<>();
        indexMap.put(birthYear, 2);
        indexMap.put(issueYear, 3);
        indexMap.put(expYear, 5);
        indexMap.put(height, 7);
        indexMap.put(hairColour, 11);
        indexMap.put(eyeColour, 13);
        indexMap.put(passportId, 17);
        indexMap.put(countryId, 19);
    }

    private int countPassports() {
        String curr;
        int count = 0;
        while (!end.equals(curr = fs.next())) {
            if (blank.equals(curr)) {
                count++;
            }
        }
        return count;
    }

    private int part1() {
        int total = 0;
        int currProd;
        String currString;
        for (int i = 0; i < numOfPassports; i++) {
            currProd = 1;
            while (!blank.equals(currString = fs.next())) {
                currProd *= indexMap.get(currString.substring(0, 3));
            }
            if (currProd % 510510 == 0) { // 510510 is product of primes from 2 to 17
                total++;
            }
        }
        return total;
    }

    private int part2() {
        int total = 0;
        int currProd;
        String currString;
        for (int i = 0; i < numOfPassports; i++) {
            currProd = 1;
            while (!blank.equals(currString = fs.next())) {
                currProd *= getValidationValue(currString);
            }
            if (currProd % 510510 == 0) { // 510510 is product of primes from 2 to 17
                total++;
            }
        }
        return total;
    }

    private static int getValidationValue(String str) {
        String[] arr = str.split(":");
        switch (arr[0]) {
        case birthYear: {
            int val = Integer.parseInt(arr[1]);
            return (1920 <= val && val <= 2002) ? 2 : 1;
        }
        case issueYear: {
            int val = Integer.parseInt(arr[1]);
            return (2010 <= val && val <= 2020) ? 3 : 1;
        }
        case expYear: {
            int val = Integer.parseInt(arr[1]);
            return (2020 <= val && val <= 2030) ? 5 : 1;
        }
        case height: {
            if (!heightPattern.matcher(arr[1]).matches()) {
                return 1;
            }
            String unit = arr[1].substring(arr[1].length() - 2);
            int val = Integer.parseInt(arr[1].substring(0, arr[1].length() - 2));
            if (unit.equals(unitCm)) {
                return (150 <= val && val <= 193) ? 7 : 1;
            } else {
                return (59 <= val && val <= 76) ? 7 : 1;
            }
        }
        case hairColour: {
            return (hairColourPattern.matcher(arr[1]).matches()) ? 11 : 1;
        }
        case eyeColour: {
            return (eyeColourPattern.matcher(arr[1]).matches()) ? 13 : 1;
        }
        case passportId: {
            return (passportIdPattern.matcher(arr[1]).matches()) ? 17 : 1;
        }
        default:
            return 1;
        }
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
                    String str = br.readLine();
                    if (str.isBlank()) {
                        str = "#";
                    }
                    st = new StringTokenizer(str);
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
