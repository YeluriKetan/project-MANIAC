#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef long long ll;
typedef pair<int, int> ii;
typedef vector<ii> VII;
typedef vector<char> VC;
typedef vector<VC> VVC;
typedef vector<bool> VB;

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

ll distWithScale(ii &a , ii &b, VB &rows, VB &cols, ll scale) {
    ll val = 0;
    for (int i = min(a.first, b.first) + 1; i <= max(a.first, b.first); ++i) {
        val += rows[i] ? scale : 1;
    }
    for (int i = min(a.second, b.second) + 1; i <= max(a.second, b.second); ++i) {
        val += cols[i] ? scale : 1;
    }
    return val;
}

void part1(Reader &reader) {
    VVC grid;
    while (!reader.isEOF()) {
        VC curr;
        for (auto currChar: reader.getLine()) curr.push_back(currChar);
        grid.push_back(curr);
    }
    int r = grid.size(), c = grid[0].size();
    VB rows = VB(r, true), cols(c, true);
    VII galaxies;
    for (int i = 0; i < r; ++i) {
        for (int j = 0; j < c; ++j) {
            if (grid[i][j] == '#') {
                rows[i] = false;
                cols[j] = false;
                galaxies.emplace_back(i, j);
            }
        }
    }
    int n = galaxies.size();
    ll total = 0;
    for (int i = 0; i < n; ++i) {
        for (int j = i + 1; j < n; ++j) {
            total += distWithScale(galaxies[i], galaxies[j], rows, cols, 2);
        }
    }
    cout << total << "\n";
}

void part2(Reader &reader) {
    VVC grid;
    while (!reader.isEOF()) {
        VC curr;
        for (auto currChar: reader.getLine()) curr.push_back(currChar);
        grid.push_back(curr);
    }
    int r = grid.size(), c = grid[0].size();
    VB rows = VB(r, true), cols(c, true);
    VII galaxies;
    for (int i = 0; i < r; ++i) {
        for (int j = 0; j < c; ++j) {
            if (grid[i][j] == '#') {
                rows[i] = false;
                cols[j] = false;
                galaxies.emplace_back(i, j);
            }
        }
    }
    int n = galaxies.size();
    ll total = 0;
    for (int i = 0; i < n; ++i) {
        for (int j = i + 1; j < n; ++j) {
            total += distWithScale(galaxies[i], galaxies[j], rows, cols, 1000000);
        }
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