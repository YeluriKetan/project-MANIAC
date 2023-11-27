#include "bits/stdc++.h"

using namespace std;

typedef long long ll;
typedef pair<int, int> ii;
typedef vector<int> VI;
typedef vector<ii> VII;
typedef vector<bool> VB;
typedef vector<ll> VLL;

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

class Vent {
public:
    int width;
    VII currRock;
    vector<VB> ventMap;
    int maxHeight;

    Vent(int w) {
        width = w;
        ventMap = vector<VB>();
        ventMap.reserve(100000);
        ventMap.emplace_back(width, true);
        currRock = VII();
        maxHeight = 0;
    }

    void createRock(int type, int leftOffset) {
        currRock.clear();
        switch (type) {
            case 0: {
                for (int i = 0; i < 4; ++i) currRock.push_back({maxHeight + 4, leftOffset + i});
                break;
            }
            case 1: {
                currRock.push_back({maxHeight + 6, leftOffset + 1});
                for (int i = 0; i < 3; ++i) currRock.push_back({maxHeight + 5, leftOffset + i});
                currRock.push_back({maxHeight + 4, leftOffset + 1});
                break;
            }
            case 2: {
                currRock.push_back({maxHeight + 6, leftOffset + 2});
                currRock.push_back({maxHeight + 5, leftOffset + 2});
                for (int i = 0; i < 3; ++i) currRock.push_back({maxHeight + 4, leftOffset + i});
                break;
            }
            case 3: {
                for (int i = 0; i < 4; ++i) currRock.push_back({maxHeight + 4 + i, leftOffset});
                break;
            }
            case 4: {
                for (int i = 0; i < 2; ++i)
                    for (int j = 0; j < 2; ++j)
                        currRock.push_back({maxHeight + 4 + i, leftOffset + j});
                break;
            }
            default:
                throw invalid_argument("unknown rock type");
        }
        while (ventMap.size() - 1 < maxHeight + 7) ventMap.emplace_back(width, false);
    }

    bool move(ii direction) {
        for (auto curr: currRock) {
            auto newX = curr.first + direction.first;
            auto newY = curr.second + direction.second;
            if (newX < 0) return false;
            if (newY < 0 || newY >= width) return false;
            if (ventMap[newX][newY]) return false;
        }
        for (auto &curr: currRock) {
            curr.first += direction.first;
            curr.second += direction.second;
        }
        return true;
    }

    void updateMaxHeight() {
        for (int i = maxHeight + 1; i < ventMap.size(); ++i) {
            for (int j = 0; j < width; ++j) {
                if (ventMap[i][j]) {
                    maxHeight = i;
                }
            }
        }
    }

    void freezeRock() {
        for (auto curr: currRock) ventMap[curr.first][curr.second] = true;
        updateMaxHeight();
    }
};

void part1(Reader &reader) {
    string wind = reader.getLine();
    VII directions = {{0, -1}, {0, 1}, {-1, 0}};
    int windN = wind.size(), windInd = 0;
    Vent vent(7);
    int prevHeight = 0;
    for (int i = 0; i < 2022; ++i) {
        vent.createRock(i % 5, 2);
        bool moved = true, isWind = true;
        while (moved) {
            if (isWind) {
                vent.move(directions[wind[windInd] == '<' ? 0 : 1]);
                windInd = (windInd + 1) % windN;
            } else {
                moved = vent.move(directions[2]);
            }
            isWind = !isWind;
        }
        vent.freezeRock();
    }
    cout << vent.maxHeight << "\n";
}

bool test(VII &heightInOrder, int currInd, int prevInd) {
    int len = currInd - prevInd;
    if (prevInd - len + 1 < 0) {
        return false;
    } else {
        for (int i = 0; i < len; ++i) {
            if (heightInOrder[currInd - i].first != heightInOrder[prevInd - i].first) {
                return false;
            }
        }
    }
    return true;
}

void part2(Reader &reader) {
    string wind = reader.getLine();
    VII directions = {{0, -1}, {0, 1}, {-1, 0}};
    int windN = wind.size(), windInd = 0;
    Vent vent(7);
    int prevHeight = 0;
    VII heightInOrder;
    unordered_map<int, int> seenIndices;
    for (int i = 0; i < 1e8; ++i) {
        if (i % 5 == 0) {
            if (seenIndices.find(windInd) == seenIndices.end()) {
                seenIndices[windInd] = heightInOrder.size();
                heightInOrder.emplace_back(windInd, vent.maxHeight);
            } else {
                heightInOrder.emplace_back(windInd, vent.maxHeight);
                if (test(heightInOrder, heightInOrder.size() - 1, seenIndices[windInd])) {
                    break;
                } else {
                    seenIndices[windInd] = heightInOrder.size() - 1;
                }
            }
        }
        vent.createRock(i % 5, 2);
        bool moved = true, isWind = true;
        while (moved) {
            if (isWind) {
                vent.move(directions[wind[windInd] == '<' ? 0 : 1]);
                windInd = (windInd + 1) % windN;
            } else {
                moved = vent.move(directions[2]);
            }
            isWind = !isWind;
        }
        vent.freezeRock();
    }
    ll lastNonRepeatInd = seenIndices[windInd] - 1;
    ll repeatingHeight = vent.maxHeight - heightInOrder[seenIndices[windInd]].second;
    ll repeatingEvery = heightInOrder.size() - seenIndices[windInd] - 1;
    ll it = 1e12 - (lastNonRepeatInd * 5);
    ll total = heightInOrder[seenIndices[windInd] - 1].second + (it / (repeatingEvery * 5)) * repeatingHeight;
    int further = (it % (repeatingEvery * 5)) / 5;
    total += heightInOrder[seenIndices[windInd] + further - 1].second - heightInOrder[seenIndices[windInd] - 1].second;
    cout << total << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}
