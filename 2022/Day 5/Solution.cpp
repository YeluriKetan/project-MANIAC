#include "bits/stdc++.h"

using namespace std;

typedef pair<int, int> ii;
typedef long long ll;
typedef vector<char> VC;
typedef vector<VC> VVC;


ifstream input;
string inputFileName = "../input.txt";

VVC readStacks() {
    VVC stacks;
    string line;
    while (getline(input, line)) {
        if (isdigit(line[1])) {
            break;
        }
        for (int i = 1, ind = 0; i < line.size(); i += 4, ++ind) {
            if (ind >= stacks.size()) {
                stacks.push_back(VC());
            }
            if (!isspace(line[i])) {
                stacks[ind].push_back(line[i]);
            }
        }
    }
    for (auto &currStack: stacks) {
        reverse(currStack.begin(), currStack.end());
    }
    return stacks;
}

void part1() {
    VVC stacks = readStacks();
    string temp;
    int q, f, t;
    while (input.peek() != EOF) {
        input >> temp >> q >> temp >> f >> temp >> t;
        f--; t--;
        for (int i = 0; i < q; ++i) {
            stacks[t].push_back(stacks[f].back());
            stacks[f].pop_back();
        }
    }
    for (auto currStack: stacks) {
        cout << currStack.back();
    }
    cout << "\n";
}

void part2() {
    VVC stacks = readStacks();
    string temp;
    int q, f, t;
    while (input.peek() != EOF) {
        input >> temp >> q >> temp >> f >> temp >> t;
        f--; t--;
        for (int i = stacks[f].size() - q; i < stacks[f].size(); ++i) {
            stacks[t].push_back(stacks[f][i]);
        }
        stacks[f].erase(stacks[f].end() - q, stacks[f].end());
    }
    for (auto currStack: stacks) {
        cout << currStack.back();
    }
    cout << "\n";
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