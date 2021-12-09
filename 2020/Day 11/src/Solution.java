import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
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
        char[][] arr1 = new char[92][];
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = fs.next().toCharArray();
        }
        char[][] arr2 = new char[arr1.length][arr1[0].length];
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < arr1.length; i++) {
                for (int j = 0; j < arr1[0].length; j++) {
                    if (arr1[i][j] == '.') {
                        arr2[i][j] = '.';
                        continue;
                    }
                    int count = 0;
                    for (int k = Math.max(0, i - 1); k <= Math.min(arr1.length - 1, i + 1); k++) {
                        for (int l = Math.max(0, j - 1); l <= Math.min(arr1[0].length - 1, j + 1); l++) {
                            if (arr1[k][l] == '#') {
                                count++;
                            }
                        }
                    }
                    if (arr1[i][j] == 'L') {
                        if (count == 0) {
                            changed = true;
                            arr2[i][j] = '#';
                        } else {
                            arr2[i][j] = 'L';
                        }
                    } else {
                        if (count - 1 > 3) {
                            changed = true;
                            arr2[i][j] = 'L';
                        } else {
                            arr2[i][j] = '#';
                        }
                    }
                }
            }
            char[][] temp = arr1;
            arr1 = arr2;
            arr2 = temp;
        }
        int ans = 0;
        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr1[0].length; j++) {
                if (arr1[i][j] == '#') {
                    ans++;
                }
            }
        }
        return ans;
    }

    private int part2() {
        char[][] arr1 = new char[92][];
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = fs.next().toCharArray();
        }
        char[][] arr2 = new char[arr1.length][arr1[0].length];
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < arr1.length; i++) {
                for (int j = 0; j < arr1[0].length; j++) {
                    if (arr1[i][j] == '.') {
                        arr2[i][j] = '.';
                        continue;
                    }
                    int count = 0;

                    for (int k = i - 1; k > -1; k--) {  /// go up
                        if (arr1[k][j] == '#') {
                            count++;
                            break;
                        } else if (arr1[k][j] == 'L') {
                            break;
                        }
                    }
                    for (int k = 1; k <= Math.min(i, j); k++) { // go left up
                        if (arr1[i - k][j - k] == '#') {
                            count++;
                            break;
                        } else if (arr1[i - k][j - k] == 'L') {
                            break;
                        }
                    }
                    for (int k = j - 1; k > -1; k--) { // go left
                        if (arr1[i][k] == '#') {
                            count++;
                            break;
                        } else if (arr1[i][k] == 'L') {
                            break;
                        }
                    }
                    for (int k = 1; k <= Math.min(arr1.length - 1 - i, j); k++) { // go left down
                        if (arr1[i + k][j - k] == '#') {
                            count++;
                            break;
                        } else if (arr1[i + k][j - k] == 'L') {
                            break;
                        }
                    }
                    for (int k = i + 1; k < arr1.length; k++) { // go down
                        if (arr1[k][j] == '#') {
                            count++;
                            break;
                        } else if (arr1[k][j] == 'L') {
                            break;
                        }
                    }
                    for (int k = 1; k <= Math.min(arr1.length - 1 - i, arr1[0].length - 1 - j); k++) { // go right down
                        if (arr1[i + k][j + k] == '#') {
                            count++;
                            break;
                        } else if (arr1[i + k][j + k] == 'L') {
                            break;
                        }
                    }
                    for (int k = j + 1; k < arr1[0].length; k++) { // go right
                        if (arr1[i][k] == '#') {
                            count++;
                            break;
                        } else if (arr1[i][k] == 'L') {
                            break;
                        }
                    }
                    for (int k = 1; k <= Math.min(i, arr1[0].length - 1 - j); k++) { // go right up
                        if (arr1[i - k][j + k] == '#') {
                            count++;
                            break;
                        } else if (arr1[i - k][j + k] == 'L') {
                            break;
                        }
                    }

                    if (arr1[i][j] == 'L') {
                        if (count == 0) {
                            changed = true;
                            arr2[i][j] = '#';
                        } else {
                            arr2[i][j] = 'L';
                        }
                    } else {
                        if (count > 4) {
                            changed = true;
                            arr2[i][j] = 'L';
                        } else {
                            arr2[i][j] = '#';
                        }
                    }
                }
            }
            char[][] temp = arr1;
            arr1 = arr2;
            arr2 = temp;
        }
        int ans = 0;
        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr1[0].length; j++) {
                if (arr1[i][j] == '#') {
                    ans++;
                }
            }
        }
        return ans;
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
