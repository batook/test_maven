package com.batook.test;

public class LnkdListTest {
    public static void main(String[] args) {
        LnkdList linkedList = new LnkdList();
        for (int i = 1; i <= 10; i++)
            linkedList.add(linkedList.new Node(Integer.toString(i)));
        linkedList.iterate();
        LnkdList.Node current, result;
        current = linkedList.first;
        result = current;
        int pos = 0;
        while (current.next != null) {
            pos++;
            current = current.next;
            if (pos % 2 == 0) result = result.next;
        }
        System.out.println("Middle=" + result);
        linkedList.reverse();
        linkedList.iterate();
    }
}

class LnkdList {
    Node first;
    Node last;

    public LnkdList() {
        first = new Node("head");
        last = first;
    }

    public void add(Node node) {
        last.next = node;
        last = node;
    }

    public void iterate() {
        Node current = this.first;
        System.out.print(current.value);
        while (current.next != null) {
            System.out.print(" -> " + current.next);
            current = current.next;
        }
        System.out.println();
    }

    public void reverse() {
        Node current = this.first;
        Node prev = null;
        Node tmp;
        while (current != null) {
            tmp = current.next;
            current.next = prev;
            prev = current;
            current = tmp;
        }
        this.first = prev;
    }

    class Node {
        Node next;
        String value;

        public Node(String data) {
            value = data;
        }

        public String toString() {
            return value;
        }
    }
}