package com.bits.heap.collection;


import com.bits.heap.utils.OffHeapFS;

import java.util.ArrayList;

/**
 * Common base class for offheap collections.
 */
public abstract class OffHeapCollection {

    public static final long BLOCK_SIZE = 1024;

    protected ArrayList<OffheapMemoryBlock> memoryBlocks;
    protected AddressHashSet addresses;

    //current memory block index
    protected int memoryBlockOffset;

    //offset of current memory block
    protected int currentMemoryBlock;
    protected long totalElements;

    public OffHeapCollection() {
        addresses = new AddressHashSet(BLOCK_SIZE);
        memoryBlocks = new ArrayList<OffheapMemoryBlock>();
        currentMemoryBlock = -1;
        totalElements = 0;
        setupOffheapMemoryBlock();
    }

    /**Allocate offheap memory and add it to memory block list.
     * Memory block created in this method will become current memory block.
     */
    public void setupOffheapMemoryBlock() {
        long baseAddress = OffHeapFS.allocateMemory(BLOCK_SIZE);
        OffHeapFS.setMemory(baseAddress, BLOCK_SIZE, (byte)-1);
        OffheapMemoryBlock offheapMemoryBlock = new OffheapMemoryBlock(baseAddress, BLOCK_SIZE);
        memoryBlocks.add(offheapMemoryBlock);
        memoryBlockOffset = 0;
        currentMemoryBlock++;
    }

    /**
     * Puts offheap address of key (eg. product_id) in offheap memory.
     * @param key
     * @param keyAddress
     * @return
     */
    public boolean putAddress(int key, long keyAddress){
        return addresses.put(key, keyAddress);
    };

    // returns current offset of current memory block
    public long currentIndexAddress() {
        return memoryBlocks.get(currentMemoryBlock).getBaseAddress() + memoryBlockOffset;
    }

    public long currentMermoryBlockLength() {
        return memoryBlocks.get(currentMemoryBlock).getLength();
    }

    public void free() {
        for(int i = 0; i< memoryBlocks.size(); i++) {
            OffHeapFS.freeMemory(memoryBlocks.get(i).getBaseAddress());
        }
        OffHeapFS.freeMemory(addresses.getOffheapMemoryBlock().getBaseAddress());
    }

    public void clear() {
        for(int i = 1; i< memoryBlocks.size(); i++) {
            OffHeapFS.freeMemory(memoryBlocks.get(i).getBaseAddress());
        }
        currentMemoryBlock = 0;
        memoryBlockOffset = 0;
        OffHeapFS.setMemory(addresses.getOffheapMemoryBlock().getBaseAddress(), addresses.getOffheapMemoryBlock().getLength(), (byte)-1);
    }

    public long size() { return totalElements; }

    abstract public boolean containsKey(int key);

    public static long getBlockSize() {
        return BLOCK_SIZE;
    }

    public ArrayList<OffheapMemoryBlock> getMemoryBlocks() {
        return memoryBlocks;
    }

    public void setMemoryBlocks(ArrayList<OffheapMemoryBlock> memoryBlocks) {
        this.memoryBlocks = memoryBlocks;
    }

    public AddressHashSet getAddresses() {
        return addresses;
    }

    public void setAddresses(AddressHashSet addresses) {
        this.addresses = addresses;
    }

    public int getMemoryBlockOffset() {
        return memoryBlockOffset;
    }

    public void setMemoryBlockOffset(int memoryBlockOffset) {
        this.memoryBlockOffset = memoryBlockOffset;
    }

    public int getCurrentMemoryBlock() {
        return currentMemoryBlock;
    }

    public void setCurrentMemoryBlock(int currentMemoryBlock) {
        this.currentMemoryBlock = currentMemoryBlock;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

}
