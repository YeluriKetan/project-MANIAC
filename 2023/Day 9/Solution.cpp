#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef long long ll;
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
regex_token_iterator<string::iterator> itEnd;

void part1(Reader &reader) {
    ll total = 0;
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        regex_token_iterator tokens(currLine.begin(), currLine.end(), space, -1);
        VLL curr;
        while (tokens != itEnd) curr.push_back(stoll(*tokens++));
        int n = curr.size();
        total += curr[n - 1];
        for (int i = n - 1; i > 0; --i) {
            for (int j = 0; j < i; ++j) {
                curr[j] = curr[j + 1] - curr[j];
            }
            total += curr[i - 1];
        }
    }
    cout << total << endl;
}

void part2(Reader &reader) {
    ll total = 0;
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        regex_token_iterator tokens(currLine.begin(), currLine.end(), space, -1);
        VLL curr;
        while (tokens != itEnd) curr.push_back(stoll(*tokens++));
        int n = curr.size();
        total += curr[0];
        for (int i = n - 1; i > 0; --i) {
            for (int j = 0; j < i; ++j) {
                curr[j] = curr[j + 1] - curr[j];
            }
            total += pow(-1, n - i) * curr[0];
        }
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