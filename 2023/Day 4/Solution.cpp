#include "bits/stdc++.h"
//#pragma GCC optimize ("O3")

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

const regex_token_iterator<string::iterator> itEnd;
const regex space(R"( +)");
unordered_set<int> getCardNums(string s) {
    unordered_set<int> ans;
    regex_token_iterator tokens(s.begin(), s.end(), space, -1);
    while (tokens != itEnd) {
        ans.insert(stol(*tokens++));
    }
    return ans;
}

const regex split(R"( \| +)");
int parseAndMatch(string s) {
    unordered_set<int> left, right;
    regex_token_iterator tokens(s.begin(), s.end(), split, -1);
    left = getCardNums(*tokens++);
    right = getCardNums(*tokens++);
    int ans = 0;
    for (auto curr: right) {
        if (left.find(curr) != left.end()) ans++;
    }
    return ans;
}

const regex metaSplit(R"(: +)");
void part1(Reader &reader) {
    int ans = 0;
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        regex_token_iterator currTokens(currLine.begin(), currLine.end(), metaSplit, -1);
        currTokens++;
        ans += pow(2, parseAndMatch(*currTokens) - 1);
    }
    cout << ans << "\n";
}

void part2(Reader &reader) {
    ll ans = 0;
    VLL cardsWon;
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        regex_token_iterator currTokens(currLine.begin(), currLine.end(), metaSplit, -1);
        currTokens++;
        cardsWon.push_back(parseAndMatch(*currTokens));
    }
    int n = cardsWon.size();
    for (int i = n - 1; i > -1; --i) {
        ll temp = 0;
        for (int j = 1; j <= cardsWon[i]; ++j) {
            temp += cardsWon[j + i];
        }
        cardsWon[i] = temp + 1;
        ans += cardsWon[i];
    }
    cout << ans << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}