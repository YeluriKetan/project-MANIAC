#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef long long ll;
typedef pair<int, int> ii;
typedef vector<int> VI;
typedef vector<ll> VLL;

class Reader {

private:
    ifstream input;
    stringstream line;

    void updateLine() {
        string nextLine;
        getline(input, nextLine);
        line = stringstream(nextLine);
    }

public:
    Reader(string filename) {
        input.open(filename);
    }

    ~Reader() {
        input.close();
    }

    template <class T>
    T getToken() {
        T curr;
        if (line >> curr) {
            return curr;
        } else {
            updateLine();
            return getToken<T>();
        }
    }

    string getLine() {
        string nextLine;
        getline(input, nextLine);
        return nextLine;
    }

    bool isNewLine() {
        if (line.peek() == -1) {
            return input.peek() == '\n';
        } else {
            return false;
        }
    }

    bool isEOF() {
        if (line.peek() == -1) {
            return input.peek() == EOF;
        } else {
            return line.peek() == EOF;
        }
    }

    Reader& operator=(Reader reader) {
        this->input = std::move(reader.input);
        this->line = std::move(reader.line);
        return *this;
    }
};

int getType(string &s) {
    string copy = s;
    sort(copy.begin(), copy.end());
    VI counts;
    char prev = ' ', prevCount = 0;
    for (auto currChar: copy) {
        if (prev == currChar) {
            prevCount++;
        } else {
            counts.push_back(prevCount);
            prevCount = 1;
            prev = currChar;
        }
    }
    counts.push_back(prevCount);
    sort(counts.begin(), counts.end(), greater<>());
    if (counts[0] == 5) {
        return 7;
    } else if (counts[0] == 4) {
        return 6;
    } else if (counts[0] == 3 && counts[1] == 2) {
        return 5;
    } else if (counts[0] == 3) {
        return 4;
    } else if (counts[0] == 2 && counts[1] == 2) {
        return 3;
    } else if (counts[0] == 2) {
        return 2;
    } else {
        return 1;
    }
}

class Hand {
public:
    string cards;
    int winning;
    int type;

    Hand(string c, int w, int t): cards(c), winning(w) {
        if (t >= 0) {
            type = t;
        } else {
            type = getType(cards);
        }
    }
};

unordered_map<char, int> ordering = {{'A', 14}, {'K', 13}, {'Q', 12}, {'J', 11},
                                     {'T', 10}, {'9', 9}, {'8', 8}, {'7', 7},
                                     {'6', 6}, {'5', 5}, {'4', 4}, {'3', 3},
                                     {'2', 2}};

bool cmp(Hand* a, Hand* b) {
    if (a->type == b->type) {
        for (int i = 0; i < 5; ++i) {
            if (a->cards[i] != b->cards[i]) {
                return ordering[a->cards[i]] > ordering[b->cards[i]];
            }
        }
        return false;
    } else {
        return a->type > b->type;
    }
}

void part1(Reader &reader) {
    vector<Hand*> hands;
    while (!reader.isEOF()) {
        Hand* newHand = new Hand(reader.getToken<string>(), reader.getToken<int>(), -1);
        hands.push_back(newHand);
    }
    sort(hands.begin(), hands.end(), cmp);
    ll n = hands.size(), total = 0;
    for (ll i = 1; i <= n; ++i) {
        total += hands[n - i]->winning * i;
    }
    cout << total << "\n";
}

unordered_map<char, int> ordering2 = {{'A', 14}, {'K', 13}, {'Q', 12}, {'J', 1},
                                      {'T', 10}, {'9', 9}, {'8', 8}, {'7', 7},
                                      {'6', 6}, {'5', 5}, {'4', 4}, {'3', 3},
                                      {'2', 2}};

bool cmp2(Hand* a, Hand* b) {
    if (a->type == b->type) {
        for (int i = 0; i < 5; ++i) {
            if (a->cards[i] != b->cards[i]) {
                return ordering2[a->cards[i]] > ordering2[b->cards[i]];
            }
        }
        return false;
    } else {
        return a->type > b->type;
    }
}

int maxType(string &s, VI &jacks, int ind) {
    if (ind >= jacks.size()) {
        return getType(s);
    }
    int maxTypeVal = -1;
    for (auto curr: ordering) {
        s[jacks[ind]] = curr.first;
        maxTypeVal = max(maxTypeVal, maxType(s, jacks, ind + 1));
    }
    return maxTypeVal;
}

void part2(Reader &reader) {
    vector<Hand*> hands;
    while (!reader.isEOF()) {
        auto cards = reader.getToken<string>();
        auto copy = cards;
        VI jacks;
        for (int i = 0; i < 5; ++i) if (cards[i] == 'J') jacks.push_back(i);
        int maxTypeVal = jacks.empty() ? -1 : maxType(copy, jacks, 0);
        Hand* newHand = new Hand(cards, reader.getToken<int>(), maxTypeVal);
        hands.push_back(newHand);
    }
    sort(hands.begin(), hands.end(), cmp2);
    ll n = hands.size(), total = 0;
    for (ll i = 1; i <= n; ++i) {
        total += hands[n - i]->winning * i;
    }
    cout << total << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}