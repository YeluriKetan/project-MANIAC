import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;

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
        LinkedList<Integer> cups = new LinkedList<>();
        for (int i = 0; i < curr.length(); i++) {
            cups.add(curr.charAt(i) - '0');
        }
        for (int i = 0; i < 100; i++) {
            move(cups);
        }
        return getOrder(cups);
    }

    private void move(LinkedList<Integer> cups) {
        LinkedList<Integer> threeCups = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            threeCups.add(cups.remove(1));
        }
        int destination = cups.peek() - 1;
        while (true) {
            if (destination < 1) {
                destination = 9;
            } else if (threeCups.contains(destination)) {
                destination--;
            } else {
                break;
            }
        }
        cups.addAll(cups.indexOf(destination) + 1, threeCups);
        cups.add(cups.pollFirst());
    }

    private int getOrder(LinkedList<Integer> cups) {
        int ans = 0;
        while (cups.peek() != 1) {
            cups.add(cups.poll());
        }
        cups.poll();
        while (!cups.isEmpty()) {
            ans *= 10;
            ans += cups.poll();
        }
        return ans;
    }

    private long part2() {
        String curr = fs.next();
        CupsInCircle cups = new CupsInCircle();
        for (int i = 0; i < curr.length(); i++) {
            cups.add(curr.charAt(i) - '0');
        }
        cups.addRest(10, 1000000);
        for (int i = 0; i < 10000000; i++) {
            cups.moveCups();
        }
        return cups.getAnswer();
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

    private static class CupsInCircle {
        private Node head;
        private Node tail;
        private HashMap<Integer, Node> map;

        private CupsInCircle() {
            map = new HashMap<>();
        }

        private void add(int newVal) {
            Node curr = new Node(newVal);
            map.put(newVal, curr);
            if (head == null) {
                head = curr;
                tail = curr;
                return;
            }
            tail.next = curr;
            tail = tail.next;
        }

        private void addRest(int from, int to) {
            Node curr = tail;
            for (int i = from; i <= to; i++) {
                curr.next = new Node(i);
                map.put(i, curr.next);
                curr = curr.next;
            }
            tail = curr;
        }

        private void moveCups() {
            Node a = head.next;
            head.next = a.next.next.next;
            int destination = head.val - 1;
            while (true) {
                if (destination < 1) {
                    destination = 1000000;
                } else if (destination == a.val || destination == a.next.val || destination == a.next.next.val) {
                    destination--;
                } else {
                    break;
                }
            }
            Node curr = map.get(destination);
            a.next.next.next = curr.next;
            curr.next = a;
            if (curr == tail) {
                tail = a.next.next;
            }
            tail.next = head;
            tail = tail.next;
            head = head.next;
            tail.next = null;
        }

        private long getAnswer() {
            Node curr = map.get(1);
            long ans = 1;
            for (int i = 0; i < 2; i++) {
                curr = curr.next;
                if (curr == null) {
                    curr = head;
                }
                ans *= curr.val;
            }
            return ans;
        }
    }

    private static class Node {
        private int val;
        private Node next;

        private Node(int val) {
            this.val = val;
        }
    }
}
