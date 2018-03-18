package com.bits.heap;

import com.bits.heap.collection.OffHeapCollection;
import com.bits.heap.utils.OffHeapFS;

/**
 * For now key and value are stored consecutively in mermoy block.
 * eg. if key address is 1000 then value address is 1004.
 */
public class OffHeapMap extends OffHeapCollection {

    public OffHeapMap() {
        super();
    }

    /**
     * If passed in key is already stored in offheap, updates correspoding value.
     * Else adds new entry in offheap.
     * @param key
     * @param currentValue
     * @return updated value
     */
    public long put(int key, long currentValue) {
        long updatedValue = currentValue;
        long keyAddress = addresses.addressOf(key);
        if(keyAddress == -1) {
            keyAddress = putInMemoryBlock(key, currentValue);
            putAddress(key, keyAddress);
        } else {
            updatedValue = updateValue(keyAddress, currentValue);
        }
        return updatedValue;
    }

    /**
     * Puts passed in key and value of group by in memory blcok and increment offset accordingly.
     * @param key
     * @param value
     * @return
     */
    private long putInMemoryBlock(int key, long value) {
        long keyAddress = -1;
        if(memoryBlockOffset + 12 >= memoryBlocks.get(currentMemoryBlock).getLength())
            setupOffheapMemoryBlock();
        keyAddress = memoryBlocks.get(currentMemoryBlock).getOffsetAddress(memoryBlockOffset);
        OffHeapFS.putInt(keyAddress, key);
        memoryBlockOffset += 4;
        OffHeapFS.putLong(valueAddress(keyAddress), value);
        memoryBlockOffset += 8;
        totalElements++;
        return keyAddress;
    }

    /**
     * Checks if key of group by (eg. product id) is present or not.
     * @param key
     * @return
     * If key is already in offset return keyaddress, else return -1.
     */
    @Override
    public boolean containsKey(int key) {
        return addresses.addressOf(key) == -1? false : true;
    }

    /**
     * Retrieves value stored at given keyaddress, add passed in currentValue to stored value
     * and then stores updated value to same keyaddress.
     * @param keyAddress : offheap address at which (group by) key is stored
     * @param currentValue : current value.
     * @return updatedValue
     */
    private long updateValue(long keyAddress, long currentValue) {
        long storedValue = OffHeapFS.getLong(valueAddress(keyAddress));
        long updatedValue = storedValue + currentValue;
        OffHeapFS.putLong(valueAddress(keyAddress), updatedValue);
        return  updatedValue;
    }

    private long valueAddress(long keyAddress) {return (keyAddress + 4);}

    @Override
    public String toString() {
        return "current HashAddress size : " + this.addresses.getSizeInBytes() + "\t Length : " + this.addresses.getOffheapMemoryBlock().getLength() +
                "\nMemory Info Length : " + this.memoryBlocks.size() + "\tcurrent offset : " + this.memoryBlockOffset +
                "\nTotal elements : " + this.totalElements;
    }

    public static void main(String args[]) {
        long startTime = System.currentTimeMillis();
        OffHeapMap groupByObj = new OffHeapMap();
        for(int i=0; i<1000; i++)
            groupByObj.put(i, i*10);

        int count = 0;
        for(int i=0; i<1000; i+=30) {
            groupByObj.put(i, 10);
            count++;
        }
        System.out.println("\nTime taken : " + (System.currentTimeMillis() - startTime));
        System.out.println(groupByObj + "\t + \t Duplicate : " + count);
        groupByObj.free();
        /*System.out.println("\nUnique: " + groupByObj.unique);
        System.out.println("Duplicate: " + groupByObj.duplicate);
        System.out.println(count + " == " + groupByObj.duplicate.size());*/
    }

}

