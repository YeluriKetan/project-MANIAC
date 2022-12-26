#include "bits/stdc++.h"

using namespace std;

typedef vector<int> VI;
typedef vector<char> VC;
typedef vector<VC> VVC;

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

VVC readStacks(Reader &reader) {
    VVC stacks;
    while (!reader.isEOF()) {
        auto line = reader.getLine();
        if (isdigit(line[1])) {
            break;
        }
        for (int i = 1, ind = 0; i < line.size(); i += 4, ++ind) {
            if (ind >= stacks.size()) {
                stacks.push_back(VC());
            }
            if (!isspace(line[i])) {
                stacks[ind].push_back(line[i]);
            }
        }
    }
    for (auto &currStack: stacks) {
        reverse(currStack.begin(), currStack.end());
    }
    return stacks;
}

void part1(Reader &reader) {
    VVC stacks = readStacks(reader);
    VI n(4); // . q f t
    while (!reader.isEOF()) {
        for (int i = 1; i < 4; ++i) {
            reader.getToken<string>();
            n[i] = reader.getToken<int>();
        }
        n[2]--; n[3]--;
        for (int i = 0; i < n[1]; ++i) {
            stacks[n[3]].push_back(stacks[n[2]].back());
            stacks[n[2]].pop_back();
        }
    }
    for (auto currStack: stacks) {
        cout << currStack.back();
    }
    cout << "\n";
}

void part2(Reader &reader) {
    VVC stacks = readStacks(reader);
    VI n(4); // . q f t
    while (!reader.isEOF()) {
        for (int i = 1; i < 4; ++i) {
            reader.getToken<string>();
            n[i] = reader.getToken<int>();
        }
        n[2]--; n[3]--;
        for (int i = stacks[n[2]].size() - n[1]; i < stacks[n[2]].size(); ++i) {
            stacks[n[3]].push_back(stacks[n[2]][i]);
        }
        stacks[n[2]].erase(stacks[n[2]].end() - n[1], stacks[n[2]].end());
    }
    for (auto currStack: stacks) {
        cout << currStack.back();
    }
    cout << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}