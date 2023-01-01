#include "bits/stdc++.h"

using namespace std;

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

int getInt(string &pack, int &point) {
    int val = 0;
    while (point < pack.size() && isdigit(pack[point])) {
        val *= 10;
        val += pack[point] - '0';
        point++;
    }
    return val;
}

int compareItem(VI &packA, int &pointA, VI &packB, int &pointB) {
    int itemA = packA[pointA], itemB = packB[pointB];
    if (itemA == itemB) {
        ++pointA; ++pointB;
        return compareItem(packA, pointA, packB, pointB);
    } else if (itemA == -2) {
        return -1;
    } else if (itemB == -2) {
        return 1;
    } else if (itemA == -1) {
        while (itemA == -1) {
            itemA = packA[++pointA];
        }
        int temp = itemA - itemB;
        return (temp >= 0) - (temp < 0);
    } else if (itemB == -1) {
        while (itemB == -1) {
            itemB = packB[++pointB];
        }
        int temp = itemA - itemB;
        return (temp > 0) - (temp <= 0);
    } else {
        int temp = itemA - itemB;
        return (temp > 0) - (temp < 0);
    }
}

bool cmp(VI &packA, VI &packB) {
    int pointA = 0, pointB = 0;
    return compareItem(packA, pointA, packB, pointB) < 1;
}

VI parsePacket(string packLine) {
    VI packVec;
    int point = 0, n = packLine.size();
    while (point < n) {
        switch (packLine[point]) {
            case '[':
                packVec.push_back(-1);
                break;
            case ']': {
                int temp = packVec.size();
                if (packVec[temp - 1] > -1 && packVec[temp - 2] == -1) {
                    temp = packVec.back(); packVec.pop_back();
                    packVec.pop_back();
                    packVec.push_back(temp);
                } else {
                    packVec.push_back(-2);
                }
            }
                break;
            case ',':
                break;
            default:
                packVec.push_back(getInt(packLine, point));
                point--;
                break;
        }
        point++;
    }
    return packVec;
}

void part1(Reader &reader) {
    int ind = 1, total = 0, pointA = 0, pointB = 0;
    VI packA, packB;
    while (!reader.isEOF()) {
        packA = parsePacket(reader.getLine());
        packB = parsePacket(reader.getLine());
        reader.getLine();
        pointA = pointB = 0;
        if (compareItem(packA, pointA, packB, pointB) != 1) {
            total += ind;
        }
        ind++;
    }
    cout << total << "\n";
}

void part2(Reader &reader) {
    int val = 1;
    VI packA, packB, two = {2}, six = {6};
    VVI packs;
    while (!reader.isEOF()) {
        packs.push_back(parsePacket(reader.getLine()));
        packs.push_back(parsePacket(reader.getLine()));
        reader.getLine();
    }
    packs.push_back(two);
    packs.push_back(six);
    sort(packs.begin(), packs.end(), cmp);
    for (int i = 0; i < packs.size(); ++i) {
        if (packs[i] == two || packs[i] == six) {
            val *= i + 1;
        }
    }
    cout << val << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}