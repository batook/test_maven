package com.batook.review;

import java.util.Arrays;

public class Sorting {
    static final int[] unsorted = {100, -90, 32, 39, 21, 45, 23, 3, -100, 3};

    public static void main(String[] args) {
        System.out.println("Unsorted " + Arrays.toString(unsorted));
        BubbleSortTest.run(unsorted);
        MergeSortTest.run(unsorted);
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
                        int tmp = a[j];
                        a[j] = a[i];
                        a[i] = tmp;
                    }
            System.out.println("Sorted " + Arrays.toString(a));
        }
    }

    static class MergeSortTest {
        public static void main(String[] args) {
            run(unsorted);
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
            int idxL = 0;
            int idxR = 0;
            for (int i = 0; i < result.length; i++)
                if (idxR >= right.length || (idxL < left.length && left[idxL] <= right[idxR])) {
                    result[i] = left[idxL];
                    idxL++;
                } else {
                    result[i] = right[idxR];
                    idxR++;
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

        private static void sort(int[] result, int lo, int hi) {
            if (lo < hi) {
                int left = lo;
                int right = hi;
                //Get the pivot element from the middle of the list
                int pivot = result[(lo + hi) / 2];
                while (left <= right) {
                    //Check until all values on left side array are lower than pivot
                    while (result[left] < pivot) left++;
                    //Check until all values on left side array are greater than pivot
                    while (pivot < result[right]) right--;
                    //Now compare values from both side of lists to see if they need swapping
                    //After swapping move the iterator on both lists
                    if (left <= right) {
                        int tmp = result[left];
                        result[left] = result[right];
                        result[right] = tmp;
                        left++;
                        right--;
                    }
                }
                //Do same operation as above recursively to sort two sub arrays
                if (lo < right) sort(result, lo, right);
                if (left < hi) sort(result, left, hi);
            }
        }
    }
}

