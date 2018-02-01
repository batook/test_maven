import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericTest {
    public static void main(String[] args) {
        GenericTest test = new GenericTest();
        List<Colors> colors = new ArrayList<>();
        colors.addAll(Arrays.asList(new Red(), new Green(), new Blue()));
        List<?> colors1 = colors;
        List<? extends Colors> colors2 = colors;
        List<? super Colors> colors3 = colors;

        System.out.println("PrintColor");
        test.printColor(colors);
        //test.printColor(colors1);
        test.printColor(colors2);
        //test.printColor(colors3);

        System.out.println("AddColor");
        test.addColor(colors);
        //test.addColor(colors1);
        //test.addColor(colors2);
        test.addColor(colors3);

        System.out.println("PrintDeepRed");
        List<DeepRed> colorList = new ArrayList<>();
        colorList.add(new DeepRed());
        test.printColor(colorList);
        System.out.println("AddList");
        List<Object> rList = new ArrayList<>();
        rList.addAll(Arrays.asList(new Object(), new Rainbow(), new Colors(), new Red(), new DeepRed()));
        List<Colors> result = test.addColor(rList);
        System.out.println(result);
    }

    public void printColor(List<? extends Colors> t) {
        for (Colors c : t) {
            System.out.println(c.getColor());
        }
    }

    public List<Colors> addColor(List<? super Colors> t) {
        for (Object c : t) {
            System.out.println(c.getClass());
        }
        t.add(new DeepRed());
        t.add(new Red());
        return (List<Colors>) t;
    }
}

class Rainbow {
    String id = "Rainbow";

    String getId() {
        return id;
    }
}

class Colors extends Rainbow {
    String id = "Colors";

    String getColor() {
        return id;
    }

    @Override
    String getId() {
        return id;
    }
}

class Red extends Colors {
    String id = "Red";

    String getRed() {
        return id;
    }

    @Override
    String getId() {
        return id;
    }

    @Override
    String getColor() {
        return id;
    }
}

class Green extends Colors {
    String id = "Green";

    String getGreen() {
        return id;
    }

    @Override
    String getId() {
        return id;
    }

    @Override
    String getColor() {
        return id;
    }
}

class Blue extends Colors {
    String id = "Blue";

    String getBlue() {
        return id;
    }

    @Override
    String getId() {
        return id;
    }

    @Override
    String getColor() {
        return id;
    }
}

class DeepRed extends Red {
    String id = "DeepRed";

    @Override
    String getId() {
        return id;
    }

    @Override
    String getColor() {
        return id;
    }

    String getDeepRed() {
        return id;
    }

    static class Test {
        public static void main(String[] args) {
            Test t = new Test();
            List<Object> list = new ArrayList<>();
            list.addAll(Arrays.asList(new Object(), new Character('a'), "WTF"));
            List<Number> result = t.getList(list);
            System.out.println(result);
        }

        List<Number> getList(List<? super Number> l) {
            l.add(new Integer(1));
            l.add(new Double(1));
            return (List<Number>) l;
        }

    }
}
