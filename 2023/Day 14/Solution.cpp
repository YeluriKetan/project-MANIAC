#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef vector<int> VI;
typedef vector<char> VC;
typedef vector<VC> VVC;

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

void rotateSqCwInPlace(VVC &grid, int n) {
    for (int i = 0; i < n / 2; ++i) {
        for (int j = i; j < n - i - 1; ++j) {
            char temp = grid[i][j];
            grid[i][j] = grid[n - j - 1][i];
            grid[n - j - 1][i] = grid[n - i - 1][n - j - 1];
            grid[n - i - 1][n - j - 1] = grid[j][n - i - 1];
            grid[j][n - i - 1] = temp;
        }
    }
}

void moveUp(VVC &grid, int r, int c) {
    for (int j = 0; j < c; ++j) {
        for (int i = 0; i < r; ++i) {
            if (grid[i][j] != 'O') continue;
            for (int k = i - 1; k > -1; --k) {
                if (grid[k][j] == '.') {
                    grid[k][j] = 'O';
                    grid[k + 1][j] = '.';
                } else {
                    break;
                }
            }
        }
    }
}

int loadVal(VVC &grid, int r, int c) {
    int total = 0;
    for (int i = 0; i < r; ++i) {
        for (int j = 0; j < c; ++j) {
            if (grid[i][j] == 'O') total += r - i;
        }
    }
    return total;
}

string hashGrid(VVC &grid) {
    int prevCount = 0;
    bool prevMir = false;
    string hashVal;
    for (const auto &currRow: grid) {
        for (auto currChar: currRow) {
            if (prevMir == (currChar == 'O')) {
                prevCount++;
            } else {
                prevMir = !prevMir;
                hashVal += to_string(prevCount) + "|";
                prevCount = 1;
            }
        }
    }
    hashVal += to_string(prevCount);
    return hashVal;
}

void part1(Reader &reader) {
    VVC grid;
    while (!reader.isEOF()) {
        VC currRow;
        for (auto currChar: reader.getLine()) currRow.push_back(currChar);
        grid.push_back(currRow);
    }
    int r = grid.size(), c = grid[0].size();
    moveUp(grid, r, c);
    int total = 0;
    for (int i = 0; i < r; ++i) {
        for (int j = 0; j < c; ++j) {
            if (grid[i][j] == 'O') total += r - i;
        }
    }
    cout << total << "\n";
}

void part2(Reader &reader) {
    VVC grid;
    while (!reader.isEOF()) {
        VC currRow;
        for (auto currChar: reader.getLine()) currRow.push_back(currChar);
        grid.push_back(currRow);
    }
    int r = grid.size(), c = grid[0].size();
    VI loadValues;
    unordered_map<string, int> hashToInt;
    int repeatInd = -1;
    while (true) {
        for (int i = 0; i < 4; ++i) {
            moveUp(grid, r, c);
            rotateSqCwInPlace(grid, r);
        }
        auto currHash = hashGrid(grid);
        auto it = hashToInt.emplace(currHash, loadValues.size());
        if (!it.second) {
            repeatInd = it.first->second;
            break;
        }
        loadValues.push_back(loadVal(grid, r, c));
    }
    cout << loadValues[((1000000000 - repeatInd - 1) % (loadValues.size() - repeatInd)) + repeatInd] << endl;
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}