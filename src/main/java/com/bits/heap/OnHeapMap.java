package com.bits.heap;

import java.util.HashMap;

public class OnHeapMap {

    private HashMap<Integer, Long> data;

    /*HashMap<Integer, Long> unique;
    HashMap<Integer, Long> duplicate;*/

    public OnHeapMap() {
        data = new HashMap<Integer, Long>();
        /*unique = new HashMap<>();
        duplicate = new HashMap<>();*/
    }

    public int size(){
        return data.size();
    }

    public long put(int key, long currentValue) {
        long updatedValue = currentValue;
        if(!data.containsKey(key)) {
            data.put(key, currentValue);
            //unique.put(key, currentValue);
        } else {
            long oldValue = data.get(key);
            updatedValue = oldValue + currentValue;
            data.put(key, updatedValue);
            //duplicate.put(key, updateValue);
        }
        return updatedValue;
    }

    public static void main(String args[]) {
        long startTime = System.currentTimeMillis();
        OnHeapMap onHeapGroupBy = new OnHeapMap();
        for(int i=0; i<1000; i++)
            onHeapGroupBy.put(i, i*10);
        int count = 0;
        for(int i=0; i<1000; i+=30) {
            onHeapGroupBy.put(i, 10);
            count++;
        }
        System.out.println("\nTime taken : " + (System.currentTimeMillis() - startTime));
        System.out.println("current data size : " + onHeapGroupBy.data.size() + "\t + \tDuplicate : " + count);
        /*System.out.println("\nunique : \t" + onHeapGroupBy.unique);
        System.out.println("duplicate : " + onHeapGroupBy.duplicate);
        System.out.println(count + " == " + onHeapGroupBy.duplicate.size());*/

    }
}
