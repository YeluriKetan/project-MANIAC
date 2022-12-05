#include "bits/stdc++.h"

using namespace std;

typedef vector<bool> VB;

ifstream input;
string inputFileName = "../input.txt";

int getPriority(char c) {
    if (c <= 'Z') {
        return c - 'A' + 27;
    } else {
        return c - 'a' + 1;
    }
}

VB getHash(string s) {
    VB hash(53, false);
    for (auto curr: s) {
        hash[getPriority(curr)] = true;
    }
    return hash;
}

VB andHash(VB a, VB b) {
    assert(a.size() == b.size());
    VB hash(a.size());
    for (int i = 0; i < a.size(); ++i) {
        hash[i] = a[i] & b[i];
    }
    return hash;
}

void part1() {
    int total = 0;
    string line;
    while (getline(input, line)) {
        auto common = andHash(getHash(line.substr(0, line.size() / 2)),
                getHash(line.substr(line.size() / 2)));
        total += find(common.begin(), common.end(), true) - common.begin();
    }
    cout << total << "\n";
}

void part2() {
    int total = 0;
    string line;
    while (getline(input, line)) {
        VB common = getHash(line);
        getline(input, line);
        common = andHash(common, getHash(line));
        getline(input, line);
        common = andHash(common, getHash(line));
        total += find(common.begin(), common.end(), true) - common.begin();
    }
    cout << total << "\n";
}

int main() {
    input = ifstream(inputFileName);
    part1();
    input.close();

    input = ifstream(inputFileName);
    part2();
    input.close();
    return 0;
}