import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// Ctrl+Q Quick Doc view
// Ctrl+Shift+i Quick Defenition View

public class CollectionsTest {
    public static void main(String[] args) {

    }
}

enum Color {
    RED(1), GREEN(2), BLUE(3), YELLOW(4), WHITE(5), BLACK(6);
    private int index;

    Color(int index) {
        this.index = index;
    }

    int getIndex() {
        return index;
    }
}

class TestArryList {
    public static void main(String[] args) {
        List<String> ls = Arrays.asList("eni beni raba".split(" "));
        ls.sort(Comparator.reverseOrder());
        System.out.println(ls);
        List<Color> lc = new ArrayList<>();
        lc.add(Color.RED);
        lc.add(Color.GREEN);
        lc.add(Color.BLUE);
        lc.sort(new Comparator<Color>() {
            @Override
            public int compare(Color o1, Color o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        System.out.println(lc);
        lc.sort(new Comparator<Color>() {
            @Override
            public int compare(Color o1, Color o2) {
                return Integer.compare(o1.getIndex(), o2.getIndex());
            }
        });
        System.out.println(lc);
        lc.sort(Comparator.naturalOrder());
        Collections.sort(lc, null);
        System.out.println(lc);
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
            Arrays.sort(sa2, Comparator.reverseOrder());
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

    static class SearchList {
        public static void main(String[] args) {
            List<Integer> li = Arrays.asList(9999, 10, 55, 28, 1);
            Collections.sort(li, null);
            System.out.println(li);
            System.out.println(Collections.binarySearch(li, 55));
            List<String> ls = Arrays.stream(Object.class.getMethods()).map(m -> m.getName()).distinct().collect(Collectors.toList());
            ls.sort(Comparator.naturalOrder());
            System.out.println(ls);
            System.out.println(Collections.binarySearch(ls, "hashCode"));
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