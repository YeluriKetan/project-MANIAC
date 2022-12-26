#include "bits/stdc++.h"

using namespace std;

typedef vector<int> VI;

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

regex format(R"((\d+)-(\d+),(\d+)-(\d+))"); // raw string
smatch match;

void part1(Reader &reader) {
    int count = 0;
    string currLine;
    VI n(5);
    while (!reader.isEOF()) {
        currLine = reader.getToken<string>();
        regex_search(currLine, match, format);
        for (int i = 1; i < 5; ++i) {
            n[i] = stoi(match.str(i));
        }
        if ((n[1] <= n[3] && n[4] <= n[2]) || (n[3] <= n[1] && n[2] <= n[4])) {
            count++;
        }
    }
    cout << count << "\n";
}

void part2(Reader &reader) {
    int count = 0;
    string currLine;
    VI n(5);
    while (!reader.isEOF()) {
        currLine = reader.getToken<string>();
        regex_search(currLine, match, format);
        for (int i = 1; i < 5; ++i) {
            n[i] = stoi(match.str(i));
        }
        if (!(n[2] < n[3] || n[4] < n[1])) {
            count++;
        }
    }
    cout << count << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}