#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef pair<int, int> ii;
typedef vector<ii> VII;
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

VII cardinal = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
unordered_map<char, VII> pipings = {{'|', {cardinal[0], cardinal[2]}},
                                    {'-', {cardinal[1], cardinal[3]}},
                                    {'L', {cardinal[0], cardinal[1]}},
                                    {'J', {cardinal[0], cardinal[3]}},
                                    {'7', {cardinal[2], cardinal[3]}},
                                    {'F', {cardinal[1], cardinal[2]}}};

bool isValid(ii pos, int r, int c) {
    return 0 <= pos.first || pos.first < r || 0 <= pos.second || pos.second < c;
}

ii add(ii &a, ii &b) {
    return {a.first + b.first, a.second + b.second};
}

// given prev and curr position, find next pos. Need prev pos to move forward and not backwards
ii next(ii prev, ii curr, VVC &grid, int r, int c) {
    if (grid[curr.first][curr.second] == '.') {
        return {-1, -1};
    }
    bool prevValid = false;
    ii nextPos = {-1, -1};
    for (auto a: pipings[grid[curr.first][curr.second]]) {
        ii temp = add(curr, a);
        if (!isValid(temp, r, c)) return {-1, -1};
        if (temp == prev) {
            prevValid = true;
        } else {
            nextPos = temp;
        }
    }
    if (!prevValid || nextPos.first == -1) {
        return {-1, -1};
    }
    return nextPos;
}

void part1(Reader &reader) {
    VVC grid;
    int r = 0, c;
    ii start;
    while (!reader.isEOF()) {
        auto currLine = reader.getLine();
        c = 0;
        VC currRow;
        for (auto currChar: currLine) {
            currRow.push_back(currChar);
            if (currChar == 'S') start = {r, c};
            c++;
        }
        grid.push_back(currRow);
        r++;
    }
    for (auto currCardinal: cardinal) {
        ii prev = start, curr = add(start, currCardinal);
        int count = 1;
        while (curr != start && curr.first != -1) {
            ii temp = next(prev, curr, grid, r, c);
            prev = curr;
            curr = temp;
            count++;
        }
        if (curr.first == -1) continue;
        cout << count / 2 << endl;
        return;
    }
    cout << "-1" << endl;
}

char replaceChar(char c) {
    if (c == 'J' || c == 'L' || c == '|') return '+';
    if (c == '-' || c == '7' || c == 'F') return '=';
    return '.';
}

void part2(Reader &reader) {
    VVC grid;
    int r = 0, c;
    ii start;
    while (!reader.isEOF()) {
        auto currLine = reader.getLine();
        c = 0;
        VC currRow;
        for (auto currChar: currLine) {
            currRow.push_back(currChar);
            if (currChar == 'S') start = {r, c};
            c++;
        }
        grid.push_back(currRow);
        r++;
    }
    ii loopCardinal;
    for (auto currCardinal: cardinal) {
        ii prev = start, curr = add(start, currCardinal);
        int count = 1;
        // iterative simulation of loop
        while (curr != start && curr.first != -1) {
            ii temp = next(prev, curr, grid, r, c);
            prev = curr;
            curr = temp;
            count++;
        }
        if (curr.first == -1) continue;
        ii begin = add(start, currCardinal), end = prev;
        for (auto currPipe: pipings) { // find exact pipe and place it in grid
            ii tmp1 = add(start, currPipe.second[0]), tmp2 = add(start, currPipe.second[1]);
            if ((tmp1 == begin && tmp2 == end) || (tmp1 == end && tmp2 == begin)) {
                grid[start.first][start.second] = currPipe.first;
                break;
            }
        }
        loopCardinal = currCardinal;
        break;
    }

    // replace edges/corners that change parity and make others parts of the loop duds
    ii prev = start, curr = add(start, loopCardinal);
    grid[prev.first][prev.second] = replaceChar(grid[prev.first][prev.second]);
    while (curr != start) {
        ii temp = next(prev, curr, grid, r, c);
        prev = curr;
        curr = temp;
        grid[prev.first][prev.second] = replaceChar(grid[prev.first][prev.second]);
    }

    // ray tracing simulation
    int parity = 0, count = 0;
    for (auto &row: grid) {
        parity = 0;
        for (auto &col: row) {
            if (col == '+') {
                parity++;
            } else if (col != '=') {
                if (parity % 2) count++;
            }
        }
    }
    cout << count << endl;
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}