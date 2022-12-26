#include "bits/stdc++.h"

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

void part1(Reader &reader) {
    ll maxTotal = 0, currTotal = 0;
    while (!reader.isEOF()) {
        if (reader.isNewLine()) {
            maxTotal = max(maxTotal, currTotal);
            currTotal = 0;
        }
        currTotal += reader.getToken<int>();
    }
    cout << max(maxTotal, currTotal) << "\n";
}

void part2(Reader &reader) {
    ll currTotal = 0;
    VLL totals;
    while (!reader.isEOF()) {
        if (reader.isNewLine()) {
            totals.push_back(currTotal);
            currTotal = 0;
        }
        currTotal += reader.getToken<int>();
    }
    totals.push_back(currTotal);
    sort(totals.begin(), totals.end());
    currTotal = 0;
    reverse(totals.begin(), totals.end());
    for (int i = 0; i < 3; ++i) currTotal += totals[i];
    cout << currTotal << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}
