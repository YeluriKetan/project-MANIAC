import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

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
        String[] inputArr = fs.next().split(",");
        int[] arr = new int[inputArr.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Integer.parseInt(inputArr[i]);
        }
        arr[1] = 12;
        arr[2] = 2;
        return intCode(arr);
    }

    private int intCode(int[] arr) {
        for (int i = 0; i < arr.length; i += 4) {
            switch (arr[i]) {
            case 1:
                arr[arr[i + 3]] = arr[arr[i + 1]] + arr[arr[i + 2]];
                break;
            case 2:
                arr[arr[i + 3]] = arr[arr[i + 1]] * arr[arr[i + 2]];
                break;
            case 99:
                return arr[0];
            default:
                break;
            }
        }
        return arr[0];
    }

    private int part2() {
        String[] inputArr = fs.next().split(",");
        int[] arr = new int[inputArr.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Integer.parseInt(inputArr[i]);
        }
        int[] newArr = new int[arr.length];
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                System.arraycopy(arr, 0, newArr, 0, arr.length);
                newArr[1] = noun;
                newArr[2] = verb;
                if (intCode(newArr) == 19690720) {
                    return 100 * noun + verb;
                }
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
