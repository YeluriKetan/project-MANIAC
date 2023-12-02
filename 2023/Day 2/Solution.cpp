#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef long long ll;
typedef pair<int, int> ii;
typedef vector<int> VI;
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

const regex setsSplit(R"(; )");
const regex cubesSplit(R"(, )");
const string cubeInfoSplit = " ";
const regex_token_iterator<string::iterator> itEnd;

bool checkBall(string s, unordered_map<string, int> &maxAvailable) {
    int space = s.find(cubeInfoSplit);
    return maxAvailable[s.substr(space + cubeInfoSplit.size())] >= stoi(s.substr(0, space));
}

bool checkSet(string s, unordered_map<string, int> &maxAvailable) {
    regex_token_iterator tokens(s.begin(), s.end(), cubesSplit, -1);
    while (tokens != itEnd) {
        if (!checkBall(*tokens++, maxAvailable)) return false;
    }
    return true;
}

bool checkGame(string s, unordered_map<string, int> &maxAvailable) {
    regex_token_iterator tokens(s.begin(), s.end(), setsSplit, -1);
    while (tokens != itEnd) {
        if (!checkSet(*tokens++, maxAvailable)) return false;
    }
    return true;
}

void part1(Reader &reader) {
    int total = 0;
    unordered_map<string, int> maxAvailable = {{"red", 12}, {"green", 13}, {"blue", 14}};
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        int delim = currLine.find(": ");
        if (checkGame(currLine.substr(delim + 2), maxAvailable)) {
            total += stoi(currLine.substr(5, delim - 5));
        }
    }
    cout << total << "\n";
}

void maxVI(VI &a, VI &b) {
    for (int i = 0; i < 3; ++i) a[i] = max(a[i], b[i]);
}

void minCubesColor(string s, VI &ans) {
    int space = s.find(cubeInfoSplit);
    string color = s.substr(space + 1);
    int count = stoi(s.substr(0, space));
    if (color == "red") {
        ans[0] = count;
    } else if (color == "green") {
        ans[1] = count;
    } else if (color == "blue") {
        ans[2] = count;
    } else {
        return;
    }
}

VI minCubesSet(string s) {
    VI ans = {0, 0, 0};
    regex_token_iterator tokens(s.begin(), s.end(), cubesSplit, -1);
    while (tokens != itEnd) minCubesColor(*tokens++, ans);
    return ans;
}

VI minCubesGame(string s) {
    VI ans = {0, 0, 0};
    regex_token_iterator tokens(s.begin(), s.end(), setsSplit, -1);
    while (tokens != itEnd) {
        VI curr = minCubesSet(*tokens++);
        maxVI(ans, curr);
    }
    return ans;
}

void part2(Reader &reader) {
    int total = 0;
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        VI ans = minCubesGame(currLine.substr(currLine.find(": ") + 2));
        total += ans[0] * ans[1] * ans[2];
    }
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