import java.util.Arrays;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

public class Test {

    public static void main(String[] args) {
        System.out.println(Arrays.asList("w", "o", "l", "f", "s").parallelStream().
                reduce("X", (s1, s2) -> {
                    System.out.println("accum " + s1 + s2);
                    return s1 + s2;
                }, (s3, s4) -> {
                    System.out.println("combiner " + s3 + s4);
                    return s3 + s4;
                }));

        Stream<String> stream = Stream.of("w", "o", "l", "f").parallel();
        SortedSet<String> set = stream.collect(ConcurrentSkipListSet::new, (c, s) -> {
            System.out.println("add " + s);
            c.add(s);
        }, (c1, c2) -> {
            System.out.println(c1 + " addAll " + c2);
            c1.addAll(c2);
        });
        System.out.println(set);
    }
}

