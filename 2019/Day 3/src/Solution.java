import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

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
        String[] strArr;
        int hor;
        int vert;
        ArrayList<ArrayList<ArrayList<Line>>> wires = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ArrayList<ArrayList<Line>> currWire = new ArrayList<>();
            currWire.add(new ArrayList<>());
            currWire.add(new ArrayList<>());
            strArr = fs.next().split(",");
            hor = 0;
            vert = 0;
            for (String s : strArr) {
                int val = Integer.parseInt(s.substring(1));
                switch (s.charAt(0)) {
                case 'R':
                    currWire.get(0).add(new Line(hor, vert, val));
                    hor += val;
                    break;
                case 'L':
                    currWire.get(0).add(new Line(hor - val, vert, val));
                    hor -= val;
                    break;
                case 'U':
                    currWire.get(1).add(new Line(hor, vert, val));
                    vert += val;
                    break;
                case 'D':
                    currWire.get(1).add(new Line(hor, vert - val, val));
                    vert -= val;
                    break;
                default:
                    break;
                }
            }
            wires.add(currWire);
        }
        int min = Integer.MAX_VALUE;
        int curr;
        for (int i = 0; i < wires.get(0).get(0).size(); i++) {
            for (int j = 0; j < wires.get(1).get(1).size(); j++) {
                curr = wires.get(0).get(0).get(i).getIntManDist(wires.get(1).get(1).get(j));
                if (curr != 0) {
                    min = Math.min(min, curr);
                }
            }
        }
        for (int i = 0; i < wires.get(1).get(0).size(); i++) {
            for (int j = 0; j < wires.get(0).get(1).size(); j++) {
                curr = wires.get(1).get(0).get(i).getIntManDist(wires.get(0).get(1).get(j));
                if (curr != 0) {
                    min = Math.min(min, curr);
                }
            }
        }
        return min;
    }

    private int part2() {
        String[] strArr;
        int hor;
        int vert;
        int prevSteps;
        ArrayList<ArrayList<ArrayList<Line>>> wires = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ArrayList<ArrayList<Line>> currWire = new ArrayList<>();
            currWire.add(new ArrayList<>());
            currWire.add(new ArrayList<>());
            strArr = fs.next().split(",");
            hor = 0;
            vert = 0;
            prevSteps = 0;
            for (String s : strArr) {
                int val = Integer.parseInt(s.substring(1));
                switch (s.charAt(0)) {
                case 'R':
                    currWire.get(0).add(new Line(hor, vert, val, prevSteps, true));
                    hor += val;
                    break;
                case 'L':
                    currWire.get(0).add(new Line(hor - val, vert, val, prevSteps, false));
                    hor -= val;
                    break;
                case 'U':
                    currWire.get(1).add(new Line(hor, vert, val, prevSteps, true));
                    vert += val;
                    break;
                case 'D':
                    currWire.get(1).add(new Line(hor, vert - val, val, prevSteps, false));
                    vert -= val;
                    break;
                default:
                    break;
                }
                prevSteps += val;
            }
            wires.add(currWire);
        }
        int min = Integer.MAX_VALUE;
        int curr;
        for (int i = 0; i < wires.get(0).get(0).size(); i++) {
            for (int j = 0; j < wires.get(1).get(1).size(); j++) {
                curr = wires.get(0).get(0).get(i).getIntSteps(wires.get(1).get(1).get(j));
                if (curr != 0) {
                    min = Math.min(min, curr);
                }
            }
        }
        for (int i = 0; i < wires.get(1).get(0).size(); i++) {
            for (int j = 0; j < wires.get(0).get(1).size(); j++) {
                curr = wires.get(1).get(0).get(i).getIntSteps(wires.get(0).get(1).get(j));
                if (curr != 0) {
                    min = Math.min(min, curr);
                }
            }
        }
        return min;
    }

    private static class Line {
        private int beginX;
        private int beginY;
        private int length;
        private int prevSteps;
        private boolean opp;

        private Line(int beginX, int beginY, int length) {
            this.beginX = beginX;
            this.beginY = beginY;
            this.length = length;
        }

        private Line(int beginX, int beginY, int length, int prevSteps, boolean opp) {
            this.beginX = beginX;
            this.beginY = beginY;
            this.length = length;
            this.prevSteps = prevSteps;
            this.opp = opp;
        }

        private int getIntManDist(Line vert) {
            if (vert.beginX < this.beginX || this.beginX + this.length < vert.beginX) {
                return Integer.MAX_VALUE;
            }
            if (this.beginY < vert.beginY || vert.beginY + vert.length < this.beginY) {
                return Integer.MAX_VALUE;
            }
            return Math.abs(vert.beginX) + Math.abs(this.beginY);
        }

        private int getIntSteps(Line vert) {
            if (vert.beginX < this.beginX || this.beginX + this.length < vert.beginX) {
                return Integer.MAX_VALUE;
            }
            if (this.beginY < vert.beginY || vert.beginY + vert.length < this.beginY) {
                return Integer.MAX_VALUE;
            }
            int steps = 0;
            if (this.opp) {
                steps += this.prevSteps + vert.beginX - this.beginX;
            } else {
                steps += this.prevSteps + this.beginX + this.length - vert.beginX;
            }
            if (vert.opp) {
                steps += vert.prevSteps + this.beginY - vert.beginY;
            } else {
                steps += vert.prevSteps + vert.beginY + vert.length - this.beginY;
            }
            return steps;
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
