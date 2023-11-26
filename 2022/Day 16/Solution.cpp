#include "bits/stdc++.h"

using namespace std;

typedef vector<int> VI;

typedef pair<int, int> ii;
typedef vector<ii> VII;

typedef long long ll;
typedef vector<ll> Vll;

typedef pair<ll, ll> LL;
typedef vector<LL> VLL;

typedef vector<string> VS;

typedef pair<string, int> SI;

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

regex format(R"(Valve (\w{2}) has flow rate=(\d+); tunnels? leads? to valves? ([A-Z ,]*))");
smatch match;

VS splitInto(string source, string delim) {
    int start = 0, end;
    VS sink;
    while ((end = source.find(delim, start)) != string::npos) {
        sink.push_back(source.substr(start, 2));
        start = end + delim.size();
    }
    sink.push_back(source.substr(start));
    return sink;
}

void bfs(string start, int startInd,
         unordered_map<string, VS> &tunnels,
         vector<VI> &distGrid,
         unordered_map<string, int> &index) {
    queue<string> q;
    unordered_set<string> visited;
    q.push(start);
    visited.insert(start);
    int len = 0;
    while (!q.empty()) {
        for (int i = q.size(); i > 0; --i) {
            auto curr = q.front(); q.pop();
            auto it = index.find(curr);
            if (it != index.end()) {
                distGrid[startInd][it->second] = len;
            }
            for (auto next: tunnels[curr]) {
                if (visited.find(next) == visited.end()) {
                    q.push(next);
                    visited.insert(next);
                }
            }
        }
        len++;
    }
}

ii dfs(int ind, int mask, int time, int n, vector<VI> &distGrid, VI &flow) {
    if (time < 1 || (mask & (1 << ind))) {
        return {0, 0};
    }
    if (flow[ind] > 0) time--;
    mask |= 1 << ind;
    int maxFlow = 0;
    int maxMask = 0;
    for (int i = 0; i < n; ++i) {
        int remTime = time - distGrid[ind][i];
        auto currResult = dfs(i, mask, remTime, n, distGrid, flow);
        if (currResult.first > maxFlow) {
            maxFlow = currResult.first;
            maxMask = currResult.second;
        }
    }
    return {maxFlow + time * flow[ind], maxMask | (1 << ind)};
}

void part1(Reader &reader) {
    unordered_map<string, int> index;
    VI flow;
    unordered_map<string, VS> tunnels;
    while (!reader.isEOF()) {
        auto currLine = reader.getLine();
        regex_search(currLine, match, format);
        string currValve = match.str(1);
        int flowVal = stoi(match.str(2));
        if (flowVal > 0 || currValve == "AA") {
            index[currValve] = index.size();
            flow.push_back(flowVal);
        }
        tunnels.insert({currValve, splitInto(match.str(3), ", ")});
    }
    int n = flow.size();
    vector<VI> distGrid = vector(n, VI(n, 0));
    for (auto curr: index) {
        bfs(curr.first, curr.second, tunnels, distGrid, index);
    }

    int time = 30, startInd = index["AA"];
    auto [total, mask] = dfs(startInd, 0, time, n, distGrid, flow);
    cout << total << "\n";
}

// based on https://jactl.io/blog/2023/04/21/advent-of-code-2022-day16.html
void part2(Reader &reader) {
    unordered_map<string, int> index;
    VI flow;
    unordered_map<string, VS> tunnels;
    while (!reader.isEOF()) {
        auto currLine = reader.getLine();
        regex_search(currLine, match, format);
        string currValve = match.str(1);
        int flowVal = stoi(match.str(2));
        if (flowVal > 0 || currValve == "AA") {
            index[currValve] = index.size();
            flow.push_back(flowVal);
        }
        tunnels.insert({currValve, splitInto(match.str(3), ", ")});
    }
    int n = flow.size();
    vector<VI> distGrid = vector(n, VI(n, 0));
    for (auto curr: index) {
        bfs(curr.first, curr.second, tunnels, distGrid, index);
    }

    int time = 26, startInd = index["AA"];
    auto [maxSingle, maxMask] = dfs(startInd, 0, time, n, distGrid, flow);
    auto [maxRem, _] = dfs(startInd, maxMask ^ (1 << startInd), time, n, distGrid, flow);
    VI maskMaxVal((1 << n), 0);
    int allMask = (1 << n) - 1, startMask = 1 << startInd;
    int antiStartMask = allMask ^ startMask;
    for (int i = 0; i <= allMask; i++) {
        if (i & startMask) {
            continue;
        }
        auto [currVal, _] = dfs(startInd, i, time, n, distGrid, flow);
        if (currVal >= maxRem) {
            maskMaxVal[i] = currVal;
        }
    }
    int maxPairVal = maxSingle + maxRem;
    for (int i = 0; i < maskMaxVal.size(); ++i) {
        maxPairVal = max(maxPairVal, maskMaxVal[i] + maskMaxVal[(allMask ^ i) & antiStartMask]);
    }
    cout << maxPairVal << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}