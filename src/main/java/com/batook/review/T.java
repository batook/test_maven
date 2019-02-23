package com.batook.review;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

class T {
    public static void main(String[] args) throws Exception {
        LinkedList<String> s = new LinkedList<>();
        Arrays.asList("Ene bene raba".split(""))
              .forEach(s::push);
        System.out.println(s.stream()
                            .collect(Collectors.joining()));
    }

}

