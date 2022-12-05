
#include "bits/stdc++.h"

using namespace std;

typedef pair<int, int> ii;
typedef long long ll;
typedef vector<int> VI;
typedef vector<ll> VLL;


ifstream input;
string inputFileName = "../input.txt";

void part1() {
    int count = 0, a1, a2, b1, b2;
    string temp;
    while (getline(input, temp, '-')) {
        a1 = stoi(temp);
        getline(input, temp, ',');
        a2 = stoi(temp);
        getline(input, temp, '-');
        b1 = stoi(temp);
        getline(input, temp, '\n');
        b2 = stoi(temp);
        if ((a1 <= b1 && b2 <= a2) || (b1 <= a1 && a2 <= b2)) {
            count++;
        }
    }
    cout << count << "\n";
}

void part2() {
    int count = 0, a1, a2, b1, b2;
    string temp;
    while (getline(input, temp, '-')) {
        a1 = stoi(temp);
        getline(input, temp, ',');
        a2 = stoi(temp);
        getline(input, temp, '-');
        b1 = stoi(temp);
        getline(input, temp, '\n');
        b2 = stoi(temp);
        if (!(a2 < b1 || b2 < a1)) {
            count++;
        }
    }
    cout << count << "\n";
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