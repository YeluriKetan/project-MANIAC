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

regex split(R"( +)");
const regex_token_iterator<string::iterator> itEnd;

void part1(Reader &reader) {
    VLL times, dist;
    string timesString = reader.getLine();
    regex_token_iterator tokens(timesString.begin(), timesString.end(), split, -1);
    tokens++;
    while (tokens != itEnd) times.push_back(stol(*tokens++));
    string distString = reader.getLine();
    tokens = regex_token_iterator(distString.begin(), distString.end(), split, -1);
    tokens++;
    while (tokens != itEnd) dist.push_back(stol(*tokens++));
    ll ans = 1, n = times.size();
    for (int i = 0; i < n; ++i) {
        ll currCount = 0;
        for (ll j = 0; j < times[i]; ++j) {
            if ((times[i] - j) * j > dist[i]) currCount++;
        }
        ans *= currCount;
    }
    cout << ans << "\n";
}

void part2(Reader &reader) {
    string timesString = reader.getLine(), timesCombined;
    regex_token_iterator tokens(timesString.begin(), timesString.end(), split, -1);
    tokens++;
    while (tokens != itEnd) timesCombined += *tokens++;
    string distString = reader.getLine(), distCombined;
    tokens = regex_token_iterator(distString.begin(), distString.end(), split, -1);
    tokens++;
    while (tokens != itEnd) distCombined += *tokens++;
    ll time = stoll(timesCombined), dist = stoll(distCombined);
    ll ans = 0;
    for (ll j = 0; j < time; ++j) {
        if ((time - j) * j > dist) ans++;
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