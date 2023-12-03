#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef long long ll;
typedef pair<int, int> ii;
typedef vector<int> VI;
typedef vector<string> VS;
typedef vector<ll> VLL;
typedef pair<char, ii> symbol;

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

struct Hash {
    int operator()(const symbol &s) const {
        int hashVal = s.first;
        hashVal <<= 8;
        hashVal |= s.second.first;
        hashVal <<= 8;
        hashVal |= s.second.second;
        return hashVal;
    }
};

int parseInt(int x, int y, int r, int c, VS &grid, unordered_map<symbol, VI, Hash> &symbolMap) {
    int end = y, num = 0;
    for (int i = y; i < c; ++i) {
        if (isdigit(grid[x][i])) {
            num = (num * 10) + grid[x][i] - '0';
            grid[x][i] = '.';
            end = i;
        } else {
            break;
        }
    }
    bool valid = false;
    for (int i = max(0, x - 1); i < min(r, x + 2); ++i) {
        for (int j = max(0, y - 1); j < min(c, end + 2); ++j) {
            if (!isdigit(grid[i][j]) && grid[i][j] != '.') {
                auto it = symbolMap.insert({symbol(grid[i][j], ii(i, j)), VI()});
                it.first->second.push_back(num);
                valid = true;
            }
        }
    }
    return valid ? num : 0;
}

void part1(Reader &reader) {
    VS grid;
    while (!reader.isEOF()) {
        grid.push_back(reader.getLine());
    }
    int ans = 0;
    int r = grid.size(), c = grid[0].size();
    unordered_map<symbol, VI, Hash> symbolMap;
    for (int i = 0; i < r; ++i) {
        for (int j = 0; j < c; ++j) {
            if (isdigit(grid[i][j])) ans += parseInt(i, j, r, c, grid, symbolMap);
        }
    }
    cout << ans << "\n";
}

void part2(Reader &reader) {
    VS grid;
    while (!reader.isEOF()) {
        grid.push_back(reader.getLine());
    }
    int ans = 0;
    int r = grid.size(), c = grid[0].size();
    unordered_map<symbol, VI, Hash> symbolMap;
    for (int i = 0; i < r; ++i) {
        for (int j = 0; j < c; ++j) {
            if (isdigit(grid[i][j])) parseInt(i, j, r, c, grid, symbolMap);
        }
    }
    for (auto curr: symbolMap) {
        if (curr.first.first == '*' && curr.second.size() == 2) {
            ans += curr.second[0] * curr.second[1];
        }
    }
    cout << ans << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}