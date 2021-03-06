package com.bits.heap.collection;


/*mvn archetype:generate -DarchetypeGroupId=org.apache.apex -DarchetypeArtifactId=apex-app-archetype -DarchetypeVersion=3.7.0-SNAPSHOT
 -DgroupId=com.experiment -Dpackage=com.example.offHeap -DartifactId=apex-offHeap -Dversion=1.0-SNAPSHOT*/

import com.bits.heap.hash.Murmur3_x86_32;
import com.bits.heap.utils.MemoryUtil;
import com.bits.heap.utils.OffHeapFS;

/**
 * Long hashset which stores offheap addresses of values stored in offheap.
 */
public class AddressHashSet {

    public final static int SEED = 42;

    public AddressHashSet() {}
    public AddressHashSet(long size) {
        setup(size);
    }

    private OffheapMemoryBlock offheapMemoryBlock;
    private long sizeInBytes;
    private long threshold;
    private int mask;

    /**
     * Calculates hashAddress of key and puts offheap address of key at hashAddress.
     * @param key : key eg. product id
     * @param value : off heap address of key
     * @return
     */
    public boolean put(long key, long value) {
        if(isFull()) rehash();
        boolean isSaved = false;
        long address = getAddress(key);
        long startAddress = address;
        long offheapValue = OffHeapFS.getLong(address);

        // iterate until empty slot is found to put offheap address of key or startAddress it reached.
        while(offheapValue != -1) {
            address = address + 8;
            if(address == startAddress) break;

            //if reached end of memory block, start of beginning and keep search till startAddress.
            if(address >= offheapMemoryBlock.getEndAddress()) {
                address = offheapMemoryBlock.getBaseAddress();
            }
            offheapValue = OffHeapFS.getLong(address);
        }
        if(offheapValue != -1) {
            System.out.println("\nCan not accommodate " + key + " for " + startAddress);
        }
        else {
            OffHeapFS.putLong(address, value);
            isSaved = true;
            sizeInBytes += 8;
        }
        return isSaved;
    }

    /**
     * Calculates hasAddress for value. Retrieves offHeap address stored at hashAddress,
     * Retrieves offheap value stored at offHeapAddress, if offheap value matches passed in value
     * then return offheap address.
     * @param value : key eg. product_id
     * @return offheap address of passed in value
     */
    public long addressOf(long value) {
        long presentAddress = -1;
        long hashAddress = getAddress(value);
        long oldAddress = hashAddress;

        while(true) {
            long address = OffHeapFS.getLong(hashAddress);
            if(address == -1) {
                break;
            }
            int val = OffHeapFS.getInt(address);
            if(val == value) {
                presentAddress = address;
                break;
            }
            hashAddress = hashAddress + 8;
            if(hashAddress == oldAddress) break;
            if(hashAddress >= offheapMemoryBlock.getBaseAddress() + offheapMemoryBlock.getLength()) hashAddress = offheapMemoryBlock.getBaseAddress();
        }
        return presentAddress;
    }

    private boolean isFull() {
        return (sizeInBytes >= threshold);
    }

    /**
     * Rehash hash set.
     */
    private void rehash() {
        OffheapMemoryBlock oldOffheapMemoryBlock = offheapMemoryBlock;
        setup(offheapMemoryBlock.getLength() * 2);
        for(long i = oldOffheapMemoryBlock.getBaseAddress(); i< oldOffheapMemoryBlock.getBaseAddress() + oldOffheapMemoryBlock.getLength(); i+=8 ) {
            long offHeapAddress = OffHeapFS.getLong(i);
            if(offHeapAddress != -1) {
                long offHeapValue = OffHeapFS.getInt(offHeapAddress);
                this.put(offHeapValue, offHeapAddress);
            }
        }
    }

    /**
     * Allocates memory block.
     * @param size
     */
    private void setup(long size) {
        int capacity = MemoryUtil.requiredMemory(size);
        long baseAddress = OffHeapFS.allocateMemory(capacity);
        OffHeapFS.setMemory(baseAddress, capacity, (byte)-1);
        this.offheapMemoryBlock = new OffheapMemoryBlock(baseAddress, capacity);
        mask = (capacity/8) - 1;
        sizeInBytes = 0;
        threshold = (offheapMemoryBlock.getLength() * 60)/100;
        //System.out.print(this);
    }

    /**
     * Returns address by calculating hash of value, masking hash to fit it in
     * memory block range.
     * @param value
     * @return
     */
    private long getAddress(long value) {
        int hash = Murmur3_x86_32.hashLong(value, SEED);
        int pos = hash & mask;
        // As each value is long, multiply by 8.
        long address = offheapMemoryBlock.getBaseAddress() + (pos * 8);
        return address;
    }

    @Override
    public String
    toString() {
        return "\nLongHashSet{" +
                "memoryInfo=" + offheapMemoryBlock +
                ", sizeInBytes=" + sizeInBytes +
                ", mask=" + mask +
                ", threshold=" + threshold +
                '}';
    }

    public OffheapMemoryBlock getOffheapMemoryBlock() {
        return offheapMemoryBlock;
    }

    public void setOffheapMemoryBlock(OffheapMemoryBlock offheapMemoryBlock) {
        this.offheapMemoryBlock = offheapMemoryBlock;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public long getThreshold() {
        return threshold;
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }
}