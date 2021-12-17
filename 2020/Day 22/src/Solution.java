import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
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
        Queue<Integer> player1 = new LinkedList<>();
        fs.next(); // skip the title
        fs.next();
        for (int i = 0; i < 25; i++) {
            player1.add(fs.nextInt());
        }
        Queue<Integer> player2 = new LinkedList<>();
        fs.next();
        fs.next();
        for (int i = 0; i < 25; i++) {
            player2.add(fs.nextInt());
        }
        while (!player1.isEmpty() && !player2.isEmpty()) {
            if (player1.peek() > player2.peek()) {
                player1.add(player1.poll());
                player1.add(player2.poll());
            } else {
                player2.add(player2.poll());
                player2.add(player1.poll());
            }
        }
        if (player1.isEmpty()) {
            player1 = player2;
        }
        return getScore(player1);
    }

    private int part2() {
        LinkedList<Integer> player1 = new LinkedList<>();
        fs.next();
        fs.next();
        for (int i = 0; i < 25; i++) {
            player1.add(fs.nextInt());
        }
        LinkedList<Integer> player2 = new LinkedList<>();
        fs.next();
        fs.next();
        for (int i = 0; i < 25; i++) {
            player2.add(fs.nextInt());
        }
        if (recursiveGame(player1, player2)) {
            return getScore(player1);
        } else {
            return getScore(player2);
        }
    }

    private boolean recursiveGame(LinkedList<Integer> player1, LinkedList<Integer> player2) {
        HashSet<Integer> previousDecks1 = new HashSet<>();
        HashSet<Integer> previousDecks2 = new HashSet<>();
        while (!player1.isEmpty() && !player2.isEmpty()) {
            int hash1 = player1.hashCode();
            int hash2 = player2.hashCode();
            if (previousDecks1.contains(hash1) || previousDecks2.contains(hash2)) { // if deck is repeated
                return true; // player 1 wins
            }
            previousDecks1.add(hash1); // else remember the current decks
            previousDecks2.add(hash2);
            int card1 = player1.poll(); // draw the top card
            int card2 = player2.poll();
            if (player1.size() < card1 || player2.size() < card2) { // if any player doesn't have at least as many cards as their card value
                if (card1 > card2) { // the player with the higher card value wins
                    player1.add(card1);
                    player1.add(card2);
                } else {
                    player2.add(card2);
                    player2.add(card1);
                }
                continue;
            }
            LinkedList<Integer> newPlayer1 = new LinkedList<>(player1.subList(0, card1)); // else copy the sub decks
            LinkedList<Integer> newPlayer2 = new LinkedList<>(player2.subList(0, card2));
            boolean winner = recursiveGame(newPlayer1, newPlayer2); // play another recursive game
            if (winner) { // if player 1 wins
                player1.add(card1);
                player1.add(card2);
            } else {
                player2.add(card2);
                player2.add(card1);
            }
        }
        return player2.isEmpty(); // if player2 deck is empty => player1 wins, else the opposite
    }

    private int getScore(Queue<Integer> deck) { // calculate the score
        int ans = 0;
        while (!deck.isEmpty()) {
            ans += deck.poll() * (deck.size() + 1);
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

        private int nextInt() {
            return Integer.parseInt(next());
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
