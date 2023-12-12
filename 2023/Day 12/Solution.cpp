// WARNING: IDEALLY DONT REFER TO THIS SOLUTION. ITS A VERY CONVOLUTED DP SOLUTION.
#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef long long ll;
typedef vector<int> VI;
typedef vector<bool> VB;
typedef vector<VB> VVB;
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

regex space(R"( )");
regex comma(R"(,)");
regex_token_iterator<string::iterator> itEnd;

VVB getPossibleGrid(string &s, int maxLen) {
    // grid[i][j] represents if i number of operations springs can be placed starting from j position
    int n = s.size();
    VVB ans;
    ans.push_back(VB());
    for (int i = 1; i <= maxLen; ++i) {
        VB curr;
        for (int j = 0; j < n; ++j) {
            if (j > n - i) {
                curr.push_back(false);
                continue;
            }
            bool possible = true;
            for (int k = j; k < j + i; ++k) {
                if (s[k] == '.') {
                    possible = false;
                    break;
                }
            }
            if (j - 1 > -1 && s[j - 1] == '#') possible = false; // preceding cannot be #
            if (j + i < n && s[j + i] == '#') possible = false; // succeeding cannot be #
            curr.push_back(possible);
        }
        ans.push_back(curr);
    }
    return ans;
}

VLL dp(VLL prevDp, int currLen, bool first, VVB &possibleGrid, string &unknown) {
    ll preSum = first ? 1 : 0;
    VLL currDp(prevDp.size(), 0);
    for (int i = 1; i < prevDp.size(); ++i) {
        if (possibleGrid[currLen][i - 1]) currDp[i] += preSum;
        if (unknown[i - 1] == '#') preSum = 0;
        preSum += prevDp[i - 1];
    }
    for (int i = prevDp.size() - 1; i > -1; --i) {
        currDp[i] = (i - currLen + 1) > -1 ? currDp[i - currLen + 1] : 0;
    }
    return currDp;
}

void part1(Reader &reader) {
    ll total = 0;
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        regex_token_iterator parts(currLine.begin(), currLine.end(), space, -1);
        string unknown = *parts++, countsString = *parts;
        VI currCount;
        regex_token_iterator tokens(countsString.begin(), countsString.end(), comma, -1);
        while (tokens != itEnd) currCount.push_back(stoi(*tokens++));
        VVB possible = getPossibleGrid(unknown, max_element(currCount.begin(), currCount.end()).operator*());
        VLL prevDp(unknown.size() + 1, 0);
        bool first = true;
        for (auto currLen: currCount) {
            prevDp = dp(prevDp, currLen, first, possible, unknown);
            first = false;
        }
        int minPos = unknown.rfind('#');
        if (minPos == string::npos) {
            minPos = 1;
        } else {
            minPos++;
        }
        ll currTotal = 0;
        for (int i = minPos; i < prevDp.size(); ++i) currTotal += prevDp[i];
        total += currTotal;
    }
    cout << total << endl;
}

void part2(Reader &reader) {
    ll total = 0;
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        regex_token_iterator parts(currLine.begin(), currLine.end(), space, -1);
        string unknown = *parts++, countsString = *parts;
        VI currCount;
        regex_token_iterator tokens(countsString.begin(), countsString.end(), comma, -1);
        while (tokens != itEnd) currCount.push_back(stoi(*tokens++));
        string newUnknown = unknown;
        VI newCount = currCount;
        for (int i = 0; i < 4; ++i) {
            newUnknown += "?" + unknown;
            newCount.insert(newCount.end(), currCount.begin(), currCount.end());
        }
        VVB possible = getPossibleGrid(newUnknown, max_element(newCount.begin(), newCount.end()).operator*());
        VLL prevDp(newUnknown.size() + 1, 0);
        bool first = true;
        for (auto currLen: newCount) {
            prevDp = dp(prevDp, currLen, first, possible, newUnknown);
            first = false;
        }
        int minPos = newUnknown.rfind('#');
        if (minPos == string::npos) {
            minPos = 1;
        } else {
            minPos++;
        }
        ll currTotal = 0;
        for (int i = minPos; i < prevDp.size(); ++i) currTotal += prevDp[i];
        total += currTotal;
    }
    cout << total << endl;
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}