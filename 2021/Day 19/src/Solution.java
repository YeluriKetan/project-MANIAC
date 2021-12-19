import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * Credits: trolando on Github
 * Through the post on Day 19 Solution Megathread
 * https://www.reddit.com/r/adventofcode/comments/rjpf7f/comment/hp5hg48/?utm_source=share&utm_medium=web2x&context=3
 * https://github.com/trolando/aoc2020/blob/b3b625c5f4d678bf648dccc77ddd8e979dc39f82/src/nl/tvandijk/aoc/year2021/day19/Day19.java
*/
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
        ScannerPossibilities[] scannerPossibilities = getScannerPossibilitiesFromInput();
        Scanner[] finalScanner = new Scanner[scannerPossibilities.length];
        Triplet[] finalPosition = new Triplet[scannerPossibilities.length];

        finalScanner[0] = scannerPossibilities[0].possibilities[0];
        finalPosition[0] = new Triplet(0, 0, 0);

        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);

        while (!queue.isEmpty()) {
            Integer currInt = queue.poll();
            for (int i = 0; i < finalPosition.length; i++) {
                if (finalPosition[i] != null) {
                    continue;
                }
                if (scannerPossibilities[currInt].matchDistancesMatrix(scannerPossibilities[i])) {
                    Pair currPair = scannerPossibilities[i].transformScanner(finalScanner[currInt]);
                    if (currPair == null) {
                        continue;
                    }
                    finalScanner[i] = currPair.scanner;
                    finalPosition[i] = currPair.triplet.add(finalPosition[currInt]);
                    queue.add(i);
                }
            }
        }

        HashSet<Triplet> uniqueTriplets = new HashSet<>();
        for (int i = 0; i < finalScanner.length; i++) {
            for (Triplet currTriplet: finalScanner[i].triplets) {
                uniqueTriplets.add(currTriplet.add(finalPosition[i]));
            }
        }
        return uniqueTriplets.size();
    }

    private ScannerPossibilities[] getScannerPossibilitiesFromInput() {
        ArrayList<Scanner> scanners = new ArrayList<>();
        String curr;
        for (int i = 0; i < 29; i++) {
            fs.next();
            Scanner currScanner = new Scanner();
            while ((curr = fs.next()) != null && !curr.isBlank()) {
                String[] currArr = curr.split(",");
                currScanner.addTriplet(new Triplet(Integer.parseInt(currArr[0]), Integer.parseInt(currArr[1]), Integer.parseInt(currArr[2])));
            }
            scanners.add(currScanner);
        }
        ScannerPossibilities[] scannerPossibilities = new ScannerPossibilities[scanners.size()];
        for (int i = 0; i < scannerPossibilities.length; i++) {
            scannerPossibilities[i] = new ScannerPossibilities(scanners.get(i));
        }
        return scannerPossibilities;
    }

    private int part2() {
        ScannerPossibilities[] scannerPossibilities = getScannerPossibilitiesFromInput();
        Scanner[] finalScanner = new Scanner[scannerPossibilities.length];
        Triplet[] finalPosition = new Triplet[scannerPossibilities.length];

        finalScanner[0] = scannerPossibilities[0].possibilities[0];
        finalPosition[0] = new Triplet(0, 0, 0);

        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);

        while (!queue.isEmpty()) {
            Integer currInt = queue.poll();
            for (int i = 0; i < finalPosition.length; i++) {
                if (finalPosition[i] != null) {
                    continue;
                }
                if (scannerPossibilities[currInt].matchDistancesMatrix(scannerPossibilities[i])) {
                    Pair currPair = scannerPossibilities[i].transformScanner(finalScanner[currInt]);
                    if (currPair == null) {
                        continue;
                    }
                    finalScanner[i] = currPair.scanner;
                    finalPosition[i] = currPair.triplet.add(finalPosition[currInt]);
                    queue.add(i);
                }
            }
        }

        int max = Integer.MIN_VALUE;
        for (int i = 0; i < finalPosition.length - 1; i++) {
            for (int j = i + 1; j < finalPosition.length; j++) {
                max = Math.max(max, Triplet.manhattanDistance(finalPosition[i], finalPosition[j]));
            }
        }
        return max;
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

    private static class Scanner {
        private ArrayList<Triplet> triplets;
        private HashSet<Triplet> tripletsSet;

        private Scanner() {
            triplets = new ArrayList<>();
            tripletsSet = new HashSet<>();
        }

        private Scanner(Scanner sc, int facing, int rotation) {
            triplets = new ArrayList<>();
            tripletsSet = new HashSet<>();
            for (Triplet currTriplet : sc.triplets) {
                Triplet newTriplet = currTriplet.changeFacing(facing).changeRotation(rotation);
                triplets.add(newTriplet);
                tripletsSet.add(newTriplet);
            }
        }

        private void addTriplet(Triplet triplet) {
            triplets.add(triplet);
            tripletsSet.add(triplet);
        }

        private Triplet getRelative(Scanner other) {
            for (Triplet currTriplet: triplets) {
                for (Triplet currOtherTriplet: other.triplets) {
                    int xRel = currTriplet.x - currOtherTriplet.x;
                    int yRel = currTriplet.y - currOtherTriplet.y;
                    int zRel = currTriplet.z - currOtherTriplet.z;
                    int count = 0;
                    for (Triplet currOtherTripletTemp: other.triplets) {
                        Triplet newRelTriplet = new Triplet(currOtherTripletTemp.x + xRel, currOtherTripletTemp.y + yRel, currOtherTripletTemp.z + zRel);
                        if (tripletsSet.contains(newRelTriplet)) {
                            count++;
                            if (count >= 12) {
                                return new Triplet(xRel, yRel, zRel);
                            }
                        }
                    }
                }
            }
            return null;
        }
    }

    private static class ScannerPossibilities {
        private int[][] distancesMatrix;
        private Scanner[] possibilities;

        private ScannerPossibilities(Scanner scanner) {
            possibilities = new Scanner[24];
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 4; j++) {
                    possibilities[(4 * i) + j] = new Scanner(scanner, i, j);
                }
            }
            distancesMatrix = new int[scanner.triplets.size()][scanner.triplets.size()];
            for (int i = 0; i < distancesMatrix.length; i++) {
                for (int j = i + 1; j < distancesMatrix[0].length; j++) {
                    distancesMatrix[i][j] = Triplet.distance(scanner.triplets.get(i), scanner.triplets.get(j));
                    distancesMatrix[j][i] = distancesMatrix[i][j];
                }
                Arrays.sort(distancesMatrix[i]);
            }
        }

        private boolean matchDistancesMatrix(ScannerPossibilities sp) {
            for (int i = 0; i < distancesMatrix.length; i++) {
                for (int j = 0; j < sp.distancesMatrix.length; j++) {
                    int count = 0;
                    int currIndex = 0;
                    int otherIndex = 0;
                    while (currIndex < distancesMatrix.length && otherIndex < sp.distancesMatrix.length) {
                        switch (Integer.compare(distancesMatrix[i][currIndex], sp.distancesMatrix[j][otherIndex])) {
                        case -1:
                            currIndex++;
                            break;
                        case 0: {
                            count++;
                            if (count >= 12) {
                                return true;
                            }
                            currIndex++;
                            otherIndex++;
                            break;
                        }
                        case 1:
                            otherIndex++;
                            break;
                        default:
                            break;
                        }
                    }
                }
            }
            return false;
        }

        private Pair transformScanner(Scanner guide) {
            for (Scanner currScanner: possibilities) {
                Triplet relative = guide.getRelative(currScanner);
                if (relative == null) {
                    continue;
                }
                return new Pair(currScanner, relative);
            }
            return null;
        }
    }

    private static class Triplet {
        private int x;
        private int y;
        private int z;

        private Triplet(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        private Triplet changeFacing(int facing) {
            switch (facing) {
            case 0:
                return this;
            case 1:
                return new Triplet(x, -y, -z);
            case 2:
                return new Triplet(x, -z, y);
            case 3:
                return new Triplet(-y, -z, x);
            case 4:
                return new Triplet(-x, -z, -y);
            case 5:
                return new Triplet(y, -z, -x);
            default:
                return null;
            }
        }

        private Triplet changeRotation(int rotation) {
            switch (rotation) {
            case 0:
                return this;
            case 1:
                return new Triplet(-y, x, z);
            case 2:
                return new Triplet(-x, -y, z);
            case 3:
                return new Triplet(y, -x, z);
            default:
                return null;
            }
        }

        /**
         * TODO
         * Check for other possible combinations of changeFacing and changeRotation
         */
//        private Triplet changeFacing(int facing) {
//            switch (facing) {
//            case 0:
//                return this;
//            case 1:
//                return new Triplet(z, y, -x);
//            case 2:
//                return new Triplet(-x, y, -z);
//            case 3:
//                return new Triplet(-z, y, x);
//            case 4:
//                return new Triplet(y, -z, x);
//            case 5:
//                return new Triplet(-y, -z, x);
//            default:
//                return null;
//            }
//        }
//
//        private Triplet changeRotation(int rotation) {
//            switch (rotation) {
//            case 0:
//                return this;
//            case 1:
//                return new Triplet(x, -z, y);
//            case 2:
//                return new Triplet(x, -y, -z);
//            case 3:
//                return new Triplet(x, z, -y);
//            default:
//                return null;
//            }
//        }

        private static int distance(Triplet a, Triplet b) {
            int dist = 0;
            dist += (a.x - b.x) * (a.x - b.x);
            dist += (a.y - b.y) * (a.y - b.y);
            dist += (a.z - b.z) * (a.z - b.z);
            return dist;
        }

        private static int manhattanDistance(Triplet a, Triplet b) {
            return Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z);
        }

        private Triplet add(Triplet other) {
            return new Triplet(x + other.x, y + other.y, z + other.z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Triplet)) {
                return false;
            }
            Triplet triplet = (Triplet) o;
            return x == triplet.x && y == triplet.y && z == triplet.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + ", " + z + "]";
        }
    }

    private static class Pair {
        private Scanner scanner;
        private Triplet triplet;

        private Pair(Scanner scanner, Triplet triplet) {
            this.scanner = scanner;
            this.triplet = triplet;
        }
    }
}
