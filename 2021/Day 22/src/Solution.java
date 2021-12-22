import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
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

    private int part1() {
        String curr;
        boolean[][][] grid = new boolean[101][101][101]; // keep track of all cubes in the initialization procedure area
        while ((curr = fs.next()) != null) {
            boolean onOff = curr.charAt(1) == 'n';
            int[] arr = new int[6];
            Matcher matcher = number.matcher(curr);
            for (int i = 0; i < 6; i++) {
                matcher.find();
                arr[i] = 50 + Integer.parseInt(matcher.group()); // -50 to 50 is converted to 0 to 100
            }
            for (int z = Math.max(0, arr[4]); z <= Math.min(100, arr[5]); z++) {
                for (int y = Math.max(0, arr[2]); y <= Math.min(100, arr[3]); y++) {
                    for (int x = Math.max(0, arr[0]); x <= Math.min(100, arr[1]); x++) {
                        grid[z][y][x] = onOff;
                    }
                }
            }
        }
        int count = 0;
        for (int z = 0; z < 101; z++) { // count the cubes that are on
            for (int y = 0; y < 101; y++) {
                for (int x = 0; x < 101; x++) {
                    if (grid[z][y][x]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private long part2() {
        // Let's call each cuboid region made up of only turned on cubes a Grid
        String curr;
        LinkedList<Grid> gridList = new LinkedList<>();
        while ((curr = fs.next()) != null) {
            int[] arr = new int[6];
            Matcher matcher = number.matcher(curr);
            for (int i = 0; i < 6; i++) {
                matcher.find();
                arr[i] = Integer.parseInt(matcher.group());
            }
            Grid grid = new Grid(arr); // current Grid
            LinkedList<Grid> newList = new LinkedList<>();
            for (Grid currGrid: gridList) {
                newList.addAll(currGrid.remove(grid)); // for each grid in the existing list, remove the new grid
            }
            if (curr.charAt(1) == 'n') { // only if the new grid is to be turned on, add it once to the list
                newList.add(grid);
            }
            gridList = newList; // replace the list
        }
        long total = 0;
        for (Grid currGrid: gridList) { // count the number of cubes in each grid
            total += currGrid.count();
        }
        return total;
    }

    private static class Grid {
        int[] bounds;

        private Grid(int[] bounds) {
            this.bounds = bounds;
        }

        private long count() {
            // Since our grids are made up of only turned on cubes, we can simply count the number of cubes in the grid
            long ans = 1;
            ans *= bounds[1] - bounds[0] + 1;
            ans *= bounds[3] - bounds[2] + 1;
            ans *= bounds[5] - bounds[4] + 1;
            return ans;
        }

        private LinkedList<Grid> remove(Grid other) {
            // remove the (intersection of the current grid with the other grid) from the current grid
            boolean check; // to check if there's are overlap between the two grids at all
            check = other.bounds[1] >= bounds[0] && other.bounds[0] <= bounds[1];
            check = check && (other.bounds[3] >= bounds[2] && other.bounds[2] <= bounds[3]);
            check = check && (other.bounds[5] >= bounds[4] && other.bounds[4] <= bounds[5]);
            LinkedList<Grid> newList = new LinkedList<>();
            if (!check) { // if no intersection, just return a list with only the current element in it
                newList.add(this);
                return newList;
            }
            if (other.bounds[5] < bounds[5]) { // get the slice of the current grid above the other grid
                int[] arr = new int[6];
                System.arraycopy(bounds, 0, arr, 0, 4);
                arr[5] = bounds[5];
                bounds[5] = other.bounds[5]; // update the current grid bounds as well
                arr[4] = bounds[5] + 1;
                Grid newGrid = new Grid(arr);
                newList.add(newGrid);
            }
            // since we update the bounds of the remaining current grid everytime we cut a slice,
            // we only get a maximum of 6 new smaller grids from this removal process
            if (other.bounds[4] > bounds[4]) { // get the slice of the current grid below the other grid
                int[] arr = new int[6];
                System.arraycopy(bounds, 0, arr, 0, 4);
                arr[4] = bounds[4];
                bounds[4] = other.bounds[4];
                arr[5] = bounds[4] - 1;
                Grid newGrid = new Grid(arr);
                newList.add(newGrid);
            }
            if (other.bounds[3] < bounds[3]) {
                // get the slice of the current grid in the positive y direction of the other grid
                int[] arr = new int[6];
                System.arraycopy(bounds, 0, arr, 0, 2);
                System.arraycopy(bounds, 4, arr, 4, 2);
                arr[3] = bounds[3];
                bounds[3] = other.bounds[3];
                arr[2] = bounds[3] + 1;
                Grid newGrid = new Grid(arr);
                newList.add(newGrid);
            }
            if (other.bounds[2] > bounds[2]) { // negative y direction of the other grid
                int[] arr = new int[6];
                System.arraycopy(bounds, 0, arr, 0, 2);
                System.arraycopy(bounds, 4, arr, 4, 2);
                arr[2] = bounds[2];
                bounds[2] = other.bounds[2];
                arr[3] = bounds[2] - 1;
                Grid newGrid = new Grid(arr);
                newList.add(newGrid);
            }
            if (other.bounds[1] < bounds[1]) { // positive x direction
                int[] arr = new int[6];
                System.arraycopy(bounds, 2, arr, 2, 4);
                arr[1] = bounds[1];
                bounds[1] = other.bounds[1];
                arr[0] = bounds[1] + 1;
                Grid newGrid = new Grid(arr);
                newList.add(newGrid);
            }
            if (other.bounds[0] > bounds[0]) { // negative x direction
                int[] arr = new int[6];
                System.arraycopy(bounds, 2, arr, 2, 4);
                arr[0] = bounds[0];
                bounds[0] = other.bounds[0];
                arr[1] = bounds[0] - 1;
                Grid newGrid = new Grid(arr);
                newList.add(newGrid);
            }
            return newList;
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
