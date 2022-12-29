#include "bits/stdc++.h"

using namespace std;

typedef long long ll;
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

class Monkey {
private:
    int test, trueTest, falseTest, operVal = 0;
    char oper;
    bool isOperValOld = false, isReduce;
    VLL items;

    Monkey() {
    }

public:
    inline static ll modVal = 1;

    int inspect() {
        for (auto &item: items) {
            operVal = isOperValOld ? item : operVal;
            switch (oper) {
                case '+':
                    item += operVal;
                    break;
                case '-':
                    item -= operVal;
                    break;
                case '/':
                    item /= operVal;
                    break;
                case '*':
                    item *= operVal;
                    break;
            }
            if (isReduce) {
                item /= 3;
            }
            item %= modVal;
        }
        return items.size();
    }

    void testAndThrow(vector<Monkey> &monkeys) {
        for (auto item: items) {
            if (item % test) {
                monkeys[falseTest].items.push_back(item);
            } else {
                monkeys[trueTest].items.push_back(item);
            }
        }
        items.clear();
    }

    static Monkey readAndCreate(Reader &reader, bool isReduce) {
        Monkey newMonkey;
        string currLine, temp;
        stringstream currLineStream;
        reader.getLine(); // discard id line
        currLineStream = stringstream(reader.getLine().substr(18));
        while (getline(currLineStream, temp, ',')) {
            newMonkey.items.push_back(stoi(temp));
        }
        currLine = reader.getLine();
        newMonkey.oper = currLine[23];
        if (isdigit(currLine[25])) {
            newMonkey.operVal = stoi(currLine.substr(25));
        } else {
            newMonkey.isOperValOld = true;
        }
        newMonkey.test = stoi(reader.getLine().substr(21));
        newMonkey.trueTest = stoi(reader.getLine().substr(29));
        newMonkey.falseTest = stoi(reader.getLine().substr(30));
        reader.getLine();
        newMonkey.isReduce = isReduce;
        Monkey::modVal *= newMonkey.test;
        return newMonkey;
    }
};

void part1(Reader &reader) {
    vector<Monkey> monkeys;
    Monkey::modVal = 1;
    while (!reader.isEOF()) {
        monkeys.push_back(Monkey::readAndCreate(reader, true));
    }
    int n = monkeys.size();
    VI activity(n, 0);
    for (int i = 0; i < 20; ++i) {
        for (int j = 0; j < n; ++j) {
            activity[j] += monkeys[j].inspect();
            monkeys[j].testAndThrow(monkeys);
        }
    }
    sort(activity.begin(), activity.end());
    cout << activity[n - 1] * activity[n - 2] << "\n";
}

void part2(Reader &reader) {
    vector<Monkey> monkeys;
    Monkey::modVal = 1;
    while (!reader.isEOF()) {
        monkeys.push_back(Monkey::readAndCreate(reader, false));
    }
    int n = monkeys.size();
    VLL activity(n, 0);
    for (int i = 0; i < 10000; ++i) {
        for (int j = 0; j < n; ++j) {
            activity[j] += monkeys[j].inspect();
            monkeys[j].testAndThrow(monkeys);
        }
    }
    sort(activity.begin(), activity.end());
    cout << activity[n - 1] * activity[n - 2] << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}