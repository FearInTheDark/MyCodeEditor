#include <iostream>
using namespace std;

int main() {
    float bill = 88.67, tax, tip;
    tax = bill * 0.0675;
    tip = (bill + tax) * 20/100;
    cout << "Total bill : " << bill + tax + tip;
}