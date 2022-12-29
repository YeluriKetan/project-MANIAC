#include "bits/stdc++.h"

using namespace std;

typedef pair<int, int> ii;
typedef vector<pair<int, int>> VII;
typedef vector<int> VI;
typedef vector<vector<int>> VVI;

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

VII dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

int shortestPath(ii start, ii end, int r, int c, VVI &grid) {
    VVI visited(r, VI(c, 0));
    queue<ii> bfs;
    bfs.push(start);
    visited[start.first][start.second] = 1;
    int level = 0;
    while (!bfs.empty()) {
        for (int i = bfs.size(); i > 0; --i) {
            auto currSq = bfs.front(); bfs.pop();
            if (currSq == end) {
                return level;
            }
            for (auto currDir: dirs) {
                int newX = currSq.first + currDir.first, newY = currSq.second + currDir.second;
                if (newX < 0 || newX >= r || newY < 0 || newY >= c || visited[newX][newY]) {
                    continue;
                }
                if (grid[newX][newY] <= grid[currSq.first][currSq.second] + 1) {
                    visited[newX][newY] = 1;
                    bfs.push({newX, newY});
                }
            }
        }
        level++;
    }
    return INT_MAX;
}

void part1(Reader &reader) {
    VVI grid;
    string currLine;
    int r = 0, c;
    ii start, end;
    while (!reader.isEOF()) {
        VI currLineVec;
        currLine = reader.getLine();
        c = 0;
        for (auto &currSq: currLine) {
            switch (currSq) {
                case 'S':
                    start = {r, c};
                    currSq = 'a';
                    break;
                case 'E':
                    end = {r, c};
                    currSq = 'z';
                default:
                    break;
            }
            currLineVec.push_back(currSq - 'a');
            c++;
        }
        grid.push_back(currLineVec);
        r++;
    }
    cout << shortestPath(start, end, r, c, grid) << "\n";
}

void part2(Reader &reader) {
    VVI grid;
    string currLine;
    int r = 0, c;
    ii start, end;
    while (!reader.isEOF()) {
        VI currLineVec;
        currLine = reader.getLine();
        c = 0;
        for (auto &currSq: currLine) {
            switch (currSq) {
                case 'S':
                    start = {r, c};
                    currSq = 'a';
                    break;
                case 'E':
                    end = {r, c};
                    currSq = 'z';
                default:
                    break;
            }
            currLineVec.push_back(currSq - 'a');
            c++;
        }
        grid.push_back(currLineVec);
        r++;
    }
    int minEx = INT_MAX;
    for (int i = 0; i < r; ++i) {
        for (int j = 0; j < c; ++j) {
            if (grid[i][j] == 0) {
                minEx = min(shortestPath({i, j}, end, r, c, grid), minEx);
            }
        }
    }
    cout << minEx << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}