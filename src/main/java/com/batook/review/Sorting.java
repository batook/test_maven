package com.batook.review;

import java.util.Arrays;

public class Sorting {
    static final int[] unsorted = {100, -90, 32, 39, 21, 45, 23, 3, -100};

    public static void main(String[] args) {
        bubbleSort(unsorted);
    }

    public static void bubbleSort(int[] unsorted) {
        System.out.println("Unsorted " + Arrays.toString(unsorted));
        for (int i = 0; i < unsorted.length; i++)
            for (int j = i + 1; j < unsorted.length; j++)
                if (unsorted[i] > unsorted[j]) {
                    int temp = unsorted[j];
                    unsorted[j] = unsorted[i];
                    unsorted[i] = temp;
                }
        System.out.println("Sorted " + Arrays.toString(unsorted));
    }
}
