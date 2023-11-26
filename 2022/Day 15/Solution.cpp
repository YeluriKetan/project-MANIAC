#include "bits/stdc++.h"

using namespace std;

typedef vector<int> VI;

typedef pair<int, int> ii;
typedef vector<ii> VII;

typedef long long ll;
typedef vector<ll> Vll;

typedef pair<ll, ll> LL;
typedef vector<LL> VLL;

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

regex format(R"(Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+))");
smatch match;

ll manDist(LL a, LL b) {
    return abs(a.first - b.first) + abs(a.second - b.second);
}

void part1(Reader &reader) {
    VLL sensors, beacons, intervals;
    while (!reader.isEOF()) {
        auto currLine = reader.getLine();
        regex_search(currLine, match, format);
        sensors.push_back({stoi(match.str(1)), stoi(match.str(2))});
        beacons.push_back({stoi(match.str(3)),stoi(match.str(4))});
    }
    int n = sensors.size();
    ll currManDist, temp, y = 2000000, minX = LLONG_MAX, maxX = LLONG_MIN, count = 0;
    for (int i = 0; i < n; ++i) {
        currManDist = manDist(sensors[i], beacons[i]);
        temp = currManDist - abs(sensors[i].second - y);
        if (temp < 0) continue;
        LL currInterval = {sensors[i].first - temp, sensors[i].first + temp};
        minX = min(minX, currInterval.first);
        maxX = max(maxX, currInterval.second);
        intervals.push_back(currInterval);
    }
    for (int i = minX; i <= maxX; ++i) {
        for (auto currInterval: intervals) {
            if (currInterval.first <= i && i <= currInterval.second) {
                count++;
                break;
            }
        }
    }
    unordered_set<ll> seenBeacons;
    for (auto [bX, bY]: beacons) {
        if (bY == y && minX <= bX && bX <= maxX
                && seenBeacons.find(bX) == seenBeacons.end()) {
            count--;
            seenBeacons.insert(bX);
        }
    }
    cout << count << "\n";
}

bool testUncovered(LL location, VLL &sensors, Vll &manDistVec) {
    if (0 > location.first || location.first > 4000000
        || 0 > location.second || location.second > 4000000) return false;
    for (int i = 0; i < sensors.size(); ++i) {
        if (manDist(sensors[i], location) <= manDistVec[i]) {
            return false;
        }
    }
    return true;
}

void part2(Reader &reader) {
    VLL sensors, beacons;
    Vll manDistVec;
    while (!reader.isEOF()) {
        auto currLine = reader.getLine();
        regex_search(currLine, match, format);
        sensors.push_back({stoi(match.str(1)), stoi(match.str(2))});
        beacons.push_back({stoi(match.str(3)),stoi(match.str(4))});
        manDistVec.push_back(manDist(sensors.back(), beacons.back()));
    }
    int n = sensors.size();
    VLL move({{1, -1}, {1, 1}, {-1, 1}, {-1, -1}});
    for (int i = 0; i < n; ++i) {
        VLL pointers;
        auto currManDist = manDistVec[i];
        auto currSensor = sensors[i];
        pointers.emplace_back(currSensor.first - currManDist - 1, currSensor.second);
        pointers.emplace_back(currSensor.first, currSensor.second - currManDist - 1);
        pointers.emplace_back(currSensor.first + currManDist + 1, currSensor.second);
        pointers.emplace_back(currSensor.first, currSensor.second + currManDist + 1);
        for (int j = 0; j <= currManDist; ++j) {
            for (int k = 0; k < 4; ++k) {
                if (testUncovered(pointers[k], sensors, manDistVec)) {
                    cout << pointers[k].first * 4000000 + pointers[k].second << "\n";
                    return;
                }
                pointers[k].first += move[k].first;
                pointers[k].second += move[k].second;
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