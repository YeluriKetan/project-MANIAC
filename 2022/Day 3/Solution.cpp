#include "bits/stdc++.h"

using namespace std;

typedef vector<bool> VB;

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

int getPriority(char c) {
    if (c <= 'Z') {
        return c - 'A' + 27;
    } else {
        return c - 'a' + 1;
    }
}

VB getHash(string s) {
    VB hash(53, false);
    for (auto curr: s) {
        hash[getPriority(curr)] = true;
    }
    return hash;
}

VB andHash(VB a, VB b) {
    assert(a.size() == b.size());
    VB hash(a.size());
    for (int i = 0; i < a.size(); ++i) {
        hash[i] = a[i] & b[i];
    }
    return hash;
}

void part1(Reader &reader) {
    int total = 0;
    while (!reader.isEOF()) {
        auto line = reader.getToken<string>();
        auto common = andHash(getHash(line.substr(0, line.size() / 2)),
                              getHash(line.substr(line.size() / 2)));
        total += find(common.begin(), common.end(), true) - common.begin();
    }
    cout << total << "\n";
}

void part2(Reader &reader) {
    int total = 0;
    while (!reader.isEOF()) {
        VB common = getHash(reader.getToken<string>());
        common = andHash(common, getHash(reader.getToken<string>()));
        common = andHash(common, getHash(reader.getToken<string>()));
        total += find(common.begin(), common.end(), true) - common.begin();
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