import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by sbaymanov on 11.10.2016.
 */
public class Test {
    private int a;

    public static void main(String[] args) {
        Object A = new Object();
        Object B = new Object();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (A) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (B) {
                        System.out.println("Lock AB");
                    }
                }

            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (B) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (A) {
                        System.out.println("Lock BA");
                    }
                }

            }
        });
        t1.start();
        t2.start();

        new Test().maxTemp(Stream.of(1));
        System.out.println(Arrays.stream("abc de".split(" ")).min(String::compareTo).get());
        Comparator<String> lengthCompare = (s1, s2) -> s1.length() - s2.length();
        System.out.println(Arrays.stream("abc de".split(" ")).min(lengthCompare).get());
        List<String> l = Arrays.stream("abc de".split(" ")).map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(l);
        Set<String> ss = Arrays.stream("a b c a b c".split(" ")).map(String::toUpperCase).collect(Collectors.toCollection(TreeSet::new));
        ss.stream().forEach(System.out::println);
        System.out.println(ss);
        Map<Integer, List<String>> m = Arrays.stream("a bb bb ccc ccc ccc".split(" ")).distinct()
                .collect(Collectors.groupingBy(String::length));
        m.forEach((count, words) -> {
            System.out.print(count);
            words.forEach(System.out::println);
        });
        Map<Boolean, List<String>> bb = Arrays.stream("a1 bb1 ccc1".split(" ")).collect(Collectors.partitioningBy(s -> s.length() > 2));
        System.out.println("length>1 " + bb.get(true));
        bb.get(true).forEach(System.out::println);
        bb.forEach((x, y) -> {
            System.out.print(x);
            y.forEach(System.out::println);
        });

        BiPredicate<List<Integer>, Integer> p1 = List::contains;
        BiPredicate<List<Integer>, Integer> p2 = (x, y) -> x.contains(y);
        System.out.println("BiPredicate " + p2.test(Arrays.asList(1, 2, 3), 1));

        ObjIntConsumer<Integer> ic = (x, y) -> System.out.println(x + y);
        ic.accept(100, 100);

        Integer iOb1 = 100;
        Integer iOb2 = 100;
        System.out.println(iOb1 == iOb2);

        iOb1 = Integer.valueOf(100);
        iOb2 = Integer.valueOf(100);
        System.out.println(iOb1 == iOb2);

        Integer iOb3 = new Integer(120);
        Integer iOb4 = new Integer(120);
        System.out.println(iOb3 == iOb4);

        Integer iOb5 = 200;
        Integer iOb6 = 200;
        System.out.println(iOb5 == iOb6);


        Calendar Cal = Calendar.getInstance();
        System.out.println(Cal.get(Cal.YEAR));
        int b = 10;
        new Test().go((long) b);
        String reverseString = "";
        String s = "abcd";
        for (int i = s.length() - 1; i >= 0; i--) {
            reverseString += s.charAt(i);
        }
        System.out.println(reverseString);
        System.out.println(new StringBuilder("abcd").reverse());
        char[] str = "abracadabra".toCharArray();
        for (int i = str.length - 1; i >= 0; i--) {
            System.out.println(str[i]);
        }
        B cb = new B();
        cb.addAll(3);
        System.out.println(cb.getCnt());
        System.out.println(Arrays.asList("a b r acadabra".split(" ")));

        "dbbeeecba".chars().distinct().sorted().forEach(s1 -> System.out.print((char) s1));
        System.out.println("");
        IntStream.rangeClosed(1, 10).filter(i -> (i % 2) == 0).forEach(System.out::println);
    }

    public void testOpt(Integer t) {
        System.out.println("Max=" + t);
    }

    public void maxTemp(Stream<Integer> temps) {
        Optional<Integer> max = temps.max(Integer::compareTo);
        System.out.println(max);
        System.out.println(max.isPresent() ? max.get() : "empty");
        max.ifPresent(new Test()::testOpt);
        max.ifPresent(i -> testOpt(i));
    }

    public void go(Long l) {
        System.out.println(l);
    }
}

class A {
    public void add(int i) {
        System.out.println(this + " A.add=" + i);
    }

    public void addAll(int t) {
        System.out.println(this + " A.addAll=" + t);
        //for (int i = 0; i < t; i++) {
        add(1);
        // }
    }
}

class B extends A {
    int cnt = 0;

