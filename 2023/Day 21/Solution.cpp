#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef long long ll;
typedef pair<int, int> ii;
typedef vector<ii> VII;
typedef vector<char> VC;
typedef vector<bool> VB;
typedef vector<VB> VVB;
typedef vector<VC> VVC;
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

VII directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

ii add(ii &a, ii &b) { return {a.first + b.first, a.second + b.second}; }

bool isValid(ii point, const ii &rc) { return 0 <= point.first && point.first < rc.first && 0 <= point.second && point.second < rc.second; }

// return number of plots at dist i from start in a single instance of the grid
VLL walkFrom(VVC &grid, const ii &rc, ii start) {
    VVB visited(rc.first, VB(rc.second, false));
    visited[start.first][start.second] = true;
    queue<ii> q;
    q.push(start);
    VLL dists;
    while (!q.empty()) {
        dists.push_back(q.size());
        for (int i = q.size(); i > 0; --i) {
            auto curr = q.front(); q.pop();
            for (auto &currDir: directions) {
                auto next = add(curr, currDir);
                if (isValid(next, rc) && grid[next.first][next.second] != '#' && !visited[next.first][next.second]) {
                    q.push(next);
                    visited[next.first][next.second] = true;
                }
            }
        }
    }
    return dists;
}

ll stepSum(VLL &counts, int maxInclInd, int step, bool includeStart) {
    ll sum = 0;
    for (int i = includeStart ? 0 : 1; i <= min(maxInclInd, (int) counts.size() - 1); i += step) sum += counts[i];
    return sum;
}

void part1(Reader &reader) {
    VVC grid;
    int r = 0, c = 0;
    ii start(0, 0);
    while (!reader.isEOF()) {
        c = 0;
        VC currRow;
        for (auto currChar: reader.getLine()) {
            currRow.push_back(currChar);
            if (currChar == 'S') start = {r, c};
            c++;
        }
        grid.push_back(currRow);
        r++;
    }
    const ii rc = {r, c};
    auto dists = walkFrom(grid, rc, start);
    cout << stepSum(dists, 64, 2, true) << endl;
}

struct PointHash {
    int operator()(const ii &p) const {
        return (p.first << 16) | p.second;
    }
};

// assumptions
// - grid is odd square
// - mid row and col are all plots
// - all edges are all plots
// - start is center
void part2(Reader &reader) {
    VVC grid;
    int r = 0, c = 0;
    while (!reader.isEOF()) {
        c = 0;
        VC currRow;
        for (auto currChar: reader.getLine()) {
            currRow.push_back(currChar);
            c++;
        }
        grid.push_back(currRow);
        r++;
    }
    unordered_map<ii, VLL, PointHash> startToDists;
    const ii rc(r, c);
    int n = r; // grid is odd square
    for (int i = 0; i < n; i += n / 2) {
        for (int j = 0; j < n; j += n / 2) {
            startToDists[{i, j}] = walkFrom(grid, rc, {i, j});
        }
    }
    int maxSteps = 26501365;
    // include centre grid
    ll ans = stepSum(startToDists.find({n / 2, n / 2})->second, maxSteps, 2, false);
    // all 4 quadrants
    for (int i = 0; i < n; i += n - 1) {
        for (int j = 0; j < n; j += n - 1) {
            for (int y = n / 2; y <= maxSteps; y += n) {
                auto dists = startToDists.find({i, j})->second;
                int fullCovered = (max(0, maxSteps - n - y - n / 2) / n);
                ans += (fullCovered / 2) * (stepSum(dists, INT_MAX, 2, false) + stepSum(dists, INT_MAX, 2, true));
                if (fullCovered % 2) ans += stepSum(dists, INT_MAX, 2, y % 2 == 0);
                for (int k = 1; k < 100; ++k) {
                    int remSteps = maxSteps - y - n / 2 - 2 - (fullCovered + k - 1) * n;
                    ll currOddSum = stepSum(dists, remSteps, 2, remSteps % 2 == 0);
                    if (currOddSum < 1) break;
                    ans += currOddSum;
                }
            }
        }
    }
    // + and - y axis
    for (int i = 0; i < n; i += n - 1) {
        VLL dists = startToDists.find({i, n / 2})->second;
        int fullCovered = (max(0, maxSteps - n + 1) / n);
        ans += (fullCovered / 2) * (stepSum(dists, INT_MAX, 2, true) + stepSum(dists, INT_MAX, 2, false));
        if (fullCovered % 2) ans += stepSum(dists, INT_MAX, 2, false);
        for (int k = 1; k < 100; ++k) {
            int remSteps = maxSteps - n / 2 - 1 - (fullCovered + k - 1) * n;
            ll currOddSum = stepSum(dists, remSteps, 2, remSteps % 2 == 0);
            if (currOddSum < 1) break;
            ans += currOddSum;
        }
    }
    // + and - x axis
    for (int i = 0; i < n; i += n - 1) {
        VLL dists = startToDists.find({ n / 2, i})->second;
        int fullCovered = (max(0, maxSteps - n + 1) / n);
        ans += (fullCovered / 2) * (stepSum(dists, INT_MAX, 2, true) + stepSum(dists, INT_MAX, 2, false));
        if (fullCovered % 2) ans += stepSum(dists, INT_MAX, 2, false);
        for (int k = 1; k < 100; ++k) {
            int remSteps = maxSteps - n / 2 - 1 - (fullCovered + k - 1) * n;
            ll currOddSum = stepSum(dists, remSteps, 2, remSteps % 2 == 0);
            if (currOddSum < 1) break;
            ans += currOddSum;
        }
    }
    cout << ans << endl;
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}