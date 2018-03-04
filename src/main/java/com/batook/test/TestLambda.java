package com.batook.test;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class TestLambda {
    public static void main(String[] args) {
        Supplier<LocalDateTime> s = LocalDateTime::now;
        System.out.println(s.get());

        Consumer<String> c = System.out::println;
        c.accept("Hi");

        Map<String, Integer> map = new HashMap<>();
        BiConsumer<String, Integer> bc = map::put;
        BiConsumer<String, Integer> bc2 = (k, v) -> map.put(k, v);
        bc.accept("BC", 1);
        bc2.accept("BC2", 2);
        System.out.println(map);

        Predicate<String> p = s1 -> s1.equals("A");
        BiPredicate<String, String> bp = (str, prefix) -> str.startsWith(prefix);
        System.out.println(p.test("B"));
        System.out.println(bp.test("SHIT", "S"));

        Function<String, Integer> f = String::length;
        BiFunction<String, String, Integer> bf = (s1, s2) -> s1.concat(s2).length();
        System.out.println(f.apply("Shit"));
        System.out.println(bf.apply("Shit", " happens"));

        UnaryOperator<String> uo = String::toUpperCase;
        BinaryOperator<Integer> bo = (i1, i2) -> i1 + i2;
        System.out.println(uo.apply("shit"));
        System.out.println(bo.apply(1, 2));

        //OptionalDouble opt = IntStream.range(1, 100).average();
        OptionalDouble opt = new Random().ints(1, 100).limit(10).average();
        System.out.println(opt.getAsDouble());
        opt.ifPresent(System.out::println);

        Stream<String> empty = Stream.empty();
        Stream<Integer> s1 = Stream.of(1, 2, 3);
        Stream<Integer> s2 = Stream.of(new Integer[]{1, 2, 3});
        Stream<Double> s3 = Stream.generate(Math::random);
        IntStream s7 = new Random().ints().limit(100);
        Stream<Integer> s8 = new Random().ints(0, 100).limit(10).mapToObj(Integer::new);
        IntStream s4 = IntStream.range(1, 100);
        Stream<String> s5 = Arrays.asList("1", "2", "3").stream();
        Stream<Integer> s6 = Stream.iterate(0, i -> i + 1).limit(100);
        System.out.println(s6.reduce((i1, i2) -> i1 + i2).get());
        System.out.println(s4.sum());
        System.out.println(IntStream.range(1, 100).sum());
        //System.out.println(s2.max(Integer::compareTo).get());
        s2.max(Integer::compareTo).ifPresent(System.out::println);
        System.out.println(s7.count());
        System.out.println(s8.collect(Collectors.toList()));
        System.out.println(Stream.of("a", "b", "c").collect(Collectors.joining("")));
        System.out.println(Stream.of("a", "b", "c").reduce(String::concat).get());

        ArrayList<Integer> i1 = new ArrayList<>(Arrays.asList(1, 1, 2, 3, 4, 5, 7));
        ArrayList<Integer> i2 = new ArrayList<>(Arrays.asList(8, 5, 6, 7, 6));
        System.out.println("union " + Stream.concat(i1.stream(), i2.stream()).collect(Collectors.toSet()));
        System.out.println("intersect " + i1.stream().filter(i2::contains).collect(Collectors.toSet()));

        IntStream.of(2, 3, 0, 1, 3).flatMap(x -> IntStream.rangeClosed(0, x)).forEach(System.out::print);
        System.out.println();
        List<List<String>> lst = new ArrayList<>();
        lst.add(Arrays.asList("A", "B"));
        lst.add(Arrays.asList("C"));
        lst.stream().forEach(System.out::print);
        System.out.println();
        lst.stream().flatMap(e -> e.stream()).forEach(System.out::print);
        System.out.println();
        System.out.println(Stream.of("2", "3", "0", "1", "3").collect(Collectors.averagingInt(n -> Integer.parseInt(n))));
        System.out.println(Stream.of(1, 3, 0, 1, 3, 2).collect(Collectors.groupingBy(i -> i)));
        System.out.println(Stream.of(1, 3, 0, 1, 3, 2).map(e -> e.toString()).collect(Collectors.joining()));
        System.out.println(Stream.of(1, 3, 0, 1, 3, 2).collect(Collectors.partitioningBy(e -> e > 2)));
        Map<Integer, String> m = Stream.of("2", "3", "0", "1")
                .collect(Collectors.toMap(k -> k.length(), v -> v, (st1, st2) -> st1 + "|" + st2));
        System.out.println(m);
    }
}
