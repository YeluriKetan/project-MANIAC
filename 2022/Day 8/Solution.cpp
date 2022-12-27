#include "bits/stdc++.h"

using namespace std;

typedef vector<int> VI;
typedef vector<VI> VVI;

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

void part1(Reader &reader) {
    string currlineStr;
    VVI grid;
    while (!reader.isEOF()) {
        currlineStr = reader.getLine();
        VI currLine;
        for (auto currTree: currlineStr) {
            currLine.push_back(currTree - '0');
        }
        grid.push_back(currLine);
    }
    int r = grid.size(), c = grid[0].size(), prev;
    VVI isVisible(r, VI(c, 0));
    for (int i = 0; i < r; ++i) {
        prev = -1;
        for (int j = 0; j < c; ++j) {
            if (grid[i][j] > prev) {
                isVisible[i][j] += 1;
                prev = grid[i][j];
            }
        }
        prev = -1;
        for (int j = c - 1; j > -1; --j) {
            if (grid[i][j] > prev) {
                isVisible[i][j] += 1;
                prev = grid[i][j];
            }
        }
    }
    for (int j = 0; j < c; ++j) {
        prev = -1;
        for (int i = 0; i < r; ++i) {
            if (grid[i][j] > prev) {
                isVisible[i][j] += 1;
                prev = grid[i][j];
            }
        }
        prev = -1;
        for (int i = r - 1; i > -1; --i) {
            if (grid[i][j] > prev) {
                isVisible[i][j] += 1;
                prev = grid[i][j];
            }
        }
    }
    int count = 0;
    for (auto currTreeLine: isVisible) {
        for (auto currTreeVis: currTreeLine) {
            if (currTreeVis > 0) count++;
        }
    }
    cout << count << "\n";
}

void part2(Reader &reader) {
    string currlineStr;
    VVI grid;
    while (!reader.isEOF()) {
        currlineStr = reader.getLine();
        VI currLine;
        for (auto currTree: currlineStr) {
            currLine.push_back(currTree - '0');
        }
        grid.push_back(currLine);
    }
    int r = grid.size(), c = grid[0].size(), maxScore = 0, currScore, count, currHeight;
    for (int i = 0; i < r; ++i) {
        for (int j = 0; j < c; ++j) {
            currScore = 1, count = 0, currHeight = grid[i][j];
            for (int x = i - 1; x > -1; --x) {
                count++;
                if (grid[x][j] >= currHeight) break;
            }
            currScore *= count, count = 0;
            for (int x = i + 1; x < r; ++x) {
                count++;
                if (grid[x][j] >= currHeight) break;
            }
            currScore *= count, count = 0;
            for (int y = j - 1; y > -1; --y) {
                count++;
                if (grid[i][y] >= currHeight) break;
            }
            currScore *= count, count = 0;
            for (int y = j + 1; y < c; ++y) {
                count++;
                if (grid[i][y] >= currHeight) break;
            }
            currScore *= count, count = 0;
            maxScore = max(maxScore, currScore);
        }
    }
    cout << maxScore << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}