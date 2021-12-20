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
        String curr = fs.next();
        boolean[] algorithm = new boolean[curr.length()];
        for (int i = 0; i < curr.length(); i++) {
            algorithm[i] = curr.charAt(i) == '#';
        }
        fs.next();
        boolean[][] image = new boolean[120][120]; // Choose the canvas size appropriately to leave enough space for growth
        for (int i = 0; i < 100; i++) {
            curr = fs.next();
            for (int j = 0; j < curr.length(); j++) {
                image[i + 10][j + 10] = curr.charAt(j) == '#'; // Choose offset to place the image in the centre of the canvas
            }
        }
        boolean[][] tempImage = new boolean[image.length][image[0].length];
        for (int k = 0; k < 2; k++) {
            for (int i = 1; i < image.length - 1; i++) {
                for (int j = 1; j < image[0].length - 1; j++) {
                    int currBin = 0;
                    for (int x = i - 1; x <= i + 1; x++) {
                        for (int y = j - 1; y <= j + 1; y++) {
                            currBin <<= 1;
                            if (image[x][y]) {
                                currBin += 1;
                            }
                        }
                    }
                    tempImage[i][j] = algorithm[currBin];
                }
            }
            // if the algorithm states that binary number 0 results in a light pixel
            // and a binary number of 511 results in a dark pixel.
            // This implies that the rest of the infinite pixels of the image will turn to light after 1 application
            // of the algorithm, and turn back to dark pixel in the next application of the algorithm.
            // Since we can't account for the infinite pixels, we manually check and manipulate the border pixels.
            // Since these are far from the actual given image pixels, we can safely assume that the border pixels are
            // influenced by the image pixels and vice versa. But we still have to manually manipulate them for the sake
            // of maintaining the state of the image as a whole
            if (algorithm[0] && !algorithm[algorithm.length - 1]) {
                boolean manual = k % 2 == 0;
                for (int i = 0; i < 120; i++) {
                    tempImage[0][i] = manual;
                    tempImage[119][i] = manual;
                    tempImage[i][0] = manual;
                    tempImage[i][119] = manual;
                }
            }
            boolean[][] holder = image;
            image = tempImage;
            tempImage = holder;
        }
        int count = 0;
        for (boolean[] booleans : image) {
            for (boolean currPixel: booleans) {
                if (currPixel) {
                    count++;
                }
            }
        }
        return count;
    }

    private int part2() { // Same as part 1, but more iterations, hence a larger canvas
        String curr = fs.next();
        boolean[] algorithm = new boolean[curr.length()];
        for (int i = 0; i < curr.length(); i++) {
            algorithm[i] = curr.charAt(i) == '#';
        }
        fs.next();
        boolean[][] image = new boolean[250][250];
        for (int i = 0; i < 100; i++) {
            curr = fs.next();
            for (int j = 0; j < curr.length(); j++) {
                image[i + 75][j + 75] = curr.charAt(j) == '#';
            }
        }
        boolean[][] tempImage = new boolean[image.length][image[0].length];
        for (int k = 0; k < 50; k++) {
            for (int i = 1; i < image.length - 1; i++) {
                for (int j = 1; j < image[0].length - 1; j++) {
                    int currBin = 0;
                    for (int x = i - 1; x <= i + 1; x++) {
                        for (int y = j - 1; y <= j + 1; y++) {
                            currBin <<= 1;
                            if (image[x][y]) {
                                currBin += 1;
                            }
                        }
                    }
                    tempImage[i][j] = algorithm[currBin];
                }
            }
            if (algorithm[0] && !algorithm[algorithm.length - 1]) {
                boolean manual = k % 2 == 0;
                for (int i = 0; i < 250; i++) {
                    tempImage[0][i] = manual;
                    tempImage[249][i] = manual;
                    tempImage[i][0] = manual;
                    tempImage[i][249] = manual;
                }
            }
            boolean[][] holder = image;
            image = tempImage;
            tempImage = holder;
        }
        int count = 0;
        for (boolean[] booleans : image) {
            for (boolean currPixel: booleans) {
                if (currPixel) {
                    count++;
                }
            }
        }
        return count;
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
