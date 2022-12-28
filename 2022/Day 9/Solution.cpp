#include "bits/stdc++.h"

using namespace std;

typedef long long ll;
typedef pair<int, int> ii;
typedef vector<ii> VII;

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

unordered_map<char, ii> dirs = {{'L', {-1, 0}}, {'R', {1, 0}}, {'U', {0, 1}}, {'D', {0, -1}}};

ll getHash(ll tX, ll tY) {
    tX <<= 32;
    tY &= 0xFFFFFFFF;
    return tX | tY;
}

void updateKnot(int hX, int hY, int &kX, int &kY) {
    if (kX - 1 <= hX && hX <= kX + 1 && kY - 1 <= hY && hY <= kY + 1) {
        return;
    }
    if (kX == hX || kY == hY) {
        kX = (hX + kX) / 2;
        kY = (hY + kY) / 2;
    } else {
        if (hX == kX - 1 || hX == kX + 1) {
            kX = hX;
            kY = (hY + kY) / 2;
        } else if (hY == kY - 1 || hY == kY + 1) {
            kY = hY;
            kX = (hX + kX) / 2;
        } else {
            kX = (hX + kX) / 2;
            kY = (hY + kY) / 2;
        }
    }
}

void part1(Reader &reader) {
    int hX = 0, hY = 0, tX = 0, tY = 0;
    unordered_set<ll> visited;
    visited.insert(0);
    while (!reader.isEOF()) {
        auto currDir = reader.getToken<char>();
        auto currVal = reader.getToken<int>();
        auto move = dirs[currDir];
        for (int i = 0; i < currVal; ++i) {
            hX += move.first, hY += move.second;
            updateKnot(hX, hY, tX, tY);
            visited.insert(getHash(tX, tY));
        }
    }
    cout << visited.size() << "\n";
}

void part2(Reader &reader) {
    VII knots(10, {0, 0});
    unordered_set<ll> visited;
    visited.insert(0);
    while (!reader.isEOF()) {
        auto currDir = reader.getToken<char>();
        auto currVal = reader.getToken<int>();
        auto move = dirs[currDir];
        for (int i = 0; i < currVal; ++i) {
            knots[0].first += move.first, knots[0].second += move.second;
            for (int j = 1; j < 10; ++j) {
                updateKnot(knots[j - 1].first, knots[j - 1].second, knots[j].first, knots[j].second);
            }
            visited.insert(getHash(knots[9].first, knots[9].second));
        }
    }
    cout << visited.size() << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}