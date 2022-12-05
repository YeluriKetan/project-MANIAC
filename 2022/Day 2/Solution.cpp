#include "bits/stdc++.h"

using namespace std;

typedef pair<int, int> ii;
typedef long long ll;
typedef vector<int> VI;
typedef vector<ll> VLL;


ifstream input;
string inputFileName = "../input.txt";
unordered_map<char, int> value = {{'A', 0}, {'B', 1}, {'C', 2}, {'X', 0}, {'Y', 1}, {'Z', 2}};


void part1() {
    string line;
    int opp, self;
    int score = 0;
    while (getline(input, line)) {
        opp = value[line[0]], self = value[line[2]];
        score += self + 1;
        if (opp == self) {
            score += 3;
        } else if (self == (opp + 1) % 3) {
            score += 6;
        }
    }
    cout << score << "\n";
}

void part2() {
    string line;
    int opp, self, out;
    int score = 0;
    while (getline(input, line)) {
        opp = value[line[0]], out = value[line[2]];
        switch (out) {
            case 0:
                self = (opp + 2) % 3;
                break;
            case 1:
                self = opp;
                score += 3;
                break;
            case 2:
                self = (opp + 1) % 3;
                score += 6;
                break;
            default:
                break;
        }
        score += self + 1;
    }
    cout << score << "\n";
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