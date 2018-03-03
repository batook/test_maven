import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T1 {
    String[] array;
    List<String> list;

    {
        array = "Ene bene raba bene raba!".split("\\s+");
        list = Arrays.asList(array);
    }

    public static void main(String[] args) {
        new Thread(() -> System.out.println(Thread.currentThread())).start();
        new T1().test();
    }

    void test() {
        long startTime = System.nanoTime();
        // ... the code being measured ...
        double estimatedTime = (double) (System.nanoTime() - startTime) / 1_000_000_000;
        System.out.println(new DecimalFormat("#.##########").format(estimatedTime));

        Arrays.asList("a b c d".split("\\s+")).forEach(System.out::println);
        Arrays.asList(Pattern.compile("\\s+").split("a b c d")).forEach(System.out::println);
        System.out.println(Stream.of("a", "b", "c").collect(Collectors.joining(",")));
        System.out.println(Stream.of("a", "b", "c").reduce("concat: ", String::concat));
        System.out.println(Stream.of(1, 2, 3, 4, 5).reduce(1, (i, j) -> i * j));
        new Random().ints(1, 10).limit(3).forEach(System.out::println);
        String text = "a r b k c d se f g a d f s s f d s ft gh f ws w f v x s g h d h j j k f sd j e wed a d f";
        List<String> list = Arrays.asList(text.split("\\s+"));
        Map<String, Long> m = list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        m.forEach((k, v) -> System.out.println(k + ":" + v));
        Set<String> set = new HashSet<>(Arrays.asList(text.split("\\s+")));
        for (String e : set)
            System.out.println(e + " " + Collections.frequency(list, e));
        System.out.println(Stream.of('w', 'o', 'l', 'f').parallel().reduce("", (c, s1) -> c + s1, (a, b) -> a + b));
        System.out.println(Stream.of("w", "o", "l", "f").reduce("", String::concat));
    }

    static class TestList {
        public static void main(String[] args) {
            T1 t = new T1();
            System.out.println(Arrays.toString(t.array));
            List<String> al = new ArrayList<>(t.list);
            Collections.reverse(al);
            al.add("a");
            al.add("b");
            al.add(null);
            al.add(null);
            System.out.println(al);
            al.addAll(new ArrayList<>(Arrays.asList(t.array)));
            al.remove("b");
            al.add(3, "X");
            System.out.println(al);
            for (Iterator<String> iterator = al.iterator(); iterator.hasNext(); ) {
                System.out.println(iterator.next());
            }
            Iterator<String> iterator = al.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
            System.out.println(al);
            LinkedList<String> ll = new LinkedList<>();
            ll.addAll(t.list);
            ll.add("a");
            ll.add(3, "X");
            ll.addLast("Z");
            ll.getFirst();
            System.out.println(ll);
            ll.offer("Z");
            ll.push("A");
            Collections.sort(ll);
            System.out.println(ll);
            ll.remove(3);
            ll.poll();
            ll.pop();
            ll.removeLast();
            try {
                iterator = ll.descendingIterator();
                while (iterator.hasNext()) {
                    iterator.next();
                    iterator.remove();
                }
            } catch (ConcurrentModificationException | IllegalStateException e) {
                throw e;
            } finally {
                System.out.println(ll);
            }
        }
    }

    static class TestSet {
        public static void main(String[] args) {
            T1 t = new T1();
            Set<String> hs = new HashSet<>(t.list);
            hs.addAll(t.list);
            hs.add("A");
            hs.add("B");
            hs.add(null);
            System.out.println(hs);
            if (hs.contains("A")) System.out.println("Found A");
            hs.removeIf(s -> hs.contains("A"));
            System.out.println(hs);
            hs.remove("B");
            hs.forEach(s -> System.out.println(s + " " + s.hashCode()));
            Iterator<String> iterator = hs.iterator();
            while (iterator.hasNext()) {
                String s = iterator.next();
                iterator.remove();
            }
            Set<String> ts = new TreeSet<>(Comparator.reverseOrder());
            ts.addAll(t.list);
            ts.add("A");
            ts.add("Z");
            ts.add("QWERRTY");
            //ts.add(null);
            for (String s : ts) {
                System.out.println(s + " " + s.hashCode());
            }
            for (Iterator<String> i = ts.iterator(); i.hasNext(); ) {
                String s = i.next();
                System.out.println(s + " " + s.hashCode());
            }
            ts.removeAll(Arrays.asList("A", "Z"));
            System.out.println(ts);
            ts.removeIf(s -> s.length() > 4);
            ts.removeIf(s -> true);
            System.out.println(ts);
            Set<String> lhs = new LinkedHashSet<>(t.list);
            lhs.add("A");
            lhs.add("B");
            System.out.println(lhs);
            lhs.retainAll(t.list);
            lhs.removeAll(Collections.singleton("bene"));
            System.out.println(lhs);
            if (lhs.contains("ene")) System.out.println("Found ene");

        }
    }

    static class TestQueue {
        public static void main(String[] args) {
            T1 t = new T1();
            ArrayDeque<String> qs = new ArrayDeque<>(t.list);
            qs.addAll(t.list);
            Collections.addAll(qs, t.array);
            qs.offer("A");
            qs.offer("Z");
            //qs.offer(null);
            System.out.println(qs);
            System.out.println(qs.poll());
            System.out.println(qs.poll());
            qs.clear();
            ArrayDeque q = new ArrayDeque();
            q.add("A");
            q.add(1);
            q.offer("B");
            q.offer("A");
            System.out.println(q.poll());
            System.out.println(q.poll());
            System.out.println(q.poll());
            System.out.println(q.poll());
            System.out.println(q);
            q.push("A");
            q.push(1);
            q.push("B");
            q.push("A");
            System.out.println(q.poll());
            System.out.println(q.poll());
            System.out.println(q.poll());
            System.out.println(q.poll());
            System.out.println(q);
        }
    }

    static class TestMap {
        public static void main(String[] args) {
            T1 t = new T1();
            Map<String, Integer> hm = new HashMap<>();
            for (int i = 0; i <= t.list.size() - 1; i++)
                hm.put(t.list.get(i), i);
            hm.put(null, null);
            System.out.println(hm);
            hm.replace("ene", 1);
            System.out.println(hm.get("ene"));
            Iterator<Map.Entry<String, Integer>> iterator = hm.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Integer> ks = iterator.next();
                System.out.println(ks.getKey() + ": " + ks.getValue());
            }
            for (Map.Entry<String, Integer> ks : hm.entrySet()) {
                System.out.println(ks.getKey() + ": " + ks.getValue());
            }
            Map<String, Integer> lhm = new LinkedHashMap<>(hm);
            System.out.println(lhm);
            hm.remove(null);
            Map<String, Integer> tm = new TreeMap<>(hm);
            System.out.println(tm);
            for (Iterator<Map.Entry<String, Integer>> it = tm.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Integer> ks = it.next();
                System.out.println(ks);
            }
            System.out.println(tm.values());
            System.out.println(tm.keySet());

            Map<String, Integer> hm2 = new HashMap<>(hm);
            System.out.println(hm2);
            Iterator<Integer> i2 = hm2.values().iterator();
            while (i2.hasNext()) {
                i2.next();
                i2.remove();
            }
            System.out.println(hm2);

            Map<String, Integer> hm3 = new HashMap<>(hm);
            System.out.println(hm3);
            Iterator<String> i3 = hm3.keySet().iterator();
            while (i3.hasNext()) {
                i3.next();
                i3.remove();
            }
            System.out.println(hm3);

            hm3.putAll(hm);
            System.out.println(hm3);
            hm3.values().removeAll(Collections.singleton(3));
            hm3.values().remove(1);
            System.out.println(hm3);

            NavigableMap<String, Integer> nm = new TreeMap<>(hm);
            nm.putAll(hm);
            nm.put("A", 0);
            System.out.println(nm);
            System.out.println(nm.floorKey("A"));
            System.out.println(nm.ceilingKey("A"));
            System.out.println(nm.tailMap("e"));
            System.out.println(nm.headMap("e"));
            System.out.println(nm.pollLastEntry());

            Map<Integer, Object> hms = Stream.of(t.array)
                    .collect(Collectors.toMap(String::length, k -> k, (s1, s2) -> s1 + "," + s2));
            System.out.println(hms.getClass() + " " + hms);

            TreeMap<Integer, String> map = t.list.stream()
                    .collect(Collectors.toMap(String::length, v -> v, (x, y) -> x + "," + y, TreeMap::new));
            System.out.println(map.getClass() + " " + map);
        }
    }
}

