#include "bits/stdc++.h"

using namespace std;

typedef pair<int, int> ii;
typedef long long ll;
typedef vector<int> VI;
typedef vector<ll> VLL;


ifstream input;
string inputFileName = "../input.txt";

void part1() {
    ll maxTotal = 0, currTotal = 0;
    VLL totals;
    string line;
    while (getline(input, line)) {
        if (line.empty()) {
            maxTotal = max(maxTotal, currTotal);
            currTotal = 0;
        } else {
            currTotal += stoi(line);
        }
    }
    cout << maxTotal << "\n";
}

void part2() {
    ll currTotal = 0;
    VLL totals;
    string line;
    while (getline(input, line)) {
        if (line.empty()) {
            totals.push_back(currTotal);
            currTotal = 0;
        } else {
            currTotal += stoi(line);
        }
    }
    sort(totals.begin(), totals.end());
    currTotal = 0;
    reverse(totals.begin(), totals.end());
    for (int i = 0; i < 3; ++i) currTotal += totals[i];
    cout << currTotal << "\n";
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