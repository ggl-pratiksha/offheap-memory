package com.bits.heap;

import java.util.HashSet;

public class OnHeapSet {

    HashSet<Integer> data;

    public OnHeapSet() {
        data = new HashSet<Integer>();
    }

    public int size(){
        return data.size();
    }

    public boolean add(int key) {
        if(!exists(key)) {
            addKey(key);
            return true;
        } else {
            return false;
        }
    }
    private boolean addKey(int value) {
        return data.add(value);
    }

    private boolean exists(int value) {
        return  data.contains(value);
    }

    public static void main(String args[]) {
        long startTime = System.currentTimeMillis();
        OnHeapSet dedupChecker = new OnHeapSet();
        int count = 0;
        for(int j=0; j<8; j++) {
            for (int i = 0; i < 1000; i++)
                dedupChecker.add(i);
            for (int i = 0; i < 1000; i += 30) {
                dedupChecker.add(i);
                count++;
            }
        }
        System.out.println("\nTime taken : " + (System.currentTimeMillis() - startTime));
        System.out.println("current data size : " + dedupChecker.data.size() + "\t + \t Duplicate : " + count);
        /*System.out.println("\nunique : \t" + dedupChecker.unique);
        System.out.println("duplicate : " + dedupChecker.duplicate);
        System.out.println(count + " == " + dedupChecker.duplicate.size());*/


    }
}
