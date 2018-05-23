package com.batook.review;

import java.util.Arrays;
import java.util.Random;

//- Recursion can be slower due to the overhead of maintaining a stack and usually takes up more memory
//- Recursion is not stack-friendly. It may cause StackOverflowException when processing big data sets
//+ Recursion adds clarity to the code as it makes it shorter in comparison to the iterative approach

public class Sorting {
    static final int[] unsorted = {100, -90, 32, 39, 21, 45, 23, 3, -100, 3};

    public static void main(String[] args) {
        System.out.println("Unsorted " + Arrays.toString(unsorted));
        BubbleSortTest.run(unsorted);
        MergeSortTest.run(unsorted);
        QuickSortTest.run(unsorted);
    }

    static class BubbleSortTest {
        public static void main(String[] args) {
            run(unsorted);
        }

        public static void run(int[] unsorted) {
            int[] a = unsorted.clone();
            for (int i = 0; i < a.length; i++)
                for (int j = i + 1; j < a.length; j++)
                    if (a[i] > a[j]) {
                        int tmp = a[i];
                        a[i] = a[j];
                        a[j] = tmp;
                        //   a[i]^=a[j];
                        //   a[j]^=a[i];
                        //   a[i]^=a[j];
                    }
            System.out.println("Sorted " + Arrays.toString(a));
        }
    }

    static class MergeSortTest {
        public static void main(String[] args) {
            int[] unsortedBig = new Random().ints(-10, 100)
                                            .limit(100)
                                            .toArray();
            run(unsortedBig);
        }

        public static void run(int[] unsorted) {
            int[] a = unsorted.clone();
            new MergeSortTest().sort(a);
            System.out.println("Sorted " + Arrays.toString(a));
        }

        private void sort(int[] array) {
            if (array.length > 1) {
                int[] left = new int[array.length / 2];
                int[] right = new int[array.length - array.length / 2];
                System.arraycopy(array, 0, left, 0, left.length);
                System.arraycopy(array, left.length, right, 0, right.length);
                sort(left);
                sort(right);
                merge(array, left, right);
            }
        }

        private void merge(int[] result, int[] left, int[] right) {
            int i = 0;
            int j = 0;
            int k = 0;
            //Копируем в массив result меньшее значение,
            while (i < left.length && j < right.length) {
                if (left[i] < right[j]) {
                    result[k++] = left[i++];
                } else {
                    result[k++] = right[j++];
                }
            }
            //Если какой-то массив прошли не до конца, то копируем оставшиеся элементы
            while (i < left.length) {
                result[k++] = left[i++];
            }
            while (j < right.length) {
                result[k++] = right[j++];
            }
        }
    }

    static class QuickSortTest {
        public static void main(String[] args) {
            run(unsorted);
        }

        public static void run(int[] unsorted) {
            int[] a = unsorted.clone();
            sort(a, 0, a.length - 1);
            System.out.println("Sorted " + Arrays.toString(a));
        }

        /*
        1.Choose a pivot value. We take the value of the middle element as pivot value, but it can be any value, which is in range of
        sorted values, even if it doesn't present in the array.
        2.Partition. Rearrange elements in such a way, that all elements which are lesser than the pivot go to the left part of the
        array and all elements greater than the pivot, go to the right part of the array. Values equal to the pivot can stay in any part of the array.
        Notice, that array may be divided in non-equal parts.
        3.Sort both parts. Apply quicksort algorithm recursively to the left and the right parts.
        */
        private static void sort(int[] result, int left, int right) {
            if (left < right) {
                int i = partition(result, left, right);
                //Do same operation as above recursively to sort two sub arrays
                if (left < i - 1) sort(result, left, i - 1);
                if (i < right) sort(result, i, right);
            }
        }

        /*
        On the partition step algorithm divides the array into two parts and every element a from the left part is less or equal than every element b from the right part.
        There are two indices i and j and at the very beginning of the partition algorithm i points to the first element in the array and j points to the last one.
        Then algorithm moves i forward, until an element with value greater or equal to the pivot is found.
        Index j is moved backward, until an element with value lesser or equal to the pivot is found.
        If i ≤ j then they are swapped and i steps to the next position (i + 1), j steps to the previous one (j - 1).
        Algorithm stops, when i becomes greater than j.
        After partition, all values before i-th element are less or equal than the pivot
        and all values after j-th element are greater or equal to the pivot.
        */
        private static int partition(int[] result, int left, int right) {
            int i = left;
            int j = right;
            //Get the pivot element from the middle of the list
            int pivot = result[left + (right - left) / 2];
            while (i <= j) {
                //Check until all values on left side array are lower than pivot
                while (result[i] < pivot) i++;
                //Check until all values on left side array are greater than pivot
                while (result[j] > pivot) j--;
                //Now compare values from both side of lists to see if they need swapping
                //After swapping move the iterator on both lists
                if (i <= j) {
                    int tmp = result[i];
                    result[i] = result[j];
                    result[j] = tmp;
                    i++;
                    j--;
                }
            }
            return i;
        }
    }
}

class BinarySearch {
    public static void main(String[] args) {
        final int[] unsorted = {100, -90, 32, 39, 21, 45, 23, 3, -100, 3};
        Arrays.sort(unsorted);
        System.out.println(Arrays.toString(unsorted));
        System.out.println(search(unsorted, 45));
    }

    public static int search(int[] sortedArray, int key) {
        int index = -1;
        int low = 0, high = sortedArray.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (sortedArray[mid] < key) {
                low = mid + 1;
            } else if (sortedArray[mid] > key) {
                high = mid - 1;
            } else if (sortedArray[mid] == key) {
                index = mid;
                break;
            }
        }
        return index;
    }

    public int searchRecursively(int[] sortedArray, int key, int low, int high) {
        int middle = (low + high) / 2;

        if (high < low) {
            return -1;
        }
        if (key == sortedArray[middle]) {
            return middle;
        } else if (key < sortedArray[middle]) {
            return searchRecursively(sortedArray, key, low, middle - 1);
        } else {
            return searchRecursively(sortedArray, key, middle + 1, high);
        }
    }
}

