#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef pair<int, int> ii;
typedef vector<int> VI;
typedef vector<ii> VII;
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

class Block {
public:
    int heat;
    int straight;
    int x;
    int y;
    int dir;

    Block(int h, int s, int _x, int _y, int d): heat(h), straight(s), x(_x), y(_y), dir(d) {}

    bool operator==(const Block &b) const {
        return this->x == b.x && this->y == b.y && this->dir == b.dir && this->straight == b.straight;
    }
};

struct BLOCK_CMP {
    bool operator()(Block &a, Block &b) {
        if (a.heat != b.heat) return a.heat > b.heat;
        else if (a.x != b.x) return a.x > b.x;
        else if (a.y != b.y) return a.y > b.y;
        else if (a.straight != b.straight) return a.straight > b.straight;
        else return a.dir > b.dir;
    }
};

struct BLOCK_HASH {
    int operator()(const Block &a) const {
        return (((((a.x << 12) | a.y) << 2) | a.dir) << 4) | a.straight;
    }
};

VII directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

bool canMove(Block &b, int dir, ii &rc, int m, int M) {
    int newX = b.x + directions[dir].first;
    if (0 > newX || newX >= rc.first) return false;
    int newY = b.y + directions[dir].second;
    if (0 > newY || newY >= rc.second) return false;
    if (b.straight < m) return dir == b.dir;
    return b.dir != dir || b.straight < M;
}

Block newBlock(Block &b, int dir, VVI &grid) {
    Block res(b.heat, b.straight, b.x + directions[dir].first, b.y + directions[dir].second, dir);
    if (b.dir == dir) res.straight++;
    else res.straight = 1;
    res.heat += grid[res.x][res.y];
    return res;
}

void part1(Reader &reader) {
    VVI grid;
    while (!reader.isEOF()) {
        VI row;
        for (auto currChar: reader.getLine()) row.push_back(currChar - '0');
        grid.push_back(row);
    }
    ii rc = {grid.size(), grid[0].size()};
    priority_queue<Block, vector<Block>, BLOCK_CMP> pq;
    Block start(0, 0, 0, 0, 1);
    pq.push(start);
    Block start2(0, 0, 0, 0, 2);
    pq.push(start2);
    unordered_map<Block, int, BLOCK_HASH> m;
    for (int i = 0; i < 4; ++i) {
        for (int j = 1; j < 4; ++j) {
            start.dir = i;
            start.straight = j;
            m[start] = 0;
        }
    }
    while (!pq.empty()) {
        auto curr = pq.top(); pq.pop();
        if (curr.x == rc.first - 1 && curr.y == rc.second - 1) {
            cout << curr.heat << endl;
            return;
        }
        for (int d = 0; d < 4; ++d) {
            if ((d + 2) % 4 == curr.dir) continue;
            if (!canMove(curr, d, rc, 1, 3)) continue;
            auto next = newBlock(curr, d, grid);
            auto it = m.find(next);
            if (it == m.end() || next.heat < it->second) {
                m[next] = next.heat;
                pq.push(next);
            }
        }
    }
}

void part2(Reader &reader) {
    VVI grid;
    while (!reader.isEOF()) {
        VI row;
        for (auto currChar: reader.getLine()) row.push_back(currChar - '0');
        grid.push_back(row);
    }
    ii rc = {grid.size(), grid[0].size()};
    priority_queue<Block, vector<Block>, BLOCK_CMP> pq;
    Block start(0, 0, 0, 0, 1);
    pq.push(start);
    Block start2(0, 0, 0, 0, 2);
    pq.push(start2);
    unordered_map<Block, int, BLOCK_HASH> m;
    for (int i = 0; i < 4; ++i) {
        for (int j = 1; j < 10; ++j) {
            start.dir = i;
            start.straight = j;
            m[start] = 0;
        }
    }
    while (!pq.empty()) {
        auto curr = pq.top(); pq.pop();
        if (curr.x == rc.first - 1 && curr.y == rc.second - 1) {
            cout << curr.heat << endl;
            return;
        }
        for (int d = 0; d < 4; ++d) {
            if ((d + 2) % 4 == curr.dir) continue;
            if (!canMove(curr, d, rc, 4, 10)) continue;
            auto next = newBlock(curr, d, grid);
            auto it = m.find(next);
            if (it == m.end() || next.heat < it->second) {
                m[next] = next.heat;
                pq.push(next);
            }
        }
    }
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}