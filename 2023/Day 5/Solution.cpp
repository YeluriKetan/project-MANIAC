#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef long long ll;
typedef vector<ll> VLL;
typedef pair<ll, ll> RANGE;
typedef vector<RANGE> VR;
typedef map<ll, VLL, greater<>> REV_ORDER_MAP;

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

bool cmp(RANGE &a, RANGE &b) {
    if (a.first == b.first) {
        return a.second < b.second;
    } else {
        return a.first < b.first;
    }
}

void fillWithIdentity(REV_ORDER_MAP &mapping) {
    VR available;
    for (auto [start, rangeInfo]: mapping) {
        available.emplace_back(rangeInfo[0], rangeInfo[1]);
    }
    sort(available.begin(), available.end(), cmp);
    ll prevEnd = -1;
    for (auto currRange: available) {
        if (prevEnd + 1 < currRange.first) {
            mapping[prevEnd + 1] = {prevEnd + 1, currRange.first - 1, 0};
        }
        prevEnd = currRange.second;
    }
    mapping[prevEnd + 1] = {prevEnd + 1, LONG_LONG_MAX, 0};
}

ll findMapped(ll source, REV_ORDER_MAP &map) {
    auto it = map.lower_bound(source);
    if (it == map.end()) {
        cerr << "Mapping not found" << endl;
        return 0;
    }
    return source + it->second[2];
}

void part1(Reader &reader) {
    VLL seeds;
    reader.getToken<string>();
    while (!reader.isNewLine()) {
        seeds.push_back(reader.getToken<ll>());
    }
    vector<REV_ORDER_MAP> mappings(7, REV_ORDER_MAP());
    for (int i = 0; i < 7; ++i) {
        reader.getLine(); reader.getLine();
        while (!reader.isNewLine() && !reader.isEOF()) {
            ll a = reader.getToken<ll>(), b = reader.getToken<ll>(), c = reader.getToken<ll>();
            mappings[i][b] = {b, b + c - 1, a - b};
        }
        fillWithIdentity(mappings[i]);
    }
    ll minLoc = LLONG_MAX;
    for (auto currSeed: seeds) {
        ll dest = currSeed;
        for (auto mapping: mappings) dest = findMapped(dest, mapping);
        minLoc = min(minLoc,dest);
    }
    cout << minLoc << "\n";
}

VR sortAndMerge(VR &ranges) {
    VR result;
    sort(ranges.begin(), ranges.end(), cmp);
    for (auto &currRange: ranges) {
        if (result.empty() || result.rbegin()->second < currRange.first) {
            result.push_back(currRange);
        } else {
            auto lastR = result.rbegin();
            lastR->second = max(lastR->second, currRange.second);
        }
    }
    return result;
}

void splitRanges(RANGE rangeToSplit, REV_ORDER_MAP &mapping, VR &newRanges) {
    auto it = mapping.lower_bound(rangeToSplit.first);
    if (it == mapping.end()) {
        cerr << "missing mapping\n";
        return;
    }
    VLL currRange = it->second;
    newRanges.emplace_back(rangeToSplit.first + currRange[2],
                           min(rangeToSplit.second, currRange[1]) + currRange[2]);
    if (rangeToSplit.second > currRange[1]) {
        splitRanges({currRange[1] + 1, rangeToSplit.second}, mapping, newRanges);
    }
}

void part2(Reader &reader) {
    VR ranges;
    reader.getToken<string>();
    while (!reader.isNewLine()) {
        ll start = reader.getToken<ll>();
        ranges.push_back({start, start + reader.getToken<ll>() - 1});
    }
    vector<REV_ORDER_MAP> mappings(7, REV_ORDER_MAP());
    for (int i = 0; i < 7; ++i) {
        reader.getLine(); reader.getLine();
        while (!reader.isNewLine() && !reader.isEOF()) {
            ll a = reader.getToken<ll>(), b = reader.getToken<ll>(), c = reader.getToken<ll>();
            mappings[i][b] = {b, b + c - 1, a - b};
        }
        fillWithIdentity(mappings[i]);
    }
    for (auto mapping: mappings) {
        ranges = sortAndMerge(ranges);
        VR newRanges;
        for (auto currRange: ranges) splitRanges(currRange, mapping, newRanges);
        ranges = newRanges;
    }
    ranges = sortAndMerge(ranges);
    cout << ranges[0].first << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}
