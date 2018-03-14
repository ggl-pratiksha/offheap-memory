package utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Takes care of storing and retrieving value from offheap memory.
 */
public class OffHeapFS {

    private static final Unsafe unsafe;

    final static int NO_OF_ELEMENTS_SIZE = 8;

    static  {
        Unsafe _unsafe;
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            _unsafe = (Unsafe) f.get(null);
        } catch (Exception e) {
            _unsafe = null;
        }
        unsafe = _unsafe;
    }

    public static long allocateMemory(long length) throws OutOfMemoryError {
        return unsafe.allocateMemory(length);
    }

    public static Long getLong(long address) {
        return unsafe.getLong(address);
    }

    public static void putLong(long address, long value) {
        unsafe.putLong(address, value);
    }

    public static int getInt(long address) {
        return unsafe.getInt(address);
    }

    public static int getInt(Object object, long offset) { return unsafe.getInt(object, offset); }

    public static void putInt(long address, int value) {
        unsafe.putInt(address, value);
    }

    public static void freeMemory(long address) {
        unsafe.freeMemory(address);
    }

    public static void setMemory(long address, long size, byte value) {
        unsafe.setMemory(address, size, value);
    }

}
