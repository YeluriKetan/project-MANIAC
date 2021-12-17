import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

    private static FastScan fs;
    private static final Pattern number = Pattern.compile("-?\\d+");

    public static void main(String[] args) {
        Solution sol = new Solution();
        File file = new File(Paths.get("src", "input.txt").toString());

        fs = new FastScan(file);
        System.out.println(sol.part1());

        fs.changeFile(file);
        System.out.println(sol.part2());
        fs.close();
    }

    private int[] xArr;
    private int[] yArr;

    private int part1() {
        Matcher matcher = number.matcher(fs.next());
        xArr = new int[2];
        for (int i = 0; i < 2; i++) {
            matcher.find();
            xArr[i] = Integer.parseInt(matcher.group());
        }
        yArr = new int[2];
        for (int i = 0; i < 2; i++) {
            matcher.find();
            yArr[i] = Integer.parseInt(matcher.group());
        }
        return (-1 - yArr[0]) * (-yArr[0]) / 2;
    }

    private int part2() {
        Matcher matcher = number.matcher(fs.next());
        xArr = new int[2];
        for (int i = 0; i < 2; i++) {
            matcher.find();
            xArr[i] = Integer.parseInt(matcher.group());
        }
        yArr = new int[2];
        for (int i = 0; i < 2; i++) {
            matcher.find();
            yArr[i] = Integer.parseInt(matcher.group());
        }
        int count = 0;
        double xMax = 1 + 8 * xArr[0];
        xMax = (Math.sqrt(xMax) - 1) / 2;
        xMax = Math.ceil(xMax);
        for (int i = (int) xMax; i <= xArr[1]; i++) {
            for (int j = yArr[0]; j <= -yArr[0] - 1; j++) {
                if (isValid(i, j)) {
                    count++;
                }
            }
        }
        return count;
    }


    private boolean isValid(int x, int y) {
        int currX = 0;
        int currY = 0;
        while (true) {
            currX += x;
            currY += y;
            if (currX > xArr[1] || currY < yArr[0]) {
                return false;
            }
            if (currX >= xArr[0] && currY <= yArr[1]) {
                return true;
            }
            if (x > 0) {
                x--;
            }
            y--;
        }
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
