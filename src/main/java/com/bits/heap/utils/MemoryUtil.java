package com.bits.heap.utils;

public class MemoryUtil {
    static final int MAX_CAPACITY = (1 << 29);

    public static int requiredMemory(long size) {
        return Math.max((int) Math.min(MAX_CAPACITY, nextPowerOf2(size)), 64);
    }

    public static long nextPowerOf2(long num) {
        final long highBit = Long.highestOneBit(num);
        return (highBit == num) ? num : highBit << 1;
    }

}
