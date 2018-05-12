package com.batook.test;

import java.util.Arrays;

public class SortTest {
    static int[] unsorted = {-1, 2, 1, 3, 4, 9, 5, 7, 8, 9, 4, 6, 7, 1, 6, 5, 2, 4, 6, 5, 6, 7, 8, 0, 3, 4, -2};
    int[] tmp;

    public static void main(String[] args) {
        new QuickSort().sort(unsorted);
    }

    void swap(int[] t, int a, int b) {
        int tmp = t[a];
        t[a] = t[b];
        t[b] = tmp;
    }
}

class BubbleSort extends SortTest {
    void sort(int[] unsorted) {
        tmp = unsorted.clone();
        System.out.println(Arrays.toString(tmp));
        for (int l = 0; l < tmp.length; l++) {
            for (int r = l + 1; r < tmp.length; r++) {
                if (tmp[l] > tmp[r]) swap(tmp, l, r);
            }
        }
        System.out.println(Arrays.toString(tmp));
    }
}

class QuickSort extends SortTest {
    void sort(int[] unsorted) {
        tmp = unsorted.clone();
        System.out.println(Arrays.toString(tmp));
        new MergeSort().sort(tmp);
        System.out.println(Arrays.toString(tmp));
    }

    class Lomuto {
        private void sort(int lo, int hi) {
            if (lo < hi) {
                int pivot = tmp[hi];
                int i = lo;
                for (int j = lo; j <= hi - 1; j++) {
                    if (tmp[j] <= pivot) {
                        swap(tmp, i, j);
                        i++;
                    }
                }
                swap(tmp, i, hi);
                sort(lo, i - 1);
                sort(i + 1, hi);
            }
        }
    }

    /*
    Select an element as a pivot element, generally from middle but not necessary.
    Data elements are grouped into two parts:
     one with elements that are in lower order than the pivot element,
     one with element that are in higher order than the pivot element.
    Sort the both parts separately by repeating step 1 and 2.
     */
    class Hoare {
        private void sort(int lo, int hi) {
            if (lo >= hi) {
                return;
            }
            //Get the pivot element from the middle of the list
            int pivot = tmp[lo + (hi - lo) / 2];

            // make left < pivot and right > pivot
            int l = lo;
            int r = hi;
            while (l <= r) {
                //Check until all values on left side array are lower than pivot
                while (tmp[l] < pivot) l++;
                //Check until all values on left side array are greater than pivot
                while (tmp[r] > pivot) r--;
                //Now compare values from both side of lists to see if they need swapping
                //After swapping move the iterator on both lists
                if (l <= r) {
                    swap(tmp, l, r);
                    l++;
                    r--;
                }
            }
            //Do same operation as above recursively to sort two sub arrays
            if (lo < r) sort(lo, r);
            if (l < hi) sort(l, hi);
        }
    }

    class MergeSort {
        public void sort(int[] array) {
            if (array.length > 1) {
                //Split the array in half in two parts
                int[] left = new int[array.length / 2];
                int[] right = new int[array.length - (array.length / 2)];
                System.arraycopy(array, 0, left, 0, left.length);
                System.arraycopy(array, left.length, right, 0, right.length);

                //Sort each half recursively
                sort(left);
                sort(right);

                //Merge both halves together, overwriting to original array
                System.out.print(Arrays.toString(array));
                merge(array, left, right);
                System.out.println("> " + Arrays.toString(left) + "+" + Arrays.toString(right) + "=" + Arrays.toString(array));
            }
        }

        public void merge(int[] result, int[] left, int[] right) {
            //Index Position in left array - starting with first element
            int iLeft = 0;
            //Index Position in right array - starting with first element
            int iRight = 0;
            //Compare elements at iLeft and iRight, and move smaller element at result
            for (int i = 0; i < result.length; i++) {
                if (iRight >= right.length || (iLeft < left.length && left[iLeft] <= right[iRight])) {
                    result[i] = left[iLeft];
                    iLeft++;
                } else {
                    result[i] = right[iRight];
                    iRight++;
                }
            }
        }
    }
}

class MergeSortTest {
    public static void main(String[] args) {
        int[] num = {4, 8, 6, 16, 1, 9, 14, 2, 3, 5, 18, 13, 17, 7, 12, 11, 15, 10};
        int n = 18;
        mergeSort(num, 0, n - 1);
        for (int h = 0; h < n; h++) System.out.printf("%d ", num[h]);
        System.out.printf("\n");
    } // end main

    public static void mergeSort(int[] A, int lo, int hi) {
        if (lo < hi) { //list contains at least 2 elements
            int mid = (lo + hi) / 2; //get the mid-point subscript
            mergeSort(A, lo, mid); //sort first half
            mergeSort(A, mid + 1, hi); //sort second half
            merge(A, lo, mid, hi); //merge sorted halves
        }
    } //end mergeSort

    public static void merge(int[] A, int lo, int mid, int hi) {
        //A[lo..mid] and A[mid+1..hi] are sorted;
        //merge the pieces so that A[lo..hi] are sorted
        int[] T = new int[hi - lo + 1];
        int i = lo, j = mid + 1;
        int k = 0;
        while (i <= mid || j <= hi) {
            if (i > mid) T[k++] = A[j++];
            else if (j > hi) T[k++] = A[i++];
            else if (A[i] < A[j]) T[k++] = A[i++];
            else T[k++] = A[j++];
        }
        for (j = 0; j < hi - lo + 1; j++) A[lo + j] = T[j];
    } //end merge
} //end class MergeSortTest
