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
        int direction = 1; // 0 -> N, 1 -> E, 2 -> S, 3 -> W
        int horizontal = 0;
        int vertical = 0;
        String curr;
        int currInt;
        for (int i = 0; i < 781; i++) {
            curr = fs.next();
            currInt = Integer.parseInt(curr.substring(1));
            switch (curr.charAt(0)) {
            case 'N':
                vertical += currInt;
                break;
            case 'S':
                vertical -= currInt;
                break;
            case 'E':
                horizontal += currInt;
                break;
            case 'W':
                horizontal -= currInt;
                break;
            case 'L':
                direction = ((direction + 4) - (currInt / 90)) % 4;
                break;
            case 'R':
                direction = (direction + (currInt / 90)) % 4;
                break;
            case 'F': {
                switch (direction) {
                case 0:
                    vertical += currInt;
                    break;
                case 1:
                    horizontal += currInt;
                    break;
                case 2:
                    vertical -= currInt;
                    break;
                case 3:
                    horizontal -= currInt;
                    break;
                default:
                    break;
                }
            }
            default:
                break;
            }
        }
        return Math.abs(horizontal) + Math.abs(vertical);
    }

    private int part2() {
        int horizontalWayPoint = 10;
        int verticalWayPoint = 1;
        int horizontalDistance = 0;
        int verticalDistance = 0;
        String curr;
        int currInt;
        for (int i = 0; i < 781; i++) {
            curr = fs.next();
            currInt = Integer.parseInt(curr.substring(1));
            switch (curr.charAt(0)) {
            case 'N':
                verticalWayPoint += currInt;
                break;
            case 'S':
                verticalWayPoint -= currInt;
                break;
            case 'E':
                horizontalWayPoint += currInt;
                break;
            case 'W':
                horizontalWayPoint -= currInt;
                break;
            case 'L': {
                switch (currInt / 90) {
                case 1: {
                    int temp = verticalWayPoint;
                    verticalWayPoint = horizontalWayPoint;
                    horizontalWayPoint = -temp;
                    break;
                }
                case 2: {
                    verticalWayPoint = -verticalWayPoint;
                    horizontalWayPoint = -horizontalWayPoint;
                    break;
                }
                case 3: {
                    int temp = horizontalWayPoint;
                    horizontalWayPoint = verticalWayPoint;
                    verticalWayPoint = -temp;
                    break;
                }
                default:
                    break;
                }
                break;
            }
            case 'R': {
                switch (currInt / 90) {
                case 1: {
                    int temp = horizontalWayPoint;
                    horizontalWayPoint = verticalWayPoint;
                    verticalWayPoint = -temp;
                    break;
                }
                case 2: {
                    verticalWayPoint = -verticalWayPoint;
                    horizontalWayPoint = -horizontalWayPoint;
                    break;
                }
                case 3: {
                    int temp = verticalWayPoint;
                    verticalWayPoint = horizontalWayPoint;
                    horizontalWayPoint = -temp;
                    break;
                }
                default:
                    break;
                }
                break;
            }
            case 'F': {
                horizontalDistance += (currInt * horizontalWayPoint);
                verticalDistance += (currInt * verticalWayPoint);
                break;
            }
            default:
                break;
            }
        }
        return Math.abs(horizontalDistance) + Math.abs(verticalDistance);
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
