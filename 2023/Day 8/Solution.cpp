#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef long long ll;
typedef pair<string, string> SS;
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

regex split(R"(( = \(|, |\)))");

void part1(Reader &reader) {
    unordered_map<string, SS> network;
    string directions = reader.getLine();
    reader.getLine();
    while (!reader.isEOF()) {
        string curr = reader.getLine();
        regex_token_iterator tokens(curr.begin(), curr.end(), split, -1);
        string node = *tokens;
        string L = *++tokens;
        string R = *++tokens;
        network[node] = {L, R};
    }
    int count = 0;
    string curr = "AAA";
    while (true) {
        for (auto currChar: directions) {
            count++;
            if (currChar == 'L') {
                curr = network[curr].first;
            } else {
                curr = network[curr].second;
            }
            if (curr == "ZZZ") {
                cout << count << "\n";
                return;
            }
        }
    }
}

ll gcd(ll a, ll b) {
    if (a < b) return gcd(b, a);
    if (b == 0) return a;
    return gcd(b, a % b);
}

ll lcm(ll a, ll b) {
    return (a * b) / gcd(a, b);
}

void part2(Reader &reader) {
    unordered_map<string, SS> network;
    VS currNodes;
    string directions = reader.getLine();
    string ab = reader.getLine();
    while (!reader.isEOF()) {
        string curr = reader.getLine();
        regex_token_iterator tokens(curr.begin(), curr.end(), split, -1);
        string node = *tokens;
        string L = *++tokens;
        string R = *++tokens;
        network[node] = {L, R};
        if (node[2] == 'A') {
            currNodes.push_back(node);
        }
    }
    ll ans = 1;
    for (auto &currNode: currNodes) {
        int count = 0;
        bool notReached = true;
        while (notReached) {
            for (auto currChar: directions) {
                count++;
                if (currChar == 'L') {
                    currNode = network[currNode].first;
                } else {
                    currNode = network[currNode].second;
                }
                if (currNode[2] == 'Z') {
                    ans = lcm(ans, count);
                    notReached = false;
                    break;
                }
            }
        }
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