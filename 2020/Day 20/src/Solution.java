import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Solution {

    private static FastScan fs;
    private static char[][] monster;

    public static void main(String[] args) {
        Solution sol = new Solution();
        File file = new File(Paths.get("src", "input.txt").toString());

        fs = new FastScan(file);
        System.out.println(sol.part1());

        fs.changeFile(file);
        System.out.println(sol.part2());
        fs.close();
    }

    private long part1() {
        HashMap<Integer, int[]> idToBorderArr = new HashMap<>();
        HashMap<Integer, Integer> borderCount = new HashMap<>();
        for (int i = 0; i < 144; i++) {
            int id = Integer.parseInt(fs.next().substring(5, 9));
            String[] currArr = new String[10];
            for (int j = 0; j < 10; j++) {
                currArr[j] = fs.next();
            }
            int[] currBorders = getBorders(currArr);
            idToBorderArr.put(id, currBorders);
            Arrays.stream(currBorders).forEach(x -> borderCount.merge(x, 1, Integer::sum));
            fs.next();
        }
        long ans = 1;
        for (Integer curr: idToBorderArr.keySet()) {
            int count = 0;
            for (int currBorder: idToBorderArr.get(curr)) {
                if (borderCount.get(currBorder) == 1) {
                    count++;
                }
            }
            if (count == 4) {
                ans *= curr;
            }
        }
        return ans;
    }

    private static int[] getBorders(String[] arr) {
        // returns an array of all possible borders, each converted to a binary number considering '.' as 0 and '#' as 1
        // the top and bottom borders are read from left to right
        // the left and right borders are read from bottom to top
        int[] borderArr = new int[8];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < borderArr.length; j++) {
                borderArr[j] <<= 1;
            }
            if (arr[0].charAt(i) == '#') {
                borderArr[0] += 1;
            }
            if (arr[0].charAt(9 - i) == '#') {
                borderArr[1] += 1;
            }
            if (arr[9].charAt(i) == '#') {
                borderArr[2] += 1;
            }
            if (arr[9].charAt(9 - i) == '#') {
                borderArr[3] += 1;
            }
            if (arr[9 - i].charAt(0) == '#') {
                borderArr[4] += 1;
            }
            if (arr[i].charAt(0) == '#') {
                borderArr[5] += 1;
            }
            if (arr[9 - i].charAt(9) == '#') {
                borderArr[6] += 1;
            }
            if (arr[i].charAt(9) == '#') {
                borderArr[7] += 1;
            }
        }
        return borderArr;
    }

    private HashMap<Integer, Image> idToImage; // mapping from id to the image
    private HashMap<Integer, LinkedList<Integer>> borderToId; // mapping from border to a list of image ids that have the border

    private int part2() {
        idToImage = new HashMap<>();
        borderToId = new HashMap<>();
        for (int i = 0; i < 144; i++) {
            int id = Integer.parseInt(fs.next().substring(5, 9));
            String[] currArr = new String[10];
            for (int j = 0; j < 10; j++) {
                currArr[j] = fs.next();
            }
            Image currImage = new Image(id, currArr);
            idToImage.put(id, currImage);
            for (int j = 0; j < currImage.borders.length; j++) {
                for (int k = 0; k < currImage.borders[0].length; k++) {
                    LinkedList<Integer> currList = borderToId.getOrDefault(currImage.borders[j][k], new LinkedList<>());
                    currList.add(id);
                    borderToId.put(currImage.borders[j][k], currList);
                }
            }
            fs.next();
        }
        Image fullImage = getFullImage(); // assemble the full image
        getMonster(); // initialize the monster image
        return transformAndCount(fullImage); // return the answer
    }

    private Image getFullImage() {
        // assemble the full image
        Image finalImage = new Image(96, 96); // initialize the canvas
        Image topLeft = null;
        for (Image currImage: idToImage.values()) { // find the top left image
            int count = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 2; j++) {
                    if (borderToId.get(currImage.borders[i][j]).size() == 1) {
                        count++;
                    }
                }
            }
            if (count == 4) { // find the first image that has no match for two of its sides
                topLeft = currImage;
                break;
            }
        }
        assert topLeft != null; // something wrong
        topLeft.transformToMatch(-1, -1); // transform it to get the no match borders on the left and top
        finalImage.addPart(0, 0, topLeft); // add it to the canvas

        // use a cache for the row above the current row of images
        // the cache get replaced with the current row images as we move from left to right
        Image[] topCache = new Image[12];
        topCache[0] = topLeft;
        Image leftCache = topLeft; // use a cache for the image on the left;

        for (int i = 1; i < 12; i++) {
            LinkedList<Integer> ids = borderToId.get(leftCache.borders[3][0]);
            int currImageId = ids.poll();
            if (currImageId == leftCache.id) {
                currImageId = ids.poll();
            }
            // find the image that matches the right border of the image on the left
            Image currImage = idToImage.get(currImageId);
            // transform it to have a open border on the top and the matching border on the left
            currImage.transformToMatch(leftCache.borders[3][0], -1);
            finalImage.addPart(0, 8 * i, currImage); // add it to the canvas
            leftCache = currImage;
            topCache[i] = currImage; // replace the caches
        }
        // now that we are done for the first row, we can repeat the same for the remaining rows using the cache
        for (int i = 1; i < 12; i++) {
            LinkedList<Integer> rowStartIds = borderToId.get(topCache[0].borders[1][0]);
            int rowStartId = rowStartIds.poll();
            if (rowStartId == topCache[0].id) {
                rowStartId = rowStartIds.poll();
            }
            Image rowStartImage = idToImage.get(rowStartId); // get image with matching top border
            // left border is open since the left most image of the row
            rowStartImage.transformToMatch(-1, topCache[0].borders[1][0]);
            finalImage.addPart(i * 8, 0, rowStartImage);
            leftCache = rowStartImage;
            topCache[0] = rowStartImage;
            for (int j = 1; j < 12; j++) {
                LinkedList<Integer> ids = borderToId.get(leftCache.borders[3][0]);
                int currImageId = ids.poll();
                if (currImageId == leftCache.id) {
                    currImageId = ids.poll();
                }
                Image currImage = idToImage.get(currImageId);
                // left matching border from the left cached image, top matching border from the respective top cached image
                currImage.transformToMatch(leftCache.borders[3][0], topCache[j].borders[1][0]);
                finalImage.addPart(i * 8, 8 * j, currImage);
                leftCache = currImage;
                topCache[j] = currImage;
            }
        }
        return finalImage;
    }

    private static void getMonster() { // initialize the monster image
        String[] strArr = {
                "                  # ",
                "#    ##    ##    ###",
                " #  #  #  #  #  #   "};
        monster = new char[strArr.length][];
        for (int i = 0; i < monster.length; i++) {
            monster[i] = strArr[i].toCharArray();
        }
    }

    private int findMonsterAndCount(Image image) {
        // finds monsters if any, replacing the parts of the monster with a ' '
        boolean found = false; // true if at least one monster is found
        for (int i = 0; i < image.content.length - monster.length + 1; i++) {
            for (int j = 0; j < image.content[0].length - monster[0].length + 1; j++) {
                boolean check = true;
                for (int x = 0; x < monster.length; x++) {
                    if (!check) {
                        continue;
                    }
                    for (int y = 0; y < monster[0].length; y++) {
                        if (monster[x][y] == ' ') {
                            continue;
                        }
                        if (image.content[i + x][j + y] != '#') {
                            check = false;
                            break;
                        }
                    }
                }
                if (!check) {
                    continue;
                }
                found = true;
                for (int x = 0; x < monster.length; x++) {
                    for (int y = 0; y < monster[0].length; y++) {
                        if (monster[x][y] == '#') {
                            image.content[i + x][j + y] = '.';
                        }
                    }
                }
            }
        }
        if (!found) { // if no monster found, return -1
            return -1;
        }
        // else count the number of '#' that are not a part of any monster
        int count = 0;
        for (int i = 0; i < image.content.length; i++) {
            for (int j = 0; j < image.content[0].length; j++) {
                if (image.content[i][j] == '#') {
                    count += 1;
                }
            }
        }
        return count;
    }

    private int transformAndCount(Image image) {
        // transforms the full image into all the possible 8 configurations
        // and returns the number of '#' that are not a part of any monster
        for (int i = 0; i < 2; i++) {
            image.rotateAxis(i);
            for (int j = 0; j < 2; j++) {
                image.flipVer(j);
                for (int k = 0; k < 2; k++) {
                    image.flipHor(k);
                    int currAns = findMonsterAndCount(image);
                    if (currAns != -1) {
                        return currAns;
                    }
                }
            }
        }
        return -1; // no monster found in any configuration, something wrong somewhere
    }

    private class Image {
        private char[][] content;
        private int[][] borders; // top, bottom, left, right
        private int id;

        private Image(int id, String[] strArr) {
            this.id = id;
            int[] bordersList = getBorders(strArr);
            borders = new int[4][2];
            for (int i = 0; i < borders.length; i++) {
                for (int j = 0; j < borders[0].length; j++) {
                    borders[i][j] = bordersList[2 * i + j];
                }
            }
            content = new char[strArr.length - 2][strArr[0].length() - 2];
            for (int i = 0; i < content.length; i++) {
                content[i] = strArr[i + 1].substring(1, strArr.length - 1).toCharArray();
            }
        }

        private Image(int r, int c) { // to create a full image
            borders = null;
            id = -1;
            content = new char[r][c];
        }

        private void rotateAxis(int rotate) {
            // rotates the image counter-clockwise
            if (rotate == 0) {
                return;
            }
            int r = content.length;
            int c = content[0].length;
            char[][] newContent = new char[c][r];
            for (int i = 0; i < c; i++) {
                for (int j = 0; j < r; j++) {
                    newContent[i][j] = content[j][c - 1 - i];
                }
            }
            content = newContent;
            if (borders == null) { // full image might not have a borders array
                return;
            }
            for (int i = 2; i < 4; i++) { // update the borders as well
                int temp = borders[i][0];
                borders[i][0] = borders[i][1];
                borders[i][1] = temp;
            }
            int[] temp = borders[2];
            borders[2] = borders[0];
            borders[0] = borders[3];
            borders[3] = borders[1];
            borders[1] = temp;
        }

        private void flipVer(int flip) {
            // flips the image along the vertical axis
            if (flip == 0) {
                return;
            }
            int r = content.length;
            int c = content[0].length;
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c / 2; j++) {
                    char temp = content[i][j];
                    content[i][j] = content[i][c - 1 - j];
                    content[i][c - 1 - j] = temp;
                }
            }
            if (borders == null) {
                return;
            }
            int[] temp = borders[2];
            borders[2] = borders[3];
            borders[3] = temp;
            for (int i = 0; i < 2; i++) {
                int tempInt = borders[i][0];
                borders[i][0] = borders[i][1];
                borders[i][1] = tempInt;
            }
        }

        private void flipHor(int flip) {
            // flips the image along the horizontal axis
            if (flip == 0) {
                return;
            }
            int r = content.length;
            for (int i = 0; i < r / 2; i++) {
                char[] temp = content[i];
                content[i] = content[r - 1 - i];
                content[r - 1 - i] = temp;
            }
            if (borders == null) {
                return;
            }
            int[] temp = borders[0];
            borders[0] = borders[1];
            borders[1] = temp;
            for (int i = 2; i < 4; i++) {
                int tempInt = borders[i][0];
                borders[i][0] = borders[i][1];
                borders[i][1] = tempInt;
            }
        }

        private void transformToMatch(int left, int top) {
            // transforms the image to all the possible 8 configurations
            // and tries to match the left and top border values to the given values
            // if the border is open (border of the full image as well and hence doesn't have a match), -1 is passed in
            for (int j = 0; j < 2; j++) {
                rotateAxis(j);
                for (int k = 0; k < 2; k++) {
                    flipVer(k);
                    for (int l = 0; l < 2; l++) {
                        flipHor(l);
                        boolean check;
                        if (top == -1) {
                            // if the border is -1, => the number of matches for the border is only 1 (from the current image)
                            check = borderToId.get(borders[0][0]).size() == 1;
                        } else {
                            check = borders[0][0] == top;
                        }
                        if (left == -1) {
                            check = check && borderToId.get(borders[2][0]).size() == 1;
                        } else {
                            check = check && borders[2][0] == left;
                        }
                        if (check) {
                            return;
                        }
                    }
                }
            }
        }

        private void addPart(int i, int j, Image other) {
            // used for forming the full image by copying the given other image
            // pasting it starting from the given i and j
            for (int x = 0; x < other.content.length; x++) {
                System.arraycopy(other.content[x], 0, content[i + x], j, other.content[0].length);
            }
        }

        @Override
        public String toString() {
            // used for visualization
            StringBuilder sb = new StringBuilder();
            for (char[] chars : content) {
                sb.append(chars).append("\n");
            }
            if (borders != null) {
                for (int[] currBorder : borders) {
                    sb.append(Arrays.toString(currBorder) + "\n");
                }
            }
            sb.append(id + "\n");
            return sb.toString();
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
