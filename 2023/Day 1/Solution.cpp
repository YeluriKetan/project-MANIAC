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

int parse(string s) {
    int f = -1, l = -1;
    for (auto curr: s) {
        if (isdigit(curr)) {
            if (f == -1) f = curr - '0';
            l = curr - '0';
        }
    }
    return f * 10 + l;
}

void part1(Reader &reader) {
    int ans = 0;
    while (!reader.isEOF()) {
        ans += parse(reader.getLine());
    }
    cout << ans << "\n";
}

const unordered_map<string, char> numbers = {{"one", '1'}, {"two", '2'}, {"three", '3'}, {"four", '4'}, {"five", '5'}, {"six", '6'},
                                            {"seven", '7'}, {"eight", '8'}, {"nine", '9'}};

bool match(string s, int ind, string against) {
    if (s.size() - ind < against.size()) return false;
    for (int i = 0; i < against.size(); ++i) {
        if (s[i + ind] != against[i]) return false;
    }
    return true;
}

string replace(string s) {
    int n = s.size();
    for (int i = 0; i < n; ++i) {
        for (const auto &number: numbers) {
            if (match(s, i, number.first)) s[i] = number.second;
        }
    }
    return s;
}

void part2(Reader &reader) {
    int ans = 0;
    while (!reader.isEOF()) {
        ans += parse(replace(reader.getLine()));
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