    @Override
    public void add(int t) {
        System.out.println(this + " B.add=" + t);
        cnt = cnt + t;
        super.add(t);
    }

    @Override
    public void addAll(int t) {
        System.out.println(this + " B.addAll=" + t);
        cnt = cnt + t;
        super.addAll(t);
    }

    public int getCnt() {
        return cnt;
    }
}

class ColTest {
    public static void main(String[] args) {
        List<? super Integer> list1 = new ArrayList<Integer>();
        List<? super Integer> list2 = new ArrayList<Number>();
        List<? super Integer> list3 = new ArrayList<Object>();
        List<? super String> list4 = new ArrayList<String>();
        list4.add("1");
        List<Object> lo = new ArrayList<Object>(list4);
        List<? super String> list5 = new ArrayList<Object>(lo);
        list3.add(1);
    }

}

class CatTest {
    public static void main(String[] args) {
        List<Animal> animalList = new ArrayList<Animal>();
        animalList.add(new Animal());
        List<Cat> catList = new ArrayList<Cat>();
        catList.add(new Cat());
        List<MyCat> myCatList = new ArrayList<MyCat>();
        myCatList.add(new MyCat());
        List<Dog> dogList = new ArrayList<Dog>();
        List<Object> objList = new ArrayList<Object>();
        objList.add(new Object());

        CatTest catTest = new CatTest();
        catTest.addMethod(animalList);
        catTest.addMethod(catList);
        //catTest.addMethod(myCatList); // DOESN'T COMPILE
        //catTest.addMethod(dogList); // DOESN'T COMPILE
        catTest.addMethod(objList);

    }

    public void addMethod(List<? super Cat> catList) {
        catList.add(new MyCat());
        catList.add(new Cat());
        catList.add(new NewCat());
        System.out.println(catList);
    }
}

class Animal {
}

class Cat extends Animal {
}

class MyCat extends Cat {
}

class NewCat extends Cat {
}

class Dog extends Animal {
}

class WildTest {
    public static void main(String[] args) {

/*      The Get and Put Principle: use an extends wildcard when you only get values out of a
        structure, use a super wildcard when you only put values into a structure, and don’t use
        a wildcard when you both get and put.
*/
        List<Integer> ints = Arrays.asList(1, 2, 3);
        assert sum(ints) == 6.0;
        List<Double> doubles = Arrays.asList(2.78, 3.14);
        assert sum(doubles) == 5.92;
        List<Number> nums = Arrays.<Number>asList(1, 2, 2.78, 3.14);
        assert sum(nums) == 8.92;
        ints = new ArrayList<Integer>();
        //
        count(ints, 5);
        assert ints.toString().equals("[0, 1, 2, 3, 4]");

        nums = new ArrayList<Number>();
        count(nums, 5);
        nums.add(5.0);
        assert nums.toString().equals("[0, 1, 2, 3, 4, 5.0]");

        List<Object> objs = new ArrayList<Object>();
        count(objs, 5);
        objs.add("five");
        assert objs.toString().equals("[0, 1, 2, 3, 4, five]");
        //
        nums = new ArrayList<Number>();
        double sum = sumCount(nums, 5);
        assert sum == 10;
        //
        ints = new ArrayList<Integer>();
        ints.add(1);
        ints.add(2);
        List<? extends Number> nums2 = ints;
        double dbl = sum(nums2); // ok
        //nums2.add(3.14); // compile-time error
        objs = new ArrayList<Object>();
        objs.add(1);
        objs.add("two");
        List<? super Integer> ints2 = objs;
        ints.add(3); // ok
        //dbl = sum(ints2); // compile-time error

        List<? super IOException> ex = new ArrayList<Object>();
        ex.add(new IOException());
        ex.add(new FileNotFoundException());
        ex.add(new EOFException());
        //ex.add(new Exception());
        //ex.add(new Throwable());
        ex.add(new WildTest().new MyEx());

    }

    public static double sum(Collection<? extends Number> nums) {
        double s = 0.0;
        for (Number num : nums) s += num.doubleValue();
        return s;
    }

    public static void count(Collection<? super Integer> ints, int n) {
        for (int i = 0; i < n; i++) ints.add(i);
    }

    public static double sumCount(Collection<Number> nums, int n) {
        count(nums, n);
        return sum(nums);
    }

    class MyEx extends EOFException {

    }
}