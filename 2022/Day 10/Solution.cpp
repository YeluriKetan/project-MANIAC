#include "bits/stdc++.h"

using namespace std;

typedef vector<vector<bool>> VVB;

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

void part1(Reader &reader) {
    string op;
    int cycle = 1, X = 1, total = 0, val;
    while (!reader.isEOF()) {
        op = reader.getToken<string>();
        if ((cycle - 20) % 40 == 0) total += cycle * X;
        cycle++;
        if (op == "addx") {
            val = reader.getToken<int>();
            if ((cycle - 20) % 40 == 0) total += cycle * X;
            cycle++;
            X += val;
        }
    }
    cout << total << "\n";
}

void drawPixel(int X, int crt, VVB &screen) {
    if (X - 1 <= crt % 40 && crt % 40 <= X + 1) {
        screen[crt / 40][crt % 40] = true;
    }
}

void part2(Reader &reader) {
    string op;
    int cycle = 0, X = 1, val;
    VVB screen(6, vector<bool>(40, false));
    while (!reader.isEOF()) {
        op = reader.getToken<string>();
        drawPixel(X, cycle, screen);
        cycle++;
        if (op == "addx") {
            val = reader.getToken<int>();
            drawPixel(X, cycle, screen);
            cycle++;
            X += val;
        }
    }
    for (auto &line: screen) {
        for (auto pixel: line) {
            cout << (pixel ? '#' : ' ');
        }
        cout << "\n";
    }
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}

