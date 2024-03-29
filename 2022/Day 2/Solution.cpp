#include "bits/stdc++.h"

using namespace std;

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

unordered_map<char, int> value = {{'A', 0}, {'B', 1}, {'C', 2}, {'X', 0}, {'Y', 1}, {'Z', 2}};

void part1(Reader &reader) {
    int opp, self;
    int score = 0;
    while (!reader.isEOF()) {
        opp = value[reader.getToken<char>()], self = value[reader.getToken<char>()];
        score += self + 1;
        if (opp == self) {
            score += 3;
        } else if (self == (opp + 1) % 3) {
            score += 6;
        }
    }
    cout << score << "\n";
}

void part2(Reader &reader) {
    int opp, self, out;
    int score = 0;
    while (!reader.isEOF()) {
        opp = value[reader.getToken<char>()], out = value[reader.getToken<char>()];
        switch (out) {
            case 0:
                self = (opp + 2) % 3;
                break;
            case 1:
                self = opp;
                score += 3;
                break;
            case 2:
                self = (opp + 1) % 3;
                score += 6;
                break;
            default:
                break;
        }
        score += self + 1;
    }
    cout << score << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}