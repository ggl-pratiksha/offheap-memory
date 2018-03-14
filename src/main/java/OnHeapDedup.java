import java.util.HashSet;

public class OnHeapDedup {

    public static final long BLOCK_SIZE = 1024;

    HashSet<Integer> data;
    /*ArrayList<Integer> unique;
    ArrayList<Integer> duplicate;*/

    public OnHeapDedup() {
        data = new HashSet<Integer>();
       /* unique = new ArrayList<>();
        duplicate = new ArrayList<>();*/
    }

    void dedup(int key) {
        if(!exists(key)) {
            addKey(key);
            //unique.add(key);
        } else {
            //duplicate.add(key);
        }
    }
    private boolean addKey(int value) {
        return data.add(value);
    }

    private boolean exists(int value) {
        return  data.contains(value);
    }

    public static void main(String args[]) {
        long startTime = System.currentTimeMillis();
        OnHeapDedup dedupChecker = new OnHeapDedup();
        int count = 0;
        for(int j=0; j<8; j++) {
            for (int i = 0; i < 10000000; i++)
                dedupChecker.dedup(i);
            for (int i = 0; i < 10000000; i += 30) {
                dedupChecker.dedup(i);
                count++;
            }
        }
        System.out.println("\n time taken : " + (System.currentTimeMillis() - startTime));
        System.out.println("current data size : " + dedupChecker.data.size() + "\nCount : " + count);
        /*System.out.println("\nunique : \t" + dedupChecker.unique);
        System.out.println("duplicate : " + dedupChecker.duplicate);
        System.out.println(count + " == " + dedupChecker.duplicate.size());*/


    }
}
