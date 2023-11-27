#include "bits/stdc++.h"

using namespace std;

typedef long long ll;
typedef pair<int, int> ii;
typedef tuple<int, int, int> iii;
typedef vector<int> VI;
typedef vector<ll> VLL;
typedef vector<bool> VB;
typedef vector<VB> VVB;
typedef vector<VVB> VVVB;

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

struct Hasher {
    int operator()(const iii &k) const {
        int hashVal = get<0>(k);
        hashVal <<= 6;
        hashVal |= get<1>(k);
        hashVal <<= 6;
        hashVal |= get<2>(k);
        return hashVal;
    }
};

VI parseCube(const string& s) {
    int one, two;
    one = s.find(',', 0);
    two = s.find(',', one + 1);
    return {stoi(s.substr(0, one)),
            stoi(s.substr(one + 1, two - one - 1)),
            stoi(s.substr(two + 1, s.size() - two - 1))};
}

void part1(Reader &reader) {
    int w = 25, off = 2;
    // true represents cube
    VVVB grid(w, VVB(w, VB(w, false)));
    while (!reader.isEOF()) {
        auto cube = parseCube(reader.getLine());
        grid[cube[0] + off][cube[1] + off][cube[2] + off] = true;
    }
    vector<VI> directions = {{-1, 0, 0}, {1, 0, 0}, {0, -1, 0}, {0, 1, 0}, {0, 0, -1}, {0, 0, 1}};
    int cubeSurfaces = 0;
    for (int x = 0; x < w; ++x) {
        for (int y = 0; y < w; ++y) {
            for (int z = 0; z < w; ++z) {
                if (!grid[x][y][z]) continue;
                cubeSurfaces += 6;
                for (auto currDir: directions) {
                    int X = x + currDir[0];
                    int Y = y + currDir[1];
                    int Z = z + currDir[2];
                    if (grid[X][Y][Z]) cubeSurfaces--;
                }
            }
        }
    }
    cout << cubeSurfaces << "\n";
}

void dfs(int x, int y, int z, int n, vector<VI> &directions, VVVB &grid) {
    if (x < 0 || x >= n || y < 0 || y >= n || z < 0 || z >= n || grid[x][y][z]) {
        return;
    }
    grid[x][y][z] = true;
    for (auto currDir: directions) {
        int X = x + currDir[0];
        int Y = y + currDir[1];
        int Z = z + currDir[2];
        dfs(X, Y, Z, n, directions, grid);
    }
}

void part2(Reader &reader) {
    int w = 25, off = 2;
    // true represents cube
    VVVB grid(w, VVB(w, VB(w, false)));
    while (!reader.isEOF()) {
        auto cube = parseCube(reader.getLine());
        grid[cube[0] + off][cube[1] + off][cube[2] + off] = true;
    }
    vector<VI> directions = {{-1, 0, 0}, {1, 0, 0}, {0, -1, 0}, {0, 1, 0}, {0, 0, -1}, {0, 0, 1}};
    int cubeSurfaces = 0;
    for (int x = 0; x < w; ++x) {
        for (int y = 0; y < w; ++y) {
            for (int z = 0; z < w; ++z) {
                if (!grid[x][y][z]) continue;
                cubeSurfaces += 6;
                for (auto currDir: directions) {
                    int X = x + currDir[0];
                    int Y = y + currDir[1];
                    int Z = z + currDir[2];
                    if (grid[X][Y][Z]) cubeSurfaces--;
                }
            }
        }
    }
    dfs(0, 0, 0, w, directions, grid);
    int airSurfaces = 0;
    for (int x = 0; x < w; ++x) {
        for (int y = 0; y < w; ++y) {
            for (int z = 0; z < w; ++z) {
                if (grid[x][y][z]) continue;
                airSurfaces += 6;
                for (auto currDir: directions) {
                    int X = x + currDir[0];
                    int Y = y + currDir[1];
                    int Z = z + currDir[2];
                    if (!grid[X][Y][Z]) airSurfaces--;
                }
            }
        }
    }
    cout << cubeSurfaces - airSurfaces << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}