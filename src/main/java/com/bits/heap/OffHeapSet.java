package com.bits.heap;

import com.bits.heap.collection.OffHeapCollection;
import com.bits.heap.utils.OffHeapFS;

public class OffHeapSet extends OffHeapCollection {


    public OffHeapSet() {
        super();
    }

    /**
     * Checks if key is already stored. If not, key is stored.
     * @param key
     * @return
     * true: if duplicate
     * false : if unique
     */
    public boolean add(int key) {
        if(!containsKey(key)) {
            long valueAddress = putInMemoryBlock(key);
            putAddress(key, valueAddress);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsKey(int key) {
       return  addresses.addressOf(key) == -1? false : true;
    }

    @Override
    public String toString() {
        return "current HashAddress size : " + this.addresses.getSizeInBytes() + "\t Length : " + this.addresses.getOffheapMemoryBlock().getLength() +
                "\nMemory Info Length : " + this.memoryBlocks.size() + "\tcurrent offset : " + this.memoryBlockOffset +
                "\nTotal elements : " + this.totalElements;
    }

    private long putInMemoryBlock(int value) {
        long address = -1;
        if(memoryBlockOffset >= currentMermoryBlockLength())
            setupOffheapMemoryBlock();
        address = currentIndexAddress();
        OffHeapFS.putInt(address, value);
        memoryBlockOffset += 4;
        totalElements++;
        return address;
    }

    public static void main(String args[]) {
        long startTime = System.currentTimeMillis();
        OffHeapSet offHeapDedup = new OffHeapSet();
        int count = 0;
        for(int j=0; j<8; j++) {
            for (int i = 0; i < 10000; i++)
                offHeapDedup.add(i);
            for (int i = 0; i < 10000; i += 30) {
                offHeapDedup.add(i);
                count++;
            }
        }
        System.out.println("\nTime taken : " + (System.currentTimeMillis() - startTime));
        System.out.println(offHeapDedup + "\t + \t Duplicates : " + count);
        offHeapDedup.free();

        /*System.out.println("\nunique : \t" + offHeapDedup.unique);
        System.out.println("duplicate : " + offHeapDedup.duplicate);
        System.out.println(count + " == " + offHeapDedup.duplicate.size());*/

    }


}