class TestEnum {
    public static void main(String[] args) {
        //Get all enums
        for (Environment env : Environment.values()) {
            System.out.println(env.name() + " :: " + env.getUrl());
        }
        System.out.println(Environment.PROD.getUrl());
        System.out.println(Environment.valueOf("SIT").getUrl());
        System.out.println(Environment.get("https://sit.domain.com:2019/"));
        assert "SIT".equals(Environment.get("https://sit.domain.com:2019/").toString());
    }

    enum Environment {
        PROD("https://prod.domain.com:1088/"),
        SIT("https://sit.domain.com:2019/"),
        CIT("https://cit.domain.com:8080/"),
        DEV("https://dev.domain.com:21323/");

        //****** Reverse Lookup Implementation************//
        //Lookup table
        private static final Map<String, Environment> lookup = new HashMap<>();

        //Populate the lookup table on loading time
        static {
            for (Environment env : Environment.values()) {
                lookup.put(env.getUrl(), env);
            }
        }

        private String url;

        Environment(String envUrl) {
            this.url = envUrl;
        }

        //This method can be used for reverse lookup purpose
        public static Environment get(String url) {
            return lookup.get(url);
        }

        public String getUrl() {
            return url;
        }
    }
}

class ArrayReversalDemo {

    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7};
        reverse(numbers);
        reverseString("Ene bene raba!");
        reverseByArray("Ene bene raba!");
    }

    /**
     * reverse the given array in place * @param input
     */
    public static void reverse(int[] input) {
        System.out.println("original array : " + Arrays.toString(input));
        // handling null, empty and one element array
        if (input == null || input.length <= 1) {
            return;
        }
        for (int i = 0; i < input.length / 2; i++) {
            int temp = input[i];
            // swap numbers
            input[i] = input[input.length - 1 - i];
            input[input.length - 1 - i] = temp;
        }
        System.out.println("reversed array : " + Arrays.toString(input));
    }

    static void reverseString(String st) {
        System.out.println(st);
        char[] c = st.toCharArray();
        for (int i = 0; i < c.length / 2; i++) {
            char tmp = c[i];
            c[i] = c[(c.length - 1) - i];
            c[(c.length - 1) - i] = tmp;
        }
        System.out.println(new String(c));
        String s = "";
        for (int i = st.length() - 1; i >= 0; i--) {
            s += st.charAt(i);
        }
        System.out.println(s);
        System.out.println(new StringBuilder(st).reverse());
    }

    static void reverseByArray(String s) {
        char[] src = s.toCharArray();
        char[] tgt = new char[src.length];
        for (int i = 0; i < src.length; i++) {
            tgt[(src.length - 1) - i] = src[i];
        }
        System.out.println(new String(tgt));
    }
}

