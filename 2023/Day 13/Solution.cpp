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

bool cmp(ii &a, ii &b) {
    if (a.first == b.first) {
        return a.second > b.second;
    } else {
        return a.first < b.first;
    }
}

// check all possible horizontal lines of reflection and return the two smallest cases of imperfections
// return {no. of imperfections, value based on rows above}
VII checkHor(VVC &grid) {
    int up = 0, down = 1, r = grid.size(), c = grid[0].size();
    VII result;
    while (down < r) {
        int currImperfections = 0;
        for (int x = up, X = down; x > -1 && X < r; --x, ++X) {
            for (int j = 0; j < c; ++j) {
                if (grid[x][j] != grid[X][j]) currImperfections++;
            }
        }
        result.push_back({currImperfections, 100 * (up + 1)});
        up++; down++;
    }
    std::sort(result.begin(), result.end(), cmp);
    return {result[0], result[1]};
}

// check all possible vertical lines of reflection and return the two smallest cases of imperfections
// return {no. of imperfections, value based on rows to the left}
VII checkVer(VVC &grid) {
    int left = 0, right = 1, r = grid.size(), c = grid[0].size();
    VII result;
    while (right < c) {
        int currImperfections = 0;
        for (int y = left, Y = right; y > -1 && Y < c; --y, ++Y) {
            for (int i = 0; i < r; ++i) {
                if (grid[i][y] != grid[i][Y]) currImperfections++;
            }
        }
        result.push_back({currImperfections, left + 1});
        left++; right++;
    }
    std::sort(result.begin(), result.end(), cmp);
    return {result[0], result[1]};
}

void part1(Reader &reader) {
    int total = 0;
    while (!reader.isEOF()) {
        if (reader.isNewLine()) reader.getLine();
        VVC currGrid;
        while (!reader.isNewLine() && !reader.isEOF()) {
            VC currLine;
            for (auto currChar: reader.getLine()) currLine.push_back(currChar);
            currGrid.push_back(currLine);
        }
        for (auto currImp: checkVer(currGrid)) if (currImp.first == 0) total += currImp.second;
        for (auto currImp: checkHor(currGrid)) if (currImp.first == 0) total += currImp.second;
    }
    cout << total << endl;
}

void part2(Reader &reader) {
    int total = 0;
    while (!reader.isEOF()) {
        if (reader.isNewLine()) reader.getLine();
        VVC currGrid;
        while (!reader.isNewLine() && !reader.isEOF()) {
            VC currLine;
            for (auto currChar: reader.getLine()) currLine.push_back(currChar);
            currGrid.push_back(currLine);
        }
        for (auto currImp: checkVer(currGrid)) if (currImp.first == 1) total += currImp.second;
        for (auto currImp: checkHor(currGrid)) if (currImp.first == 1) total += currImp.second;
    }
    cout << total << endl;
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}