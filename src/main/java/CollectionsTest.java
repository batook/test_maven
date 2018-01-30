import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// Ctrl+Q Quick Doc view
// Ctrl+Shift+i Quick Defenition View

public class CollectionsTest {
    public static void main(String[] args) {

    }
}

enum Color {RED, GREEN, BLUE, YELLOW, WHITE, BLACK}

class TestArryList {
    public static void main(String[] args) {
        List<String> ls = Arrays.asList("eni beni raba".split(" "));
        ls.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println(ls);
    }
}

class TestLinkedList {

}

class TestHashSet {

}

class TestTreeSet {
    public static void main(String[] args) {
        String s = "eni beni raba";
        Set<String> ss = new TreeSet<>(Arrays.asList(s.split(" ")));
        System.out.println(ss);
        Set<Character> cs = new TreeSet<>();
        for (char c : s.toCharArray()) {
            cs.add(c);
        }
        System.out.println(cs);
    }

}

class TestHashMap {

}

class TestLinkedHashMap {

}

class TestTreeMap {

}

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
            Arrays.sort(sa2, Collections.reverseOrder());
            System.out.println(Arrays.asList(sa2));
        }
    }

    static class SortList {
        public static void main(String[] args) {
            List<Integer> il = Arrays.asList(1, 2, 3, 4, 5, -6);
            Collections.reverse(il);
            System.out.println(il);
            Collections.sort(il, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o2.compareTo(o1);
                }
            });
            System.out.println(il);

            List<String> ls = Arrays.asList("Ene, bene, raba!".split(" "));
            Collections.reverse(ls);
            System.out.println(String.join(" ", ls));
            System.out.println(ls.stream().map(String::toUpperCase).collect(Collectors.joining(" ")));

            List<String> ls2 = Arrays.asList("Ene, bene, raba!".split(""));
            Collections.reverse(ls2);
            System.out.println(String.join("", ls2));
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