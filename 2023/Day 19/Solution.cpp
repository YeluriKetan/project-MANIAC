#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef long long ll;
typedef vector<int> VI;
typedef vector<VI> VVI;

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
    Reader(string filename) { input.open(filename); }
    ~Reader() { input.close(); }
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
    bool isNewLine() { return (line.peek() == -1) && input.peek() == '\n'; }
    bool isEOF() { return (line.peek() == -1) ? input.peek() == EOF : line.peek() == EOF; }
    Reader& operator=(Reader reader) {
        this->input = std::move(reader.input);
        this->line = std::move(reader.line);
        return *this;
    }
};

class XmasRange {
public:
    VVI ranges;
    string wf;

    XmasRange(VVI r, string w): ranges(std::move(r)), wf(w) {}

    ll count() { return accumulate(ranges.begin(), ranges.end(), 1LL, [](ll prev, VI &curr) { return prev * (ll) max(0, curr[1] - curr[0] + 1); }); }
    ll sum() { return accumulate(ranges.begin(), ranges.end(), 0LL, [](ll prev, VI &curr) { return prev + curr[0]; }); }

    XmasRange copy() {
        VVI newRanges;
        for (auto currRange: ranges) newRanges.push_back({currRange[0], currRange[1]});
        return {newRanges, wf};
    }

    static XmasRange emptyInstance() { return {{{0, -1}, {0, -1}, {0, -1}, {0, -1}}, "R"}; }
};

typedef XmasRange XR;
regex ruleSplit(R"((<|>|:))");
regex_token_iterator<string::iterator> itEnd;
unordered_map<char, int> xmasMap = {{'x', 0}, {'m', 1}, {'a', 2}, {'s', 3}};

class Rule {
public:
    int ind;
    bool greater;
    int bound;
    string dest;

    Rule(int i, bool g, int b, string d): ind(i), greater(g), bound(b), dest(std::move(d)) {}

    static Rule fromString(string s) {
        if (s.size() < 2 || isalpha(s[1])) return {0, true, -1, s};
        regex_token_iterator tokens(s.begin(), s.end(), ruleSplit, -1);
        return {xmasMap[(*tokens++).str()[0]], s[1] == '>', stoi(*tokens++), *tokens};
    }

    XR splitAndQueue(XR &x, queue<XR> &q) {
        if (!greater) return splitLess(x, q);
        int mid = max(x.ranges[ind][0], bound + 1);
        if (mid > x.ranges[ind][1]) return x;
        auto newX = x.copy();
        newX.ranges[ind][0] = mid;
        newX.wf = dest;
        q.push(newX);
        if (mid <= x.ranges[ind][0]) return XR::emptyInstance();
        auto remX = x.copy();
        remX.ranges[ind][1] = mid - 1;
        return remX;
    }

private:
    XR splitLess(XR &x, queue<XR> &q) {
        int mid = min(x.ranges[ind][1], bound - 1);
        if (mid < x.ranges[ind][0]) return x;
        auto newX = x.copy();
        newX.ranges[ind][1] = mid;
        newX.wf = dest;
        q.push(newX);
        if (mid >= x.ranges[ind][1]) return XR::emptyInstance();
        auto remX = x.copy();
        remX.ranges[ind][0] = mid + 1;
        return remX;
    }
};

class Workflow {
public:
    vector<Rule> rules;

    Workflow(vector<Rule> r): rules(std::move(r)) {}

    void splitAndQueue(XR x, queue<XR> &q) {
        for (auto currRule: rules) {
            x = currRule.splitAndQueue(x, q);
            if (x.wf == "R") break;
        }
    }
};

regex split(R"((\{|,|\}))");
regex partFormat(R"(\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)\})");
smatch match;

void part1(Reader &reader) {
    unordered_map<string, Workflow> workflows;
    while (!reader.isNewLine()) {
        string currLine = reader.getLine();
        regex_token_iterator tokens(currLine.begin(), currLine.end(), split, -1);
        string name = *tokens++;
        vector<Rule> currRules;
        while (tokens != itEnd) currRules.push_back(Rule::fromString(*tokens++));
        Workflow res = Workflow(currRules);
        workflows.insert({name, res});
    }
    reader.getLine();
    ll total = 0;
    queue<XR> q;
    while (!reader.isEOF()) {
        string currPartLine = reader.getLine();
        regex_match(currPartLine, match, partFormat);
        VVI ranges;
        for (int i = 1; i < 5; ++i) ranges.push_back({stoi(match.str(i)), stoi(match.str(i))});
        XR currX(ranges, "in");
        q.push(currX);
        while (!q.empty()) {
            auto curr = q.front(); q.pop();
            if (curr.wf == "R") continue;
            if (curr.wf == "A") {
                total += curr.sum();
                continue;
            }
            workflows.find(curr.wf)->second.splitAndQueue(curr, q);
        }
    }
    cout << total << endl;
}

void part2(Reader &reader) {
    unordered_map<string, Workflow> workflows;
    while (!reader.isNewLine()) {
        string currLine = reader.getLine();
        regex_token_iterator tokens(currLine.begin(), currLine.end(), split, -1);
        string name = *tokens++;
        vector<Rule> currRules;
        while (tokens != itEnd) currRules.push_back(Rule::fromString(*tokens++));
        Workflow res = Workflow(currRules);
        workflows.insert({name, res});
    }
    queue<XR> q;
    VVI fullRanges = {{1, 4000}, {1, 4000}, {1, 4000}, {1, 4000}};
    XR start(fullRanges, "in");
    q.push(start);
    ll total = 0;
    while (!q.empty()) {
        auto curr = q.front(); q.pop();
        if (curr.wf == "R") continue;
        if (curr.wf == "A") {
            total += curr.count();
            continue;
        }
        workflows.find(curr.wf)->second.splitAndQueue(curr, q);
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