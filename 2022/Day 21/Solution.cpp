#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef long long ll;
typedef pair<int, int> ii;
typedef vector<int> VI;
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

regex format1(R"((\w+): (\w+) (\+|-|\/|\*) (\w+))");
regex format2(R"((\w+): (\d+))");
smatch match;

class Monkey {
public:
    string name;
    string left;
    char oper;
    string right;
    bool done;

    Monkey(string n, string l, char o, string r): name(n), left(l), oper(o), right(r) {
        done = false;
    }

    bool updateIfReady(unordered_map<string, ll> &shouted) {
        auto leftIt = shouted.find(left);
        auto rightIt = shouted.find(right);
        if (leftIt == shouted.end() || rightIt == shouted.end()) {
            return false;
        }
        ll ans = leftIt->second;
        switch (oper) {
            case '+':
                ans += rightIt->second;
                break;
            case '-':
                ans -= rightIt->second;
                break;
            case '*':
                ans *= rightIt->second;
                break;
            case '/':
                ans /= rightIt->second;
                break;
            default:
                break;
        }
        shouted[name] = ans;
        done = true;
        return true;
    }

    bool revUpdateIfReady(unordered_map<string, ll> &shouted) {
        auto leftIt = shouted.find(left);
        auto rightIt = shouted.find(right);
        auto currIt = shouted.find(name);
        if ((leftIt == shouted.end() && rightIt == shouted.end()) || currIt == shouted.end()) {
            return false;
        }
        if (leftIt == shouted.end()) {
            ll ans = currIt->second;
            switch (oper) {
                case '+':
                    ans -= rightIt->second;
                    break;
                case '-':
                    ans += rightIt->second;
                    break;
                case '*':
                    ans /= rightIt->second;
                    break;
                case '/':
                    ans *= rightIt->second;
                    break;
                default:
                    break;
            }
            shouted[left] = ans;
        } else if (rightIt == shouted.end()) {
            ll ans = leftIt->second;
            switch (oper) {
                case '+':
                    ans = currIt->second - ans;
                    break;
                case '-':
                    ans -= currIt->second;
                    break;
                case '*':
                    ans = currIt->second / ans;
                    break;
                case '/':
                    ans /= currIt->second;
                    break;
                default:
                    break;
            }
            shouted[right] = ans;
        } else {
            return false;
        }
        done = true;
        return true;
    }
};

void part1(Reader &reader) {
    vector<Monkey*> monkeys;
    unordered_map<string, ll> shouted;
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        Monkey* newMonkey = nullptr;
        if (regex_match(currLine, match, format1)) {
            newMonkey = new Monkey(match.str(1), match.str(2), match.str(3)[0], match.str(4));
            monkeys.push_back(newMonkey);
        } else if (regex_match(currLine, match, format2)) {
            shouted[match.str(1)] = stoi(match.str(2));
        } else {
            cerr << "Doesn't match" << "\n";
            return;
        }
    }
    int done = 0;
    while (done < monkeys.size()) {
        for (auto currMonkey: monkeys) {
            if (currMonkey->done) continue;
            if (currMonkey->updateIfReady(shouted)) done++;
        }
    }
    cout << shouted["root"] << "\n";
}

void part2(Reader &reader) {
    vector<Monkey*> monkeys;
    unordered_map<string, ll> shouted;
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        Monkey* newMonkey = nullptr;
        if (regex_match(currLine, match, format1)) {
            if (match.str(1) == "root") {
                newMonkey = new Monkey(match.str(1), match.str(2), '-', match.str(4));
            } else {
                newMonkey = new Monkey(match.str(1), match.str(2), match.str(3)[0], match.str(4));
            }
            monkeys.push_back(newMonkey);
        } else if (regex_match(currLine, match, format2)) {
            shouted[match.str(1)] = stoi(match.str(2));
        } else {
            cerr << "Doesn't match" << "\n";
            return;
        }
    }
    shouted["root"] = 0;
    shouted.erase("humn");
    bool updated = true;
    while (updated) {
        updated = false;
        for (auto currMonkey: monkeys) {
            if (currMonkey->done) continue;
            if (currMonkey->updateIfReady(shouted)) updated = true;
        }
    }
    updated = true;
    while (updated) {
        updated = false;
        for (auto currMonkey: monkeys) {
            if (currMonkey->done) continue;
            if (currMonkey->revUpdateIfReady(shouted)) updated = true;
        }
    }
    cout << shouted["humn"] << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}