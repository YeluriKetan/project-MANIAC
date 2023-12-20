#include "bits/stdc++.h"
#pragma GCC optimize ("O3")

using namespace std;

typedef long long ll;
typedef pair<int, int> ii;
typedef vector<bool> VB;
typedef vector<string> VS;
typedef vector<ii> VII;

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

class Signal {
public:
    string from;
    bool pulse; // low - false, high - true
    string to;

    Signal(string f, bool p, string t): from(std::move(f)), pulse(p), to(std::move(t)) {}
};

class Module {
public:
    string name;
    VS dest;
    Module(string n, VS d): name(std::move(n)), dest(std::move(d)) {}
    virtual void process(Signal &sig, queue<Signal> &q) = 0;
    virtual void addInput(string source) {}
    virtual bool isReset() = 0;
};

class Broadcast: public Module {
public:
    Broadcast(string n, VS d) : Module(std::move(n), std::move(d)) { }

    void process(Signal &sig, queue<Signal> &q) override { for (const auto &currDest: this->dest) q.emplace(this->name, false, currDest); }

    bool isReset() override { return true; }
};

class FlipFlop: public Module {
public:
    bool state;
    FlipFlop(string n, VS d) : Module(std::move(n), std::move(d)) { state = false; }

    void process(Signal &sig, queue<Signal> &q) override {
        if (sig.pulse) return;
        this->state = !this->state;
        for (const auto &currDest: this->dest) q.emplace(this->name, this->state, currDest);
    }

    bool isReset() override { return !state; }
};

class Conjunction: public Module {
public:
    unordered_map<string, int> inputInd;
    VB inputMem;

    Conjunction(string n, VS d): Module(std::move(n), std::move(d)) {}

    void process(Signal &sig, queue<Signal> &q) override {
        inputMem[inputInd[sig.from]] = sig.pulse;
        bool output = !std::accumulate(inputMem.begin(), inputMem.end(), true, [](bool accum, bool curr) { return accum && curr; });
        for (const auto &currDest: this->dest) q.emplace(this->name, output, currDest);
    }

    void addInput(string source) override {
        inputInd[source] = inputMem.size();
        inputMem.push_back(false);
    }

    bool isReset() override { return !std::accumulate(this->inputMem.begin(), this->inputMem.end(), false, [](bool accum, bool curr) { return accum || curr; }); }
};

regex split(R"(( -> |, ))");
regex_token_iterator<string::iterator> itEnd;
typedef unique_ptr<Module> MOD_PTR;

void part1(Reader &reader) {
    unordered_map<string, MOD_PTR> modules;
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        regex_token_iterator tokens(currLine.begin(), currLine.end(), split, -1);
        string name = *tokens++;
        if (!isalpha(name[0])) name = name.substr(1);
        VS dest;
        while (tokens != itEnd) dest.push_back(*tokens++);
        if (currLine[0] == '%') modules.insert({name, MOD_PTR(new FlipFlop(name.substr(), dest))});
        else if (currLine[0] == '&') modules.insert({name, MOD_PTR(new Conjunction(name, dest))});
        else modules.insert({name, MOD_PTR(new Broadcast(name, dest))});
    }
    for (const auto &currMod: modules) {
        for (const auto &currDest: currMod.second->dest) {
            auto it = modules.find(currDest);
            if (it != modules.end()) it->second->addInput(currMod.first);
        }
    }
    VII sigCount;
    int pushCount = 0;
    bool allReset = false;
    queue<Signal> sigQ;
    while (pushCount < 1000 && !allReset) {
        sigQ.push(Signal("", false, "broadcaster"));
        int low = 0, high = 0;
        while (!sigQ.empty()) {
            auto currSig = sigQ.front(); sigQ.pop();
            if (currSig.pulse) high++;
            else low++;
            auto it = modules.find(currSig.to);
            if (it != modules.end()) it->second->process(currSig, sigQ);
        }
        allReset = true;
        for (const auto &currMod: modules) allReset &= currMod.second->isReset();
        pushCount++;
        sigCount.emplace_back(low, high);
    }
    int totalPush = 1000;
    ii repeat = std::accumulate(sigCount.begin(), sigCount.end(), ii(0, 0), [](ii accum, ii curr) { return ii(accum.first + curr.first, accum.second + curr.second); });
    repeat.first *= totalPush / pushCount;
    repeat.second *= totalPush / pushCount;
    repeat = std::accumulate(sigCount.begin(), sigCount.begin() + (totalPush) % pushCount, repeat, [](ii accum, ii curr) { return ii(accum.first + curr.first, accum.second + curr.second); });
    cout << repeat.first * repeat.second << endl;
}

void part2(Reader &reader) {
    unordered_map<string, MOD_PTR> modules;
    string endIn;
    int endInInSize = 0;
    while (!reader.isEOF()) {
        string currLine = reader.getLine();
        regex_token_iterator tokens(currLine.begin(), currLine.end(), split, -1);
        string name = *tokens++;
        if (!isalpha(name[0])) name = name.substr(1);
        VS dest;
        while (tokens != itEnd) dest.push_back(*tokens++);
        if (currLine[0] == '%') modules.insert({name, MOD_PTR(new FlipFlop(name.substr(), dest))});
        else if (currLine[0] == '&') modules.insert({name, MOD_PTR(new Conjunction(name, dest))});
        else modules.insert({name, MOD_PTR(new Broadcast(name, dest))});
        if (dest[0] == "rx") endIn = name;
    }
    for (const auto &currMod: modules) {
        for (const auto &currDest: currMod.second->dest) {
            auto it = modules.find(currDest);
            if (it != modules.end()) it->second->addInput(currMod.first);
            if (currDest == endIn) endInInSize++;
        }
    }
    ll pushCount = 0;
    queue<Signal> sigQ;
    unordered_map<string, ll> endInInPeriods;
    while (endInInPeriods.size() < endInInSize) {
        sigQ.push(Signal("", false, "broadcaster"));
        pushCount++;
        while (!sigQ.empty()) {
            auto currSig = sigQ.front(); sigQ.pop();
            if (currSig.to == endIn && currSig.pulse) endInInPeriods.emplace(currSig.from, pushCount);
            auto it = modules.find(currSig.to);
            if (it != modules.end()) it->second->process(currSig, sigQ);
        }
    }
    cout << std::accumulate(endInInPeriods.begin(), endInInPeriods.end(), 1LL, [](ll accum, auto pair) { return lcm(accum, pair.second); });
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}