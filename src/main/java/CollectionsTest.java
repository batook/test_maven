import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;

public class CollectionsTest {
    public static void main(String[] args) {

    }
}

enum Color {RED, GREEN, BLUE, YELLOW, WHITE, BLACK}

class TestArryList {

}

class TestLinkedList {

}

class TestHashSet {

}

class TestTreeSet {

}

class TestHashMap {

}

class TestLinkedHashMap {

}

class TestTreeMap {

}

// Ctrl+Q Quick Doc view
// Ctrl+Shift+i Quick Defenition View
class TestSortAndSearch {
    static class SortArrays {
        public static void main(String[] args) {
            int[] ia = {5, 4, 3, 2, 1};
            Arrays.sort(ia);
            for (int i : ia) {
                System.out.println(i);
            }
            String[] sa = "c b a".split(" ");
            Arrays.sort(sa);
            System.out.println(Arrays.asList(sa));

            String[] sa2 = Pattern.compile("").split("abcdef");
            Arrays.sort(sa2, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o2.compareTo(o1);
                }
            });
            System.out.println(Arrays.asList(sa2));
        }
    }

}

class MyObject implements Comparable<MyObject> {
    double d;
    int i;
    String s;
    char c;

    @Override
    public int compareTo(MyObject o) {
        int result;
        result = Double.compare(d, o.d);
        if (result != 0) return result;
        result = Integer.compare(i, o.i);
        if (result != 0) return result;
        result = s.compareTo(o.s);
        if (result != 0) return result;
        result = Character.compare(c, o.c);
        return result;
    }
}