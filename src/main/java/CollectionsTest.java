import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// Ctrl+Q Quick Doc view  ^+J
// Ctrl+Shift+i Quick Defenition View option+space

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

public class CollectionsTest {
    public static void main(String[] args) {

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
        Iterator<Color> iter = lc.iterator();
        while (iter.hasNext()) {
            Color next = iter.next();
            System.out.println(next);
        }
        List<MyObject> mo = new ArrayList<>();
        mo.add(new MyObject(1));
        mo.add(new MyObject(2));
        mo.add(new MyObject(3));
    }
}

class TestHashSet {
    public static void main(String[] args) {
        Set<MyObject> hs = new HashSet<>();
        hs.add(new MyObject(1));
        hs.add(new MyObject(2));
        hs.add(new MyObject(3));
        hs.add(new MyObject(4));
        hs.add(new MyObject(1));
        hs.add(null);
        hs.add(null);
        System.out.println(hs.size() + " " + hs);
        System.out.println(hs.contains(new MyObject(2)));
        System.out.println(hs.contains(new MyObject(5)));
        Iterator<MyObject> it = hs.iterator();
        while (it.hasNext()) {
            MyObject o = it.next();
            System.out.println(o);
        }
    }
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
        System.out.println(cs.contains('b'));
    }

}

class TestHashMap {
    public static void main(String[] args) {
        Map<String, List<MyObject>> hm = new HashMap<>();
        hm.put("1-3", new ArrayList<MyObject>(Arrays.asList(new MyObject(1), new MyObject(2), new MyObject(3))));
        hm.put("1-3", new ArrayList<MyObject>(Arrays.asList(new MyObject(1), new MyObject(3))));
        hm.put("4", new ArrayList<MyObject>(Arrays.asList(new MyObject(4))));
        hm.put("empty", null);
        hm.put("empty_array", new ArrayList<MyObject>());
        hm.put(null, null);
        System.out.println(hm.size() + " " + hm);
        System.out.println(hm.get("1-3"));
        Collection<List<MyObject>> val = hm.values();
        Set<String> ks = hm.keySet();
        Set<Map.Entry<String, List<MyObject>>> es = hm.entrySet();
        for (List<MyObject> o : val)
            System.out.println("Value " + o);
        for (String s : ks) System.out.println("Key " + s);
        for (Map.Entry<String, List<MyObject>> entry : es)
            System.out.println("Entry " + entry.getKey() + ":" + entry.getValue());
    }
}

class TestTreeMap {
    public static void main(String[] args) {
        new TestSortAndSearch().new Nested();
        new TestSortAndSearch.SortList();
        String[] s1 = new String[5];
        s1[0] = "1";
        s1[1] = "2";
        s1[4] = "4";
        String[] s2 = {"1", "2", null, "4", ""};
        Map<Integer, List<String>> tm = new TreeMap<>();
        tm.put(1, new ArrayList<>(Arrays.asList("a d".split(" "))));
        tm.put(1, new ArrayList<>(Arrays.asList("a b c d".split(" "))));
        tm.put(2, new ArrayList<>());
        tm.put(0, null);
        tm.put(3, new ArrayList<>(Arrays.asList(s1)));
        System.out.println(tm);
        tm.replace(3, new ArrayList<>(Arrays.asList(s2)));
        for (Map.Entry entry : tm.entrySet())
            System.out.println(entry);
        System.out.println(tm.get(1).get(1).toUpperCase());

        List<String> list = Arrays.stream(Object.class.getMethods()).map(Method::getName).distinct().collect(Collectors.toList());
        Map<Integer, String> m = list.stream().collect(Collectors.toMap(String::hashCode, Function.identity()));
        System.out.println(m);
        TreeMap<Integer, Set<String>> tm2 = list.stream().collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.mapping(Function.identity(), Collectors.toSet())));
        System.out.println(tm2);
    }

}

class TestSortAndSearch {
    String id = "MainClass";

    public static void main(String[] args) {
        TestSortAndSearch t = new TestSortAndSearch();
        new TestSortAndSearch.SortList();
        System.out.println(new SortList().sl_id);
        System.out.println(new TestSortAndSearch().new Nested().get_id());
        System.out.println(t.getLocal().get_id());
    }

    public Nested getLocal() {
        class Local extends Nested {
            String local_id = "LocalClass";

            @Override
            String get_id() {
                return local_id;
            }
        }
        return new Local();
    }

    static class SortArrays {
        public static void main(String[] args) {
            int[] ia = {5, 4, 3, 2, 1};
            String id = new TestSortAndSearch().id;
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
        String sl_id = "StaticClass";

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

    class Nested {
        String nested_id = "NestedClass";

        String get_id() {
            return nested_id;
        }
    }
}


class MyObject implements Comparable<MyObject> {
    double d;
    char c;
    private int i;
    private String s = "MyObject";

    public MyObject(int id) {
        this.i = id;
    }

    public MyObject() {
        i = 1;
    }

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

    @Override
    public String toString() {
        return s + " ID:" + i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyObject myObject = (MyObject) o;
        return i == myObject.i &&
                Objects.equals(s, myObject.s);
    }

    @Override
    public int hashCode() {
        return (7 * i) ^ s.hashCode();
    }
}