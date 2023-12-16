#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef pair<int, int> ii;
typedef vector<ii> VII;
typedef vector<bool> VB;
typedef vector<VB> VVB;
typedef vector<char> VC;
typedef vector<VC> VVC;
typedef pair<ii, int> BEAM;
typedef vector<BEAM> VBEAM;

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

struct BeamHash {
    int operator()(const BEAM &beam) const {
        int hashVal = beam.first.first;
        hashVal <<= 8;
        hashVal |= beam.first.second;
        hashVal <<= 8;
        hashVal |= beam.second;
        return hashVal;
    }
};

VII directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

bool isValid(ii &pos, const ii &rc) { return 0 <= pos.first && pos.first < rc.first && 0 <= pos.second && pos.second < rc.second; }

ii nextInDir(ii pos, int dir) { return {pos.first + directions[dir].first, pos.second + directions[dir].second}; }

VBEAM propagate(BEAM beam, char c, const ii &rc) {
    VBEAM ans, filtered;
    auto [pos, dir] = beam;
    switch (c) {
        case '.': {
            ans.emplace_back(nextInDir(pos, dir), dir);
            break;
        }
        case '-': {
            if (dir % 2) {
                ans.emplace_back(nextInDir(pos, dir), dir);
            } else {
                for (int d = 1; d < 4; d += 2) ans.emplace_back(nextInDir(pos, d), d);
            }
            break;
        }
        case '|': {
            if (dir % 2) {
                for (int d = 0; d < 4; d += 2) ans.emplace_back(nextInDir(pos, d), d);
            } else {
                ans.emplace_back(nextInDir(pos, dir), dir);
            }
            break;
        }
        case '/': {
            int newDir = (dir % 2) ? dir - 1 : dir + 1;
            ans.emplace_back(nextInDir(pos, newDir), newDir);
            break;
        }
        case '\\': {
            int newDir = (dir % 2) ? (dir + 1) % 4 : (dir + 3) % 4;
            ans.emplace_back(nextInDir(pos, newDir), newDir);
            break;
        }
        default:
            break;
    }
    copy_if(ans.begin(), ans.end(), back_inserter(filtered), [rc](BEAM ele) { return isValid(ele.first, rc); });
    return filtered;
}

int energised(BEAM start, VVC &grid, const ii &rc) {
    VVB isEnergised(rc.first, VB(rc.second, false));
    queue<BEAM> q;
    unordered_set<BEAM, BeamHash> seen;
    q.push(start);
    seen.insert(q.front());
    while (!q.empty()) {
        for (int i = q.size(); i > 0; --i) {
            auto curr = q.front(); q.pop();
            isEnergised[curr.first.first][curr.first.second] = true;
            for (auto next: propagate(curr, grid[curr.first.first][curr.first.second], rc)) {
                if (seen.find(next) == seen.end()) {
                    q.push(next);
                    seen.insert(next);
                }
            }
        }
    }
    int count = 0;
    for (auto &row: isEnergised) for (auto tile: row) if (tile) count++;
    return count;
}

void part1(Reader &reader) {
    VVC grid;
    while (!reader.isEOF()) {
        VC row;
        for (auto currChar: reader.getLine()) row.push_back(currChar);
        grid.push_back(row);
    }
    const ii rc = {grid.size(), grid[0].size()};
    cout << energised({{0, 0}, 1}, grid, rc) << endl;
}

void part2(Reader &reader) {
    VVC grid;
    while (!reader.isEOF()) {
        VC row;
        for (auto currChar: reader.getLine()) row.push_back(currChar);
        grid.push_back(row);
    }
    const ii rc = {grid.size(), grid[0].size()};
    int maxEnergised = 0;
    for (int j = 0; j < rc.second; ++j) maxEnergised = max(maxEnergised, energised({{0, j}, 2}, grid, rc));
    for (int i = 0; i < rc.first; ++i) maxEnergised = max(maxEnergised, energised({{i, rc.second - 1}, 3}, grid, rc));
    for (int j = 0; j < rc.second; ++j) maxEnergised = max(maxEnergised, energised({{rc.first - 1, j}, 0}, grid, rc));
    for (int i = 0; i < rc.first; ++i) maxEnergised = max(maxEnergised, energised({{i, 0}, 1}, grid, rc));
    cout << maxEnergised << endl;
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}