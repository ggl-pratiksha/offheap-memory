package com.bits.heap.collection;


import com.bits.heap.utils.OffHeapFS;

/**
 * Stores information of memory allocated in off heap.
 */

public class OffheapMemoryBlock {

    private long baseAddress;
    private long length;

    public long getLength() {
        return length;
    }

    public OffheapMemoryBlock(long pBaseAddress, long pLength) {
        this.baseAddress = pBaseAddress;
        this.length = pLength;
    }

    public long getBaseAddress() {

        return baseAddress;
    }

    public long getEndAddress() {
        return baseAddress + length;
    }

    public long getOffsetAddress(long offset) {
        return baseAddress + offset;
    }

    public int get(long address) {
        return OffHeapFS.getInt(address);
    }

    @Override
    public String toString() {
        return "MemoryInfo{" +
                "baseAddress=" + baseAddress +
                ", length=" + length +
                ", end=" + (baseAddress + length) +
                '}';
    }

}
