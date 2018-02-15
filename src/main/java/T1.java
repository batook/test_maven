import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T1 {
    String[] array;
    List<String> list;

    {
        array = "ene bene raba bene raba".split("\\s+");
        list = Arrays.asList(array);
    }

    public static void main(String[] args) {
        new T1().test();
    }

    void test() {
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
            } catch (ConcurrentModificationException e) {
                throw e;
            } catch (IllegalStateException e) {
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
            System.out.println(lhs);

        }
    }

    static class TestQueue {
        public static void main(String[] args) {
            T1 t = new T1();
            Queue<String> qs = new ArrayDeque<>(t.list);
            qs.addAll(t.list);
            qs.offer("A");
            qs.offer("Z");
            qs.offer(null);
            System.out.println(qs);
            System.out.println(qs.poll());
            System.out.println(qs.poll());
            qs.clear();
            System.out.println(qs.poll());
            System.out.println(qs.poll());
        }
    }

    static class TestMap {
        public static void main(String[] args) {
            T1 t = new T1();
            Map<String, Integer> hm = new HashMap();
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
