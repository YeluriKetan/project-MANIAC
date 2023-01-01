#include "bits/stdc++.h"

using namespace std;

typedef pair<int, int> ii;
typedef vector<pair<int, int>> VII;
typedef vector<bool> VB;
typedef vector<vector<bool>> VVB;

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

VII dirs = {{1, 0}, {1, -1}, {1, 1}};

ii strToPair(string str, int offset) {
    auto delim = str.find(',');
    return {stoi(str.substr(delim + 1)), stoi(str.substr(0, delim)) - offset};
}

bool cmp(ii &a, ii &b) {
    if (a.first == b.first) {
        return a.second <= b.second;
    } else {
        return a.first <= b.first;
    }
}

void fillRock(ii prevPoint, ii currPoint, VVB &grid) {
    if (!cmp(prevPoint, currPoint)) {
        fillRock(currPoint, prevPoint, grid);
        return;
    }
    if (prevPoint.first == currPoint.first) {
        fill(grid[prevPoint.first].begin() + prevPoint.second, grid[prevPoint.first].begin() + currPoint.second + 1, true);
    } else {
        for (int i = prevPoint.first; i <= currPoint.first; ++i) {
            grid[i][prevPoint.second] = true;
        }
    }
}

int dropSand(int x, int y, int r, int c, VVB &grid) {
    if (x >= r || y < 0 || y >= c) {
        return -1;
    }
    if (grid[x][y]) {
        return 0;
    }
    int newX, newY, temp;
    for (auto [dX, dY]: dirs) {
        newX = x + dX, newY = y + dY;
        temp = dropSand(newX, newY, r, c, grid);
        if (temp != 0) {
            return temp;
        }
    }
    grid[x][y] = true;
    return 1;
}

void part1(Reader &reader) {
    VVB grid(200, VB(200, false));
    string currStr;
    ii prevPoint, currPoint;
    while (!reader.isEOF()) {
        currStr = reader.getToken<string>();
        if (currStr[0] == '-') {
            prevPoint = currPoint;
            currPoint = strToPair(reader.getToken<string>(), 400);
            fillRock(prevPoint, currPoint, grid);
        } else {
            currPoint = strToPair(currStr, 400);
        }
    }
    int count = 0, r = 200, c = 200, sX = 0, sY = 100;
    while (dropSand(sX, sY, r, c, grid) > 0) {
        count++;
    }
    cout << count << "\n";
}

void part2(Reader &reader) {
    VVB grid(200, VB(400, false));
    string currStr;
    ii prevPoint, currPoint;
    int count = 0, r = 200, c = 400, sX = 0, sY = 200, maxX = 0, t;
    while (!reader.isEOF()) {
        currStr = reader.getToken<string>();
        if (currStr[0] == '-') {
            prevPoint = currPoint;
            currPoint = strToPair(reader.getToken<string>(), 300);
            fillRock(prevPoint, currPoint, grid);
        } else {
            currPoint = strToPair(currStr, 300);
        }
        maxX = max(maxX, currPoint.first);
    }
    fillRock({maxX + 2, 0}, {maxX + 2, c - 1}, grid);
    while (dropSand(sX, sY, r, c, grid) > 0) {
        count++;
    }
    cout << count << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}