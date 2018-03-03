import java.util.*;

enum Priority {
    HIGH,
    MEDIUM,
    LOW
}

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
        //
        Collection<Task> mergedTasks = MergeCollections.merge(mondayTasks, tuesdayTasks);
        assert mergedTasks.toString().equals("[code db, code gui, code logic, phone Mike, phone Paul]");
        //
        Set<Task> phoneAndMondayTasks = new TreeSet<Task>(mondayTasks);
        phoneAndMondayTasks.addAll(phoneTasks);
        assert phoneAndMondayTasks.toString().equals("[code logic, phone Mike, phone Paul]");
        //
        Set<Character> s2 = new LinkedHashSet<Character>();
        Collections.addAll(s2, 'a', 'b', 'j');
        // iterators of a LinkedHashSet return their elements in proper order:
        assert s2.toString().equals("[a, b, j]");
        //
        Set<Task> naturallyOrderedTasks = new TreeSet<Task>(mondayTasks);
        naturallyOrderedTasks.addAll(tuesdayTasks);
        assert naturallyOrderedTasks.toString().equals("[code db, code gui, code logic, phone Mike, phone Paul]");
        //
        NavigableSet<PriorityTask> priorityTasks = new TreeSet<PriorityTask>();
        priorityTasks.add(new PriorityTask(mikePhone, Priority.MEDIUM));
        priorityTasks.add(new PriorityTask(paulPhone, Priority.HIGH));
        priorityTasks.add(new PriorityTask(databaseCode, Priority.MEDIUM));
        priorityTasks.add(new PriorityTask(interfaceCode, Priority.LOW));
        assert (priorityTasks.toString()).equals("[phone Paul: HIGH, code db: MEDIUM, phone Mike: MEDIUM, code gui: LOW]");
        //
        PriorityTask firstLowPriorityTask = new PriorityTask(new EmptyTask(), Priority.LOW);
        SortedSet<PriorityTask> highAndMediumPriorityTasks = priorityTasks.headSet(firstLowPriorityTask);
        assert highAndMediumPriorityTasks.toString().equals("[phone Paul: HIGH, code db: MEDIUM, phone Mike: MEDIUM]");
        //
        PriorityTask firstMediumPriorityTask = new PriorityTask(new EmptyTask(), Priority.MEDIUM);
        SortedSet<PriorityTask> mediumPriorityTasks = priorityTasks.subSet(firstMediumPriorityTask, firstLowPriorityTask);
        assert mediumPriorityTasks.toString().equals("[code db: MEDIUM, phone Mike: MEDIUM]");
        //
        NavigableSet<String> stringSet = new TreeSet<String>();
        Collections.addAll(stringSet, "abc", "cde", "x-ray", "zed");
        String last = stringSet.floor("x-ray");
        assert last.equals("x-ray");
        String secondToLast = last == null ? null : stringSet.lower(last);
        String thirdToLast = secondToLast == null ? null : stringSet.lower(secondToLast);
        assert thirdToLast.equals("abc");
        //
        NavigableSet<String> headSet = stringSet.headSet(last, true);
        NavigableSet<String> reverseHeadSet = headSet.descendingSet();
        assert reverseHeadSet.toString().equals("[x-ray, cde, abc]");
        String conc = " ";
        for (String s : reverseHeadSet) {
            conc += s + " ";
        }
        assert conc.equals(" x-ray cde abc ");
        for (Iterator<String> itr = headSet.descendingIterator(); itr.hasNext(); ) {
            itr.next();
            itr.remove();
        }
        assert headSet.isEmpty();
        //
        // construct and populate a NavigableSet whose iterator returns its
        // elements in the reverse of natural order:
        NavigableSet<String> base = new TreeSet<String>(Collections.reverseOrder());
        Collections.addAll(base, "b", "a", "c");
        // call the two different constructors for TreeSet, supplying the
        // set just constructed, but with different static types:
        NavigableSet<String> sortedSet1 = new TreeSet<String>((Set<String>) base);
        NavigableSet<String> sortedSet2 = new TreeSet<String>(base);
        System.out.println(base);
        System.out.println(sortedSet1);
        System.out.println(sortedSet2);
        // and the two sets have different iteration orders:
        List<String> forward = new ArrayList<String>();
        forward.addAll(sortedSet1);
        List<String> backward = new ArrayList<String>();
        backward.addAll(sortedSet2);
        assert !forward.equals(backward);
        Collections.reverse(forward);
        assert forward.equals(backward);
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

final class PriorityTask implements Comparable<PriorityTask> {
    private final Task task;
    private final Priority priority;

    PriorityTask(Task task, Priority priority) {
        this.task = task;
        this.priority = priority;
    }

    public Task getTask() {
        return task;
    }

    public Priority getPriority() {
        return priority;
    }

    public int compareTo(PriorityTask pt) {
        int c = priority.compareTo(pt.priority);
        return c != 0 ? c : task.compareTo(pt.task);
    }

    public boolean equals(Object o) {
        if (o instanceof PriorityTask) {
            PriorityTask pt = (PriorityTask) o;
            return task.equals(pt.task) && priority.equals(pt.priority);
        } else return false;
    }

    public int hashCode() {
        return task.hashCode();
    }

    public String toString() {
        return task + ": " + priority;
    }
}