class TestSets {
    List<Integer> i1, i2;

    public static void main(String[] args) {
        TestSets t = new TestSets();
        t.i1 = Arrays.asList(1, 1, 2, 3, 4, 5, 7);
        t.i2 = Arrays.asList(4, 5, 6, 7, 6);
        Set<Integer> union = new HashSet<>(t.i1);
        union.addAll(t.i2);
        System.out.println(union);
        Set<Integer> intersect = new HashSet<>(t.i1);
        intersect.retainAll(t.i2);
        System.out.println(intersect);
        TestSets.TestLambda t2 = t.new TestLambda();
        t2.test();
    }

    class TestLambda {
        public void test() {
            System.out.println(this.toString());
            System.out.println(TestSets.this.toString());
            System.out.println("union " + Stream.concat(TestSets.this.i1.stream(), TestSets.this.i2.stream())
                    .collect(Collectors.toSet()));
            System.out.println("intersect " + i1.stream().filter(i2::contains).collect(Collectors.toSet()));
        }
    }
}

class CompareTest implements Comparable<Integer> {

    Integer i;

    public static void main(String[] args) {
        for (Method m : CompareTest.class.getMethods()) {
            if (m.getName().equals("compareTo")) {
                System.out.println(m.toGenericString());
            }
        }
    }

    @Override
    public int compareTo(Integer o) {
        return i.compareTo(o);
        //return i.intValue() < o.intValue() ? -1 : i.intValue() == o.intValue() ? 0 : 1;
    }

}

class Dummy {
    public static void main(String[] args) {
        String s = "You are an idiot! Idiot!!!";
        Map<Character, Integer> m = new HashMap<>();
        for (int i = 0; i <= s.length() - 1; i++) {
            int c = m.get(s.charAt(i)) == null ? 1 : m.get(s.charAt(i)) + 1;
            m.put(s.charAt(i), c);
        }
        System.out.println(m);

        List<String> list = Arrays.asList(s.split(""));
        Map<String, Long> m2 = list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        m2.forEach((k, v) -> System.out.println(k + ":" + v));

        Set<String> set = new HashSet<>(Arrays.asList(s.split("")));
        for (String e : set)
            System.out.println(e + " " + Collections.frequency(list, e));
    }

}

class Alfa {
    protected Alfa() {
        System.out.println("Alfa");
    }
}

class subAlfa extends Alfa {
    private subAlfa() {

    }
}

class Beta extends Alfa {
    private Beta() {
        System.out.println("Beta");
    }

    public static void main(String[] args) {
        Beta b = new Beta();
    }

}



