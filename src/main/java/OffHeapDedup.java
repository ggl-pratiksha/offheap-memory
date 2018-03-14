import collection.OffHeapCollection;
import utils.OffHeapFS;

public class OffHeapDedup extends OffHeapCollection {

    public OffHeapDedup() {
        super();
    }

    void dedup(int key) {
        if(!containsKey(key)) {
            put(key);
        } else {
        }
    }

    public boolean put(int value) {
        long valueAddress = putInMemoryBlock(value);
        return putAddress(value, valueAddress);
    }

    @Override
    public boolean containsKey(int key) {
       return  addresses.addressOf(key) == -1? false : true;
    }

    public long putInMemoryBlock(int value) {
        long address = -1;
        if(memoryBlockOffset >= currentMermoryBlockLength())
            setupOffheapMemoryBlock();
        address = currentIndexAddress();
        OffHeapFS.putInt(address, value);
        memoryBlockOffset += 4;
        totalElements++;
        return address;
    }

    @Override
    public String toString() {
        return "current HashAddress size : " + this.addresses.getSizeInBytes() + "\t Length : " + this.addresses.getOffheapMemoryBlock().getLength() +
                "\nMemory Info Length : " + this.memoryBlocks.size() + "\tcurrent offset : " + this.memoryBlockOffset +
                "\nTotal elements : " + this.totalElements;
    }

    public static void main(String args[]) {
        long startTime = System.currentTimeMillis();
        OffHeapDedup offHeapDedup = new OffHeapDedup();
        int count = 0;
        for(int j=0; j<8; j++) {
            for (int i = 0; i < 10000000; i++)
                offHeapDedup.dedup(i);
            for (int i = 0; i < 10000000; i += 30) {
                offHeapDedup.dedup(i);
                count++;
            }
        }
        System.out.println("\n time taken : " + (System.currentTimeMillis() - startTime));
        System.out.println(offHeapDedup + "\nCount : " + count);
        offHeapDedup.free();

        /*System.out.println("\nunique : \t" + dedupChecker.unique);
        System.out.println("duplicate : " + dedupChecker.duplicate);
        System.out.println(count + " == " + dedupChecker.duplicate.size());*/

    }


}
