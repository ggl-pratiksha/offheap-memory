import java.util.HashMap;

public class OnHeapGroupBy {

    HashMap<Integer, Long> data;
    /*HashMap<Integer, Long> unique;
    HashMap<Integer, Long> duplicate;*/

    public OnHeapGroupBy() {
        data = new HashMap<Integer, Long>();
        /*unique = new HashMap<>();
        duplicate = new HashMap<>();*/
    }

    void groupBy(int key, long currentValue) {
        if(!data.containsKey(key)) {
            data.put(key, currentValue);
            //unique.put(key, currentValue);
        } else {
            long oldValue = data.get(key);
            long updateValue = oldValue + currentValue;
            data.put(key, updateValue);
            //duplicate.put(key, updateValue);
        }
    }

    public static void main(String args[]) {
        long startTime = System.currentTimeMillis();
        OnHeapGroupBy onHeapGroupBy = new OnHeapGroupBy();
        for(int i=0; i<1000000; i++)
            onHeapGroupBy.groupBy(i, i*10);
        int count = 0;
        for(int i=0; i<1000000; i+=30) {
            onHeapGroupBy.groupBy(i, 10);
            count++;
        }
        System.out.println("\n time taken : " + (System.currentTimeMillis() - startTime));
        System.out.println("current data size : " + onHeapGroupBy.data.size() + "\tDuplicate : " + count);
        /*System.out.println("\nunique : \t" + onHeapGroupBy.unique);
        System.out.println("duplicate : " + onHeapGroupBy.duplicate);
        System.out.println(count + " == " + onHeapGroupBy.duplicate.size());*/

    }
}
