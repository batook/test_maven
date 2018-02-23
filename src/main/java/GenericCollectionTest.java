import java.util.*;

public class GenericCollectionTest {
    public static void main(String[] args) {
        PhoneTask mikePhone = new PhoneTask("Mike", "987 6543");
        PhoneTask paulPhone = new PhoneTask("Paul", "123 4567");
        CodingTask databaseCode = new CodingTask("db");
        CodingTask interfaceCode = new CodingTask("gui");
        CodingTask logicCode = new CodingTask("logic");
        Collection<PhoneTask> phoneTasks = new ArrayList<PhoneTask>();
        Collection<CodingTask> codingTasks = new ArrayList<CodingTask>();
        Collection<Task> mondayTasks = new ArrayList<Task>();
        Collection<Task> tuesdayTasks = new ArrayList<Task>();
        Collections.addAll(phoneTasks, mikePhone, paulPhone);
        Collections.addAll(codingTasks, databaseCode, interfaceCode, logicCode);
        Collections.addAll(mondayTasks, logicCode, mikePhone);
        Collections.addAll(tuesdayTasks, databaseCode, interfaceCode, paulPhone);
        assert phoneTasks.toString().equals("[phone Mike, phone Paul]");
        assert codingTasks.toString().equals("[code db, code gui, code logic]");
        assert mondayTasks.toString().equals("[code logic, phone Mike]");
        assert tuesdayTasks.toString().equals("[code db, code gui, phone Paul]");
        Collection<Task> mergedTasks = MergeCollections.merge(mondayTasks, tuesdayTasks);
        assert mergedTasks.toString().equals("[code db, code gui, code logic, phone Mike, phone Paul]");
        Set<Task> phoneAndMondayTasks = new TreeSet<Task>(mondayTasks);
        phoneAndMondayTasks.addAll(phoneTasks);
        assert phoneAndMondayTasks.toString().equals("[code logic, phone Mike, phone Paul]");
    }
}

abstract class Task implements Comparable<Task> {
    protected Task() {
    }

    public boolean equals(Object o) {
        if (o instanceof Task) {
            return toString().equals(o.toString());
        } else return false;
    }

    public int compareTo(Task t) {
        return toString().compareTo(t.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public abstract String toString();
}

final class CodingTask extends Task {
    private final String spec;

    public CodingTask(String spec) {
        this.spec = spec;
    }

    public String getSpec() {
        return spec;
    }

    public String toString() {
        return "code " + spec;
    }
}

final class PhoneTask extends Task {
    private final String name;
    private final String number;

    public PhoneTask(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String toString() {
        return "phone " + name;
    }
}

class EmptyTask extends Task {
    public EmptyTask() {
    }

    public String toString() {
        return "";
    }
}

class MergeCollections {
    static <T extends Comparable<? super T>> List<T> merge(Collection<? extends T> c1, Collection<? extends T> c2) {
        List<T> mergedList = new ArrayList<T>();
        Iterator<? extends T> itr1 = c1.iterator();
        Iterator<? extends T> itr2 = c2.iterator();
        T c1Element = getNextElement(itr1);
        T c2Element = getNextElement(itr2);
        // each iteration will take a task from one of the iterators;
        // continue until neither iterator has any further tasks
        while (c1Element != null || c2Element != null) {
            // use the current c1 element if either the current c2
            // element is null, or both are non-null and the c1 element
            // precedes the c2 element in the natural order
            boolean useC1Element = c2Element == null || c1Element != null && c1Element.compareTo(c2Element) < 0;
            if (useC1Element) {
                mergedList.add(c1Element);
                c1Element = getNextElement(itr1);
            } else {
                mergedList.add(c2Element);
                c2Element = getNextElement(itr2);
            }
        }
        return mergedList;
    }

    static <E> E getNextElement(Iterator<E> itr) {
        if (itr.hasNext()) {
            E nextElement = itr.next();
            if (nextElement == null) throw new NullPointerException();
            return nextElement;
        } else {
            return null;
        }
    }
}
