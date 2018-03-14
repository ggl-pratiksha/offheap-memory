package collection;

import utils.OffHeapFS;

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

    abstract public boolean containsKey(int key);

}
