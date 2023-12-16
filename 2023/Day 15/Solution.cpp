#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef pair<int, int> ii;
typedef vector<ii> VII;
typedef vector<string> VS;

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

regex comma(R"(,)");
regex_token_iterator<string::iterator> itEnd;

int hashVal(string s) {
    int val = 0;
    for (auto currChar: s) val = ((val + currChar) * 17) % 256;
    return val;
}

void part1(Reader &reader) {
    string line = reader.getLine();
    regex_token_iterator tokens(line.begin(), line.end(), comma, -1);
    int total = 0;
    while (tokens != itEnd) total += hashVal(*tokens++);
    cout << total << endl;
}

VS parse(string s) {
    if (s[s.size() - 1] == '-') return {s.substr(0, s.size() - 1), "-"};
    else return {s.substr(0, s.size() - 2), "+", string(1, s[s.size() - 1])};
}

void part2(Reader &reader) {
    string line = reader.getLine();
    regex_token_iterator tokens(line.begin(), line.end(), comma, -1);
    int id = 0;
    vector<unordered_map<string, ii>> boxes(256, unordered_map<string, ii>());
    while (tokens != itEnd) {
        VS parsed = parse(*tokens++);
        auto box = hashVal(parsed[0]);
        if (parsed[1] == "-") {
            boxes[box].erase(parsed[0]);
        } else {
            auto it = boxes[box].try_emplace(parsed[0], id++, stoi(parsed[2]));
            if (!it.second) boxes[box][parsed[0]].second = stoi(parsed[2]);
        }
    }
    int total = 0;
    for (int i = 0; i < 256; ++i) {
        VII lenses;
        for (const auto &currLens: boxes[i]) lenses.push_back(currLens.second);
        std::sort(lenses.begin(), lenses.end(), [](ii &a, ii &b) { return a.first < b.first; });
        for (int j = 0; j < lenses.size(); ++j) total += (i + 1) * (j + 1) * lenses[j].second;
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