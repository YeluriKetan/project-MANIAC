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

void part1(Reader &reader) {
    string line = reader.getLine();
    VB seen(26, false);
    int ind, n = line.size();
    bool flag;
    for (ind = 3; ind < n; ++ind) {
        flag = true;
        for (int i = ind - 3; i <= ind; ++i) {
            if (seen[line[i] - 'a']) {
                flag = false;
                break;
            } else {
                seen[line[i] - 'a'] = true;
            }
        }
        if (flag) break;
        for (int i = ind - 3; i <= ind; ++i) seen[line[i] - 'a'] = false;
    }
    cout << ind + 1 << "\n";
}

void part2(Reader &reader) {
    string line = reader.getLine();
    VB seen(26, false);
    int ind, n = line.size();
    bool flag;
    for (ind = 13; ind < n; ++ind) {
        flag = true;
        for (int i = ind - 13; i <= ind; ++i) {
            if (seen[line[i] - 'a']) {
                flag = false;
                break;
            } else {
                seen[line[i] - 'a'] = true;
            }
        }
        if (flag) break;
        for (int i = ind - 13; i <= ind; ++i) seen[line[i] - 'a'] = false;
    }
    cout << ind + 1 << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}