#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef long long ll;
typedef pair<ll, ll> LL;

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

regex split(R"(( \(#| |\)))");

vector<LL> directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

unordered_map<char, int> charDirMap = {{'U', 0}, {'R', 1}, {'D', 2}, {'L', 3}};

LL add(LL p, int dir, ll n) { return {p.first + directions[dir].first * n, p.second + directions[dir].second * n}; }

ll cross(LL a, LL b) { return a.first * b.second - a.second * b.first; }

void part1(Reader &reader) {
    ll total = 0, boundary = 0;
    LL curr = {0, 0};
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        regex_token_iterator tokens(currLine.begin(), currLine.end(), split, -1);
        int dir = charDirMap[(*tokens).str()[0]];
        ll n = stoi(*++tokens);
        LL next = add(curr, dir, n);
        total += cross(curr, next);
        boundary += n;
        curr = next;
    }
    total += cross(curr, {0, 0});
    cout << abs(total) / 2 + boundary / 2 + 1 << endl;
}

ll hexToll(string s) {
    ll val = 0;
    for (auto currChar: s) {
        val <<= 4;
        val += isdigit(currChar) ? currChar - '0' : 10 + currChar - 'a';
    }
    return val;
}

void part2(Reader &reader) {
    ll total = 0, boundary = 0;
    LL curr = {0, 0};
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        regex_token_iterator tokens(currLine.begin(), currLine.end(), split, -1);
        tokens++; tokens++;
        ll n = hexToll((*tokens).str().substr(0, 5));
        int dir = ((*tokens).str()[5] - '0' + 1) % 4;
        LL next = add(curr, dir, n);
        total += cross(curr, next);
        boundary += n;
        curr = next;
    }
    total += cross(curr, {0, 0});
    cout << abs(total) / 2 + boundary / 2 + 1 << endl;
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}