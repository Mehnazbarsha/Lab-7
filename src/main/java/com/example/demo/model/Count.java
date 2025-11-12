package com.example.demo.model;

public class Count {
    // keep public field used by some tests
    public int count;

    public Count() {
        this.count = 0;
    }

    public Count(int count) {
        this.count = count;
    }

    // existing getter (keeps current tests working)
    public int getCount() {
        return count;
    }

    // add setter so controllers can call setCount(...)
    public void setCount(int count) {
        this.count = count;
    }

};

