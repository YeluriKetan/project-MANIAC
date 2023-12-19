#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef long long ll;
typedef pair<int, int> ii;
typedef vector<ii> VII;
typedef vector<VII> VVII;

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
    Reader(string filename) { input.open(filename); }
    ~Reader() { input.close(); }
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
    bool isNewLine() { return (line.peek() == -1) && input.peek() == '\n'; }
    bool isEOF() { return (line.peek() == -1) ? input.peek() == EOF : line.peek() == EOF; }
    Reader& operator=(Reader reader) {
        this->input = std::move(reader.input);
        this->line = std::move(reader.line);
        return *this;
    }
};

VVII directions = {{{-1, 0}, {-1, -1}, {-1, 1}},
                   {{1, 0}, {1, -1}, {1, 1}},
                   {{0, -1}, {-1, -1}, {1, -1}},
                   {{0, 1}, {-1, 1}, {1, 1}}};

struct ElfHash {
    int operator()(const ii &elf) const {
        return (elf.first << 16) | elf.second;
    }
};

ii add(ii &a, ii &b) {
    return {a.first + b.first, a.second + b.second};
}

typedef unordered_set<ii, ElfHash> ELVES_SET;

bool processRound(ELVES_SET &elves, int dirInd) {
    unordered_map<ii, VII, ElfHash> proposals;
    for (auto elf: elves) {
        int neighbouringElves = 0;
        for (int i = elf.first - 1; i < elf.first + 2; ++i) {
            for (int j = elf.second - 1; j < elf.second + 2; ++j) {
                if (elves.find({i, j}) != elves.end()) neighbouringElves++;
            }
        }
        if (neighbouringElves == 1) continue;
        for (int i = 0; i < 4; ++i) {
            bool elfInDirection = false;
            for (auto &currSubDir: directions[(dirInd + i) % 4]) {
                if (elves.find(add(elf, currSubDir)) != elves.end()) {
                    elfInDirection = true;
                    break;
                }
            }
            if (elfInDirection) continue;
            auto it = proposals.emplace(add(elf, directions[(dirInd + i) % 4][0]), VII());
            it.first->second.push_back(elf);
            break;
        }
    }
    bool moved = false;
    for (const auto &proposedMove: proposals) {
        if (proposedMove.second.size() == 1) {
            elves.erase(proposedMove.second[0]);
            elves.insert(proposedMove.first);
            moved = true;
        }
    }
    return moved;
}

void part1(Reader &reader) {
    unordered_set<ii, ElfHash> elves;
    int r = 0, c = 0;
    while (!reader.isEOF()) {
        c = 0;
        for (auto currChar: reader.getLine()) {
            if (currChar == '#') elves.insert({r, c});
            c++;
        }
        r++;
    }
    for (int i = 0; i < 10; ++i) processRound(elves, i);
    int minX = INT_MAX, maxX = INT_MIN;
    int minY = INT_MAX, maxY = INT_MIN;
    for (auto currElf: elves) {
        minX = min(minX, currElf.first);
        maxX = max(maxX, currElf.first);
        minY = min(minY, currElf.second);
        maxY = max(maxY, currElf.second);
    }
    cout << (maxX - minX + 1) * (maxY - minY + 1) - elves.size() << endl;
}

void part2(Reader &reader) {
    unordered_set<ii, ElfHash> elves;
    int r = 0, c = 0;
    while (!reader.isEOF()) {
        c = 0;
        for (auto currChar: reader.getLine()) {
            if (currChar == '#') elves.insert({r, c});
            c++;
        }
        r++;
    }
    int dirInd = 0;
    while (processRound(elves, dirInd)) dirInd++;
    cout << dirInd + 1 << endl;
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}