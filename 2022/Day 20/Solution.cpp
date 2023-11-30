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

class Node {
public:
    int ogIndex;
    ll val;
    Node* prev;
    Node* next;

    Node(int ind, ll v, Node* p, Node* n): ogIndex(ind), val(v), prev(p), next(n) {}

    void insertAfter(Node* n) {
        this->next->prev = n;
        n->next = this->next;
        n->prev = this;
        this->next = n;
    }

    void insertBefore(Node* n) {
        this->prev->next = n;
        n->prev = this->prev;
        n->next = this;
        this->prev = n;
    }

    Node* removeCurr() {
        this->prev->next = this->next;
        this->next->prev = this->prev;
        this->prev = nullptr;
        this->next = nullptr;
        return this;
    }

    static Node* backwards(Node* start, int n) {
        auto curr = start;
        while (n > 0) {
            curr = curr->prev;
            n--;
        }
        return curr;
    }

    static Node* forwards(Node* start, int n) {
        auto curr = start;
        while (n > 0) {
            curr = curr->next;
            n--;
        }
        return curr;
    }
};

void part1(Reader &reader) {
    vector<Node*> pointers;
    Node* prev = nullptr;
    Node* zero = nullptr;
    while (!reader.isEOF()) {
        Node* newNode = new Node(pointers.size(), reader.getToken<int>(), prev, nullptr);
        if (newNode->val == 0) zero = newNode;
        pointers.push_back(newNode);
        if (prev != nullptr) prev->next = newNode;
        prev = newNode;
    }
    prev->next = pointers[0];
    pointers[0]->prev = prev;
    int n = pointers.size();
    for (auto currNode: pointers) {
        bool isForwards = currNode->val > 0;
        int moveVal = abs(currNode->val) % (n - 1);
        if (!moveVal) continue;
        if (isForwards) {
            auto end = Node::forwards(currNode, moveVal);
            currNode->removeCurr();
            end->insertAfter(currNode);
        } else {
            auto end = Node::backwards(currNode, moveVal);
            currNode->removeCurr();
            end->insertBefore(currNode);
        }
    }
    int sum = 0;
    Node* curr = zero;
    for (int i = 0; i < 3; ++i) {
        curr = Node::forwards(curr, 1000 % n);
        sum += curr->val;
    }
    cout << sum << "\n";
}

void part2(Reader &reader) {
    ll key = 811589153;
    vector<Node*> pointers;
    Node* prev = nullptr;
    Node* zero = nullptr;
    while (!reader.isEOF()) {
        Node* newNode = new Node(pointers.size(), key * reader.getToken<ll>(), prev, nullptr);
        if (newNode->val == 0) zero = newNode;
        pointers.push_back(newNode);
        if (prev != nullptr) prev->next = newNode;
        prev = newNode;
    }
    prev->next = pointers[0];
    pointers[0]->prev = prev;
    int n = pointers.size();
    for (int i = 0; i < 10; ++i) {
        for (auto currNode: pointers) {
            bool isForwards = currNode->val > 0;
            int moveVal = abs(currNode->val) % (n - 1);
            if (!moveVal) continue;
            if (isForwards) {
                auto end = Node::forwards(currNode, moveVal);
                currNode->removeCurr();
                end->insertAfter(currNode);
            } else {
                auto end = Node::backwards(currNode, moveVal);
                currNode->removeCurr();
                end->insertBefore(currNode);
            }
        }
    }
    ll sum = 0;
    Node* curr = zero;
    for (int i = 0; i < 3; ++i) {
        curr = Node::forwards(curr, 1000 % n);
        sum += curr->val;
    }
    cout << sum << "\n";
}

int main() {
    string filename = "../input.txt";
    Reader reader(filename);
    part1(reader);

    reader = Reader(filename);
    part2(reader);
    return 0;
}