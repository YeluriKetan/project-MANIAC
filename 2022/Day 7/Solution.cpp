#include "bits/stdc++.h"

using namespace std;

typedef long long ll;
typedef pair<ll, ll> llll;
typedef vector<ll> VLL;
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

string getStrPath(VS &path) {
    string strPath;
    for (auto &currDir: path) {
        strPath += "/" + currDir;
    }
    return strPath;
}

void buildTree(Reader &reader, unordered_map<string, ll> &dirSizes, unordered_map<string, VS> &subDirs) {
    ll currTotal = 0;
    VS path = {""};
    VS dirList;
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        switch (currLine[0]) {
            case '$': {
                auto currStrPath = getStrPath(path);
                if (currTotal > 0 && dirSizes.find(currStrPath) == dirSizes.end()) {
                    dirSizes[currStrPath] = currTotal;
                    currTotal = 0;
                }
                if (!dirList.empty() && subDirs.find(currStrPath) == subDirs.end()) {
                    subDirs[currStrPath] = dirList;
                    dirList.clear();
                }
                if (currLine[2] == 'c') {
                    switch (currLine[5]) {
                        case '/':
                            path = {""};
                            break;
                        case '.':
                            path.pop_back();
                            break;
                        default:
                            path.push_back(currLine.substr(5));
                            break;
                    }
                    break;
                }
                break;
            }
            case 'd': {
                dirList.push_back(currLine.substr(4));
                break;
            }
            default: {
                currTotal += stoi(currLine.substr(0, currLine.find(' ')));
                break;
            }
        }
    }
    auto currStrPath = getStrPath(path);
    if (currTotal > 0 && dirSizes.find(currStrPath) == dirSizes.end()) {
        dirSizes[currStrPath] = currTotal;
        currTotal = 0;
    }
    if (!dirList.empty() && subDirs.find(currStrPath) == subDirs.end()) {
        subDirs[currStrPath] = dirList;
        dirList.clear();
    }
}

llll dfsPart1(string currPath, unordered_map<string, ll> &dirSizes, unordered_map<string, VS> &subDirs) {
    ll sumTotal = 0, currSize = dirSizes[currPath];
    for (auto currSubDir: subDirs[currPath]) {
        auto subDfs = dfsPart1(currPath + "/" + currSubDir, dirSizes, subDirs);
        sumTotal += subDfs.first;
        currSize += subDfs.second;
    }
    if (currSize < 100001) {
        sumTotal += currSize;
    }
    return {sumTotal, currSize};
}

void part1(Reader &reader) {
    unordered_map<string, ll> dirSizes;
    unordered_map<string, VS> subDirs;
    buildTree(reader, dirSizes, subDirs);
    cout << dfsPart1("/", dirSizes, subDirs).first << "\n";
}

ll dfsPart2(string currPath, unordered_map<string, ll> &dirSizes, unordered_map<string, VS> &subDirs, VLL &sizesList) {
    ll currSize = dirSizes[currPath];
    for (auto currSubDir: subDirs[currPath]) {
        currSize += dfsPart2(currPath + "/" + currSubDir, dirSizes, subDirs, sizesList);
    }
    sizesList.push_back(currSize);
    return currSize;
}

void part2(Reader &reader) {
    unordered_map<string, ll> dirSizes;
    unordered_map<string, VS> subDirs;
    VLL sizesList;
    buildTree(reader, dirSizes, subDirs);
    dfsPart2("/", dirSizes, subDirs, sizesList);
    sort(sizesList.begin(), sizesList.end());
    ll minRemoval = sizesList.rbegin().operator*() - 40000000;
    cout << lower_bound(sizesList.begin(), sizesList.end(), minRemoval).operator*() << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}